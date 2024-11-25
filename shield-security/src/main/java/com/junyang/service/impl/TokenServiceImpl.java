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
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.junyang.aop.SysLogAnnotation;
import com.junyang.base.BaseApiService;
import com.junyang.base.ResponseBase;
import com.junyang.entity.response.RpcResponseEntity;
import com.junyang.entity.token.PlatformTokenEntity;
import com.junyang.entity.token.TokenEntity;
import com.junyang.enums.HttpAddressEunms;
import com.junyang.query.PublicQueryEntity;
import com.junyang.service.TokenService;
import com.junyang.utils.CoinGeckoAPIUtil;
import com.junyang.utils.HttpUtil;

@RestController
@Transactional
@CrossOrigin
public class TokenServiceImpl extends BaseApiService implements TokenService{
	
	@Value("${http_url}")
	private String HTTP_URL;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	@SysLogAnnotation(module = "代币管理", type = "GET", remark = "接口获取代币列表")
	public ResponseBase rpcList() {
		try {
			String baseStr = HttpUtil.get(HTTP_URL + HttpAddressEunms.TOKEN_LIST.getName());
			RpcResponseEntity responseEntity = JSONObject.parseObject(baseStr, RpcResponseEntity.class);
			if(responseEntity.getData() != null && responseEntity.getData().toString().length() > 0) {
				List<TokenEntity> list = JSONArray.parseArray(responseEntity.getData().toString(), TokenEntity.class);
				if(list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						Query query = new Query(Criteria.where("_id").is(list.get(i).getId()));
						boolean exists = mongoTemplate.exists(query, TokenEntity.class, "token_db");
						if (exists == false) {
							mongoTemplate.insert(list.get(i));
						}
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
	@SysLogAnnotation(module = "代币管理", type = "GET", remark = "获取第三方平台代币")
	public ResponseBase ThirdPartylist() {
		try {
			String str = CoinGeckoAPIUtil.fetchTokenData();
			if(str != null &&str.length() > 0) {
				List<PlatformTokenEntity> list = JSONArray.parseArray(str, PlatformTokenEntity.class);
				if(list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						Query query = new Query(Criteria.where("_id").is(list.get(i).getId()));
						boolean exists = mongoTemplate.exists(query, PlatformTokenEntity.class, "platform_token");
						if (exists == false) {
							mongoTemplate.insert(list.get(i));
						}
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return null;
	}

	@Override
	@SysLogAnnotation(module = "代币管理", type = "POST", remark = "分页代币列表")
	public ResponseBase findList(@RequestBody PublicQueryEntity entity) {
		try {
			Query query = new Query();
			if(entity.getChainId() != null) {
				query.addCriteria(Criteria.where("chainId").is(entity.getChainId()));
			}
			if(entity.getName() != null && entity.getName().length() > 0) {
				query.addCriteria(Criteria.where("symbol").is(entity.getName()));
			}
			long totalCount = mongoTemplate.count(query, TokenEntity.class);// 总条数
			// 构建分页请求对象
			int pageNumber = Math.max(entity.getPageNumber() - 1, 0);
			PageRequest pageRequest = PageRequest.of(pageNumber, entity.getPageSize(),
					Sort.by(Sort.Direction.ASC, "createdAt"));
			query.with(pageRequest);
			// 执行分页查询
			List<TokenEntity> list = mongoTemplate.find(query, TokenEntity.class);
			// 获取总记录数
			PageInfo<TokenEntity> info = new PageInfo<>(list);
			info.setTotal(totalCount);
			return setResultSuccess(info);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "代币管理", type = "POST", remark = "新增代币")
	public ResponseBase add(@RequestBody TokenEntity entity) {
		try {
			String str = HttpUtil.sendPostRequest(HTTP_URL + HttpAddressEunms.TOKEN_ADD.getName(), JSON.toJSONString(entity));
			System.out.println(str);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return null;
	}

	@Override
	@SysLogAnnotation(module = "代币管理", type = "POST", remark = "更新代币")
	public ResponseBase update(@RequestBody TokenEntity entity) {
		try {
			String str = HttpUtil.sendPostRequest(HTTP_URL + HttpAddressEunms.TOKEN_UPDATE.getName(), JSON.toJSONString(entity));
			System.out.println(str);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return null;
	}

	@Override
	public ResponseBase findListThirdParty(@RequestBody PublicQueryEntity entity) {
		try {
			Query query = new Query();
			long totalCount = mongoTemplate.count(query, PlatformTokenEntity.class);// 总条数
			// 构建分页请求对象
			int pageNumber = Math.max(entity.getPageNumber() - 1, 0);
			PageRequest pageRequest = PageRequest.of(pageNumber, entity.getPageSize(),
					Sort.by(Sort.Direction.ASC, "last_updated"));
			query.with(pageRequest);
			// 执行分页查询
			List<PlatformTokenEntity> list = mongoTemplate.find(query, PlatformTokenEntity.class);
			// 获取总记录数
			PageInfo<PlatformTokenEntity> info = new PageInfo<>(list);
			info.setTotal(totalCount);
			return setResultSuccess(info);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

}
