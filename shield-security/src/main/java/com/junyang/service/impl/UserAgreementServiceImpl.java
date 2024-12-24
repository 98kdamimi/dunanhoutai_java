package com.junyang.service.impl;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.List;
import org.bson.types.ObjectId;
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
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.github.pagehelper.PageInfo;
import com.junyang.aop.SysLogAnnotation;
import com.junyang.base.BaseApiService;
import com.junyang.base.ResponseBase;
import com.junyang.constants.Constants;
import com.junyang.entity.carouse.CarouselEntity;
import com.junyang.entity.dapp.DappEntity;
import com.junyang.entity.response.RpcResponseEntity;
import com.junyang.entity.system.UserAgreementEntity;
import com.junyang.entity.uploadefiel.UploadFileEntity;
import com.junyang.enums.AgreementTypeEnums;
import com.junyang.enums.FilePathEnums;
import com.junyang.enums.HttpAddressEunms;
import com.junyang.query.PublicQueryEntity;
import com.junyang.service.UserAgreementService;
import com.junyang.utils.FileUploadUtil;
import com.junyang.utils.GenericityUtil;
import com.junyang.utils.HttpUtil;

@RestController
@Transactional
@CrossOrigin
public class UserAgreementServiceImpl extends BaseApiService implements UserAgreementService{
	
	@Autowired
	private MongoTemplate mongoTemplate;
	

	@Value("${http_url}")
	private String HTTP_URL;
	
	@Value("${aws-s3-bucket-name}")
	private String bucketName;

	private final AmazonS3 amazonS3;
	
	public UserAgreementServiceImpl(AmazonS3 amazonS3) {
		this.amazonS3 = amazonS3;
	}


	@Override
	@SysLogAnnotation(module = "协议管理", type = "POST", remark = "新增协议")
	public ResponseBase add(@RequestBody UserAgreementEntity entity) {
		try {
			Query query = new Query();
			if(entity.getTypeId() != null) {
				query.addCriteria(Criteria.where("typeId").is(entity.getTypeId()));
			}
			if(entity.getLanguageType() != null) {
				query.addCriteria(Criteria.where("languageType").is(entity.getLanguageType()));
			}
			UserAgreementEntity agreementEntity = mongoTemplate.findOne(query, UserAgreementEntity.class);
			if(agreementEntity != null) {
				if(AgreementTypeEnums.USER_AGREEMENT.getIndex().equals(entity.getTypeId())) {
					return setResultError("此语言用户协议已存在，无法再次添加");
				}else if(AgreementTypeEnums.PRIVACY_AGREEMENT.getIndex().equals(entity.getTypeId())) {
					return setResultError("此语言隐私协议已存在，无法再次添加");
				}
				return setResultError("协议信息已存在，无法再次添加");
			}
			String fileName = null;
			if(AgreementTypeEnums.USER_AGREEMENT.getIndex().equals(entity.getTypeId())) {//用户协议
				fileName = AgreementTypeEnums.USER_AGREEMENT.getValue();
			}else if(AgreementTypeEnums.PRIVACY_AGREEMENT.getIndex().equals(entity.getTypeId())) {
				fileName = AgreementTypeEnums.PRIVACY_AGREEMENT.getValue();
			}
			MultipartFile file = FileUploadUtil.saveHtmlToFile(entity.getContentInfo(),fileName);
			String url = this.fileUploadUtil(file, 
					FilePathEnums.HELP.getIndex(), null,entity.getLanguageType());
			entity.setHtmlSite(url);
			entity.setTypeName(AgreementTypeEnums.getName(entity.getTypeId()));
//			UserAgreementEntity userAgreementEntity = mongoTemplate.insert(entity);
			JSONObject jsonObject = (JSONObject) JSONObject.toJSON(entity);
			String jsonParam = JSON.toJSONString(jsonObject);
			String res = HttpUtil.sendPostRequest(HTTP_URL+HttpAddressEunms.GREEMENT_ADD.getName(), jsonParam);
			RpcResponseEntity rpcResponse = JSONObject.parseObject(res, RpcResponseEntity.class);
			if(rpcResponse.getSuccess() != null && rpcResponse.getSuccess()) {
				this.getRpc();
				return setResultSuccess();

			}
			return setResultError(Constants.ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "协议管理", type = "POST", remark = "编辑协议")
	public ResponseBase update(@RequestBody UserAgreementEntity entity) {
		try {
			UserAgreementEntity agreementEntity = mongoTemplate.findById(entity.getId(), UserAgreementEntity.class);
			if(agreementEntity != null) {
				String fileName = null;
				if(AgreementTypeEnums.USER_AGREEMENT.getIndex().equals(entity.getTypeId())) {//用户协议
					fileName = AgreementTypeEnums.USER_AGREEMENT.getValue();
				}else if(AgreementTypeEnums.PRIVACY_AGREEMENT.getIndex().equals(entity.getTypeId())) {
					fileName = AgreementTypeEnums.PRIVACY_AGREEMENT.getValue();
				}
				MultipartFile file = FileUploadUtil.saveHtmlToFile(entity.getContentInfo(),fileName);
				String url = this.fileUploadUtil(file, 
						FilePathEnums.HELP.getIndex(), null,entity.getLanguageType());
				entity.setHtmlSite(url);
				JSONObject jsonObject = (JSONObject) JSONObject.toJSON(entity);
				String jsonParam = JSON.toJSONString(jsonObject);
				
				String res = HttpUtil.sendPostRequest(HTTP_URL+HttpAddressEunms.GREEMENT_UPDATE.getName(), jsonParam);
				RpcResponseEntity rpcResponse = JSONObject.parseObject(res, RpcResponseEntity.class);
				if(rpcResponse.getSuccess() != null && rpcResponse.getSuccess()) {
					entity.setGmtModified(new Date());
					mongoTemplate.save(entity);
					return setResultSuccess();
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

	@Override
	public ResponseBase findType(PublicQueryEntity entity) {
		try {
			Query query = new Query();
			if(entity.getTypeId() != null) {
				query.addCriteria(Criteria.where("typeId").is(entity.getTypeId()));
			}else {
				return setResult(400, "缺少类型参数", null);
			}
			long totalCount = mongoTemplate.count(query, UserAgreementEntity.class);// 总条数
			// 构建分页请求对象
			int pageNumber = Math.max(entity.getPageNumber() - 1, 0);
			PageRequest pageRequest = PageRequest.of(pageNumber, entity.getPageSize(),
					Sort.by(Sort.Direction.DESC, "setTime"));
			query.with(pageRequest);
			// 执行分页查询
			List<UserAgreementEntity> list = mongoTemplate.find(query, UserAgreementEntity.class);
			// 获取总记录数
			PageInfo<UserAgreementEntity> info = new PageInfo<>(list);
			info.setTotal(totalCount);
			return setResultSuccess(info);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	public void getRpc() {
		try {
			String baseStr = HttpUtil.get(HTTP_URL + HttpAddressEunms.GREEMENT_LIST.getName());
			RpcResponseEntity responseEntity = JSONObject.parseObject(baseStr, RpcResponseEntity.class);
			if (responseEntity.getData() != null && responseEntity.getData().toString().length() > 0) {
				List<UserAgreementEntity> list = JSONArray.parseArray(responseEntity.getData().toString(), UserAgreementEntity.class);
				if (list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						Query query = new Query(Criteria.where("_id").is(list.get(i).getId()));
						boolean exists = mongoTemplate.exists(query, UserAgreementEntity.class, "user_agreement");
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
	
	@Override
	public ResponseBase delete(String id) {
		try {
			if(id != null && id.length() > 0) {
				String baseStr = HttpUtil.get(HTTP_URL + HttpAddressEunms.GREEMENT_DELETE.getName()+"?id="+id+"");
				RpcResponseEntity responseEntity = JSONObject.parseObject(baseStr, RpcResponseEntity.class);
				if(responseEntity.getSuccess()) {
					 Query query = new Query(Criteria.where("_id").is(id));
					 mongoTemplate.remove(query, UserAgreementEntity.class);
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
	
	public String fileUploadUtil(MultipartFile file, Integer typeId, String dbId, String site) throws IllegalAccessException, InvocationTargetException {
	    try {
	        // 文件检查
	        if (file.isEmpty() || file.getSize() > 200 * 1024 * 1024) {
	            return "文件为空或超过最大限制";
	        }
	        // 创建临时文件
	        File tempFile = createTempFile(file);
	        // 上传到 S3
	        String fileName = FilePathEnums.getName(typeId) + site + "/" + file.getOriginalFilename();
	        String fileUrl = uploadToS3(tempFile, fileName);
	        if (fileUrl != null && !fileUrl.isEmpty()) {
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
	        }
	        // 删除临时文件
	        tempFile.delete();
	        return fileUrl;
	    } catch (IOException e) {
	        e.printStackTrace();
	        return "文件上传失败";
	    }
	}

	private File createTempFile(MultipartFile file) throws IOException {
	    // 创建临时文件
	    File tempFile = File.createTempFile("upload-", file.getOriginalFilename(),
	            new File(System.getProperty("java.io.tmpdir")));
	    
	    // 使用输入流将 MultipartFile 内容写入临时文件
	    try (InputStream inputStream = file.getInputStream();
	         FileOutputStream fos = new FileOutputStream(tempFile)) {
	        byte[] buffer = new byte[1024];
	        int bytesRead;
	        while ((bytesRead = inputStream.read(buffer)) != -1) {
	            fos.write(buffer, 0, bytesRead);
	        }
	    }
	    return tempFile;
	}

	private String uploadToS3(File file, String fileName) {
	    try {
	        ObjectMetadata metadata = new ObjectMetadata();
	        metadata.setContentType("text/html; charset=UTF-8");

	        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, file);
	        putObjectRequest.setMetadata(metadata);

	        amazonS3.putObject(putObjectRequest);
	        return amazonS3.getUrl(bucketName, fileName).toString();
	        
	    } catch (AmazonS3Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}

}
