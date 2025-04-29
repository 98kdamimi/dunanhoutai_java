package com.junyang.service.impl;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import com.github.pagehelper.PageInfo;
import com.junyang.aop.SysLogAnnotation;
import com.junyang.base.BaseApiService;
import com.junyang.base.ResponseBase;
import com.junyang.constants.Constants;
import com.junyang.entity.help.HelpEntity;
import com.junyang.service.HelpService;
import com.junyang.utils.GenericityUtil;

@RestController
@Transactional
@CrossOrigin
public class HelpServiceImpl extends BaseApiService implements HelpService{
	
	@Value("${http_url}")
	private String HTTP_URL;
	
	@Autowired
	@Qualifier("secondaryMongoTemplate") // 次数据源
	private MongoTemplate secondaryMongoTemplate;

	@Override
	@SysLogAnnotation(module = "帮助菜单管理", type = "POST", remark = "帮助菜单列表查询")
	public ResponseBase getList(@RequestBody HelpEntity entity) {
		try {
			Query query = new Query();
			if(entity.getLanguage() != null && entity.getLanguage().length() > 0) {
				query.addCriteria(Criteria.where("language").is(entity.getLanguage()));
			}
			long totalCount = secondaryMongoTemplate.count(query, HelpEntity.class);// 总条数
			// 构建分页请求对象
			int pageNumber = Math.max(entity.getPageNumber() - 1, 0);
			PageRequest pageRequest = PageRequest.of(pageNumber, entity.getPageSize(),
					Sort.by(Sort.Direction.DESC, "language"));
			query.with(pageRequest);
			List<HelpEntity> list = secondaryMongoTemplate.find(query, HelpEntity.class);
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
	@SysLogAnnotation(module = "帮助菜单管理", type = "GET", remark = "帮助菜单删除")
	public ResponseBase delete(String id) {
		try {
			if(id != null && id.length() > 0) {
				 Query query = new Query(Criteria.where("_id").is(id));
				 secondaryMongoTemplate.remove(query, HelpEntity.class);
			     return setResultSuccess();
			}else {
				return setResult(400, "参数异常", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "帮助菜单管理", type = "POST", remark = "帮助菜单新增")
	public ResponseBase add(@RequestBody HelpEntity entity) {
		try {
			if(entity != null) {
				GenericityUtil.setDateStrTwo(entity);
				secondaryMongoTemplate.insert(entity);
				return setResultSuccess();
			}
			return setResultError(Constants.ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "帮助菜单管理", type = "POST", remark = "帮助菜单编辑")
	public ResponseBase update(@RequestBody HelpEntity entity) {
		try {
			if(entity != null) {
				GenericityUtil.setDateStrTwoUp(entity);
				secondaryMongoTemplate.save(entity);
				return setResultSuccess();
			}
			return setResultError(Constants.ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	
	
}
