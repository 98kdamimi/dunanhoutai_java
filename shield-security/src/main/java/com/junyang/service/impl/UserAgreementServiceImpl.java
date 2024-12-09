package com.junyang.service.impl;
import java.io.File;
import java.io.IOException;
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
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.github.pagehelper.PageInfo;
import com.junyang.aop.SysLogAnnotation;
import com.junyang.base.BaseApiService;
import com.junyang.base.ResponseBase;
import com.junyang.constants.Constants;
import com.junyang.entity.system.UserAgreementEntity;
import com.junyang.entity.uploadefiel.UploadFileEntity;
import com.junyang.enums.AgreementTypeEnums;
import com.junyang.enums.FilePathEnums;
import com.junyang.query.PublicQueryEntity;
import com.junyang.service.UserAgreementService;
import com.junyang.utils.FileUploadUtil;
import com.junyang.utils.GenericityUtil;

@RestController
@Transactional
@CrossOrigin
public class UserAgreementServiceImpl extends BaseApiService implements UserAgreementService{
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
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
			File file = FileUploadUtil.saveHtmlToFile(entity.getContentInfo(),fileName);
			String url = this.fileUploadUtil(FileUploadUtil.getMultipartFile(file), 
					FilePathEnums.HELP.getIndex(), null,entity.getLanguageType());
			entity.setHtmlSite(url);
			ObjectId id = new ObjectId();
			entity.setId(id.toString());
			entity.setTypeName(AgreementTypeEnums.getName(entity.getTypeId()));
			GenericityUtil.setDate(entity);
			mongoTemplate.insert(entity);
			return setResultSuccess();
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
				File file = FileUploadUtil.saveHtmlToFile(entity.getContentInfo(),fileName);
				String url = this.fileUploadUtil(FileUploadUtil.getMultipartFile(file), 
						FilePathEnums.HELP.getIndex(), null,entity.getLanguageType());
				entity.setHtmlSite(url);
				entity.setGmtModified(new Date());
				mongoTemplate.save(entity);
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
	
	public String fileUploadUtil(MultipartFile file,Integer typeId, String dbId,String site) {
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
			String fileName = FilePathEnums.getName(typeId)+ site+"/" + file.getOriginalFilename();
			 // 创建 ObjectMetadata 并设置文件的 Content-Type
	        ObjectMetadata metadata = new ObjectMetadata();
	        metadata.setContentType("text/html; charset=UTF-8"); // 明确设置字符编码
	        // 创建上传请求
			PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, tempFile);
			putObjectRequest.setMetadata(metadata);
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
	public ResponseBase delete(String id) {
		try {
	        Query query = new Query(Criteria.where("id").is(id));
	        mongoTemplate.remove(query, UserAgreementEntity.class);
	        return setResultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

}
