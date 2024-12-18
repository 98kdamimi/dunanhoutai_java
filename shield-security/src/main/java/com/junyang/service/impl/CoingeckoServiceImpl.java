package com.junyang.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.fastjson.JSONArray;
import com.junyang.aop.SysLogAnnotation;
import com.junyang.base.BaseApiService;
import com.junyang.base.ResponseBase;
import com.junyang.entity.coingecko.AssetplatformsEntity;
import com.junyang.entity.coingecko.CoingeckoNetWorkEntity;
import com.junyang.entity.network.NetWorkEntity;
import com.junyang.entity.token.PlatformTokenEntity;
import com.junyang.enums.CoingeckoSiteEunms;
import com.junyang.service.CoingeckoService;
import com.junyang.utils.HttpUtil;

@RestController
@Transactional
@CrossOrigin
public class CoingeckoServiceImpl extends BaseApiService implements CoingeckoService{
	
	@Value("${coingecko-sign}")
	private String COINGECKO_SIGN;
	
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	@SysLogAnnotation(module = "coingecko平台调用", type = "GET", remark = "资产平台列表拉取")
	public ResponseBase assetPlatforms() {
		String str = HttpUtil.getCoingecko(CoingeckoSiteEunms.ASSET_PLATFORMS.getName(),COINGECKO_SIGN);
		if(str != null && str.length() > 0) {
			List<AssetplatformsEntity> list = JSONArray.parseArray(str, AssetplatformsEntity.class);
			if(list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Query query = new Query(Criteria.where("_id").is(list.get(i).getId()));
					boolean exists = mongoTemplate.exists(query, AssetplatformsEntity.class, "assetplat_forms");
					if (exists == false) {
						mongoTemplate.insert(list.get(i));
					}
				}
			}
		}
		return setResultSuccess();
	}

	@Override
	@SysLogAnnotation(module = "coingecko平台调用", type = "GET", remark = "网络列表列表拉取")
	public ResponseBase network() {
		String str = HttpUtil.getCoingecko(CoingeckoSiteEunms.NETWORKS.getName(),COINGECKO_SIGN);
		if(str != null && str.length() > 0) {
			List<CoingeckoNetWorkEntity> list = JSONArray.parseArray(str, CoingeckoNetWorkEntity.class);
			if(list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Query query = new Query(Criteria.where("_id").is(list.get(i).getId()));
					boolean exists = mongoTemplate.exists(query, CoingeckoNetWorkEntity.class, "coingecko_network");
					if (exists == false) {
						mongoTemplate.insert(list.get(i));
					}
				}
			}
		}
		return setResultSuccess();
	}

	@Override
	public ResponseBase addToken(@RequestBody PlatformTokenEntity entity) {
		mongoTemplate.insert(entity);
		return setResultSuccess();
	}

	@Override
	public ResponseBase findCoingeckoToken(@RequestBody NetWorkEntity entity) {
		List<PlatformTokenEntity> list = mongoTemplate.findAll(PlatformTokenEntity.class);
		if(list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				list.get(i).getData().put("impl", "evm");
			}
		}
		return setResultSuccess(list);
	}

}
