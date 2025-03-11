package com.junyang.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.junyang.aop.SysLogAnnotation;
import com.junyang.base.BaseApiService;
import com.junyang.base.ResponseBase;
import com.junyang.constants.Constants;
import com.junyang.entity.response.RpcResponseEntity;
import com.junyang.entity.version.ReleaseEntity;
import com.junyang.enums.ForceUpdateEnums;
import com.junyang.enums.HttpAddressEunms;
import com.junyang.enums.ReleaseStateEnums;
import com.junyang.query.PublicQueryEntity;
import com.junyang.service.ReleaseService;
import com.junyang.utils.GenericityUtil;
import com.junyang.utils.HttpUtil;
import com.junyang.utils.RedisUtil;
import org.springframework.data.mongodb.core.query.Update;

@RestController
@Transactional
@CrossOrigin
public class ReleaseServiceImpl extends BaseApiService implements ReleaseService {
	
	@Value("${http_url}")
	private String HTTP_URL;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private RedisUtil redisUtil;

	@Override
	public ResponseBase add(@RequestBody ReleaseEntity entity) {
		try {
			entity.getHardware().setOnlineState(ReleaseStateEnums.WAIT_LINE.getIndex());
			entity.getSoftware().setOnlineState(ReleaseStateEnums.WAIT_LINE.getIndex());
			GenericityUtil.setTokenDateStr(entity);
			mongoTemplate.insert(entity);
			// 更新通知
			redisUtil.set(Constants.MSG_KEY, JSON.toJSON(entity));
			return setResultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase softwareList(@RequestBody PublicQueryEntity entity) {
		try {
			Query query = new Query();
			if (entity.getStatus() != null && entity.getStatus().length() > 0) {
				query.addCriteria(Criteria.where("software.onlineState").is(Integer.parseInt(entity.getStatus())));
			}
			long totalCount = mongoTemplate.count(query, ReleaseEntity.class);// 总条数
			// 构建分页请求对象
			int pageNumber = Math.max(entity.getPageNumber() - 1, 0);
			PageRequest pageRequest = PageRequest.of(pageNumber, entity.getPageSize(),
					Sort.by(Sort.Direction.DESC, "createdAt"));
			query.with(pageRequest);
			List<ReleaseEntity> list = mongoTemplate.find(query, ReleaseEntity.class);
			// 获取总记录数
			PageInfo<ReleaseEntity> info = new PageInfo<>(list);
			info.setTotal(totalCount);
			return setResultSuccess(info);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase onlineSoftware(String id, Integer forceUpdateLable) {
		try {
			ReleaseEntity entity = mongoTemplate.findById(id, ReleaseEntity.class);
			if (entity == null) {
				return setResultError(Constants.ERROR);
			}
			if (ForceUpdateEnums.ENFORCEMENT.getIndex().equals(forceUpdateLable)) {// 强制更新
				entity.getSoftware().getAndroid().setForceUpdateVersion(entity.getSoftware().getAndroid().getVersion());
				entity.getSoftware().getIos().setForceUpdateVersion(entity.getSoftware().getIos().getVersion());
				entity.setForceUpdateLable(ForceUpdateEnums.ENFORCEMENT.getIndex());
			}
//			ReleaseEntity releaseEntity = new ReleaseEntity();
//			BeanUtils.copyProperties(entity, releaseEntity);
//			releaseEntity.getSoftware().setOnlineState(null);
//			releaseEntity.getHardware().setOnlineState(null);
			String jsonParam = JSON.toJSONString(entity);
			String res = HttpUtil.sendPostRequest(HTTP_URL + HttpAddressEunms.ONLINE.getName(), jsonParam);
			RpcResponseEntity rpcResponse = JSONObject.parseObject(res, RpcResponseEntity.class);
			if (rpcResponse.getSuccess() != null && rpcResponse.getSuccess()) {
				entity.getSoftware().setOnlineState(ReleaseStateEnums.TOP_LINE.getIndex());
				// 创建查询条件，表示所有文档
				Query query = new Query();
				// 创建更新操作
				Update update = new Update();
				update.set("software.onlineState", ReleaseStateEnums.DOWN_LINE.getIndex());
				// 执行更新操作
				mongoTemplate.updateMulti(query, update, ReleaseEntity.class);
				
				// 执行更新操作
				mongoTemplate.save(entity);
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
	public ResponseBase hardwareList(@RequestBody PublicQueryEntity entity) {
		try {
			Query query = new Query();
			if (entity.getStatus() != null && entity.getStatus().length() > 0) {
				query.addCriteria(Criteria.where("hardware.onlineState").is(Integer.parseInt(entity.getStatus())));
			}
			long totalCount = mongoTemplate.count(query, ReleaseEntity.class);// 总条数
			// 构建分页请求对象
			int pageNumber = Math.max(entity.getPageNumber() - 1, 0);
			PageRequest pageRequest = PageRequest.of(pageNumber, entity.getPageSize(),
					Sort.by(Sort.Direction.DESC, "createdAt"));
			query.with(pageRequest);
			List<ReleaseEntity> list = mongoTemplate.find(query, ReleaseEntity.class);
			// 获取总记录数
			PageInfo<ReleaseEntity> info = new PageInfo<>(list);
			info.setTotal(totalCount);
			return setResultSuccess(info);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase onlineHardware(String id) {
		try {
			ReleaseEntity entity = mongoTemplate.findById(id, ReleaseEntity.class);
			if (entity == null) {
				return setResultError(Constants.ERROR);
			}
//			ReleaseEntity releaseEntity = new ReleaseEntity();
//			BeanUtils.copyProperties(entity, releaseEntity);
//			releaseEntity.getSoftware().setOnlineState(null);
//			releaseEntity.getHardware().setOnlineState(null);
			String jsonParam = JSON.toJSONString(entity);
			String res = HttpUtil.sendPostRequest(HTTP_URL + HttpAddressEunms.ONLINE.getName(), jsonParam);
			RpcResponseEntity rpcResponse = JSONObject.parseObject(res, RpcResponseEntity.class);
			if (rpcResponse.getSuccess() != null && rpcResponse.getSuccess()) {
				// 创建查询条件，表示所有文档
				Query query = new Query();
				// 创建更新操作，将 status 字段更新为 newStatus
				Update update = new Update();
				update.set("hardware.onlineState", ReleaseStateEnums.DOWN_LINE.getIndex());
				// 执行更新操作
				mongoTemplate.updateMulti(query, update, ReleaseEntity.class);

				// 执行更新操作
				entity.getHardware().setOnlineState(ReleaseStateEnums.TOP_LINE.getIndex());
				mongoTemplate.save(entity);
				return setResultSuccess();
			}else {
				return setResultError(Constants.ERROR);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "应用及硬件版本管理", type = "POST", remark = "新版本提示")
	public ResponseBase msgWarn() {
		Object obj = redisUtil.get(Constants.MSG_KEY);
		if (obj != null) {
			return setResultSuccess(obj);
		} else {
			return setResultSuccess();
		}
	}

	@Override
	public ResponseBase deleteMsg() {
		try {
			redisUtil.del(Constants.MSG_KEY);
			return setResultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

}
