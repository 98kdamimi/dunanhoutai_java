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
import com.github.pagehelper.PageInfo;
import com.junyang.aop.SysLogAnnotation;
import com.junyang.base.BaseApiService;
import com.junyang.base.ResponseBase;
import com.junyang.constants.Constants;
import com.junyang.entity.AbbreviationEntity.AbbreviationEntity;
import com.junyang.service.AbbreviationService;
import com.junyang.utils.GenericityUtil;

@RestController
@Transactional
@CrossOrigin
public class AbbreviationServiceImpl extends BaseApiService implements AbbreviationService{
	
	@Value("${http_url}")
	private String HTTP_URL;

	@Autowired
	@Qualifier("secondaryMongoTemplate") // 次数据源
	private MongoTemplate secondaryMongoTemplate;

	@Override
	@SysLogAnnotation(module = "联系方式管理", type = "POST", remark = "联系方式列表查询")
	public ResponseBase getList(@RequestBody AbbreviationEntity entity) {
		try {
			Query query = new Query();
			if(entity.getLanguage() != null && entity.getLanguage().length() > 0) {
				query.addCriteria(Criteria.where("language").is(entity.getLanguage()));
			}
			long totalCount = secondaryMongoTemplate.count(query, AbbreviationEntity.class);// 总条数
			// 构建分页请求对象
			int pageNumber = Math.max(entity.getPageNumber() - 1, 0);
			PageRequest pageRequest = PageRequest.of(pageNumber, entity.getPageSize(),
					Sort.by(Sort.Direction.DESC, "language"));
			query.with(pageRequest);
			List<AbbreviationEntity> list = secondaryMongoTemplate.find(query, AbbreviationEntity.class);
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
	@SysLogAnnotation(module = "联系方式管理", type = "POST", remark = "删除联系方式")
	public ResponseBase delete(String id) {
		try {
			if(id != null && id.length() > 0) {
				 Query query = new Query(Criteria.where("_id").is(id));
				 secondaryMongoTemplate.remove(query, AbbreviationEntity.class);
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
	@SysLogAnnotation(module = "联系方式管理", type = "POST", remark = "新增联系方式")
	public ResponseBase add(@RequestBody AbbreviationEntity entity) {
		try {
			if(entity != null) {
				GenericityUtil.setDateStrTwo(entity);
				secondaryMongoTemplate.insert(entity);
				return setResultSuccess();
			}
			return setResultError(Constants.ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "联系方式管理", type = "POST", remark = "编辑联系方式")
	public ResponseBase update(@RequestBody AbbreviationEntity entity) {
		try {
			if(entity != null) {
				GenericityUtil.setDateStrTwoUp(entity);
				secondaryMongoTemplate.save(entity);
				return setResultSuccess();
			}
			return setResultError(Constants.ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	
}
