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
import com.junyang.entity.AbbreviationEntity.AbbreviationEntity;
import com.junyang.entity.help.HelpEntity;
import com.junyang.entity.response.RpcResponseEntity;
import com.junyang.enums.HttpAddressEunms;
import com.junyang.service.AbbreviationService;
import com.junyang.utils.GenericityUtil;
import com.junyang.utils.HttpUtil;

@RestController
@Transactional
@CrossOrigin
public class AbbreviationServiceImpl extends BaseApiService implements AbbreviationService{
	
	@Value("${http_url}")
	private String HTTP_URL;
	
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public ResponseBase getList(@RequestBody AbbreviationEntity entity) {
		try {
			Query query = new Query();
			if(entity.getLanguage() != null && entity.getLanguage().length() > 0) {
				query.addCriteria(Criteria.where("language").is(entity.getLanguage()));
			}
			long totalCount = mongoTemplate.count(query, AbbreviationEntity.class);// 总条数
			// 构建分页请求对象
			int pageNumber = Math.max(entity.getPageNumber() - 1, 0);
			PageRequest pageRequest = PageRequest.of(pageNumber, entity.getPageSize(),
					Sort.by(Sort.Direction.DESC, "createdAt"));
			query.with(pageRequest);
			List<AbbreviationEntity> list = mongoTemplate.find(query, AbbreviationEntity.class);
			// 获取总记录数
			PageInfo<AbbreviationEntity > info = new PageInfo<>(list);
			info.setTotal(totalCount);
			return setResultSuccess(info);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase delete(String id) {
		try {
			if(id != null && id.length() > 0) {
				String baseStr = HttpUtil.get(HTTP_URL + HttpAddressEunms.ABBREVIATION_DELETE.getName()+"?id="+id+"");
				RpcResponseEntity responseEntity = JSONObject.parseObject(baseStr, RpcResponseEntity.class);
				if(responseEntity.getSuccess()) {
					 Query query = new Query(Criteria.where("_id").is(id));
				     mongoTemplate.remove(query, AbbreviationEntity.class);
				     return setResultSuccess();
				}else {
					return setResultError(Constants.ERROR);
				}
			}else {
				return setResult(400, "参数异常", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase add(@RequestBody AbbreviationEntity entity) {
		try {
			if(entity != null) {
				JSONObject jsonObject = (JSONObject) JSONObject.toJSON(entity);
				String jsonParam = JSON.toJSONString(jsonObject);
				String res = HttpUtil.sendPostRequest(HTTP_URL+HttpAddressEunms.ABBREVIATION_ADD.getName(), jsonParam);
				RpcResponseEntity rpcResponse = JSONObject.parseObject(res, RpcResponseEntity.class);
				if(rpcResponse.getSuccess() != null && rpcResponse.getSuccess()) {
					this.getRpc();
					return setResultSuccess();
				}
			}
			return setResultError(Constants.ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase update(@RequestBody AbbreviationEntity entity) {
		try {
			if(entity != null) {
				JSONObject jsonObject = (JSONObject) JSONObject.toJSON(entity);
				String jsonParam = JSON.toJSONString(jsonObject);
				String res = HttpUtil.sendPostRequest(HTTP_URL+HttpAddressEunms.ABBREVIATION_UPDATE.getName(), jsonParam);
				RpcResponseEntity rpcResponse = JSONObject.parseObject(res, RpcResponseEntity.class);
				if(rpcResponse.getSuccess() != null && rpcResponse.getSuccess()) {
					entity.setGmtModified(new Date());
					mongoTemplate.save(entity);
					return setResultSuccess();
				}
			}
			return setResultError(Constants.ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	public void getRpc() {
		try {
			String baseStr = HttpUtil.get(HTTP_URL + HttpAddressEunms.ABBREVIATION_LIST.getName());
			RpcResponseEntity responseEntity = JSONObject.parseObject(baseStr, RpcResponseEntity.class);
			if (responseEntity.getData() != null && responseEntity.getData().toString().length() > 0) {
				List<AbbreviationEntity> list = JSONArray.parseArray(responseEntity.getData().toString(), AbbreviationEntity.class);
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						Query query = new Query(Criteria.where("_id").is(list.get(i).getId()));
						boolean exists = mongoTemplate.exists(query, AbbreviationEntity.class, "abbreviation_db");
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
