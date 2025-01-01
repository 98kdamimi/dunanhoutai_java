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
import com.junyang.entity.message.JpushMessageEntity;
import com.junyang.entity.message.MessageEntity;
import com.junyang.entity.response.RpcResponseEntity;
import com.junyang.enums.HttpAddressEunms;
import com.junyang.query.PublicQueryEntity;
import com.junyang.service.MessageService;
import com.junyang.utils.GenericityUtil;
import com.junyang.utils.HttpUtil;

@RestController
@Transactional
@CrossOrigin
public class MessageServiceImpl extends BaseApiService implements MessageService {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Value("${http_url}")
	private String HTTP_URL;

	@Override
	@SysLogAnnotation(module = "消息推送管理", type = "POST", remark = "编辑消息")
	public ResponseBase add(@RequestBody MessageEntity entity) {
		try {
			GenericityUtil.setDate(entity);
			mongoTemplate.insert(entity);
			return setResultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "消息推送管理", type = "POST", remark = "删除消息")
	public ResponseBase delete(String id) {
		try {
			Query query = new Query(Criteria.where("_id").is(id));
			boolean exists = mongoTemplate.exists(query, MessageEntity.class, "message");

			if (exists) {
				mongoTemplate.remove(query, MessageEntity.class);
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
	@SysLogAnnotation(module = "消息推送管理", type = "POST", remark = "消息列表")
	public ResponseBase findList(@RequestBody PublicQueryEntity entity) {
		try {
			Query query = new Query();
			if (entity.getTitle() != null && entity.getTitle().length() > 0) {
				query.addCriteria(Criteria.where("messageTitleZh").is(entity.getTitle()));
			}
			long totalCount = mongoTemplate.count(query, MessageEntity.class);// 总条数
			// 构建分页请求对象
			int pageNumber = Math.max(entity.getPageNumber() - 1, 0);
			PageRequest pageRequest = PageRequest.of(pageNumber, entity.getPageSize(),
					Sort.by(Sort.Direction.ASC, "setTime"));
			query.with(pageRequest);
			// 执行分页查询
			List<MessageEntity> list = mongoTemplate.find(query, MessageEntity.class);
			// 获取总记录数
			PageInfo<MessageEntity> info = new PageInfo<>(list);
			info.setTotal(totalCount);
			return setResultSuccess(info);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase pushZh(String id) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("id").is(id));
			// 指定返回的字段
			query.fields().include("messageTitleZh").include("messageContentZh");
			MessageEntity entity = mongoTemplate.findOne(query, MessageEntity.class);
			if (entity != null) {
				JpushMessageEntity messageEntity = new JpushMessageEntity();
				messageEntity.setExtras(new JSONObject());
				messageEntity.setMsgContent(entity.getMessageContentZh());
				messageEntity.setMsgContentType(Constants.MSG_CONTENT_TYPE);
				messageEntity.setSendType(Constants.SEND_TYPE);
				messageEntity.setTitle(entity.getMessageTitleZh());
				JSONObject jsonObject = (JSONObject) JSONObject.toJSON(messageEntity);
				String jsonParam = JSON.toJSONString(jsonObject);
				String res = HttpUtil.sendPostRequest(HTTP_URL + HttpAddressEunms.MESSAGE_PUSH.getName(), jsonParam);
				RpcResponseEntity rpcResponse = JSONObject.parseObject(res, RpcResponseEntity.class);
				if (rpcResponse.getSuccess() != null && rpcResponse.getSuccess()) {
					return setResultSuccess();
				}
				return setResultError("发送失败");
			} else {
				return setResultError(Constants.ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase pushEl(String id) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("id").is(id));
			// 指定返回的字段
			query.fields().include("messageTitleEn").include("messageContentEn");
			MessageEntity entity = mongoTemplate.findOne(query, MessageEntity.class);
			if (entity != null) {
				JpushMessageEntity messageEntity = new JpushMessageEntity();
				messageEntity.setExtras(new JSONObject());
				messageEntity.setMsgContent(entity.getMessageContentEn());
				messageEntity.setMsgContentType(Constants.MSG_CONTENT_TYPE);
				messageEntity.setSendType(Constants.SEND_TYPE);
				messageEntity.setTitle(entity.getMessageTitleEn());
				JSONObject jsonObject = (JSONObject) JSONObject.toJSON(messageEntity);
				String jsonParam = JSON.toJSONString(jsonObject);
				String res = HttpUtil.sendPostRequest(HTTP_URL + HttpAddressEunms.MESSAGE_PUSH.getName(), jsonParam);
				RpcResponseEntity rpcResponse = JSONObject.parseObject(res, RpcResponseEntity.class);
				if (rpcResponse.getSuccess() != null && rpcResponse.getSuccess()) {
					return setResultSuccess();
				}
				return setResultError("发送失败");
			} else {
				return setResultError(Constants.ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

}
