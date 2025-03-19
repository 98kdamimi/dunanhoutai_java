package com.junyang.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.junyang.aop.SysLogAnnotation;
import com.junyang.base.BaseApiService;
import com.junyang.base.ResponseBase;
import com.junyang.entity.dapp.DiscoverLableStateEntity;
import com.junyang.service.DiscoverStateService;
import com.junyang.utils.GenericityUtil;

@RestController
@Transactional
@CrossOrigin
public class DiscoverStateServiceImpl extends BaseApiService implements DiscoverStateService {

	@Autowired
	@Qualifier("secondaryMongoTemplate") // 次数据源
	private MongoTemplate secondaryMongoTemplate;

	@Override
	@SysLogAnnotation(module = "发现页标签控制", type = "GET", remark = "发现页标签查询")
	public ResponseBase getList() {
		try {
			List<DiscoverLableStateEntity> list = secondaryMongoTemplate.findAll(DiscoverLableStateEntity.class);
			if (list == null || list.size() < 1) {
				DiscoverLableStateEntity entity = new DiscoverLableStateEntity();
				entity.setStates(false);
				GenericityUtil.setTokenDateStr(entity);
				secondaryMongoTemplate.insert(entity);
				List<DiscoverLableStateEntity> newList = secondaryMongoTemplate.findAll(DiscoverLableStateEntity.class);
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
	@SysLogAnnotation(module = "发现页标签控制", type = "GET", remark = "发现页标签状态修改")
	public ResponseBase update(boolean states) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Query query = new Query();

			Update update = new Update().set("states", states).set("updatedAt", dateFormat.format(new Date())) ;

			secondaryMongoTemplate.updateMulti(query, update, DiscoverLableStateEntity.class);
			return setResultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

}
