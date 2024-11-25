package com.junyang.service.impl;

import java.io.File;
import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.github.pagehelper.PageInfo;
import com.junyang.base.BaseApiService;
import com.junyang.base.ResponseBase;
import com.junyang.constants.Constants;
import com.junyang.entity.response.DicEntity;
import com.junyang.entity.uploadefiel.UploadFileEntity;
import com.junyang.enums.FilePathEnums;
import com.junyang.query.PublicQueryEntity;
import com.junyang.service.UploadService;
import com.junyang.utils.FileUploadUtil;
import com.junyang.utils.GenericityUtil;

@RestController
@Transactional
@CrossOrigin
public class UploadServiceImpl extends BaseApiService implements UploadService {

	@Value("${aws-s3-bucket-name}")
	private String bucketName;
	
	@Autowired
	private MongoTemplate mongoTemplate;

	private final AmazonS3 amazonS3;

	public UploadServiceImpl(AmazonS3 amazonS3) {
		this.amazonS3 = amazonS3;
	}

	@Override
	public ResponseBase fileUpload(@RequestParam("file") MultipartFile file,@RequestParam("typeId")Integer typeId, String dbId) {
		try {
			// 检查文件是否为空或大小是否超过限制
			if (file.isEmpty() || file.getSize() > 200 * 1024 * 1024) { // 假设最大文件大小为 200MB
				return setResultError("文件为空或超过最大限制");
			}
			// 创建临时文件
			File tempFile = File.createTempFile("upload-", file.getOriginalFilename(),
					new File(System.getProperty("java.io.tmpdir")));
			file.transferTo(tempFile); // 将 MultipartFile 保存为 File
			// 构建 S3 中的完整路径（目录+文件名）
			String fileName = FilePathEnums.getValue(typeId) + file.getOriginalFilename();
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
					entity.setDatabseId(dbId);
					GenericityUtil.setDate(entity);
					mongoTemplate.insert(entity);
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException();
				}
			}
			return setResultSuccess(fileUrl);

		} catch (IOException e) {
			e.printStackTrace();
			return setResultError(Constants.ERROR);
		}
	}

	@Override
	public ResponseBase findList(@RequestBody PublicQueryEntity entity) {
		try {
			Query query = new Query();
			if (entity.getTypeId() != null) {
	            query.addCriteria(Criteria.where("typeId").is(entity.getTypeId()));
	        }
			long totalCount = mongoTemplate.count(query, UploadFileEntity.class);// 总条数
			// 构建分页请求对象
			int pageNumber = Math.max(entity.getPageNumber() - 1, 0);
			PageRequest pageRequest = PageRequest.of(pageNumber, entity.getPageSize(),
					Sort.by(Sort.Direction.ASC, "setTime"));
			query.with(pageRequest);
			// 执行分页查询
			List<UploadFileEntity> list = mongoTemplate.find(query, UploadFileEntity.class);
			// 获取总记录数
			PageInfo<UploadFileEntity> info = new PageInfo<>(list);
			info.setTotal(totalCount);
			return setResultSuccess(info);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase findTypeList() {
		List<DicEntity> list = FilePathEnums.getList();
		return setResultSuccess(list);
	}



}
