package com.junyang.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.github.pagehelper.PageInfo;
import com.junyang.aop.SysLogAnnotation;
import com.junyang.base.BaseApiService;
import com.junyang.base.ResponseBase;
import com.junyang.constants.Constants;
import com.junyang.entity.message.MessageEntity;
import com.junyang.query.PublicQueryEntity;
import com.junyang.service.MessageService;
import com.junyang.utils.GenericityUtil;

@RestController
@Transactional
@CrossOrigin
public class MessageServiceImpl extends BaseApiService implements MessageService{
	
	@Autowired
	private MongoTemplate mongoTemplate;

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
	        
	        if(exists) {
	        	mongoTemplate.remove(query, MessageEntity.class);
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
	@SysLogAnnotation(module = "消息推送管理", type = "POST", remark = "消息列表")
	public ResponseBase findList(@RequestBody PublicQueryEntity entity) {
		try {
			Query query = new Query();
			if(entity.getTitle() != null && entity.getTitle().length() > 0) {
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

}
