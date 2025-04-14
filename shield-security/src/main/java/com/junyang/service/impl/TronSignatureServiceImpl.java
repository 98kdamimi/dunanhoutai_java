package com.junyang.service.impl;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import com.junyang.service.TronSignatureService;
import com.junyang.utils.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.junyang.base.BaseApiService;
import com.junyang.base.HttpResponse;
import com.junyang.constants.Constants;
import com.junyang.entity.tronsignature.TokenInfoDataEntity;
import com.junyang.entity.tronsignature.TronAccountEntity;
import com.junyang.entity.tronsignature.TronSignatureEntity;

@RestController
@Transactional
@CrossOrigin
public class TronSignatureServiceImpl extends BaseApiService implements TronSignatureService{
	
	@Value("${GET_ACCOUNT_URL}")
	private String GET_ACCOUNT_URL;
	
	@Value("${MULTI_SIGN_URL}")
	private String MULTI_SIGN_URL;
	
	@Value("${ACCOUNT_TOKENS_URL}")
	private String ACCOUNT_TOKENS_URL;

	@Override
	public HttpResponse signJudge(String address) {
		try {
			String base = HttpUtil.get(GET_ACCOUNT_URL+"?address="+address);
			TronAccountEntity accountEntity = JSONObject.parseObject(base, TronAccountEntity.class);
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

	
	
	

}
