package com.junyang.service.impl;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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
import com.junyang.entity.dapp.DappEntity;
import com.junyang.entity.dapp.DappTypeEntity;
import com.junyang.entity.response.RpcResponseEntity;
import com.junyang.entity.system.UserAgreementEntity;
import com.junyang.entity.uploadefiel.UploadFileEntity;
import com.junyang.enums.FilePathEnums;
import com.junyang.enums.HttpAddressEunms;
import com.junyang.enums.ReleaseStateEnums;
import com.junyang.enums.TokenTypeLableEunms;
import com.junyang.query.PublicQueryEntity;
import com.junyang.service.DappService;
import com.junyang.utils.FileUploadUtil;
import com.junyang.utils.GenericityUtil;
import com.junyang.utils.HttpUtil;

@RestController
@Transactional
@CrossOrigin
public class DappServiceImpl extends BaseApiService implements DappService {

	@Value("${http_url}")
	private String HTTP_URL;
	
	@Value("${aws-s3-bucket-name}")
	private String bucketName;

	private final AmazonS3 amazonS3;
	
	public DappServiceImpl(AmazonS3 amazonS3) {
		this.amazonS3 = amazonS3;
	}

	@Autowired
	private MongoTemplate mongoTemplate;
	

	@Override
	@SysLogAnnotation(module = "Dapp发现页配置管理", type = "GET", remark = "获取列表")
	public ResponseBase rpcList() {
		try {
			this.typeRpcList();
			String baseStr = HttpUtil.get(HTTP_URL + HttpAddressEunms.DAPP_LIST.getName());
			RpcResponseEntity responseEntity = JSONObject.parseObject(baseStr, RpcResponseEntity.class);
			if (responseEntity.getData() != null && responseEntity.getData().toString().length() > 0) {
				List<DappEntity> list = JSONArray.parseArray(responseEntity.getData().toString(), DappEntity.class);
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						Query query = new Query(Criteria.where("_id").is(list.get(i).getId()));
						boolean exists = mongoTemplate.exists(query, DappEntity.class, "dapp_db");
						if (exists == false) {
							if(list.get(i).getLogoURL() != null && list.get(i).getLogoURL().length() > 0) {
								MultipartFile urlFile = FileUploadUtil.convertUrlToMultipartFile(list.get(i).getLogoURL());
								if(urlFile != null) {
									String logoImg = this.fileUploadUtil(urlFile, FilePathEnums.DAPP.getIndex(), null);
									if(logoImg!= null && logoImg.length() > 0) {
										list.get(i).setLogoURL(logoImg);
									}
								}
							}
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
	@SysLogAnnotation(module = "Dapp发现页配置管理", type = "GET", remark = "调用远程列表")
	public ResponseBase typeRpcList() {
		try {
			String baseStr = HttpUtil.get(HTTP_URL + HttpAddressEunms.DAPP_TYPE_LIST.getName());
			RpcResponseEntity responseEntity = JSONObject.parseObject(baseStr, RpcResponseEntity.class);
			if (responseEntity.getData() != null && responseEntity.getData().toString().length() > 0) {
				List<DappTypeEntity> list = JSONArray.parseArray(responseEntity.getData().toString(), DappTypeEntity.class);
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						Query query = new Query(Criteria.where("_id").is(list.get(i).getId()));
						boolean exists = mongoTemplate.exists(query, DappTypeEntity.class, "dapp_type");
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
	@SysLogAnnotation(module = "Dapp发现页配置管理", type = "GET", remark = "查询发现页类型")
	public ResponseBase findTypeList() {
		try {
//			this.typeRpcList();
			List<DappTypeEntity> list = mongoTemplate.findAll(DappTypeEntity.class);
			return setResultSuccess(list);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}


	@Override
	@SysLogAnnotation(module = "Dapp发现页配置管理", type = "post", remark = "查询发现页列表")
	public ResponseBase findList(@RequestBody PublicQueryEntity entity) {
		try {
//			this.rpcList();
			Query query = new Query();
			if(entity.getStatus() != null && entity.getStatus().length() > 0) {
				query.addCriteria(Criteria.where("status").is(entity.getStatus()));
			}
			if(entity.getType() != null && entity.getType().length() > 0) {
				DappTypeEntity typeEntity = mongoTemplate.findById(entity.getType(), DappTypeEntity.class);
				if(typeEntity != null) {
					if(TokenTypeLableEunms.TAG.getName().equals(typeEntity.getType())) {
						query.addCriteria(Criteria.where("tagIds").in(entity.getType()));
					}else {
						query.addCriteria(Criteria.where("categoryIds").in(entity.getType()));
					}
				}
			}
			if(entity.getTitle() != null && entity.getTitle().length() > 0) {
				query.addCriteria(Criteria.where("subtitle").regex(entity.getTitle()));
			}
			long totalCount = mongoTemplate.count(query, DappEntity.class);// 总条数
			// 构建分页请求对象
			int pageNumber = Math.max(entity.getPageNumber() - 1, 0);
			PageRequest pageRequest = PageRequest.of(pageNumber, entity.getPageSize(),
					Sort.by(Sort.Direction.DESC, "createdAt"));
			query.with(pageRequest);
			// 执行分页查询
			List<DappEntity> list = mongoTemplate.find(query, DappEntity.class);
			// 获取总记录数
			PageInfo<DappEntity> info = new PageInfo<>(list);
			info.setTotal(totalCount);
			return setResultSuccess(info);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}


	@Override
	@SysLogAnnotation(module = "Dapp发现页配置管理", type = "post", remark = "发现页下线")
	public ResponseBase Offline(String id) {
		try {
			DappEntity dappEntity = mongoTemplate.findById(id, DappEntity.class);
			if(dappEntity != null) {
				dappEntity.setStatus(ReleaseStateEnums.DOWN_LINE.getLable());
				JSONObject jsonObject = (JSONObject) JSONObject.toJSON(dappEntity);
//				jsonObject.put("_id", dappEntity.getId());
				String jsonParam = JSON.toJSONString(jsonObject);
				String res = HttpUtil.sendPostRequest(HTTP_URL+HttpAddressEunms.DAPP_UPDATE.getName(), jsonParam);
				RpcResponseEntity rpcResponse = JSONObject.parseObject(res, RpcResponseEntity.class);
				if(rpcResponse.getSuccess() != null && rpcResponse.getSuccess()) {
					Query query = new Query(Criteria.where("_id").is(id));
					Update update = new Update();
					update.set("status", ReleaseStateEnums.DOWN_LINE.getLable());
					mongoTemplate.findAndModify(query, update, DappEntity.class);
				}
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
	@SysLogAnnotation(module = "Dapp发现页配置管理", type = "post", remark = "发现页上线")
	public ResponseBase online(String id) {
		try {
			DappEntity dappEntity = mongoTemplate.findById(id, DappEntity.class);
			if(dappEntity != null) {
				dappEntity.setStatus(ReleaseStateEnums.TOP_LINE.getLable());
				JSONObject jsonObject = (JSONObject) JSONObject.toJSON(dappEntity);
//				jsonObject.put("_id", dappEntity.getId());
				String jsonParam = JSON.toJSONString(jsonObject);
				String res = HttpUtil.sendPostRequest(HTTP_URL+HttpAddressEunms.DAPP_UPDATE.getName(), jsonParam);
				RpcResponseEntity rpcResponse = JSONObject.parseObject(res, RpcResponseEntity.class);
				if(rpcResponse.getSuccess() != null && rpcResponse.getSuccess()) {
					Query query = new Query(Criteria.where("_id").is(id));
					Update update = new Update();
					update.set("status", ReleaseStateEnums.TOP_LINE.getLable());
					mongoTemplate.findAndModify(query, update, DappEntity.class);
				}
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
	@SysLogAnnotation(module = "Dapp发现页配置管理", type = "post", remark = "发现页更新")
	public ResponseBase update(String dataStr,MultipartFile file) {
		try {
			if(dataStr != null && dataStr.length() > 0) {
				DappEntity dappEntity = JSONObject.parseObject(dataStr, DappEntity.class);
				DappEntity entity = mongoTemplate.findById(dappEntity.getId(), DappEntity.class);
				if(dappEntity!= null && entity != null) {
					if(file != null) {
						String logUrl = this.fileUploadUtil(file, FilePathEnums.DAPP.getIndex(), dappEntity.getId());
						if(logUrl!= null && logUrl.length() > 0) {
							dappEntity.setLogoURL(logUrl);
						}
					}
					JSONObject jsonObject = (JSONObject) JSONObject.toJSON(dappEntity);
//					jsonObject.put("_id", dappEntity.getId());
					jsonObject.remove("id");
					String jsonParam = JSON.toJSONString(jsonObject);
					String res = HttpUtil.sendPostRequest(HTTP_URL+HttpAddressEunms.DAPP_UPDATE.getName(), jsonParam);
					RpcResponseEntity rpcResponse = JSONObject.parseObject(res, RpcResponseEntity.class);
					if(rpcResponse.getSuccess() != null && rpcResponse.getSuccess()) {
						mongoTemplate.save(dappEntity);
					}
					return setResultSuccess();
				}else {
					return setResultError(Constants.ERROR);
				}
			}else {
				return setResultError(Constants.ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "Dapp发现页配置管理", type = "post", remark = "发现页新增")
	public ResponseBase add(String dataStr, MultipartFile file) {
		try {
			if(dataStr != null && dataStr.length() > 0) {
				DappEntity dappEntity = JSONObject.parseObject(dataStr, DappEntity.class);
				if(dappEntity!= null) {
					//判断url是否已存在
					if(dappEntity.getUrl() != null && dappEntity.getUrl().length()> 0) {
						Query query = new Query();
						query.addCriteria(Criteria.where("url").is(dappEntity.getUrl()));
						DappEntity entity = mongoTemplate.findOne(query, DappEntity.class);
						if(entity != null) {
							return setResultError("url已存在，无法创建");
						}
					}
					
					if(file != null) {
						String logUrl = this.fileUploadUtil(file, FilePathEnums.DAPP.getIndex(), null);
						if(logUrl!= null && logUrl.length() > 0) {
							dappEntity.setLogoURL(logUrl);
						}
					}
					List<String> tagList = new ArrayList<>();
					List<String> categoryList = new ArrayList<>();
					if(dappEntity.getCategoryIds() != null && dappEntity.getCategoryIds().size() > 0) {
						for (int i = 0; i < dappEntity.getCategoryIds().size(); i++) {
							DappTypeEntity typeEntity = mongoTemplate.findById(dappEntity.getCategoryIds().get(i), DappTypeEntity.class);
							if(typeEntity != null) {
								if(TokenTypeLableEunms.TAG.getName().equals(typeEntity.getType())) {
									tagList.add(typeEntity.getId());
								}else {
									categoryList.add(typeEntity.getId());
								}
							}
						}
					}
					dappEntity.setTagIds(tagList);
					dappEntity.setCategoryIds(categoryList);
					JSONObject jsonObject = (JSONObject) JSONObject.toJSON(dappEntity);
					JSONObject localization = jsonObject.getJSONObject("localization");
				    // 删除 _id 属性
				    if (localization != null) {
				      localization.remove("_id");
				    }
					String jsonParam = JSON.toJSONString(jsonObject);
					String res = HttpUtil.sendPostRequest(HTTP_URL+HttpAddressEunms.DAPP_ADD.getName(), jsonParam);
					RpcResponseEntity rpcResponse = JSONObject.parseObject(res, RpcResponseEntity.class);
					if(rpcResponse.getSuccess() != null && rpcResponse.getSuccess()) {
//						GenericityUtil.setTokenDateStr(dappEntity);
//						mongoTemplate.save(dappEntity);
						this.rpcList();
					}
					return setResultSuccess();
				}else {
					return setResultError(Constants.ERROR);
				}
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
			String fileName = FilePathEnums.getName(typeId) + file.getOriginalFilename();
			System.out.println(fileName);
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

}
