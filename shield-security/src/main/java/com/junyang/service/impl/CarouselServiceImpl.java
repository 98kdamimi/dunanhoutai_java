package com.junyang.service.impl;

import java.io.File;
import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.github.pagehelper.PageInfo;
import com.junyang.aop.SysLogAnnotation;
import com.junyang.base.BaseApiService;
import com.junyang.base.ResponseBase;
import com.junyang.constants.Constants;
import com.junyang.entity.carouse.CarouselEntity;
import com.junyang.entity.response.RpcResponseEntity;
import com.junyang.entity.uploadefiel.UploadFileEntity;
import com.junyang.enums.FilePathEnums;
import com.junyang.enums.HttpAddressEunms;
import com.junyang.service.CarouselService;
import com.junyang.utils.FileUploadUtil;
import com.junyang.utils.GenericityUtil;
import com.junyang.utils.HttpUtil;

@RestController
@Transactional
@CrossOrigin
public class CarouselServiceImpl extends BaseApiService implements CarouselService{
	

	@Value("${http_url}")
	private String HTTP_URL;
	
	@Value("${aws-s3-bucket-name}")
	private String bucketName;

	private final AmazonS3 amazonS3;
	
	public CarouselServiceImpl(AmazonS3 amazonS3) {
		this.amazonS3 = amazonS3;
	}

	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	@SysLogAnnotation(module = "轮播图管理", type = "POST", remark = "轮播图列表查询")
	public ResponseBase getList(@RequestBody CarouselEntity entity) {
		try {
			Query query = new Query();
			if(entity.getLanguage() != null && entity.getLanguage().length() > 0) {
				query.addCriteria(Criteria.where("language").is(entity.getLanguage()));
			}
			if(entity.getSortType() != null && entity.getSortType().length() > 0) {
				query.addCriteria(Criteria.where("sortType").is(entity.getSortType()));
			}
			long totalCount = mongoTemplate.count(query, CarouselEntity.class);// 总条数
			// 构建分页请求对象
			int pageNumber = Math.max(entity.getPageNumber() - 1, 0);
			PageRequest pageRequest = PageRequest.of(pageNumber, entity.getPageSize(),
					Sort.by(Sort.Direction.DESC, "createdAt"));
			query.with(pageRequest);
			List<CarouselEntity> list = mongoTemplate.find(query, CarouselEntity.class);
			// 获取总记录数
			PageInfo<CarouselEntity> info = new PageInfo<>(list);
			info.setTotal(totalCount);
			return setResultSuccess(info);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "轮播图管理", type = "GET", remark = "轮播图删除")
	public ResponseBase delete(String id) {
		try {
			if(id != null && id.length() > 0) {
				String baseStr = HttpUtil.get(HTTP_URL + HttpAddressEunms.CAROUSE_DELETE.getName()+"?id="+id+"");
				RpcResponseEntity responseEntity = JSONObject.parseObject(baseStr, RpcResponseEntity.class);
				if(responseEntity.getSuccess()) {
					 Query query = new Query(Criteria.where("_id").is(id));
				     mongoTemplate.remove(query, CarouselEntity.class);
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
	@SysLogAnnotation(module = "轮播图管理", type = "POST", remark = "轮播图新增")
	public ResponseBase add(String dataStr,MultipartFile file) {
		try {
			if(dataStr != null && dataStr.length() > 0) {
				CarouselEntity entity = JSONObject.parseObject(dataStr, CarouselEntity.class);
				if(entity != null) {
					if(file != null) {
						String logUrl = this.fileUploadUtil(file, FilePathEnums.CAROUSEL.getIndex(), null);
						if(logUrl!= null && logUrl.length() > 0) {
							entity.setLogoURI(logUrl);
						}
					}
					JSONObject jsonObject = (JSONObject) JSONObject.toJSON(entity);
					String jsonParam = JSON.toJSONString(jsonObject);
					System.out.println(HTTP_URL+HttpAddressEunms.CAROUSE_ADD.getName());
					System.out.println(jsonParam);
					String res = HttpUtil.sendPostRequest(HTTP_URL+HttpAddressEunms.CAROUSE_ADD.getName(), jsonParam);
					RpcResponseEntity rpcResponse = JSONObject.parseObject(res, RpcResponseEntity.class);
					if(rpcResponse.getSuccess() != null && rpcResponse.getSuccess()) {
						this.getRpc();
						return setResultSuccess();
					}
				}
				return setResultError(Constants.ERROR);
			}else {
				return setResultError(Constants.ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	
	public String fileUploadUtil(MultipartFile file,Integer typeId, String dbId) {
		try {
			// 检查文件是否为空或大小是否超过限制
			if (file.isEmpty() || file.getSize() > 200 * 1024 * 1024) { // 假设最大文件大小为 200MB
				return "文件为空或超过最大限制";
			}
			// 创建临时文件
			File tempFile = File.createTempFile("upload-", file.getOriginalFilename(),
					new File(System.getProperty("java.io.tmpdir")));
			file.transferTo(tempFile); // 将 MultipartFile 保存为 File
			// 构建 S3 中的完整路径（目录+文件名）
			String fileName = FilePathEnums.getName(typeId) +System.currentTimeMillis()+ file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
			// 创建上传请求
			PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, tempFile);
			// 执行上传
			amazonS3.putObject(putObjectRequest);
			String fileUrl = amazonS3.getUrl(bucketName, fileName).toString();
			if(fileUrl != null && fileUrl.length() > 0) {
				try {
					UploadFileEntity entity = new UploadFileEntity();
					entity.setDatabseName(FilePathEnums.getDatabaseName(typeId));
					entity.setFilePath(fileUrl);
					// 获取文件名
					String oldFileName = file.getOriginalFilename();
					// 获取文件的后缀名
					String suffixName = oldFileName.substring(oldFileName.lastIndexOf(".")+1);
					entity.setFileType(suffixName);
					entity.setFileSize(FileUploadUtil.GetFileSize(file));
					entity.setImageLable(FileUploadUtil.isImage(file));
					entity.setTypeId(typeId);
					entity.setTypeName(FilePathEnums.getValue(typeId));
					entity.setFileCatalogue(FilePathEnums.getName(typeId));
					entity.setDatabseName(FilePathEnums.getValue(typeId));
					entity.setDatabseId(dbId);
					GenericityUtil.setDate(entity);
					mongoTemplate.insert(entity);
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException();
				}
			}
			return fileUrl;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	@SysLogAnnotation(module = "轮播图管理", type = "POST", remark = "轮播图编辑")
	public ResponseBase update(String dataStr, MultipartFile file) {
		try {
			if(dataStr != null && dataStr.length() > 0) {
				CarouselEntity entity = JSONObject.parseObject(dataStr, CarouselEntity.class);
				if(entity != null) {
					if(file != null) {
						String logUrl = this.fileUploadUtil(file, FilePathEnums.CAROUSEL.getIndex(), null);
						if(logUrl!= null && logUrl.length() > 0) {
							entity.setLogoURI(logUrl);
						}
					}
					JSONObject jsonObject = (JSONObject) JSONObject.toJSON(entity);
					String jsonParam = JSON.toJSONString(jsonObject);
					String res = HttpUtil.sendPostRequest(HTTP_URL+HttpAddressEunms.CAROUSE_UPDATE.getName(), jsonParam);
					RpcResponseEntity rpcResponse = JSONObject.parseObject(res, RpcResponseEntity.class);
					if(rpcResponse.getSuccess() != null && rpcResponse.getSuccess()) {
						entity.setGmtModified(new Date());
						mongoTemplate.save(entity);
						return setResultSuccess();
					}
				}
				return setResultError(Constants.ERROR);
			}else {
				return setResultError(Constants.ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	public void getRpc() {
		try {
			String baseStr = HttpUtil.get(HTTP_URL + HttpAddressEunms.CAROUSE_LIST.getName());
			RpcResponseEntity responseEntity = JSONObject.parseObject(baseStr, RpcResponseEntity.class);
			if (responseEntity.getData() != null && responseEntity.getData().toString().length() > 0) {
				List<CarouselEntity> list = JSONArray.parseArray(responseEntity.getData().toString(), CarouselEntity.class);
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						Query query = new Query(Criteria.where("_id").is(list.get(i).getId()));
						boolean exists = mongoTemplate.exists(query, CarouselEntity.class, "carousel_db");
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
	
}
