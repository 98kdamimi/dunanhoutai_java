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
import com.junyang.entity.AbbreviationEntity.AbbreviationEntity;
import com.junyang.entity.wallet.WalletBalanceEntity;
import com.junyang.service.WalletBalanceService;


@RestController
@Transactional
@CrossOrigin
public class WalletBalanceServiceImpl extends BaseApiService implements WalletBalanceService{
	
	
	@Autowired
	@Qualifier("secondaryMongoTemplate") // 次数据源
	private MongoTemplate secondaryMongoTemplate;

	@Override
	public ResponseBase getList(@RequestBody WalletBalanceEntity entity) {
		try {
			Query query = new Query();
			
			long totalCount = secondaryMongoTemplate.count(query, WalletBalanceEntity.class);// 总条数
			// 构建分页请求对象
			int pageNumber = Math.max(entity.getPageNumber() - 1, 0);
			PageRequest pageRequest = PageRequest.of(pageNumber, entity.getPageSize(),
					Sort.by(Sort.Direction.DESC, "setTime"));
			query.with(pageRequest);
			List<WalletBalanceEntity> list = secondaryMongoTemplate.find(query, WalletBalanceEntity.class);
			// 获取总记录数
			PageInfo<WalletBalanceEntity > info = new PageInfo<>(list);
			info.setTotal(totalCount);
			return setResultSuccess(info);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

}
