package com.junyang.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.junyang.aop.SysLogAnnotation;
import com.junyang.base.BaseApiService;
import com.junyang.base.ResponseBase;
import com.junyang.entity.response.DicEntity;
import com.junyang.entity.trading.TradingEntity;
import com.junyang.enums.TradingLableEunms;
import com.junyang.service.TradingService;
import com.junyang.utils.GenericityUtil;

@RestController
@Transactional
@CrossOrigin
public class TradingServiceImpl  extends BaseApiService implements TradingService {
	
	@Autowired
	@Qualifier("secondaryMongoTemplate") // 次数据源
	private MongoTemplate secondaryMongoTemplate;

	@Override
	@SysLogAnnotation(module = "交易标签状态管理", type = "GET", remark = "交易标签状态列表查询")
	public ResponseBase getList() {
		try {
			List<TradingEntity> list = secondaryMongoTemplate.findAll(TradingEntity.class);
			if (list == null || list.size() < 1) {
				List<DicEntity> dicList = TradingLableEunms.getList();
				for (int i = 0; i < dicList.size(); i++) {
					TradingEntity entity = new TradingEntity();
					entity.setStates(false);
					entity.setLableId(dicList.get(i).getId());
					entity.setLableName(dicList.get(i).getName());
					GenericityUtil.setTokenDateStr(entity);
					secondaryMongoTemplate.insert(entity);
				}
				List<TradingEntity> newList = secondaryMongoTemplate.findAll(TradingEntity.class);
				return setResultSuccess(newList);
			} else {
				return setResultSuccess(list);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "交易标签状态管理", type = "GET", remark = "交易标签状态修改")
	public ResponseBase update(String id, boolean states) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Query query = new Query(Criteria.where("_id").is(id));
			Update update = new Update().set("states", states).set("updatedAt", dateFormat.format(new Date())) ;
			secondaryMongoTemplate.updateFirst(query, update, TradingEntity.class);
			return setResultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

}
