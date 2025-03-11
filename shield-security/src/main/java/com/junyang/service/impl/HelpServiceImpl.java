package com.junyang.service.impl;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Date;
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
import com.junyang.base.BaseApiService;
import com.junyang.base.ResponseBase;
import com.junyang.constants.Constants;
import com.junyang.entity.help.HelpEntity;
import com.junyang.entity.response.RpcResponseEntity;
import com.junyang.enums.HttpAddressEunms;
import com.junyang.service.HelpService;
import com.junyang.utils.GenericityUtil;
import com.junyang.utils.HttpUtil;

@RestController
@Transactional
@CrossOrigin
public class HelpServiceImpl extends BaseApiService implements HelpService{
	
	@Value("${http_url}")
	private String HTTP_URL;
	
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public ResponseBase getList(@RequestBody HelpEntity entity) {
		try {
			Query query = new Query();
			if(entity.getLanguage() != null && entity.getLanguage().length() > 0) {
				query.addCriteria(Criteria.where("language").is(entity.getLanguage()));
			}
			long totalCount = mongoTemplate.count(query, HelpEntity.class);// 总条数
			// 构建分页请求对象
			int pageNumber = Math.max(entity.getPageNumber() - 1, 0);
			PageRequest pageRequest = PageRequest.of(pageNumber, entity.getPageSize(),
					Sort.by(Sort.Direction.DESC, "setTime"));
			query.with(pageRequest);
			List<HelpEntity> list = mongoTemplate.find(query, HelpEntity.class);
			// 获取总记录数
			PageInfo<HelpEntity > info = new PageInfo<>(list);
			info.setTotal(totalCount);
			return setResultSuccess(info);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase delete(String id) {
		try {
			if(id != null && id.length() > 0) {
				String baseStr = HttpUtil.get(HTTP_URL + HttpAddressEunms.HELP_DELETE.getName()+"?id="+id+"");
				RpcResponseEntity responseEntity = JSONObject.parseObject(baseStr, RpcResponseEntity.class);
				if(responseEntity.getSuccess()) {
					 Query query = new Query(Criteria.where("_id").is(id));
				     mongoTemplate.remove(query, HelpEntity.class);
				     return setResultSuccess();
				}else {
					return setResultError(Constants.ERROR);
				}
			}else {
				return setResult(400, "参数异常", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase add(@RequestBody HelpEntity entity) {
		try {
			if(entity != null) {
				JSONObject jsonObject = (JSONObject) JSONObject.toJSON(entity);
				String jsonParam = JSON.toJSONString(jsonObject);
				String res = HttpUtil.sendPostRequest(HTTP_URL+HttpAddressEunms.HELP_ADD.getName(), jsonParam);
				RpcResponseEntity rpcResponse = JSONObject.parseObject(res, RpcResponseEntity.class);
				if(rpcResponse.getSuccess() != null && rpcResponse.getSuccess()) {
					this.getRpc();
					return setResultSuccess();
				}
			}
			return setResultError(Constants.ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase update(@RequestBody HelpEntity entity) {
		try {
			if(entity != null) {
				JSONObject jsonObject = (JSONObject) JSONObject.toJSON(entity);
				String jsonParam = JSON.toJSONString(jsonObject);
				String res = HttpUtil.sendPostRequest(HTTP_URL+HttpAddressEunms.HELP_UPDATE.getName(), jsonParam);
				RpcResponseEntity rpcResponse = JSONObject.parseObject(res, RpcResponseEntity.class);
				if(rpcResponse.getSuccess() != null && rpcResponse.getSuccess()) {
					entity.setGmtModified(new Date());
					mongoTemplate.save(entity);
					return setResultSuccess();
				}
			}
			return setResultError(Constants.ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	public void getRpc() {
		try {
			String baseStr = HttpUtil.get(HTTP_URL + HttpAddressEunms.HELP_LIST.getName());
			RpcResponseEntity responseEntity = JSONObject.parseObject(baseStr, RpcResponseEntity.class);
			if (responseEntity.getData() != null && responseEntity.getData().toString().length() > 0) {
				List<HelpEntity> list = JSONArray.parseArray(responseEntity.getData().toString(), HelpEntity.class);
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						Query query = new Query(Criteria.where("_id").is(list.get(i).getId()));
						boolean exists = mongoTemplate.exists(query, HelpEntity.class, "help_db");
						if (exists == false) {
							GenericityUtil.setDate(list.get(i));
							mongoTemplate.insert(list.get(i));
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	public static void main(String[] args) {
        // Base64 字符串
        String base64String = "Az8fMFAhc+F2fZek0OY+yxdwMRXehh0MCnchzehW/FVLpA6JoBNsskRfhNjbjcU+LNceYPTDnFClVOTXbcHv8NvtedAMCLKaLyLq7jwHgCOZ330rGvJ9YAjMIYxWMLiSQxBkX6BITXAaQv79x3+ySe6iivq4wcbJiB2nZnLAiUe6+0ydXZ7f1yOV9M6PEHTiubB1WQO56fGExSi+ToGHXnJW+EuriNhxz48PdPjOGW+nr2b30UhowrDagbRZfoLJF5IpE9cZ/PR91B8qYFxe0Y66hNvfi6VbeX5I6T9fGrAsm7nt6NZ2H/mSpesKWggplMpH5mJa2ElUjOzsQNnjEGA6uJ3DLHRNGUzO/mFCz1rfW6V+IGmUsk/1/2XPQN36zPKfHuR2PcO";

        // 解码 Base64 字符串为字节数组
        byte[] decodedBytes = Base64.getDecoder().decode(base64String);

        // 假设解码内容是UTF-8编码的文本
        try {
            String decodedString = new String(decodedBytes, "UTF-8");
            System.out.println("Decoded String: " + decodedString);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
