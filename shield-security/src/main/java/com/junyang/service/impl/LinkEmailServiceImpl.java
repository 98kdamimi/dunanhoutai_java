package com.junyang.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import com.junyang.aop.SysLogAnnotation;
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
	
	@Autowired
	@Qualifier("secondaryMongoTemplate") // 次数据源
	private MongoTemplate secondaryMongoTemplate;

	@Override
	@SysLogAnnotation(module = "联系邮箱管理", type = "POST", remark = "联系邮箱新增")
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
			LinkEmailEntity emailEntity = secondaryMongoTemplate.findOne(query, LinkEmailEntity.class);
			if(emailEntity != null) {
				return setResultError("此邮箱已存在");
			}
			GenericityUtil.setDateStrTwo(entity);
			secondaryMongoTemplate.insert(entity);
			return setResultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "联系邮箱管理", type = "GET", remark = "联系邮箱删除")
	public ResponseBase delete(String id) {
		try {
			if(id != null && id.length() > 0) {
				Query query = new Query(Criteria.where("_id").is(id));
				secondaryMongoTemplate.remove(query, LinkEmailEntity.class);
			     return setResultSuccess();
			}else {
				return setResult(400, "参数异常", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "联系邮箱管理", type = "POST", remark = "联系邮箱列表查询")
	public ResponseBase findList(@RequestBody LinkEmailEntity entity) {
		Query query = new Query();
		if(entity.getLanguage() != null && entity.getLanguage().length() > 0) {
			query.addCriteria(Criteria.where("language").is(entity.getLanguage()));
		}
		long totalCount = secondaryMongoTemplate.count(query, LinkEmailEntity.class);// 总条数
		// 构建分页请求对象
		int pageNumber = Math.max(entity.getPageNumber() - 1, 0);
		PageRequest pageRequest = PageRequest.of(pageNumber, entity.getPageSize(),
				Sort.by(Sort.Direction.DESC, "createdAt"));
		query.with(pageRequest);
		List<LinkEmailEntity> list = secondaryMongoTemplate.find(query, LinkEmailEntity.class);
		// 获取总记录数
		PageInfo<LinkEmailEntity> info = new PageInfo<>(list);
		info.setTotal(totalCount);
		return setResultSuccess(info);
	}
	

	@Override
	@SysLogAnnotation(module = "联系邮箱管理", type = "POST", remark = "联系邮箱列表编辑")
	public ResponseBase update(@RequestBody LinkEmailEntity entity) {
		try {
			if(entity != null && entity.getId() != null) {
				LinkEmailEntity emailEntity = secondaryMongoTemplate.findById(entity.getId(), LinkEmailEntity.class);
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
					GenericityUtil.setDateStrTwoUp(entity);
					secondaryMongoTemplate.save(entity);
					return setResultSuccess();
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
