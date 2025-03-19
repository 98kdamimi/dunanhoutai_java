package com.junyang.service.impl;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.junyang.aop.SysLogAnnotation;
import com.junyang.base.BaseApiService;
import com.junyang.base.ResponseBase;
import com.junyang.constants.Constants;
import com.junyang.entity.ipWhitelist.IpWhitelistEntity;
import com.junyang.service.IpWhitelistService;
import com.junyang.utils.GenericityUtil;

@RestController
@Transactional
@CrossOrigin
public class IpWhitelistServiceImpl extends BaseApiService implements IpWhitelistService{
	
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	@SysLogAnnotation(module = "ip白名单", type = "POST", remark = "查询ip白名单")
	public ResponseBase getList(@RequestBody IpWhitelistEntity entity) {
		try {
			Query query = new Query();
			if(entity.getIpSite() != null && entity.getIpSite().length() > 0) {
				query.addCriteria(Criteria.where("ipSite").is(entity.getIpSite()));
			}
			long totalCount = mongoTemplate.count(query, IpWhitelistEntity.class);// 总条数
			// 构建分页请求对象
			int pageNumber = Math.max(entity.getPageNumber() - 1, 0);
			PageRequest pageRequest = PageRequest.of(pageNumber, entity.getPageSize(),
					Sort.by(Sort.Direction.DESC, "setTime"));
			query.with(pageRequest);
			List<IpWhitelistEntity> list = mongoTemplate.find(query, IpWhitelistEntity.class);
			// 获取总记录数
			PageInfo<IpWhitelistEntity > info = new PageInfo<>(list);
			info.setTotal(totalCount);
			return setResultSuccess(info);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "ip白名单", type = "GET", remark = "删除ip白名单")
	public ResponseBase delete(String id) {
		try {
			Query query = new Query(Criteria.where("_id").is(id));
			boolean exists = mongoTemplate.exists(query, IpWhitelistEntity.class, "ip_whitelist");
			if (exists) {
				mongoTemplate.remove(query, IpWhitelistEntity.class);
				return setResultSuccess();
			} else {
				return setResultError(Constants.ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "ip白名单", type = "POST", remark = "新增ip白名单")
	public ResponseBase add(@RequestBody IpWhitelistEntity entity) {
		try {
			Query query = new Query(Criteria.where("ipSite").is(entity.getIpSite()));
			IpWhitelistEntity whitelistEntity = mongoTemplate.findOne(query, IpWhitelistEntity.class);
			if(whitelistEntity != null) {
				return setResultError("此ip已存在，无法再次添加!");
			}
			GenericityUtil.setDate(entity);
			mongoTemplate.insert(entity);
			return setResultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

}
