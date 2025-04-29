package com.junyang.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import com.junyang.entity.delegate.TronStakingEntity;
import com.junyang.service.TronStakingService;
import com.junyang.utils.GenericityUtil;

@RestController
@Transactional
@CrossOrigin
public class TronStakingServiceImpl extends BaseApiService implements TronStakingService{
	
	@Autowired
	@Qualifier("secondaryMongoTemplate") // 次数据源
	private MongoTemplate secondaryMongoTemplate;

	@Override
	public ResponseBase add(@RequestBody TronStakingEntity entity) {
		try {
			GenericityUtil.setTokenDateStr(entity);
			secondaryMongoTemplate.save(entity);
			return setResultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase find(@RequestBody TronStakingEntity entity) {
		try {
			Query query = new Query();
			if(entity.getAddress() != null && entity.getAddress().length() > 0) {
				query.addCriteria(Criteria.where("address").is(entity.getAddress()));
			}
			long totalCount = secondaryMongoTemplate.count(query, TronStakingEntity.class);// 总条数
			// 构建分页请求对象
			int pageNumber = Math.max(entity.getPageNumber() - 1, 0);
			PageRequest pageRequest = PageRequest.of(pageNumber, entity.getPageSize(),
					Sort.by(Sort.Direction.DESC, "setTime"));
			query.with(pageRequest);
			List<TronStakingEntity> list = secondaryMongoTemplate.find(query, TronStakingEntity.class);
			// 获取总记录数
			PageInfo<TronStakingEntity > info = new PageInfo<>(list);
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
			Query query = new Query(Criteria.where("_id").is(id));
			secondaryMongoTemplate.remove(query, TronStakingEntity.class);
			return setResultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

}
