package com.junyang.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.junyang.base.BaseApiService;
import com.junyang.base.ResponseBase;
import com.junyang.entity.network.NetWorkEntity;
import com.junyang.entity.system.SysLogEntity;
import com.junyang.query.PublicQueryEntity;
import com.junyang.service.LogService;

@RestController
@Transactional
@CrossOrigin
public class LogServiceImpl extends BaseApiService implements LogService{
	

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public ResponseBase findList(@RequestBody PublicQueryEntity entity) {
		Query query = new Query();
//		if(entity.getName() != null && entity.getName().length() > 0) {
//			query.addCriteria(Criteria.where("name").is(entity.getName()));
//		}
		long totalCount = mongoTemplate.count(query, SysLogEntity.class);// 总条数
		// 构建分页请求对象
		int pageNumber = Math.max(entity.getPageNumber() - 1, 0);
		PageRequest pageRequest = PageRequest.of(pageNumber, entity.getPageSize(),
				Sort.by(Sort.Direction.ASC, "begTime"));
		query.with(pageRequest);
		// 执行分页查询
		List<SysLogEntity> list = mongoTemplate.find(query, SysLogEntity.class);
		// 获取总记录数
		PageInfo<SysLogEntity> info = new PageInfo<>(list);
		info.setTotal(totalCount);
		return setResultSuccess(info);
	}

}
