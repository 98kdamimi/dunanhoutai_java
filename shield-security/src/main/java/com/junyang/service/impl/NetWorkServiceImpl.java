package com.junyang.service.impl;

import java.io.File;
import java.io.IOException;
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
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.github.pagehelper.PageInfo;
import com.junyang.aop.SysLogAnnotation;
import com.junyang.base.BaseApiService;
import com.junyang.base.ResponseBase;
import com.junyang.entity.network.NetWorkEntity;
import com.junyang.entity.response.DicEntity;
import com.junyang.entity.uploadefiel.UploadFileEntity;
import com.junyang.enums.FilePathEnums;
import com.junyang.enums.HttpAddressEunms;
import com.junyang.enums.ReleaseStateEnums;
import com.junyang.query.PublicQueryEntity;
import com.junyang.service.NetWorkService;
import com.junyang.utils.FileUploadUtil;
import com.junyang.utils.GenericityUtil;
import com.junyang.utils.HttpUtil;

@RestController
@Transactional
@CrossOrigin
public class NetWorkServiceImpl extends BaseApiService implements NetWorkService {

	@Value("${http_url}")
	private String HTTP_URL;
	
	@Value("${aws-s3-bucket-name}")
	private String bucketName;

	private final AmazonS3 amazonS3;
	
	public NetWorkServiceImpl(AmazonS3 amazonS3) {
		this.amazonS3 = amazonS3;
	}

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
								if(entity.getLogoURI() != null && entity.getLogoURI().length() > 0) {
									MultipartFile urlFile = FileUploadUtil.convertUrlToMultipartFile(entity.getLogoURI());
									if(urlFile != null) {
										String logoImg = this.fileUploadUtil(urlFile, FilePathEnums.NETWORK.getIndex(), null);
										if(logoImg!= null && logoImg.length() > 0) {
											entity.setLogoURI(logoImg);
										}
									}
								}
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
