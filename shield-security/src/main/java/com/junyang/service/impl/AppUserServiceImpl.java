package com.junyang.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.types.Decimal128;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationExpression;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.junyang.service.AppUserService;
import com.junyang.utils.GenericityUtil;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.junyang.aop.SysLogAnnotation;
import com.junyang.base.BaseApiService;
import com.junyang.base.ResponseBase;
import com.junyang.entity.appuser.AppUserEntity;
import com.junyang.entity.appuser.AppUserLogEntity;
import com.junyang.entity.wallet.WalletBalanceInfoEntity;

@RestController
@Transactional
@CrossOrigin
public class AppUserServiceImpl extends BaseApiService implements AppUserService{
	
	@Autowired
	@Qualifier("secondaryMongoTemplate") // 次数据源
	private MongoTemplate secondaryMongoTemplate;

	@Override
	@SysLogAnnotation(module = "app用户登录日志管理", type = "POST", remark = "app用户登录日志列表查询")
	public ResponseBase findList(@RequestBody AppUserEntity entity) {
		try {
			Query query = new Query();
			if (entity.getContent() != null && entity.getContent().length() > 0) {
				query.addCriteria(Criteria.where("ipAddress").regex(entity.getContent(), "i"));
			}
			long totalCount = secondaryMongoTemplate.count(query, AppUserEntity.class);// 总条数
			int pageNumber = Math.max(entity.getPageNumber() - 1, 0);
			PageRequest pageRequest = PageRequest.of(pageNumber, entity.getPageSize(),
					Sort.by(Sort.Direction.DESC, "createdAt"));
			query.with(pageRequest);
			List<AppUserEntity> list = secondaryMongoTemplate.find(query, AppUserEntity.class);
			PageInfo<AppUserEntity> info = new PageInfo<>(list);
			info.setTotal(totalCount);
			return setResultSuccess(info);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "app用户登录日志管理", type = "POST", remark = "app用户登录日志详情列表查询")
	public ResponseBase findInfoList(@RequestBody AppUserLogEntity entity) {
		try {
			Query query = new Query();
			if (entity.getContent() != null && entity.getContent().length() > 0) {
				query.addCriteria(Criteria.where("ipAddress").regex(entity.getContent(), "i"));
			}
			if(entity.getDeviceId() != null && entity.getDeviceId().length() > 0) {
				query.addCriteria(Criteria.where("deviceId").is(entity.getDeviceId()));
			}
			long totalCount = secondaryMongoTemplate.count(query, AppUserLogEntity.class);// 总条数
			int pageNumber = Math.max(entity.getPageNumber() - 1, 0);
			PageRequest pageRequest = PageRequest.of(pageNumber, entity.getPageSize(),
					Sort.by(Sort.Direction.DESC, "createdAt"));
			query.with(pageRequest);
			List<AppUserLogEntity> list = secondaryMongoTemplate.find(query, AppUserLogEntity.class);
			PageInfo<AppUserLogEntity> info = new PageInfo<>(list);
			info.setTotal(totalCount);
			return setResultSuccess(info);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase findWalletList(@RequestBody AppUserLogEntity entity) {
		try {
			Query query = new Query();
			if(entity.getInstanceId() != null && entity.getInstanceId().length() > 0) {
				query.addCriteria(Criteria.where("instanceId").is(entity.getInstanceId()));
			}
			long totalCount = secondaryMongoTemplate.count(query, WalletBalanceInfoEntity.class);// 总条数
			int pageNumber = Math.max(entity.getPageNumber() - 1, 0);
			PageRequest pageRequest = PageRequest.of(pageNumber, entity.getPageSize(),
					Sort.by(Sort.Direction.DESC, "updatedAt"));
			query.with(pageRequest);
			List<WalletBalanceInfoEntity> list = secondaryMongoTemplate.find(query, WalletBalanceInfoEntity.class);
			PageInfo<WalletBalanceInfoEntity> info = new PageInfo<>(list);
			info.setTotal(totalCount);
			return setResultSuccess(info);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase findWalletAll(@RequestBody AppUserLogEntity entity) {
		try {
			// 1. 创建查询条件
			Criteria criteria = Criteria.where("usdValue").exists(true).ne("");

			if (entity.getInstanceId() != null && !entity.getInstanceId().isEmpty()) {
				criteria.and("instanceId").is(entity.getInstanceId());
			}
			
			MatchOperation match = Aggregation.match(criteria);

			AggregationExpression toDecimalUsd = context -> new Document("$toDecimal", "$usdValue");

			GroupOperation group = Aggregation.group("walletAddress")
					.sum(toDecimalUsd).as("totalUsdValue")
					.first("walletAddress").as("walletAddress");
			
			SortOperation sortOperation = Aggregation.sort(Sort.by(Sort.Order.desc("totalUsdValue")));

			Aggregation aggregation = Aggregation.newAggregation(match, group,sortOperation);

			AggregationResults<Document> results = secondaryMongoTemplate.aggregate(
					aggregation, 
					"walletbalanceinfos",
					Document.class);
			
			List<Document> list = results.getMappedResults();
			List<Document> newList = GenericityUtil.Page(list, entity.getPageNumber(), entity.getPageSize());

			List<Map<String, Object>> formattedResults = new ArrayList<>();
			for (Document doc : newList) {
				Map<String, Object> result = new LinkedHashMap<>();
				BigDecimal totalUsd = doc.get("totalUsdValue", Decimal128.class).bigDecimalValue();
				result.put("_id", doc.get("_id"));
				result.put("totalUsdValue", totalUsd.toPlainString());
				result.put("walletAddress", doc.get("walletAddress"));
				formattedResults.add(result);
			}
			PageInfo<Map<String, Object>> info = new PageInfo<>(formattedResults);
			info.setTotal(list.size());
			return setResultSuccess(info);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}


}
