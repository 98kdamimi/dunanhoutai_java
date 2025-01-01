package com.junyang.service.impl;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.junyang.base.BaseApiService;
import com.junyang.base.ResponseBase;
import com.junyang.constants.Constants;
import com.junyang.entity.appconfig.AppconfigEntity;
import com.junyang.entity.network.NetWorkEntity;
import com.junyang.entity.response.RpcResponseEntity;
import com.junyang.enums.HttpAddressEunms;
import com.junyang.query.PublicQueryEntity;
import com.junyang.service.AppConfigService;
import com.junyang.utils.GenericityUtil;
import com.junyang.utils.HttpUtil;

@RestController
@Transactional
@CrossOrigin
public class AppConfigServiceImpl extends BaseApiService implements AppConfigService {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Value("${http_url}")
	private String HTTP_URL;

	@Override
	public ResponseBase add(@RequestBody AppconfigEntity entity) {
		try {
			Query queryVer = new Query();
			queryVer.addCriteria(Criteria.where("version").in(entity.getVersion()));
			AppconfigEntity appconfigEntity = mongoTemplate.findOne(queryVer, AppconfigEntity.class);
			if(appconfigEntity != null) {
				return setResultError("此版本已存在!!!");
			}
			if (entity.getNetworkIds() != null && !entity.getNetworkIds().isEmpty()) {
				System.out.println(entity.getNetworkIds());
				Query query = new Query();
				query.addCriteria(Criteria.where("id").in(entity.getNetworkIds()));
				List<NetWorkEntity> list = mongoTemplate.find(query, NetWorkEntity.class);
				System.out.println(JSON.toJSON(list));
				if(list != null && list.size() > 0) {
					JSONObject obj = new JSONObject();
					obj.put("networks", list);
					entity.setApp(obj);
					JSONObject jsonObject = (JSONObject) JSONObject.toJSON(entity);
					jsonObject.remove("networkIds");
					String jsonParam = JSON.toJSONString(jsonObject);
					String res = HttpUtil.sendPostRequest(HTTP_URL+HttpAddressEunms.APPCONFIG_ADD.getName(), jsonParam);
					RpcResponseEntity rpcResponse = JSONObject.parseObject(res, RpcResponseEntity.class);
					if(rpcResponse.getSuccess() != null && rpcResponse.getSuccess()) {
						this.getRpc();
						return setResultSuccess();
					}
					return setResultError(Constants.ERROR);
				}else {
					return setResultError("没有对应的网络信息");
				}
			} else {
				return setResultError("请添加网络配置!!!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase update(@RequestBody AppconfigEntity entity) {
		try {
			AppconfigEntity appconfigEntity = mongoTemplate.findById(entity.getId(), AppconfigEntity.class);
			if(appconfigEntity != null) {
				if(!appconfigEntity.getVersion().equals(entity.getVersion())) {
					return setResultError("版本号不匹配");
				}
				if (entity.getNetworkIds() != null && !entity.getNetworkIds().isEmpty()) {
					Query query = new Query();
					query.addCriteria(Criteria.where("_id").in(entity.getNetworkIds()));
					List<NetWorkEntity> list = mongoTemplate.find(query, NetWorkEntity.class);
					if(list != null && list.size() > 0) {
						JSONObject obj = new JSONObject();
						obj.put("networks", list);
						entity.setApp(obj);
						JSONObject jsonObject = (JSONObject) JSONObject.toJSON(entity);
						jsonObject.remove("networkIds");
						jsonObject.remove("id");
						String jsonParam = JSON.toJSONString(jsonObject);
						String res = HttpUtil.sendPostRequest(HTTP_URL+HttpAddressEunms.APPCONFIG_UPDATE.getName(), jsonParam);
						RpcResponseEntity rpcResponse = JSONObject.parseObject(res, RpcResponseEntity.class);
						if(rpcResponse.getSuccess() != null && rpcResponse.getSuccess()) {
							entity.setNetworkIds(null);
							entity.setGmtModified(new Date());
							mongoTemplate.save(entity);
							return setResultSuccess();
						}
						return setResultError(Constants.ERROR);
					}else {
						return setResultError("没有对应的网络信息");
					}
				} else {
					return setResultError("请添加网络配置!!!");
				}
			}else {
				return setResultError(Constants.ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase findList(@RequestBody PublicQueryEntity entity) {
		try {
			this.getRpc();
			Query query = new Query();
			if (entity.getVersion() != null && entity.getVersion().length() > 0) {
				query.addCriteria(Criteria.where("version").is(entity.getVersion()));
			}
			long totalCount = mongoTemplate.count(query, AppconfigEntity.class);// 总条数
			// 构建分页请求对象
			int pageNumber = Math.max(entity.getPageNumber() - 1, 0);
			PageRequest pageRequest = PageRequest.of(pageNumber, entity.getPageSize(),
					Sort.by(Sort.Direction.DESC, "setTime"));
			query.with(pageRequest);
			List<AppconfigEntity> list = mongoTemplate.find(query, AppconfigEntity.class);
			PageInfo<AppconfigEntity> info = new PageInfo<>(list);
			info.setTotal(totalCount);
			return setResultSuccess(info);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public void getRpc() {
		try {
			String baseStr = HttpUtil.get(HTTP_URL + HttpAddressEunms.APPCONFIG_LIST.getName());
			RpcResponseEntity responseEntity = JSONObject.parseObject(baseStr, RpcResponseEntity.class);
			if (responseEntity.getData() != null && responseEntity.getData().toString().length() > 0) {
				List<AppconfigEntity> list = JSONArray.parseArray(responseEntity.getData().toString(),
						AppconfigEntity.class);
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						Query query = new Query(Criteria.where("version").is(list.get(i).getVersion()));
						boolean exists = mongoTemplate.exists(query, AppconfigEntity.class, "appconfig_db");
						if (exists == false) {
							GenericityUtil.setDate(list.get(i));
							mongoTemplate.insert(list.get(i));
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

}
