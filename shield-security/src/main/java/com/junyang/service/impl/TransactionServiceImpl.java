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
import com.junyang.entity.transaction.TransactionEntity;
import com.junyang.service.TransactionService;

@RestController
@Transactional
@CrossOrigin
public class TransactionServiceImpl extends BaseApiService implements TransactionService{
	
	@Autowired
	@Qualifier("secondaryMongoTemplate") // 次数据源
	private MongoTemplate secondaryMongoTemplate;

	@Override
	@SysLogAnnotation(module = "流水记录管理", type = "GET", remark = "流水记录列表查询")
	public ResponseBase findList(@RequestBody TransactionEntity entity) {
		try {
			Query query = new Query();
			if(entity.getTxid() != null && entity.getTxid().length() > 0) {
				query.addCriteria(Criteria.where("txid").is(entity.getTxid()));
			}
			if(entity.getFromAddress() != null && entity.getFromAddress().length() > 0) {
				query.addCriteria(Criteria.where("fromAddress").is(entity.getFromAddress()));
			}
			if(entity.getToAddress() != null && entity.getToAddress().length() > 0) {
				query.addCriteria(Criteria.where("toAddress").is(entity.getToAddress()));
			}
			if(entity.getNetworkName() != null && entity.getNetworkName().length() > 0) {
				query.addCriteria(Criteria.where("networkName").is(entity.getNetworkName()));
			}
			if(entity.getTokenName() != null && entity.getTokenName().length() > 0) {
				query.addCriteria(Criteria.where("tokenName").is(entity.getTokenName()));
			}
			long totalCount = secondaryMongoTemplate.count(query, TransactionEntity.class);// 总条数
			// 构建分页请求对象
			int pageNumber = Math.max(entity.getPageNumber() - 1, 0);
			PageRequest pageRequest = PageRequest.of(pageNumber, entity.getPageSize(),
					Sort.by(Sort.Direction.DESC, "createdAt"));
			query.with(pageRequest);
			List<TransactionEntity> list = secondaryMongoTemplate.find(query, TransactionEntity.class);
			// 获取总记录数
			PageInfo<TransactionEntity > info = new PageInfo<>(list);
			info.setTotal(totalCount);
			return setResultSuccess(info);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}

	}

}
