package com.junyang.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.junyang.base.BaseApiService;
import com.junyang.base.HttpResponse;
import com.junyang.base.ResponseBase;
import com.junyang.constants.Constants;
import com.junyang.entity.monitorEvent.AddAddressesQueryEntity;
import com.junyang.entity.monitorEvent.AddressesJpushEntity;
import com.junyang.entity.monitorEvent.MonitorEventEntity;
import com.junyang.entity.monitorEvent.TrackerAddressesInfoEntity;
import com.junyang.entity.monitorEvent.TrackerInfoEntity;
import com.junyang.entity.monitorEvent.UpAddressesQueryEntity;
import com.junyang.entity.monitorEvent.TrackerAddressesInfoEntity.AddressInfo;
import com.junyang.entity.monitorEvent.TrackerInfoEntity.Tracker;
import com.junyang.entity.token.DaTokenEntity;
import com.junyang.enums.ActionTypeEnums;
import com.junyang.enums.EventTypeEunms;
import com.junyang.enums.NetWorkNameEnums;
import com.junyang.enums.ReleaseStateEnums;
import com.junyang.enums.WebHookUrlEnums;
import com.junyang.service.WebHookService;
import com.junyang.utils.HttpUtil;
import com.junyang.utils.JPushUtils;

@RestController
@Transactional
@CrossOrigin
public class WebHookServiceImpl extends BaseApiService implements WebHookService {

	@Value("${WEB_HOOK_URL}")
	private String WEB_HOOK_URL;

	@Value("${WEB_HOOK_KEY}")
	private String WEB_HOOK_KEY;

	@Value("${WEB_HOOK_CALLBACKS}")
	private String WEB_HOOK_CALLBACKS;

	@Autowired
	@Qualifier("secondaryMongoTemplate") // 次数据源
	private MongoTemplate secondaryMongoTemplate;

	@Override
	public ResponseBase addTaskAddresses(@RequestBody AddAddressesQueryEntity queryEntity) {
		try {
			// 获取现有监听任务
			HttpResponse base = this.findTaskList(Constants.LIMIT, Constants.PAGE);
			String temp = JSON.toJSONString(base.getData());
			JSONArray array = JSONArray.parseArray(temp);
			String data = array.get(0).toString();
			TrackerInfoEntity entity = JSONObject.parseObject(data, TrackerInfoEntity.class);
			if (entity.getTrackerList() != null && entity.getTrackerList().size() > 0) {// 已有任务
				List<Tracker> trackerList = entity.getTrackerList();
				for (int i = 0; i < trackerList.size(); i++) {
					if (trackerList.get(i).getChainShortName()
							.equals(NetWorkNameEnums.getValue(queryEntity.getNetWorkName()))) {// 判断是否存在此链的任务
						// 查询任务详情，判断地址是否已满
						HttpResponse response = this.findTaskaddAressList(trackerList.get(i).getTrackerId(),
								Constants.LIMIT, Constants.PAGE);
						String addressesStr = response.getData().toString();
						TrackerAddressesInfoEntity addressesInfoEntity = JSONObject.parseObject(addressesStr,
								TrackerAddressesInfoEntity.class);
						List<AddressInfo> addressList = addressesInfoEntity.getAddressList();
						if (addressList != null && addressList.size() < Constants.SIZE) {// 调用任务修改添加地址
							// 判断地址集合中是否存在现有的地址
							boolean addressesState = this.judgeAddresses(addressList, queryEntity.getAddresses());
							if (addressesState) {
								this.addAddresses(queryEntity,trackerList.get(i).getTrackerId());//往任务中的地址列表中追加
							}
							return setResultSuccess();
						}
					}
				}
				this.addTracker(queryEntity);// 没有此链任务，新增任务
				return setResultSuccess();
			} else {// 没有任务
				this.addTracker(queryEntity);
				return setResultSuccess();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * @category 新增任务
	 */
	public void addTracker(AddAddressesQueryEntity queryEntity) {
		MonitorEventEntity entity = new MonitorEventEntity();
		entity.setEvent(EventTypeEunms.TOKENTRANSFER.getName());// 任务类型
		entity.setChainShortName(NetWorkNameEnums.getValue(queryEntity.getNetWorkName()));// 链缩写
		entity.setTrackerName(NetWorkNameEnums.getLable(queryEntity.getNetWorkName()));// 任务名称
		List<String> addressesList = new ArrayList<String>();
		addressesList.add(queryEntity.getAddresses());
		entity.setAddresses(addressesList);
		// 获取此链对应的token
		List<String> tokenList = this.findNetWorkToken(queryEntity);
		entity.setTokenContractAddress(tokenList);
		entity.setWebhookUrl(WEB_HOOK_CALLBACKS);
		JSONObject jsonObject = (JSONObject) JSONObject.toJSON(entity);
		String jsonParam = JSON.toJSONString(jsonObject);
		String res = HttpUtil.sendWebHookPostRequest(WEB_HOOK_URL + WebHookUrlEnums.WEBHOOK_ADD_TASK.getValue(),
				WEB_HOOK_KEY, jsonParam);
		System.out.println(res);
	}

	/**
	 * @category 新增地址
	 */
	public void addAddresses(AddAddressesQueryEntity queryEntity, String trackerId) {
		List<String> addressOneList = new ArrayList<String>();
		addressOneList.add(queryEntity.getAddresses());
		UpAddressesQueryEntity upAddressesQuery = new UpAddressesQueryEntity();
		upAddressesQuery.setAction(ActionTypeEnums.ADD.getName());
		upAddressesQuery.setAddresses(addressOneList);
		upAddressesQuery
				.setChainShortName(NetWorkNameEnums.getValue(queryEntity.getNetWorkName()));
		upAddressesQuery.setTrackerId(trackerId);
		
		JSONObject jsonObject = (JSONObject) JSONObject.toJSON(queryEntity);
		String jsonParam = JSON.toJSONString(jsonObject);
		String res = HttpUtil.sendWebHookPostRequest(WEB_HOOK_URL + WebHookUrlEnums.WEBHOOK_ADD_ADDRESSES.getValue(),
				WEB_HOOK_KEY, jsonParam);
		System.out.println(res);
	}

	/**
	 * @category 判断地址集合中是否存在当前地址
	 */
	public boolean judgeAddresses(List<AddressInfo> addressList, String address) {
		Set<String> addressSet = new HashSet<>();
		for (AddressInfo addressInfo : addressList) {
			addressSet.add(addressInfo.getAddress());
		}
		// 使用 Set 的 contains 方法判断地址是否存在
		return addressSet.contains(address);
	}

	/**
	 * @category 获取任务列表
	 */
	@SuppressWarnings("unchecked")
	public HttpResponse findTaskList(String limit, String page) {
		try {
			String temp;
			if (limit != null && page != null) {
				Map<String, String> params = new HashedMap();
				params.put("limit", limit);
				params.put("page", page);
				temp = HttpUtil.sendWebHookGetRequest(WEB_HOOK_URL + WebHookUrlEnums.WEBHOOK_TASK_LIST.getValue(),
						WEB_HOOK_KEY, params);
			} else {
				temp = HttpUtil.sendWebHookGetRequest(WEB_HOOK_URL + WebHookUrlEnums.WEBHOOK_TASK_LIST.getValue(),
						WEB_HOOK_KEY, null);
			}
			HttpResponse response = JSONObject.parseObject(temp, HttpResponse.class);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * @category 获取 Webhook 任务订阅地址列表
	 * @param limit
	 * @param page
	 * @param
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public HttpResponse findTaskaddAressList(String trackerId, String limit, String page) {
		try {
			String temp;
			if (limit != null && page != null) {
				Map<String, String> params = new HashedMap();
				params.put("trackerId", trackerId);
				params.put("limit", limit);
				params.put("page", page);
				temp = HttpUtil.sendWebHookGetRequest(WEB_HOOK_URL + WebHookUrlEnums.WEBHOOK_ADDRESSES_TASK.getValue(),
						WEB_HOOK_KEY, params);
			} else {
				temp = HttpUtil.sendWebHookGetRequest(WEB_HOOK_URL + WebHookUrlEnums.WEBHOOK_ADDRESSES_TASK.getValue(),
						WEB_HOOK_KEY, null);
			}
			HttpResponse response = JSONObject.parseObject(temp, HttpResponse.class);
			return response;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * @category 任务回调
	 */
	@Override
	public HttpResponse Callbacks(@RequestBody Map<String, Object> payload) {
		// 解构数据获取钱包地址获取对应的jpushId
		List<String> registrationIds = this.getRegistrationIds("0xcd9ba57e825feeef8ff6ae73a6daacedb39eaf3a");
		// 构建消息，推送消息
		JPushUtils.sendPushNotification(registrationIds, "ceshi", "ceshiceshi");
		// 返回webhook结果
		HttpResponse httpResponse = new HttpResponse();
		httpResponse.setCode(Constants.HTTP_RES_CODE_200);
		httpResponse.setMsg(Constants.SUCCESS);
		return httpResponse;
	}

	/**
	 * @category 根据地址获取极光id
	 * @param address
	 * @return
	 */
	public List<String> getRegistrationIds(String address) {
		// 创建聚合查询
		Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(Criteria.where("addresses").is(address)), // 匹配包含指定地址的文档
				Aggregation.project("registrationId") // 只保留 registrationId 字段
		);
		// 执行聚合查询
		AggregationResults<AddressesJpushEntity> results = secondaryMongoTemplate.aggregate(aggregation,
				AddressesJpushEntity.class, AddressesJpushEntity.class);
		// 获取结果并返回 registrationId 列表
		List<AddressesJpushEntity> entities = results.getMappedResults();
		// 提取 registrationId 并进行去重、过滤 null 或空值
		Set<String> registrationIdsSet = entities.stream().map(AddressesJpushEntity::getRegistrationId)
				.filter(id -> id != null && !id.isEmpty())
				.collect(Collectors.toSet());
		List<String> list = new ArrayList<>(registrationIdsSet);
		return list;
	}

	/**
	 * @category ADDRESS绑定极光
	 */
	@Override
	public ResponseBase bindingAddresses(@RequestBody AddressesJpushEntity entity) {
	    try {
	        // 获取当前时间
	        Date now = new Date();
	        
	        // 判断是否有 registrationId，存在则执行更新操作
	        AddressesJpushEntity existingEntity = secondaryMongoTemplate.findOne(
	                Query.query(Criteria.where("registrationId").is(entity.getRegistrationId())),
	                AddressesJpushEntity.class);
	        
	        if (existingEntity != null) {
	            // 如果存在，判断 addresses 中是否已经包含了新地址
	            List<String> existingAddresses = existingEntity.getAddresses();
	            List<String> newAddresses = entity.getAddresses();
	            
	            // 遍历新传入的 addresses，如果不存在，则追加
	            for (String newAddress : newAddresses) {
	                if (!existingAddresses.contains(newAddress)) {
	                    existingAddresses.add(newAddress);
	                }
	            }
	            // 更新 addresses 和更新时间
	            existingEntity.setAddresses(existingAddresses);
	            existingEntity.setGmtModified(now);
	            // 保存更新后的记录
	            secondaryMongoTemplate.save(existingEntity);
	        } else {
	            // 如果不存在，设置创建时间和更新时间
	            entity.setSetTime(now);
	            entity.setGmtModified(now);
	            // 新增记录
	            secondaryMongoTemplate.save(entity);
	        }
	        return setResultSuccess();
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException("绑定地址时发生错误", e);
	    }
	}


	/**
	 * @category 查询链下代币合约地址
	 * @param entity
	 * @return
	 */
	public List<String> findNetWorkToken(AddAddressesQueryEntity entity) {
		Query query = new Query();
		// 根据传入的条件构造查询
		if (entity.getChainId() != null && entity.getChainId().length() > 0) {
			query.addCriteria(Criteria.where("chainId").is(entity.getChainId()));
		}
		if (entity.getImpl() != null && entity.getImpl().length() > 0) {
			query.addCriteria(Criteria.where("impl").is(entity.getImpl()));
		}
		// 添加状态条件
		query.addCriteria(Criteria.where("status").is(ReleaseStateEnums.TOP_LINE.getLable()));
		// 限制只返回 address 字段
		query.fields().include("address");
		// 排序并限制查询结果为前20条数据
		query.with(Sort.by(Sort.Order.asc("createdAt")));
		query.limit(20); // 限制返回20条数据
		// 执行查询
		List<DaTokenEntity> list = secondaryMongoTemplate.find(query, DaTokenEntity.class);
		// 从查询结果中提取出 address 字段，并过滤掉 null 或空字符串
		List<String> addresses = list.stream().map(DaTokenEntity::getAddress)
				.filter(address -> address != null && !address.isEmpty())
				.collect(Collectors.toList());

		return addresses;
	}

}
