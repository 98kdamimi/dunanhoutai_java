package com.junyang.service.impl;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.pagehelper.PageInfo;
import com.junyang.base.ResponseBase;
import com.junyang.entity.system.UserAgreementEntity;
import com.junyang.entity.tronsignature.*;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.junyang.service.TronSignatureService;
import com.junyang.utils.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.junyang.base.BaseApiService;
import com.junyang.base.HttpResponse;
import com.junyang.constants.Constants;
import com.junyang.entity.tronsignature.MultiSignaturesEntity;

@RestController
@Transactional
@CrossOrigin
public class TronSignatureServiceImpl extends BaseApiService implements TronSignatureService{

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Value("${GET_ACCOUNT_URL}")
	private String GET_ACCOUNT_URL;
	
	@Value("${MULTI_SIGN_URL}")
	private String MULTI_SIGN_URL;
	
	@Value("${ACCOUNT_TOKENS_URL}")
	private String ACCOUNT_TOKENS_URL;

	@Override
	public HttpResponse signJudge(String address) {
		try {
			String base = HttpUtil.get(GET_ACCOUNT_URL+"?address="+address);
			TronAccountEntity accountEntity = JSONObject.parseObject(base, TronAccountEntity.class);
			if(accountEntity.getOwnerPermission().getKeys().size() > 1 || accountEntity.getActivePermissions().get(0).getKeys().size() > 1) {
				return senSuccess(true);
			}
			return senSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();  
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public HttpResponse multiSign(String address) {
		try {
			Map<String, Object> params = new HashedMap();
			params.put("address", address);
			params.put("start", 0);
			params.put("state", 255);
			params.put("limit", 20);
			params.put("netType", "main_net");
			params.put("serializable", true);
			params.put("selfSign", 0);
			String base = HttpUtil.get(MULTI_SIGN_URL+"?address="+address+"&start=0&state=255&limit=20&netType=main_net&serializable=true&selfSign=0");
			HttpResponse response = JSONObject.parseObject(base, HttpResponse.class);
			String temp = response.getData().toString();
			JSONObject jsonObject = JSONObject.parseObject(temp);
			String dataStr = jsonObject.get("data").toString();
			List<TronSignatureEntity> list = JSONArray.parseArray(dataStr, TronSignatureEntity.class);
			return senSuccess(list);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	@Override
	public HttpResponse getAccount(String address) {
		try {
			String base = HttpUtil.get(GET_ACCOUNT_URL+"?address="+address);
			TronAccountEntity accountEntity = JSONObject.parseObject(base, TronAccountEntity.class);
			return senSuccess(JSON.toJSON(accountEntity));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	public HttpResponse senSuccess(Object object) {
		HttpResponse httpResponse = new HttpResponse();
		httpResponse.setCode(Constants.HTTP_RES_CODE_200);
		httpResponse.setMsg(Constants.SUCCESS);
		httpResponse.setData(object);
		return httpResponse;
	}

	@Override
	public HttpResponse getAddressToken(String address) {
		try {
			String base = HttpUtil.get(ACCOUNT_TOKENS_URL+"?address="+address+"&start=0&limit=20&hidden=0&show=0&sortType=0&sortBy=0");
			JSONObject jsonObject = JSONObject.parseObject(base);
			String dataStr = jsonObject.getString("data").toString();
			List<TokenInfoDataEntity> list = JSONArray.parseArray(dataStr, TokenInfoDataEntity.class);
			return senSuccess(JSON.toJSON(list));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase createMultiSign(@RequestBody MultiSignaturesRequest request) {
		try {
			// 将请求参数转换为实体类
			MultiSignaturesEntity entity = new MultiSignaturesEntity();
			entity.setVisible(request.isVisible());
			entity.setTxID(request.getTxID());
			entity.setRawaddress(request.getRawaddress());
			entity.setRawDataHex(request.getRawDataHex());
			entity.setSignature(request.getSignature());

			// 映射 RawData
			if (request.getRawData() != null) {
				MultiSignaturesEntity.RawData rawData = new MultiSignaturesEntity.RawData();
				rawData.setRefBlockBytes(request.getRawData().getRefBlockBytes());
				rawData.setRefBlockHash(request.getRawData().getRefBlockHash());
				rawData.setExpiration(request.getRawData().getExpiration());
				rawData.setTimestamp(request.getRawData().getTimestamp());

				// 映射 Contract
				if (request.getRawData().getContract() != null) {
					rawData.setContract(request.getRawData().getContract());
				}
				entity.setRawData(rawData);
			}


			//这里去调用接口 拿到有所有权的账户
			if (request.getRawaddress() != null  && !request.getRawaddress().isEmpty()) {
				String account = HttpUtil.get(GET_ACCOUNT_URL+"?address="+request.getRawaddress());
				// 解析返回的 JSON 数据
				JSONObject accountjsonObject = JSONObject.parseObject(account);
				JSONObject ownerPermission = accountjsonObject.getJSONObject("ownerPermission");
				// 映射 ownerPermission 数据到 MultiSignaturesEntity
				if (ownerPermission != null) {
					MultiSignaturesEntity.OwnerPermission ownerPermissionObj = new MultiSignaturesEntity.OwnerPermission();
					ownerPermissionObj.setThreshold(ownerPermission.getInteger("threshold"));
					ownerPermissionObj.setPermissionName(ownerPermission.getString("permissionName"));

					// 映射 keys
					List<MultiSignaturesEntity.OwnerPermission.Key> keys = ownerPermission.getJSONArray("keys").stream()
							.map(key -> {
								JSONObject keyObj = (JSONObject) key;
								MultiSignaturesEntity.OwnerPermission.Key keyEntity = new MultiSignaturesEntity.OwnerPermission.Key();
								keyEntity.setAddress(keyObj.getString("address"));
								keyEntity.setWeight(keyObj.getInteger("weight"));
								return keyEntity;
							}).collect(Collectors.toList());
					ownerPermissionObj.setKeys(keys);

					// 设置到 MultiSignaturesEntity
					entity.setOwnerPermission(ownerPermissionObj);
				}
				//存储activePermissions
				JSONArray  activePermissions = accountjsonObject.getJSONArray("activePermissions");
//				System.out.print(activePermissions);
				entity.setActivePermissions(activePermissions);
			}
			// 保存到 MongoDB
			// 查询条件
			Query query = new Query(Criteria.where("txID").is(entity.getTxID()));

			// 更新操作
			Update update = new Update();
			update.set("visible", entity.isVisible());
			update.set("rawData", entity.getRawData());
			update.set("rawaddress", entity.getRawaddress());
			update.set("rawDataHex", entity.getRawDataHex());
			update.set("signature", entity.getSignature());
			update.set("ownerPermission", entity.getOwnerPermission());

			// 如果不存在则新增
			FindAndModifyOptions options = FindAndModifyOptions.options().upsert(true).returnNew(true);

			// 执行更新或新增
			MultiSignaturesEntity result = mongoTemplate.findAndModify(query, update, options, MultiSignaturesEntity.class);
			return setResultSuccess(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase getMultiSign(String address) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("ownerPermission.keys.address").is(address));
			List<MultiSignaturesEntity> list = mongoTemplate.find(query, MultiSignaturesEntity.class);
			System.out.println(JSON.toJSON(list));
			return setResultSuccess(list);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	
	

}
