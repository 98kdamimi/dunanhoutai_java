package com.junyang.service.impl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.junyang.aop.SysLogAnnotation;
import com.junyang.base.BaseApiService;
import com.junyang.base.ResponseBase;
import com.junyang.constants.Constants;
import com.junyang.entity.dapp.DappEntity;
import com.junyang.entity.network.NetWorkEntity;
import com.junyang.entity.response.RpcResponseEntity;
import com.junyang.enums.HttpAddressEunms;
import com.junyang.enums.ReleaseStateEnums;
import com.junyang.query.PublicQueryEntity;
import com.junyang.service.DappService;
import com.junyang.utils.HttpUtil;

@RestController
@Transactional
@CrossOrigin
public class DappServiceImpl extends BaseApiService implements DappService {

	@Value("${http_url}")
	private String HTTP_URL;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	@SysLogAnnotation(module = "Dapp发现页配置管理", type = "GET", remark = "获取列表")
	public ResponseBase rpcList() {
		try {
			String baseStr = HttpUtil.get(HTTP_URL + HttpAddressEunms.DAPP_LIST.getName());
			RpcResponseEntity responseEntity = JSONObject.parseObject(baseStr, RpcResponseEntity.class);
			if (responseEntity.getData() != null && responseEntity.getData().toString().length() > 0) {
				List<DappEntity> list = JSONArray.parseArray(baseStr, DappEntity.class);
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						mongoTemplate.insert(list.get(i));
					}
				}
			}
			return setResultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "Dapp发现页配置管理", type = "POST", remark = "分页列表查询")
	public ResponseBase findList(@RequestBody PublicQueryEntity entity) {
		try {
//			this.rpcList();
			Query query = new Query();
			if (entity.getStatus() != null && !entity.getStatus().isEmpty()) {
	            query.addCriteria(Criteria.where("status").is(entity.getStatus()));
	        }
			long totalCount = mongoTemplate.count(query, NetWorkEntity.class);// 总条数
			// 构建分页请求对象
			int pageNumber = Math.max(entity.getPageNumber() - 1, 0);
			PageRequest pageRequest = PageRequest.of(pageNumber, entity.getPageSize(),
					Sort.by(Sort.Direction.ASC, "createdAt"));
			query.with(pageRequest);
			// 执行分页查询
			List<DappEntity> list = mongoTemplate.find(query, DappEntity.class);
			// 获取总记录数
			PageInfo<DappEntity> info = new PageInfo<>(list);
			info.setTotal(totalCount);
			return setResultSuccess(info);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "Dapp发现页配置管理", type = "GET", remark = "上线")
	public ResponseBase online(String id) {
		try {
			try {
				DappEntity entity = mongoTemplate.findById(id, DappEntity.class);
				if (entity != null) {
					DappEntity dappEntity = new DappEntity();
					dappEntity.setStatus(ReleaseStateEnums.TOP_LINE.getValue());
					dappEntity.setVersion(entity.getVersion());
					ResponseBase base = this.rpcUpdate(entity);//调用远程更新
					if (base.getRtncode().equals(Constants.HTTP_RES_CODE_200)) {
						Query query = new Query(Criteria.where("_id").is(id));
						Update update = new Update();
						update.set("status", ReleaseStateEnums.TOP_LINE.getValue());
						mongoTemplate.findAndModify(query, update, DappEntity.class);
						return setResultSuccess();
					} else {
						return base;
					}
				} else {
					return setResultError(Constants.ERROR);
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new RuntimeException();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "Dapp发现页配置管理", type = "GET", remark = "下线")
	public ResponseBase Offline(String id) {
		try {
			DappEntity entity = mongoTemplate.findById(id, DappEntity.class);
			if (entity != null) {
				DappEntity dappEntity = new DappEntity();
				dappEntity.setStatus(ReleaseStateEnums.DOWN_LINE.getValue());
				dappEntity.setVersion(entity.getVersion());
				ResponseBase base = this.rpcUpdate(entity);//调用远程更新
				if (base.getRtncode().equals(Constants.HTTP_RES_CODE_200)) {
					Query query = new Query(Criteria.where("_id").is(id));
					Update update = new Update();
					update.set("status", ReleaseStateEnums.DOWN_LINE.getValue());
					mongoTemplate.findAndModify(query, update, DappEntity.class);
					return setResultSuccess();
				} else {
					return base;
				}
			} else {
				return setResultError(Constants.ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "Dapp发现页配置管理", type = "POST", remark = "调用更新")
	public ResponseBase rpcUpdate(@RequestBody DappEntity entity) {
		try {
			String jsonParam = JSON.toJSONString(entity);
			String res = HttpUtil.sendPostRequest(HTTP_URL + HttpAddressEunms.DAPP_UPDATE.getName(), jsonParam);
			RpcResponseEntity rpcResponse = JSONObject.parseObject(res, RpcResponseEntity.class);
			if (rpcResponse.getSuccess() != null && rpcResponse.getSuccess()) {
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
	@SysLogAnnotation(module = "Dapp发现页配置管理", type = "POST", remark = "本地更新")
	public ResponseBase update(@RequestBody DappEntity entity) {
		try {
			DappEntity dappEntity = mongoTemplate.findById(entity.getId(), DappEntity.class);
			if(dappEntity != null) {
				Query query = new Query();
				query.addCriteria(Criteria.where("_id").is(entity.getId()));
				Update update = new Update();
				update.set("path", entity.getPath());
				update.set("remark", entity.getRemark());
				update.set("status", entity.getStatus());
				update.set("version", entity.getVersion());
				DappEntity dapp = mongoTemplate.findAndModify(query, update, DappEntity.class);
				this.rpcUpdate(dapp);
				return setResultSuccess();
			}else {
				return setResultError(Constants.ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

}
