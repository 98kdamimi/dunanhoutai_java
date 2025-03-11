package com.junyang.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.junyang.base.BaseApiService;
import com.junyang.base.ResponseBase;
import com.junyang.entity.delegate.TronDelegateCofigEntity;
import com.junyang.service.TronDelegateConfigService;
import com.junyang.utils.GenericityUtil;

@RestController
@Transactional
@CrossOrigin
public class TronDelegateConfigServiceImpl extends BaseApiService implements TronDelegateConfigService{
	
	@Autowired
	@Qualifier("secondaryMongoTemplate") // 次数据源
	private MongoTemplate secondaryMongoTemplate;

	@Override
	public ResponseBase add(@RequestBody TronDelegateCofigEntity entity) {
		try {
			if(entity.getInvalidationTime() != null) {
				entity.setInvalidationTime(entity.getInvalidationTime()*60000);
			}
			GenericityUtil.setTokenDateStr(entity);
			secondaryMongoTemplate.save(entity);
			return setResultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase find() {
		List<TronDelegateCofigEntity> list = secondaryMongoTemplate.findAll(TronDelegateCofigEntity.class);
		return setResultSuccess(list);
	}

	@Override
	public ResponseBase delete(String id) {
		try {
			Query query = new Query(Criteria.where("_id").is(id));
			secondaryMongoTemplate.remove(query, TronDelegateCofigEntity.class);
			return setResultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

}
