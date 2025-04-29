package com.junyang.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators.ToDouble;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
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
import com.junyang.entity.delegate.TronDelegatelistEntity;
import com.junyang.service.TronDelegatelistService;
import com.junyang.utils.GenericityUtil;

@RestController
@Transactional
@CrossOrigin
public class TronDelegatelistServiceImpl extends BaseApiService implements TronDelegatelistService {

	@Autowired
	@Qualifier("secondaryMongoTemplate") // 次数据源
	private MongoTemplate secondaryMongoTemplate;

	@Override
	@SysLogAnnotation(module = "委托记录管理", type = "POST", remark = "委托记录列表查询")
	public ResponseBase findList(@RequestBody TronDelegatelistEntity entity) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("tradeState").is("send"));
			if (entity.getReceiverAddress() != null && entity.getReceiverAddress().length() > 0) {
				query.addCriteria(Criteria.where("receiverAddress").regex(entity.getReceiverAddress(), "i"));
			}
			if (entity.getResourceType() != null && entity.getResourceType().length() > 0) {
				query.addCriteria(Criteria.where("resourceType").is(entity.getResourceType()));
			}
			if (entity.getStakingAddress() != null && entity.getStakingAddress().length() > 0) {
				query.addCriteria(Criteria.where("stakingAddress").is(entity.getStakingAddress()));
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

	@Override
	public ResponseBase findStatistics(@RequestBody TronDelegatelistEntity entity) {
	    try {
	        Criteria criteria = Criteria.where("tradeState").is("send");
	        if (entity.getStakingAddress() != null && !entity.getStakingAddress().isEmpty()) {
	            criteria.and("stakingAddress").is(entity.getStakingAddress());
	        } else {
	            criteria.and("stakingAddress").exists(true);
	        }
	        
	        Aggregation aggregation = Aggregation.newAggregation(
	                Aggregation.match(criteria),

	                // 转换字段，使用 $ifNull 为缺失的字段提供默认值
	                Aggregation.project().andExpression("toDouble(amount)").as("amount") // 转换 amount 为数字
	                        .andExpression("toDouble(resourceAmount)").as("resourceAmount") // 转换 resourceAmount 为数字
	                        .and("stakingAddress").as("stakingAddress"), // 保留 stakingAddress 字段

	                // 按 stakingAddress 分组，计算金额和资源金额总和
	                Aggregation.group("stakingAddress")
	                        .sum("amount").as("amount")
	                        .sum("resourceAmount").as("resourceAmount"),

	                // 根据 resourceAmount 降序排序
	                Aggregation.sort(Sort.by(Sort.Order.desc("resourceAmount"))),

	                // 最后投影出需要的字段
	                Aggregation.project("amount", "resourceAmount")
	        );

	        // 执行聚合查询
	        AggregationResults<TronDelegatelistEntity> results = secondaryMongoTemplate.aggregate(
	                aggregation, "trondelegatelists", TronDelegatelistEntity.class);

	        // 你可以根据需求返回数据格式，例如将查询结果封装成分页响应
	        List<TronDelegatelistEntity> aggregatedResults = results.getMappedResults();
	        List<TronDelegatelistEntity> temp = GenericityUtil.Page(aggregatedResults, entity.getPageNumber(), entity.getPageSize());
	        PageInfo<TronDelegatelistEntity> info = new PageInfo<>(temp);
	        info.setTotal(aggregatedResults.size());

	        return setResultSuccess(info);
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException();
	    }
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ResponseBase findStatisticsNum(@RequestBody TronDelegatelistEntity entity) {
		try {
			Criteria criteria = Criteria.where("tradeState").is("send");
	        if (entity.getStakingAddress() != null && !entity.getStakingAddress().isEmpty()) {
	            criteria.and("stakingAddress").is(entity.getStakingAddress());
	        } else {
	            criteria.and("stakingAddress").exists(true);
	        }

	        // 构建匹配操作
	        MatchOperation matchOperation = Aggregation.match(criteria);

	        // 转换字段，确保 amount 和 resourceAmount 字段为数字类型
	        ToDouble toAmountOperator = ConvertOperators.valueOf("$amount").convertToDouble();
	        ToDouble toResourceAmountOperator = ConvertOperators.valueOf("$resourceAmount").convertToDouble();

	        // 汇总操作，统计 amount 和 resourceAmount 字段的总和
	        GroupOperation groupOperation = Aggregation.group()
	                .sum(toAmountOperator).as("totalAmount")
	                .sum(toResourceAmountOperator).as("totalResourceAmount");

	        // 构建聚合操作
	        Aggregation aggregation = Aggregation.newAggregation(
	                matchOperation,
	                Aggregation.project().and(toAmountOperator).as("amount")
	                        .and(toResourceAmountOperator).as("resourceAmount"),
	                groupOperation
	        );

	        // 执行聚合查询
	        AggregationResults<Map> results = secondaryMongoTemplate.aggregate(aggregation, "trondelegatelists", Map.class);
	        return setResultSuccess(results.getMappedResults().get(0));
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new RuntimeException();
	    }
	}

}
