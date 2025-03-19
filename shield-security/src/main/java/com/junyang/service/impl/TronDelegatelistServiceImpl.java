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
import com.junyang.aop.SysLogAnnotation;
import com.junyang.base.BaseApiService;
import com.junyang.base.ResponseBase;
import com.junyang.entity.delegate.TronDelegatelistEntity;
import com.junyang.service.TronDelegatelistService;

@RestController
@Transactional
@CrossOrigin
public class TronDelegatelistServiceImpl extends BaseApiService implements TronDelegatelistService{
	
	@Autowired
	@Qualifier("secondaryMongoTemplate") // 次数据源
	private MongoTemplate secondaryMongoTemplate;
	
	@Override
	@SysLogAnnotation(module = "委托记录管理", type = "POST", remark = "委托记录列表查询")
	public ResponseBase findList(@RequestBody TronDelegatelistEntity entity) {
		try {
			Query query = new Query();
			if (entity.getReceiverAddress() != null && entity.getReceiverAddress().length() > 0) {
				query.addCriteria(Criteria.where("receiverAddress").regex(entity.getReceiverAddress(), "i"));
			}
			if(entity.getTradeState() != null && entity.getTradeState().length() > 0) {
				query.addCriteria(Criteria.where("tradeState").is(entity.getTradeState()));
			}
			if(entity.getResourceType() != null && entity.getResourceType().length() > 0) {
				query.addCriteria(Criteria.where("resourceType").is(entity.getResourceType()));
			}
			long totalCount = secondaryMongoTemplate.count(query, TronDelegatelistEntity.class);// 总条数
			int pageNumber = Math.max(entity.getPageNumber() - 1, 0);
			PageRequest pageRequest = PageRequest.of(pageNumber, entity.getPageSize(),
					Sort.by(Sort.Direction.DESC, "timestamp"));
			query.with(pageRequest);
			List<TronDelegatelistEntity> list = secondaryMongoTemplate.find(query, TronDelegatelistEntity.class);
			PageInfo<TronDelegatelistEntity> info = new PageInfo<>(list);
			info.setTotal(totalCount);
			return setResultSuccess(info);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

}
