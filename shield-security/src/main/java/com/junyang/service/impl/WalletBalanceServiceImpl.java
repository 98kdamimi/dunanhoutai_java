package com.junyang.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators.ToDouble;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.github.pagehelper.PageInfo;
import com.junyang.base.BaseApiService;
import com.junyang.base.ResponseBase;
import com.junyang.entity.wallet.WalletBalanceEntity;
import com.junyang.service.WalletBalanceService;

@RestController
@Transactional
@CrossOrigin
public class WalletBalanceServiceImpl extends BaseApiService implements WalletBalanceService {

	@Autowired
	@Qualifier("secondaryMongoTemplate") // 次数据源
	private MongoTemplate secondaryMongoTemplate;

	@SuppressWarnings("rawtypes")
	@Override
	public ResponseBase getList(@RequestBody WalletBalanceEntity entity) {
		try {
			Query query = new Query();
			if (entity.getWalletAddress() != null && entity.getWalletAddress().length() > 0) {
				query.addCriteria(Criteria.where("walletAddress").regex(entity.getWalletAddress(), "i"));
			}
			if (entity.getNetwork() != null && entity.getNetwork().length() > 0) {
				query.addCriteria(Criteria.where("networkName").is(entity.getNetwork()));
			}
			if (entity.getTokenName() != null && entity.getTokenName().length() > 0) {
				query.addCriteria(
						Criteria.where("tokenBalan").elemMatch(Criteria.where("name").is(entity.getTokenName())));
			}
			query.addCriteria(Criteria.where("accountId").ne(null));
			long totalCount = secondaryMongoTemplate.count(query, WalletBalanceEntity.class);// 总条数
			// 构建分页请求对象
			int pageNumber = Math.max(entity.getPageNumber() - 1, 0);
			PageRequest pageRequest = PageRequest.of(pageNumber, entity.getPageSize(),
					Sort.by(Sort.Direction.DESC, "walletSum"));
			query.with(pageRequest);
			List<WalletBalanceEntity> list = secondaryMongoTemplate.find(query, WalletBalanceEntity.class);
			if (list != null && !list.isEmpty()) {
			    // 收集所有需要查询的 accountId
			    List<String> accountIds = list.stream()
			        .map(WalletBalanceEntity::getAccountId)
			        .filter(Objects::nonNull)
			        .distinct()
			        .collect(Collectors.toList());

			    if (!accountIds.isEmpty()) {
			        // 构建聚合查询，一次性获取所有 accountId 对应的 address
			        Aggregation aggregation = Aggregation.newAggregation(
			            Aggregation.match(Criteria.where("accounts.id").in(accountIds)), // 筛选包含目标 accountId 的文档
			            Aggregation.unwind("accounts"), // 展开 accounts 数组
			            Aggregation.match(Criteria.where("accounts.id").in(accountIds)), // 匹配目标 accountId
			            Aggregation.project() // 投影需要的字段
			                .and("accounts.id").as("accountId")
			                .and("accounts.address").as("address")
			        );

			        // 执行聚合查询
			        AggregationResults<Map> results = secondaryMongoTemplate.aggregate(
			            aggregation, "instances", Map.class); // 替换为实际的集合名

			        // 将结果转换为 accountId -> address 的映射
			        Map<String, String> accountAddressMap = results.getMappedResults().stream()
			            .collect(Collectors.toMap(
			                entry -> (String) entry.get("accountId"),
			                entry -> (String) entry.get("address"),
			                (existing, replacement) -> existing // 如果存在重复，保留现有值
			            ));

			        // 遍历 WalletBalanceEntity 列表，设置 address
			        for (WalletBalanceEntity wallet : list) {
			            String accountId = wallet.getAccountId();
			            if (accountId != null) {
			                String address = accountAddressMap.get(accountId);
			                if (address != null) {
			                    wallet.setWalletAddress(address); // 设置地址到 wallet 对象
			                }
			            }
			        }
			    }
			}
			// 获取总记录数
			PageInfo<WalletBalanceEntity> info = new PageInfo<>(list);
			info.setTotal(totalCount);
			return setResultSuccess(info);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ResponseBase getNumAll() {
		try {
			// 1. 过滤无效数据（可选）
			MatchOperation matchOperation = Aggregation.match(Criteria.where("walletSum").exists(true).ne(""));

			// 2. 将 walletSum 转换为 Double 类型
			ToDouble toStringOperator = ConvertOperators.valueOf("$walletSum").convertToDouble();

			// 3. 分组求和
			GroupOperation groupOperation = Aggregation.group().sum(toStringOperator).as("totalWalletSum");

			// 4. 定义聚合操作
			Aggregation aggregation = Aggregation.newAggregation(matchOperation, // 过滤无效数据
					Aggregation.project().and(toStringOperator).as("walletSum"), // 转换字段类型
					groupOperation // 分组求和
			);

			// 5. 执行聚合操作
			AggregationResults<Map> results = secondaryMongoTemplate.aggregate(aggregation, "walletbalances",
					Map.class);

			// 6. 提取总额
			Double totalWalletSum = null;
			if (results.getMappedResults() != null && !results.getMappedResults().isEmpty()) {
				totalWalletSum = (Double) results.getMappedResults().get(0).get("totalWalletSum");
			}

			// 7. 返回结果
			return setResultSuccess(totalWalletSum != null ? totalWalletSum : 0.0);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

}
