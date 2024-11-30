package com.junyang.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import com.junyang.entity.network.NetWorkEntity;
import com.junyang.entity.response.DicEntity;
import com.junyang.enums.HttpAddressEunms;
import com.junyang.enums.ReleaseStateEnums;
import com.junyang.query.PublicQueryEntity;
import com.junyang.service.NetWorkService;
import com.junyang.utils.HttpUtil;

@RestController
@Transactional
@CrossOrigin
public class NetWorkServiceImpl extends BaseApiService implements NetWorkService {

	@Value("${http_url}")
	private String HTTP_URL;

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private TokenServiceImpl tokenServiceImpl;

	@SuppressWarnings("unchecked")
	@Override
	@SysLogAnnotation(module = "主链管理", type = "GET", remark = "主链列表获取")
	public ResponseBase obtainList() {
		try {
			String baseStr = HttpUtil.get(HTTP_URL + HttpAddressEunms.NETWORK_LIST_ALL.getName());
			if(baseStr != null && baseStr.length() > 0) {
				ResponseBase base = JSONObject.parseObject(baseStr, ResponseBase.class);
				if (base.getData() != null && base.getData().toString().length() > 0) {
					String str = base.getData().toString();
					if (str != null && str.length() > 0) {
						List<Map<String, Object>> listData = JSON.parseObject(str, List.class);
						for (int i = 0; i < listData.size(); i++) {
							String id = listData.get(i).get("_id").toString();
							String version = listData.get(i).get("__v").toString();
							NetWorkEntity entity = JSONObject.parseObject(JSON.toJSONString(listData.get(i)), NetWorkEntity.class);
							entity.setWorkId(entity.getId());
							entity.setId(id);
							entity.set__v(Integer.parseInt(version));
							Query query = new Query(Criteria.where("_id").is(entity.getId()));
							boolean exists = mongoTemplate.exists(query, NetWorkEntity.class, "net_work");
							if (exists == false) {
								mongoTemplate.insert(entity);
							}
						}
					}
				}
				List<NetWorkEntity> netWorkList = mongoTemplate.findAll(NetWorkEntity.class);
				if(netWorkList != null && netWorkList.size() > 0) {
					for (int i = 0; i < netWorkList.size(); i++) {
						tokenServiceImpl.rpcList(HTTP_URL+HttpAddressEunms.TOKEN_LIST.getName()+"chainId="+netWorkList.get(i).getChainId()+"&impl="+netWorkList.get(i).getImpl());
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
	@SysLogAnnotation(module = "主链管理", type = "POST", remark = "主链分页列表查询")
	public ResponseBase findList(@RequestBody PublicQueryEntity entity) {
		Query query = new Query();
		if(entity.getName() != null && entity.getName().length() > 0) {
			query.addCriteria(Criteria.where("name").is(entity.getName()));
		}
		query.addCriteria(Criteria.where("status").is(ReleaseStateEnums.TOP_LINE.getLable()));
		long totalCount = mongoTemplate.count(query, NetWorkEntity.class);// 总条数
		// 构建分页请求对象
		int pageNumber = Math.max(entity.getPageNumber() - 1, 0);
		PageRequest pageRequest = PageRequest.of(pageNumber, entity.getPageSize(),
				Sort.by(Sort.Direction.ASC, "updatedAt"));
		query.with(pageRequest);
		// 执行分页查询
		List<NetWorkEntity> list = mongoTemplate.find(query, NetWorkEntity.class);
		// 获取总记录数
		PageInfo<NetWorkEntity> info = new PageInfo<>(list);
		info.setTotal(totalCount);
		return setResultSuccess(info);
	}

	@Override
	@SysLogAnnotation(module = "主链管理", type = "GET", remark = "查询所有主链")
	public ResponseBase findAll() {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("status").is(ReleaseStateEnums.TOP_LINE.getLable()));
			List<NetWorkEntity> listAll = mongoTemplate.find(query, NetWorkEntity.class);
			List<DicEntity> list = new ArrayList<DicEntity>();
			if(listAll != null && listAll.size() > 0) {
				for (int i = 0; i < listAll.size(); i++) {
					DicEntity dicEntity = new DicEntity();
					dicEntity.setLable(listAll.get(i).getId());
					dicEntity.setName(listAll.get(i).getChainId());
					dicEntity.setValue("主链名称:"+listAll.get(i).getName()+
							" ___ 标识符:"+listAll.get(i).getShortcode()+
							" ___ 代币符号:"+listAll.get(i).getSymbol()+
							" ___ 类型:"+listAll.get(i).getImpl());
					list.add(dicEntity);
				}
			}
			return setResultSuccess(list);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "主链管理", type = "GET", remark = "查询所有主链id")
	public ResponseBase findNetWorkIdList() {
		List<NetWorkEntity> listAll = mongoTemplate.findAll(NetWorkEntity.class);
		List<DicEntity> list = new ArrayList<DicEntity>();
		if(listAll != null && listAll.size() > 0) {
			for (int i = 0; i < listAll.size(); i++) {
				DicEntity dicEntity = new DicEntity();
				dicEntity.setName(listAll.get(i).getWorkId());
				list.add(dicEntity);
			}
		}
		return setResultSuccess(list);
	}

	@Override
	public ResponseBase findNetWorkById(String id) {
		NetWorkEntity entity = mongoTemplate.findById(id, NetWorkEntity.class);
		return setResultSuccess(entity);
	}

}
