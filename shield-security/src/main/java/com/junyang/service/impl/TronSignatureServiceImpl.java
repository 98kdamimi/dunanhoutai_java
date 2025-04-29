package com.junyang.service.impl;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.github.pagehelper.PageInfo;
import com.junyang.base.ResponseBase;
import com.junyang.entity.system.UserAgreementEntity;
import com.junyang.entity.tronsignature.*;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.junyang.service.TronSignatureService;
import com.junyang.utils.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.junyang.base.BaseApiService;
import com.junyang.base.HttpResponse;
import com.junyang.constants.Constants;
import com.junyang.entity.tronsignature.MultiSignaturesEntity;

import static cn.hutool.json.XMLTokener.entity;

@RestController
@Transactional
@CrossOrigin
public class TronSignatureServiceImpl extends BaseApiService implements TronSignatureService{

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Value("${GET_ACCOUNT_URL}")
	private String GET_ACCOUNT_URL;
	
	@Value("${MULTI_SIGN_URL}")
	private String MULTI_SIGN_URL;
	
	@Value("${ACCOUNT_TOKENS_URL}")
	private String ACCOUNT_TOKENS_URL;

	@Override
	public HttpResponse signJudge(String address) {
		try {
			System.out.println(address);
			String base = HttpUtil.get(GET_ACCOUNT_URL+"?address="+address);
			System.out.println(base);
			TronAccountEntity accountEntity = JSONObject.parseObject(base, TronAccountEntity.class);
			if(accountEntity.getOwnerPermission() == null) {
				return senSuccess(false);
			}
			if(accountEntity.getOwnerPermission().getKeys().size() > 1 || accountEntity.getActivePermissions().get(0).getKeys().size() > 1) {
				return senSuccess(true);
			}
			return senSuccess(false);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();  
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public HttpResponse multiSign(String address) {
		try {
			Map<String, Object> params = new HashedMap();
			params.put("address", address);
			params.put("start", 0);
			params.put("state", 255);
			params.put("limit", 20);
			params.put("netType", "main_net");
			params.put("serializable", true);
			params.put("selfSign", 0);
			String base = HttpUtil.get(MULTI_SIGN_URL+"?address="+address+"&start=0&state=255&limit=20&netType=main_net&serializable=true&selfSign=0");
			HttpResponse response = JSONObject.parseObject(base, HttpResponse.class);
			String temp = response.getData().toString();
			JSONObject jsonObject = JSONObject.parseObject(temp);
			String dataStr = jsonObject.get("data").toString();
			List<TronSignatureEntity> list = JSONArray.parseArray(dataStr, TronSignatureEntity.class);
			return senSuccess(list);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	@Override
	public HttpResponse getAccount(String address) {
		try {
			String base = HttpUtil.get(GET_ACCOUNT_URL+"?address="+address);
			TronAccountEntity accountEntity = JSONObject.parseObject(base, TronAccountEntity.class);
			return senSuccess(JSON.toJSON(accountEntity));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	public HttpResponse senSuccess(Object object) {
		HttpResponse httpResponse = new HttpResponse();
		httpResponse.setCode(Constants.HTTP_RES_CODE_200);
		httpResponse.setMsg(Constants.SUCCESS);
		httpResponse.setData(object);
		return httpResponse;
	}

	@Override
	public HttpResponse getAddressToken(String address) {
		try {
			String base = HttpUtil.get(ACCOUNT_TOKENS_URL+"?address="+address+"&start=0&limit=20&hidden=0&show=0&sortType=0&sortBy=0");
			JSONObject jsonObject = JSONObject.parseObject(base);
			String dataStr = jsonObject.getString("data").toString();
			List<TokenInfoDataEntity> list = JSONArray.parseArray(dataStr, TokenInfoDataEntity.class);
			return senSuccess(JSON.toJSON(list));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase createMultiSign(@RequestBody MultiSignaturesEntity entity) {
		try {
			// 查询条件
			Query query = new Query(Criteria.where("txID").is(entity.getTxID()));



			//判断数据库中txId是否存在
			Boolean hasId = mongoTemplate.exists(query, MultiSignaturesEntity.class);
			if(hasId)
			{

				// 查询数据库中原有的数据
				MultiSignaturesEntity existingEntity = mongoTemplate.findOne(query, MultiSignaturesEntity.class);
				if (existingEntity != null) {
					// 将原有的 signdata 值赋给 lastsigndata
					entity.setLastsigndata(existingEntity.getSigndata());
				}

				//修改signatureProgress中相对应address的issign和signTime
				// 获取 signatureProgress 列表
				List<MultiSignaturesEntity.SignatureProgress> progressList = (List<MultiSignaturesEntity.SignatureProgress>) existingEntity.getSignatureProgress();
				// 遍历并修改对应 address 的 isSign 和 signTime
				for (MultiSignaturesEntity.SignatureProgress progress : progressList) {
					if (progress.getAddress().equals(entity.getNowAddress())) {
						progress.setIsSign(1); // 设置 isSign 为 1
						progress.setSignTime(System.currentTimeMillis()/1000); // 设置签名时间为当前时间戳
						break;
					}
				}
				entity.setSignatureProgress(progressList);


				//如果存在 已经创建完成交易了  修改 signdata 和 signatureProgress中 相对应address的issign和signTime
				Update update = new Update();
				update.set("lastsigndata", entity.getLastsigndata());
				update.set("nowAddress", entity.getNowAddress());
				update.set("signdata", entity.getSigndata());
				update.set("threshold", entity.getThreshold());
				update.set("tokeninfo", entity.getTokeninfo());
				update.set("signatureProgress", entity.getSignatureProgress());

				mongoTemplate.updateFirst(
						query,
						update,
						MultiSignaturesEntity.class
				);
			}else{
				//不存在 直接写入
				// 设置lastsigndata
				entity.setLastsigndata(entity.getSigndata());
				// 遍历 signatureProgress 列表
				for (MultiSignaturesEntity.SignatureProgress progress : entity.getSignatureProgress()) {
					// 判断是否是目标 address
					if (progress.getAddress().equals(entity.getNowAddress())) {
						// 插入 signdata
						progress.setSigndata(entity.getSigndata());
						break;
					}
				}

				//修改signatureProgress中相对应address的issign和signTime
				// 获取 signatureProgress 列表
				List<MultiSignaturesEntity.SignatureProgress> progressList = (List<MultiSignaturesEntity.SignatureProgress>) entity.getSignatureProgress();
				// 遍历并修改对应 address 的 isSign 和 signTime
				for (MultiSignaturesEntity.SignatureProgress progress : progressList) {
					if (progress.getAddress().equals(entity.getNowAddress())) {
						progress.setIsSign(1); // 设置 isSign 为 1
						progress.setSignTime(System.currentTimeMillis()/1000); // 设置签名时间为当前时间戳
						break;
					}
				}

				mongoTemplate.insert(entity);
			}
			// 遍历 signatureProgress 列表并隐藏 signdata 字段
			for (MultiSignaturesEntity.SignatureProgress progress : entity.getSignatureProgress()) {
				progress.setSigndata(null); // 将 signdata 设置为 null
			}
			// 隐藏 lastsigndata 字段
			entity.setLastsigndata(null);
			return setResultSuccess(entity);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase getMultiSign(String address, int pageNumber, int pageSize) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("signatureProgress.address").is(address));

			// 构建排序对象
			Sort sort = Sort.by(Sort.Direction.DESC, "_id"); // 替换为实际的时间字段
			query.with(sort);

			// 构建分页请求对象
			pageNumber = Math.max(pageNumber - 1, 0); // 页码从 0 开始
			PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
			query.with(pageRequest);

			List<MultiSignaturesEntity> list = mongoTemplate.find(query, MultiSignaturesEntity.class);

			// 移除不需要的字段
			list.forEach(entity -> {
				entity.setLastsigndata(null); // 删除 tokeninfo 字段
				for (MultiSignaturesEntity.SignatureProgress progress : entity.getSignatureProgress()) {
					progress.setSigndata(null); // 将 signdata 设置为 null
				}
			});

			// 获取总记录数
			long totalCount = mongoTemplate.count(query, MultiSignaturesEntity.class);

			// 构建分页结果
			PageInfo<MultiSignaturesEntity> pageInfo = new PageInfo<>(list);
			pageInfo.setTotal(totalCount);

			return setResultSuccess(pageInfo);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase failMultiSign(String txID,String address) {
		try{
			Query query = new Query(Criteria.where("txID").is(txID));
			// 查询数据库中原有的数据
			MultiSignaturesEntity existingEntity = mongoTemplate.findOne(query, MultiSignaturesEntity.class);
			// 遍历 signatureProgress 列表
			for (MultiSignaturesEntity.SignatureProgress progress : existingEntity.getSignatureProgress()) {
				// 判断是否是目标 address
				if (progress.getAddress().equals(address)) {
					//设置issign 为 0
					progress.setIsSign(0);
					progress.setSignTime(0);
					break;
				}
			}
			existingEntity.setSigndata(existingEntity.getLastsigndata());

			Update update = new Update();
			update.set("signatureProgress", existingEntity.getSignatureProgress());
			update.set("signdata", existingEntity.getSigndata());
			mongoTemplate.updateFirst(
					query,
					update,
					MultiSignaturesEntity.class
			);

			// 遍历 signatureProgress 列表并隐藏 signdata 字段
			for (MultiSignaturesEntity.SignatureProgress progress : existingEntity.getSignatureProgress()) {
				progress.setSigndata(null); // 将 signdata 设置为 null
			}
			// 隐藏 lastsigndata 字段
			existingEntity.setLastsigndata(null);

			return setResultSuccess(existingEntity);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
	

}
