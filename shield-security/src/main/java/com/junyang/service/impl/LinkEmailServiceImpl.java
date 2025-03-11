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
import com.junyang.entity.link.LinkEmailEntity;
import com.junyang.entity.response.RpcResponseEntity;
import com.junyang.enums.HttpAddressEunms;
import com.junyang.service.LinkEmailService;
import com.junyang.utils.GenericityUtil;
import com.junyang.utils.HttpUtil;

@RestController
@CrossOrigin
@Transactional
public class LinkEmailServiceImpl extends BaseApiService implements LinkEmailService{
	

	@Value("${http_url}")
	private String HTTP_URL;
	
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public ResponseBase add(@RequestBody LinkEmailEntity entity) {
		try {
			Query query = new Query();
			if(entity.getLanguage() != null && entity.getLanguage().length() > 0) {
				query.addCriteria(Criteria.where("language").is(entity.getLanguage()));
			}else {
				return setResultError("请选择语言!!!");
			}
			if(entity.getEmail() != null && entity.getEmail().length() > 0) {
				query.addCriteria(Criteria.where("email").is(entity.getEmail()));
			}else {
				return setResultError("请填写邮箱!!!");
			}
			LinkEmailEntity emailEntity = mongoTemplate.findOne(query, LinkEmailEntity.class);
			if(emailEntity != null) {
				return setResultError("此邮箱已存在");
			}
			JSONObject jsonObject = (JSONObject) JSONObject.toJSON(entity);
			String jsonParam = JSON.toJSONString(jsonObject);
			String res = HttpUtil.sendPostRequest(HTTP_URL+HttpAddressEunms.LINKEMAIL_ADD.getName(), jsonParam);
			RpcResponseEntity rpcResponse = JSONObject.parseObject(res, RpcResponseEntity.class);
			if(rpcResponse.getSuccess() != null && rpcResponse.getSuccess()) {
				this.getRpc();
				return setResultSuccess();
			}
			return setResultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase delete(String id) {
		try {
			if(id != null && id.length() > 0) {
				String baseStr = HttpUtil.get(HTTP_URL + HttpAddressEunms.LINKEMAIL_DELETE.getName()+"?id="+id+"");
				RpcResponseEntity responseEntity = JSONObject.parseObject(baseStr, RpcResponseEntity.class);
				if(responseEntity.getSuccess()) {
					 Query query = new Query(Criteria.where("_id").is(id));
				     mongoTemplate.remove(query, LinkEmailEntity.class);
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
	public ResponseBase findList(@RequestBody LinkEmailEntity entity) {
		Query query = new Query();
		if(entity.getLanguage() != null && entity.getLanguage().length() > 0) {
			query.addCriteria(Criteria.where("language").is(entity.getLanguage()));
		}
		long totalCount = mongoTemplate.count(query, LinkEmailEntity.class);// 总条数
		// 构建分页请求对象
		int pageNumber = Math.max(entity.getPageNumber() - 1, 0);
		PageRequest pageRequest = PageRequest.of(pageNumber, entity.getPageSize(),
				Sort.by(Sort.Direction.DESC, "createdAt"));
		query.with(pageRequest);
		List<LinkEmailEntity> list = mongoTemplate.find(query, LinkEmailEntity.class);
		// 获取总记录数
		PageInfo<LinkEmailEntity> info = new PageInfo<>(list);
		info.setTotal(totalCount);
		return setResultSuccess(info);
	}
	
	public void getRpc() {
		try {
			String baseStr = HttpUtil.get(HTTP_URL + HttpAddressEunms.LINKEMAIL_LIST.getName());
			RpcResponseEntity responseEntity = JSONObject.parseObject(baseStr, RpcResponseEntity.class);
			if (responseEntity.getData() != null && responseEntity.getData().toString().length() > 0) {
				List<LinkEmailEntity> list = JSONArray.parseArray(responseEntity.getData().toString(), LinkEmailEntity.class);
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						Query query = new Query(Criteria.where("_id").is(list.get(i).getId()));
						boolean exists = mongoTemplate.exists(query, LinkEmailEntity.class, "link_email");
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

	@Override
	public ResponseBase update(@RequestBody LinkEmailEntity entity) {
		try {
			if(entity != null && entity.getId() != null) {
				LinkEmailEntity emailEntity = mongoTemplate.findById(entity.getId(), LinkEmailEntity.class);
				if(emailEntity != null) {
					if(!entity.getEmail().equals(emailEntity.getEmail())) {
						Query query = new Query();
						if(entity.getLanguage() != null && entity.getLanguage().length() > 0) {
							query.addCriteria(Criteria.where("language").is(entity.getLanguage()));
						}else {
							return setResultError("请选择语言!!!");
						}
						if(entity.getEmail() != null && entity.getEmail().length() > 0) {
							query.addCriteria(Criteria.where("email").is(entity.getEmail()));
						}else {
							return setResultError("请填写邮箱!!!");
						}
						LinkEmailEntity linkEmailEntity = mongoTemplate.findOne(query, LinkEmailEntity.class);
						if(linkEmailEntity != null) {
							return setResultError("此邮箱已存在");
						}
					}
					JSONObject jsonObject = (JSONObject) JSONObject.toJSON(entity);
					String jsonParam = JSON.toJSONString(jsonObject);
					String res = HttpUtil.sendPostRequest(HTTP_URL+HttpAddressEunms.LINKEMAIL_UPDATE.getName(), jsonParam);
					RpcResponseEntity rpcResponse = JSONObject.parseObject(res, RpcResponseEntity.class);
					if(rpcResponse.getSuccess() != null && rpcResponse.getSuccess()) {
						entity.setGmtModified(new Date());
						mongoTemplate.save(entity);
						return setResultSuccess();
					}
				}else {
					return setResultError(Constants.ERROR);
				}

				
			}
			return setResultError(Constants.ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

}
