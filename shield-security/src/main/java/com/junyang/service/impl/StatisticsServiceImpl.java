package com.junyang.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.junyang.aop.SysLogAnnotation;
import com.junyang.base.BaseApiService;
import com.junyang.base.ResponseBase;
import com.junyang.entity.appuser.AppUserEntity;
import com.junyang.entity.instance.InstanceEntity;
import com.junyang.entity.response.AccountStatisticsEntity;
import com.junyang.entity.response.TotalAccounts;
import com.junyang.query.PublicQueryEntity;
import com.junyang.service.StatisticsService;
import com.junyang.utils.DateUtil;

@RestController
@Transactional
@CrossOrigin
public class StatisticsServiceImpl extends BaseApiService implements StatisticsService {

//	@Autowired
//	@Qualifier("primaryMongoTemplate") // 主数据源
//	private MongoTemplate primaryMongoTemplate;

	@Autowired
	@Qualifier("secondaryMongoTemplate") // 次数据源
	private MongoTemplate secondaryMongoTemplate;
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	@SysLogAnnotation(module = "统计", type = "GET", remark = "注册数量统计查询")
	public ResponseBase equipment() {
		AccountStatisticsEntity statisticsEntity = new AccountStatisticsEntity();
		// 获取今日新增量
		long countToday = this.getDateCount(DateUtil.getStartOfDay(new Date()), DateUtil.getEndOfDay(new Date()));
		statisticsEntity.setCountToday(countToday);
		// 获取近七天新增量
		long countWeek = this.getDateCount(DateUtil.getSevenDaysAgo(new Date()), DateUtil.getStartOfDay(new Date()));
		statisticsEntity.setCountWeek(countWeek);

		// 获取本月新增量
		long countMonth = this.getDateCount(DateUtil.getStartOfMonth(new Date()), DateUtil.getEndOfMonth(new Date()));
		statisticsEntity.setCountMonth(countMonth);
		// 获取本年新增量
		long countYear = this.getDateCount(DateUtil.getStartOfYear(new Date()), DateUtil.getEndOfYear(new Date()));
		statisticsEntity.setCountYear(countYear);
		// 总量
		long countAll = secondaryMongoTemplate.count(new Query(), AppUserEntity.class);
		statisticsEntity.setCountAll(countAll);
		return setResultSuccess(statisticsEntity);
	}

	@Override
	@SysLogAnnotation(module = "统计", type = "POST", remark = "注册数量统计图表")
	public ResponseBase equipmentChar(@RequestBody PublicQueryEntity entity) {
		try {
			//设置默认
	        if ((entity.getBegTime() == null || entity.getBegTime().isEmpty()) &&
	            (entity.getEndTime() == null || entity.getEndTime().isEmpty()) &&
	            (entity.getBegMonth() == null || entity.getBegMonth().isEmpty()) &&
	            (entity.getEndMonth() == null || entity.getEndMonth().isEmpty())) {
	        	String endTime = dateFormat.format(new Date());
	    		String begTime = DateUtil.addDate(endTime, -6);
	    		entity.setBegTime(begTime);
	    		entity.setEndTime(endTime);
	        }
			if (entity.getBegTime() != null && entity.getBegTime().length() > 0 && entity.getEndTime() != null
					&& entity.getEndTime().length() > 0) {
				List<String> list = DateUtil.getDateRangeAsString(entity.getBegTime(), entity.getEndTime());
				List<Long> countList = new ArrayList<>();
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						long count = this.getDateCount(DateUtil.getStartOfDay(dateFormat.parse(list.get(i))),
								DateUtil.getEndOfDay(dateFormat.parse(list.get(i))));
						countList.add(count);
					}
				}
				Map<String, Object> map = new HashMap<>();
				map.put("xList", list);
				map.put("yList", countList);
				return setResultSuccess(map);
			} else if (entity.getBegMonth() != null && entity.getBegMonth().length() > 0 && entity.getEndMonth() != null
					&& entity.getEndMonth().length() > 0) {
				List<String> list = DateUtil.getMonthRange(entity.getBegMonth(), entity.getEndMonth());
				List<Long> countList = new ArrayList<>();
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						String begMonth = DateUtil.getMonthStart(list.get(i));
						String endMonth = DateUtil.getMonthEnd(list.get(i));
						long count = this.getDateCount(begMonth, endMonth);
						countList.add(count);
					}
				}
				Map<String, Object> map = new HashMap<>();
				map.put("xList", list);
				map.put("yList", countList);
				return setResultSuccess(map);
			}
			return setResultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "统计", type = "GET", remark = "账号数量统计")
	public ResponseBase account() {
		AccountStatisticsEntity statisticsEntity = new AccountStatisticsEntity();
		// 获取今日新增量
		long countToday = this.getAccountCount(DateUtil.getStartOfDay(new Date()), DateUtil.getEndOfDay(new Date()));
		statisticsEntity.setCountToday(countToday);

		// 获取近七天新增量
		long countWeek = this.getAccountCount(DateUtil.getSevenDaysAgo(new Date()), DateUtil.getStartOfDay(new Date()));
		statisticsEntity.setCountWeek(countWeek);

		// 获取本月新增量
		long countMonth = this.getAccountCount(DateUtil.getStartOfMonth(new Date()),
				DateUtil.getEndOfMonth(new Date()));
		statisticsEntity.setCountMonth(countMonth);

		// 获取本年新增量
		long countYear = this.getAccountCount(DateUtil.getStartOfYear(new Date()), DateUtil.getEndOfYear(new Date()));
		statisticsEntity.setCountYear(countYear);

		// 总量
		long countAll = this.getTotalAccountsCount();
		statisticsEntity.setCountAll(countAll);
		return setResultSuccess(statisticsEntity);
	}

	@Override
	@SysLogAnnotation(module = "统计", type = "POST", remark = "账号统计图表")
	public ResponseBase accountChar(@RequestBody PublicQueryEntity entity) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			//设置默认
	        if ((entity.getBegTime() == null || entity.getBegTime().isEmpty()) &&
	            (entity.getEndTime() == null || entity.getEndTime().isEmpty()) &&
	            (entity.getBegMonth() == null || entity.getBegMonth().isEmpty()) &&
	            (entity.getEndMonth() == null || entity.getEndMonth().isEmpty())) {
	        	String endTime = dateFormat.format(new Date());
	    		String begTime = DateUtil.addDate(endTime, -6);
	    		entity.setBegTime(begTime);
	    		entity.setEndTime(endTime);
	        }
			if (entity.getBegTime() != null && entity.getBegTime().length() > 0 && entity.getEndTime() != null
					&& entity.getEndTime().length() > 0) {
				List<String> list = DateUtil.getDateRangeAsString(entity.getBegTime(), entity.getEndTime());
				List<Long> countList = new ArrayList<>();
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						long count = this.getAccountCount(DateUtil.getStartOfDay(dateFormat.parse(list.get(i))),
								DateUtil.getEndOfDay(dateFormat.parse(list.get(i))));
						countList.add(count);
					}
				}
				Map<String, Object> map = new HashMap<>();
				map.put("xList", list);
				map.put("yList", countList);
				return setResultSuccess(map);
			} else if (entity.getBegMonth() != null && entity.getBegMonth().length() > 0 && entity.getEndMonth() != null
					&& entity.getEndMonth().length() > 0) {
				List<String> list = DateUtil.getMonthRange(entity.getBegMonth(), entity.getEndMonth());
				List<Long> countList = new ArrayList<>();
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						String begMonth = DateUtil.getMonthStart(list.get(i));
						String endMonth = DateUtil.getMonthEnd(list.get(i));
						long count = this.getAccountCount(begMonth, endMonth);
						countList.add(count);
					}
				}
				Map<String, Object> map = new HashMap<>();
				map.put("xList", list);
				map.put("yList", countList);
				return setResultSuccess(map);
			}
			return setResultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	// 根据时间范围查询记录数
	public long getDateCount(String startDate, String endDate) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"); // 根据你的时间格式调整
			Date start = sdf.parse(startDate);
			Date end = sdf.parse(endDate);
			// 创建查询
			Query query = new Query();
			query.addCriteria(Criteria.where("createdAt").gte(start).lte(end));
			return secondaryMongoTemplate.count(query, AppUserEntity.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	// 符合条件的accout数量
	public long getAccountCount(String startDate, String endDate) {
		Query query = new Query();
		query.addCriteria(Criteria.where("createdAt").gte(startDate).lte(endDate));
		List<InstanceEntity> list = secondaryMongoTemplate.find(query, InstanceEntity.class);
		long temp = 0;
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				temp += list.get(i).getAccounts().size();
			}
		}
		return temp;
	}

	// 所有accout数量
	public long getTotalAccountsCount() {
		Aggregation aggregation = Aggregation.newAggregation(Aggregation.unwind("accounts"), // 展开 accounts 数组
				Aggregation.count().as("totalAccounts") // 统计所有 accounts 的数量
		);
		AggregationResults<TotalAccounts> results = secondaryMongoTemplate.aggregate(aggregation, "instances",
				TotalAccounts.class);
		return results.getMappedResults().isEmpty() ? 0 : results.getMappedResults().get(0).getTotalAccounts();
	}

	// 符合条件的accout数量（备选）
	public long getFilteredAccountsCount(String startDate, String endDate) {
		Aggregation aggregation = Aggregation.newAggregation(
				Aggregation.match(Criteria.where("createdAt").gte(startDate).lte(endDate)),
				Aggregation.unwind("accounts"), Aggregation.count().as("filteredAccounts"));

		AggregationResults<TotalAccounts> results = secondaryMongoTemplate.aggregate(aggregation, "instances",
				TotalAccounts.class);

		// 返回统计结果，或者为 0
		return results.getMappedResults().isEmpty() ? 0 : results.getMappedResults().get(0).getTotalAccounts();
	}

	// 格式化是按后根据时间范围查询记录数（备选）
	public long countByDateRange(String startDate, String endDate) {
		Aggregation aggregation = Aggregation.newAggregation(
				Aggregation.match(Criteria.where("createdAt").gte(startDate).lte(endDate)), // 原始字符串匹配
				Aggregation.project("createdAt") // 显示 createdAt 字段
						.andExpression("{$dateFromString: {dateString: \"$createdAt\", format: \"%Y-%m-%d\"}}")
						.as("parsedCreatedAt") // 将字符串转换为 Date 类型
		);

		AggregationResults<InstanceEntity> results = secondaryMongoTemplate.aggregate(aggregation, "instances",
				InstanceEntity.class);
		return results.getMappedResults().size();
	}

}
