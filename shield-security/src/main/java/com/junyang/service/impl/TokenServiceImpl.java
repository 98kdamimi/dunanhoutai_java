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
import com.junyang.entity.response.DicEntity;
import com.junyang.entity.response.RpcResponseEntity;
import com.junyang.entity.token.PlatformTokenEntity;
import com.junyang.entity.token.TokenEntity;
import com.junyang.entity.uploadefiel.UploadFileEntity;
import com.junyang.enums.FilePathEnums;
import com.junyang.enums.HttpAddressEunms;
import com.junyang.enums.SourceEnums;
import com.junyang.query.PublicQueryEntity;
import com.junyang.service.TokenService;
import com.junyang.utils.CoinGeckoAPIUtil;
import com.junyang.utils.FileUploadUtil;
import com.junyang.utils.GenericityUtil;
import com.junyang.utils.HttpUtil;

@RestController
@Transactional
@CrossOrigin
public class TokenServiceImpl extends BaseApiService implements TokenService{
	
	@Value("${http_url}")
	private String HTTP_URL;
	
	@Value("${aws-s3-bucket-name}")
	private String bucketName;

	private final AmazonS3 amazonS3;
	
	public TokenServiceImpl(AmazonS3 amazonS3) {
		this.amazonS3 = amazonS3;
	}

	@Autowired
	private MongoTemplate mongoTemplate;

	@SysLogAnnotation(module = "代币管理", type = "GET", remark = "接口获取代币列表")
	public ResponseBase rpcList(String url) {
		try {
			String baseStr = HttpUtil.get(url);
			RpcResponseEntity responseEntity = JSONObject.parseObject(baseStr, RpcResponseEntity.class);
			if(responseEntity.getData() != null && responseEntity.getData().toString().length() > 0) {
				List<TokenEntity> list = JSONArray.parseArray(responseEntity.getData().toString(), TokenEntity.class);
				if(list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						Query query = new Query(Criteria.where("_id").is(list.get(i).getId()));
						boolean exists = mongoTemplate.exists(query, TokenEntity.class, "token_db");
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
	@SysLogAnnotation(module = "代币管理", type = "GET", remark = "获取第三方平台代币")
	public ResponseBase ThirdPartylist() {
		try {
			String str = CoinGeckoAPIUtil.fetchTokenData();
			if(str != null &&str.length() > 0) {
				List<PlatformTokenEntity> list = JSONArray.parseArray(str, PlatformTokenEntity.class);
				if(list != null && list.size() > 0) {
					for (int i = 0; i < list.size(); i++) {
						Query query = new Query(Criteria.where("_id").is(list.get(i).getId()));
						boolean exists = mongoTemplate.exists(query, PlatformTokenEntity.class, "platform_token");
						if (exists == false) {
							mongoTemplate.insert(list.get(i));
						}
					}
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		return null;
	}

	@Override
	@SysLogAnnotation(module = "代币管理", type = "POST", remark = "分页代币列表")
	public ResponseBase findList(@RequestBody PublicQueryEntity entity) {
		try {
			//manager/token/list?chainId=1&impl=evm
//			this.rpcList(HTTP_URL+HttpAddressEunms.TOKEN_LIST.getName()+"chainId="+entity.getChainId()+"&impl="+entity.getImpl());
			Query query = new Query();
			if(entity.getChainId() != null) {
				query.addCriteria(Criteria.where("chainId").is(entity.getChainId()));
			}
			if(entity.getName() != null && entity.getName().length() > 0) {
				query.addCriteria(Criteria.where("symbol").is(entity.getName()));
			}
			long totalCount = mongoTemplate.count(query, TokenEntity.class);// 总条数
			// 构建分页请求对象
			int pageNumber = Math.max(entity.getPageNumber() - 1, 0);
			PageRequest pageRequest = PageRequest.of(pageNumber, entity.getPageSize(),
					Sort.by(Sort.Direction.DESC, "createdAt"));
			query.with(pageRequest);
			// 执行分页查询
			List<TokenEntity> list = mongoTemplate.find(query, TokenEntity.class);
			// 获取总记录数
			PageInfo<TokenEntity> info = new PageInfo<>(list);
			info.setTotal(totalCount);
			return setResultSuccess(info);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "代币管理", type = "POST", remark = "新增代币")
	public ResponseBase add(String dataStr,MultipartFile file) {
		try {
			if(dataStr != null && dataStr.length() > 0) {
				JSONObject jsonObject = JSONObject.parseObject(dataStr);
				if(jsonObject != null) {
					if(file != null) {
						String logoImg = this.fileUploadUtil(file, FilePathEnums.TOKENS.getIndex(), null);
						if(logoImg!= null && logoImg.length() > 0) {
							jsonObject.put("logoURI", logoImg);
						}
					}
					String jsonParam = JSON.toJSONString(jsonObject);
					String res = HttpUtil.sendPostRequest(HTTP_URL+HttpAddressEunms.TOKEN_ADD.getName(), jsonParam);
					RpcResponseEntity rpcResponse = JSONObject.parseObject(res, RpcResponseEntity.class);
					if(rpcResponse.getSuccess() != null && rpcResponse.getSuccess()) {
						TokenEntity entity = JSONObject.parseObject(jsonParam, TokenEntity.class);
						GenericityUtil.setTokenDateStr(entity);
						mongoTemplate.insert(entity);
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
	@SysLogAnnotation(module = "代币管理", type = "POST", remark = "更新代币")
	public ResponseBase update(String dataStr,MultipartFile file) {
		try {
			if(dataStr != null && dataStr.length() > 0) {
				TokenEntity entity = JSONObject.parseObject(dataStr, TokenEntity.class);
				TokenEntity tokenEntity = mongoTemplate.findById(entity.getId(), TokenEntity.class);
				if(entity != null && tokenEntity != null) {
					if(file != null) {
						String logoImg = this.fileUploadUtil(file, FilePathEnums.TOKENS.getIndex(), entity.getId());
						if(logoImg!= null && logoImg.length() > 0) {
							entity.setLogoURI(logoImg);
						}
					}
					JSONObject jsonObject = (JSONObject) JSONObject.toJSON(entity);
					jsonObject.put("_id", entity.getId());
					String jsonParam = JSON.toJSONString(jsonObject);
					String res = HttpUtil.sendPostRequest(HTTP_URL+HttpAddressEunms.TOKEN_UPDATE.getName(), jsonParam);
					RpcResponseEntity rpcResponse = JSONObject.parseObject(res, RpcResponseEntity.class);
					if(rpcResponse.getSuccess() != null && rpcResponse.getSuccess()) {
						mongoTemplate.save(entity);
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
	public ResponseBase findListThirdParty(@RequestBody PublicQueryEntity entity) {
		try {
			Query query = new Query();
			long totalCount = mongoTemplate.count(query, PlatformTokenEntity.class);// 总条数
			// 构建分页请求对象
			int pageNumber = Math.max(entity.getPageNumber() - 1, 0);
			PageRequest pageRequest = PageRequest.of(pageNumber, entity.getPageSize(),
					Sort.by(Sort.Direction.ASC, "last_updated"));
			query.with(pageRequest);
			// 执行分页查询
			List<PlatformTokenEntity> list = mongoTemplate.find(query, PlatformTokenEntity.class);
			// 获取总记录数
			PageInfo<PlatformTokenEntity> info = new PageInfo<>(list);
			info.setTotal(totalCount);
			return setResultSuccess(info);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase findSource() {
		List<DicEntity> list = SourceEnums.getList();
		return setResultSuccess(list);
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
			return fileUrl;

		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
