package com.junyang.service.impl;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
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
import org.springframework.data.mongodb.core.aggregation.ConvertOperators;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.SortOperation;
import org.springframework.data.mongodb.core.aggregation.ConvertOperators.ToDouble;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.github.pagehelper.PageInfo;
import com.junyang.base.BaseApiService;
import com.junyang.base.ResponseBase;
import com.junyang.constants.Constants;
import com.junyang.entity.wallet.WalletBalanceInfoEntity;
import com.junyang.service.WalletBalanceInfoService;
import com.junyang.utils.GenericityUtil;

@RestController
@Transactional
@CrossOrigin
public class WalletBalanceInfoServiceImpl extends BaseApiService implements WalletBalanceInfoService {

	@Autowired
	@Qualifier("secondaryMongoTemplate") // 次数据源
	private MongoTemplate secondaryMongoTemplate;

	@Override
	public ResponseBase getList(@RequestBody WalletBalanceInfoEntity entity) {
		try {
			Query query = new Query();
			if (entity.getWalletAddress() != null && entity.getWalletAddress().length() > 0) {
				query.addCriteria(new Criteria()
						.andOperator(Criteria.where("walletAddress").regex(entity.getWalletAddress(), "i")));
			}
			if (entity.getNetworkName() != null && entity.getNetworkName().length() > 0) {
				query.addCriteria(Criteria.where("networkName").is(entity.getNetworkName()));
			}
			if (entity.getName() != null && entity.getName().length() > 0) {
				query.addCriteria(Criteria.where("name").is(entity.getName()));
			}
			query.addCriteria(Criteria.where("accountId").ne(null));
			query.with(Sort.by(Sort.Order.desc("usdValue")));
			long totalCount = secondaryMongoTemplate.count(query, WalletBalanceInfoEntity.class);// 总条数
//			// 构建分页请求对象
			int pageNumber = Math.max(entity.getPageNumber() - 1, 0);
			PageRequest pageRequest = PageRequest.of(pageNumber, entity.getPageSize());
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

	@SuppressWarnings({ "rawtypes" })
	@Override
	public ResponseBase getNumAll(@RequestBody WalletBalanceInfoEntity entity) {
		try {

			Criteria criteria = Criteria.where("usdValue").exists(true).ne("");

			if (entity.getName() != null && !entity.getName().isEmpty()) {
				criteria.and("name").is(entity.getName());
			}
			if (entity.getNetworkName() != null && !entity.getNetworkName().isEmpty()) {
				criteria.and("networkName").is(entity.getNetworkName());
			}
			if (entity.getWalletAddress() != null && !entity.getWalletAddress().isEmpty()) {
				criteria.and("walletAddress").is(entity.getWalletAddress());
			}

			MatchOperation matchOperation = Aggregation.match(criteria);

			ToDouble toStringOperator = ConvertOperators.valueOf("$usdValue").convertToDouble();

			GroupOperation groupOperation = Aggregation.group().sum(toStringOperator).as("totalUsdValue");

			Aggregation aggregation = Aggregation.newAggregation(matchOperation, 
					Aggregation.project().and(toStringOperator).as("usdValue"),
					groupOperation
			);

			AggregationResults<Map> results = secondaryMongoTemplate.aggregate(aggregation, "walletbalanceinfos",
					Map.class);

			Double totalUsdValue = null;
			if (results.getMappedResults() != null && !results.getMappedResults().isEmpty()) {
				totalUsdValue = (Double) results.getMappedResults().get(0).get("totalUsdValue");
			}

			String formattedTotalUsdValue = new BigDecimal(totalUsdValue != null ? totalUsdValue : 0.0)
					.setScale(4, RoundingMode.HALF_UP).toPlainString();

			return setResultSuccess(formattedTotalUsdValue, Constants.SUCCESS);

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public ResponseBase getTotalUsdValueByName(@RequestBody WalletBalanceInfoEntity entity) {
		try {
			// 1. 创建查询条件
			Criteria criteria = Criteria.where("usdValue").exists(true).ne("");

			if (entity.getName() != null && !entity.getName().isEmpty()) {
				criteria.and("name").is(entity.getName());
			}
			if (entity.getNetworkName() != null && !entity.getNetworkName().isEmpty()) {
				criteria.and("networkName").is(entity.getNetworkName());
			}
			if (entity.getWalletAddress() != null && !entity.getWalletAddress().isEmpty()) {
				criteria.and("walletAddress").is(entity.getWalletAddress());
			}
			if (entity.getAddress() != null && !entity.getAddress().isEmpty()) {
				criteria.and("address").is(entity.getAddress());
			}

			MatchOperation match = Aggregation.match(criteria);

			AggregationExpression toDecimalUsd = context -> new Document("$toDecimal", "$usdValue");
			AggregationExpression toDecimalBalance = context -> new Document("$toDecimal", "$balance");

			GroupOperation group = Aggregation.group("name")
					.sum(toDecimalUsd).as("totalUsdValue")
					.sum(toDecimalBalance).as("totalBalance")
					.first("address").as("address")
					.first("symbol").as("symbol");
			
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
				BigDecimal totalBalance = doc.get("totalBalance", Decimal128.class).bigDecimalValue();
				result.put("_id", doc.get("_id"));
				result.put("totalUsdValue", totalUsd.toPlainString());
				result.put("totalBalance", totalBalance.toPlainString());
				result.put("address", doc.get("address"));
				result.put("symbol", doc.get("symbol"));
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
