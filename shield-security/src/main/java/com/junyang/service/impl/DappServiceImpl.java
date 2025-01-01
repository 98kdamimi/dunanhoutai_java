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
import com.junyang.entity.dapp.RpcDappEntity;
import com.junyang.entity.dapp.RpcDappTagsEntity;
import com.junyang.entity.network.NetWorkEntity;
import com.junyang.entity.response.DappResEntity;
import com.junyang.entity.response.RpcResponseEntity;
import com.junyang.entity.uploadefiel.UploadFileEntity;
import com.junyang.enums.DappHttpAddressEunms;
import com.junyang.enums.DappTypeLableEunms;
import com.junyang.enums.FilePathEnums;
import com.junyang.enums.HttpAddressEunms;
import com.junyang.enums.LanguageEnums;
import com.junyang.enums.ReleaseStateEnums;
import com.junyang.query.PublicQueryEntity;
import com.junyang.service.DappService;
import com.junyang.utils.DappUtil;
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
							if (list.get(i).getLogoURL() != null && list.get(i).getLogoURL().length() > 0) {
								MultipartFile urlFile = FileUploadUtil
										.convertUrlToMultipartFile(list.get(i).getLogoURL());
								if (urlFile != null) {
									String logoImg = this.fileUploadUtil(urlFile, FilePathEnums.DAPP.getIndex(), null);
									if (logoImg != null && logoImg.length() > 0) {
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
				List<DappTypeEntity> list = JSONArray.parseArray(responseEntity.getData().toString(),
						DappTypeEntity.class);
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
			if (entity.getStatus() != null && entity.getStatus().length() > 0) {
				query.addCriteria(Criteria.where("status").is(entity.getStatus()));
			}
			if (entity.getType() != null && entity.getType().length() > 0) {
				DappTypeEntity typeEntity = mongoTemplate.findById(entity.getType(), DappTypeEntity.class);
				if (typeEntity != null) {
					if (DappTypeLableEunms.TAG.getName().equals(typeEntity.getType())) {
						query.addCriteria(Criteria.where("tagIds").in(entity.getType()));
					} else {
						query.addCriteria(Criteria.where("categoryIds").in(entity.getType()));
					}
				}
			}
			if (entity.getTitle() != null && entity.getTitle().length() > 0) {
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
			if (dappEntity != null) {
				dappEntity.setStatus(ReleaseStateEnums.DOWN_LINE.getLable());
				JSONObject jsonObject = (JSONObject) JSONObject.toJSON(dappEntity);
//				jsonObject.put("_id", dappEntity.getId());
				String jsonParam = JSON.toJSONString(jsonObject);
				String res = HttpUtil.sendPostRequest(HTTP_URL + HttpAddressEunms.DAPP_UPDATE.getName(), jsonParam);
				RpcResponseEntity rpcResponse = JSONObject.parseObject(res, RpcResponseEntity.class);
				if (rpcResponse.getSuccess() != null && rpcResponse.getSuccess()) {
					Query query = new Query(Criteria.where("_id").is(id));
					Update update = new Update();
					update.set("status", ReleaseStateEnums.DOWN_LINE.getLable());
					mongoTemplate.findAndModify(query, update, DappEntity.class);
				}
				return setResultSuccess();
			} else {
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
			if (dappEntity != null) {
				dappEntity.setStatus(ReleaseStateEnums.TOP_LINE.getLable());
				JSONObject jsonObject = (JSONObject) JSONObject.toJSON(dappEntity);
//				jsonObject.put("_id", dappEntity.getId());
				String jsonParam = JSON.toJSONString(jsonObject);
				String res = HttpUtil.sendPostRequest(HTTP_URL + HttpAddressEunms.DAPP_UPDATE.getName(), jsonParam);
				RpcResponseEntity rpcResponse = JSONObject.parseObject(res, RpcResponseEntity.class);
				if (rpcResponse.getSuccess() != null && rpcResponse.getSuccess()) {
					Query query = new Query(Criteria.where("_id").is(id));
					Update update = new Update();
					update.set("status", ReleaseStateEnums.TOP_LINE.getLable());
					mongoTemplate.findAndModify(query, update, DappEntity.class);
				}
				return setResultSuccess();
			} else {
				return setResultError(Constants.ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "Dapp发现页配置管理", type = "post", remark = "发现页更新")
	public ResponseBase update(String dataStr, MultipartFile file) {
		try {
			if (dataStr != null && dataStr.length() > 0) {
				DappEntity dappEntity = JSONObject.parseObject(dataStr, DappEntity.class);
				DappEntity entity = mongoTemplate.findById(dappEntity.getId(), DappEntity.class);
				if (dappEntity != null && entity != null) {
					if (file != null) {
						String logUrl = this.fileUploadUtil(file, FilePathEnums.DAPP.getIndex(), dappEntity.getId());
						if (logUrl != null && logUrl.length() > 0) {
							dappEntity.setLogoURL(logUrl);
						}
					}
					JSONObject jsonObject = (JSONObject) JSONObject.toJSON(dappEntity);
//					jsonObject.put("_id", dappEntity.getId());
					jsonObject.remove("id");
					String jsonParam = JSON.toJSONString(jsonObject);
					String res = HttpUtil.sendPostRequest(HTTP_URL + HttpAddressEunms.DAPP_UPDATE.getName(), jsonParam);
					RpcResponseEntity rpcResponse = JSONObject.parseObject(res, RpcResponseEntity.class);
					if (rpcResponse.getSuccess() != null && rpcResponse.getSuccess()) {
						mongoTemplate.save(dappEntity);
					}
					return setResultSuccess();
				} else {
					return setResultError(Constants.ERROR);
				}
			} else {
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
			if (dataStr != null && dataStr.length() > 0) {
				DappEntity dappEntity = JSONObject.parseObject(dataStr, DappEntity.class);
				if (dappEntity != null) {
					// 判断url是否已存在
					if (dappEntity.getUrl() != null && dappEntity.getUrl().length() > 0) {
						Query query = new Query();
						query.addCriteria(Criteria.where("url").is(dappEntity.getUrl()));
						DappEntity entity = mongoTemplate.findOne(query, DappEntity.class);
						if (entity != null) {
							return setResultError("url已存在，无法创建");
						}
					}

					if (file != null) {
						String logUrl = this.fileUploadUtil(file, FilePathEnums.DAPP.getIndex(), null);
						if (logUrl != null && logUrl.length() > 0) {
							dappEntity.setLogoURL(logUrl);
						}
					}
					List<String> tagList = new ArrayList<>();
					List<String> categoryList = new ArrayList<>();
					if (dappEntity.getCategoryIds() != null && dappEntity.getCategoryIds().size() > 0) {
						for (int i = 0; i < dappEntity.getCategoryIds().size(); i++) {
							DappTypeEntity typeEntity = mongoTemplate.findById(dappEntity.getCategoryIds().get(i),
									DappTypeEntity.class);
							if (typeEntity != null) {
								if (DappTypeLableEunms.TAG.getName().equals(typeEntity.getType())) {
									tagList.add(typeEntity.getId());
								} else {
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
					String res = HttpUtil.sendPostRequest(HTTP_URL + HttpAddressEunms.DAPP_ADD.getName(), jsonParam);
					RpcResponseEntity rpcResponse = JSONObject.parseObject(res, RpcResponseEntity.class);
					if (rpcResponse.getSuccess() != null && rpcResponse.getSuccess()) {
//						GenericityUtil.setTokenDateStr(dappEntity);
//						mongoTemplate.save(dappEntity);
						this.rpcList();
					}
					return setResultSuccess();
				} else {
					return setResultError(Constants.ERROR);
				}
			} else {
				return setResultError(Constants.ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public String fileUploadUtil(MultipartFile file, Integer typeId, String dbId) {
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
			// 创建上传请求
			PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, tempFile);
			// 执行上传
			amazonS3.putObject(putObjectRequest);
			String fileUrl = amazonS3.getUrl(bucketName, fileName).toString();
			if (fileUrl != null && fileUrl.length() > 0) {
				try {
					UploadFileEntity entity = new UploadFileEntity();
					entity.setDatabseName(FilePathEnums.getDatabaseName(typeId));
					entity.setFilePath(fileUrl);
					// 获取文件名
					String oldFileName = file.getOriginalFilename();
					// 获取文件的后缀名
					String suffixName = oldFileName.substring(oldFileName.lastIndexOf(".") + 1);
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
	public ResponseBase rpcTop() {
		try {
			String resData = HttpUtil.dappGet(DappHttpAddressEunms.GET_TOP.getName());
			DappResEntity entity = JSONObject.parseObject(resData, DappResEntity.class);
			List<RpcDappEntity> list = entity.getResults();
			List<DappEntity> dappList = new ArrayList<DappEntity>();
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					DappEntity dappEntity = new DappEntity();
					dappEntity.setName(list.get(i).getName());
					dappEntity.setStatus(ReleaseStateEnums.TOP_LINE.getLable());
					dappEntity.setSubtitle(list.get(i).getDescription());
					dappEntity.setUrl(list.get(i).getWebsite());
					if (list.get(i).getLogo() != null && list.get(i).getLogo().length() > 0) {
						MultipartFile urlFile = FileUploadUtil.convertUrlToMultipartFile(list.get(i).getLogo());//上传至亚马逊()
						if (urlFile != null) {
							String logoImg = this.fileUploadUtil(urlFile, FilePathEnums.DAPP.getIndex(), null);
							if (logoImg != null && logoImg.length() > 0) {
								dappEntity.setLogoURL(logoImg);
							}
						}else {
							dappEntity.setLogoURL(list.get(i).getLogo());
						}
					}else {
						continue;
					}
					// 添加多语言
					JSONObject object = this.addLocalization(dappEntity.getSubtitle());
					dappEntity.setLocalization(object);
					// 格式化network
					List<String> networks = this.formatNetwork(list.get(i).getChains());
					dappEntity.setNetworkIds(networks);
					// 格式化Categories
					List<String> categoriesList = this.formstCategories(list.get(i).getCategories());
					dappEntity.setCategoryIds(categoriesList);
					// 格式化tag
					List<String> tagsList = this.formstTags(list.get(i).getTags());
					dappEntity.setTagIds(tagsList);
					if(dappEntity.getSubtitle() != null && dappEntity.getSubtitle().length() > 0 
							&& dappEntity.getLogoURL() != null && dappEntity.getLogoURL().length() > 0) {
						dappList.add(dappEntity);
					}
				}
			}
			if(dappList != null && dappList.size() > 0) {
				for (int i = 0; i < dappList.size(); i++) {
					String jsonParam = JSON.toJSONString(dappList.get(i));
					HttpUtil.sendPostRequest(HTTP_URL + HttpAddressEunms.DAPP_ADD.getName(), jsonParam);
				}
			}
			this.rpcList();
			return setResultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	@Override
	public ResponseBase rpcDapp() {
		try {
			String resData = HttpUtil.dappGet(DappHttpAddressEunms.GET_NETWORK.getName());//获取第三方支持的链
			JSONObject jsonObject = JSONObject.parseObject(resData);
			List<String> chainList = JSONArray.parseArray(jsonObject.getString("chains"), String.class); 
			List<DappEntity> dappList = new ArrayList<DappEntity>();
			if(chainList != null && chainList.size() > 0) {
				for (int i = 0; i < chainList.size(); i++) {
					//根据第三方链信息匹配数据库对应链信息
					Query query = new Query();
					query.addCriteria(Criteria.where("name").is(DappUtil.capitalizeFirstLetter(chainList.get(i))));
					NetWorkEntity entity = mongoTemplate.findOne(query, NetWorkEntity.class);
					if (entity != null) {
						//调用第三方获取本条链下dapp
						String resDapp = HttpUtil.dappGet(DappHttpAddressEunms.GET_DAPP.getName()+"?chain="+chainList.get(i)+"&page="+Constants.PAGENUMBER+"&resultsPerPage="+Constants.DAPP_PAGESIZE+"");//获取第三方支持的链
						DappResEntity dappResEntity = JSONObject.parseObject(resDapp, DappResEntity.class);
						List<RpcDappEntity> list = dappResEntity.getResults();
						if(list != null && list.size() > 0) {
							for (int j = 0; j <  list.size(); j++) {
								DappEntity dappEntity = new DappEntity();
								dappEntity.setName(list.get(j).getName());
								dappEntity.setStatus(ReleaseStateEnums.TOP_LINE.getLable());
								dappEntity.setSubtitle(list.get(j).getDescription());
								dappEntity.setUrl(list.get(j).getWebsite());
								if (list.get(j).getLogo() != null && list.get(j).getLogo().length() > 0) {
									MultipartFile urlFile = FileUploadUtil.convertUrlToMultipartFile(list.get(j).getLogo());// 上传至亚马逊()
									if (urlFile != null) {
										String logoImg = this.fileUploadUtil(urlFile, FilePathEnums.DAPP.getIndex(), null);
										if (logoImg != null && logoImg.length() > 0) {
											dappEntity.setLogoURL(logoImg);
										}
									}else {
										dappEntity.setLogoURL(list.get(j).getLogo());
									}
								}else {
									continue;
								}
								// 添加多语言
								JSONObject object = this.addLocalization(dappEntity.getSubtitle());
								dappEntity.setLocalization(object);
								// 格式化network
								List<String> networks = this.formatNetwork(list.get(j).getChains());
								dappEntity.setNetworkIds(networks);
								// 格式化Categories
								List<String> categoriesList = this.formstCategories(list.get(j).getCategories());
								dappEntity.setCategoryIds(categoriesList);
								// 格式化tag
								List<String> tagsList = this.addTags(list.get(j).getTags());
								dappEntity.setTagIds(tagsList);
								if(dappEntity.getSubtitle() != null && dappEntity.getSubtitle().length() > 0 
										&& dappEntity.getLogoURL() != null && dappEntity.getLogoURL().length() > 0) {
									dappList.add(dappEntity);
								}
							}
						}
					}
				}
			}
			if(dappList != null && dappList.size() > 0) {
				for (int i = 0; i < dappList.size(); i++) {
					String jsonParam = JSON.toJSONString(dappList.get(i));
					HttpUtil.sendPostRequest(HTTP_URL + HttpAddressEunms.DAPP_ADD.getName(), jsonParam);
				}
			}
			this.rpcList();
			return setResultSuccess(dappList);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public JSONObject addLocalization(String subtitle) {
		JSONObject object = new JSONObject();
		String simplifiedChinese = DappUtil.translate(subtitle, LanguageEnums.EN_US.getValue(), LanguageEnums.ZH_CN.getValue()); // 中文繁体体
		String content = DappUtil.unicodeToString(simplifiedChinese);
		JSONObject jsonObject = JSONObject.parseObject(content);
		if(jsonObject.get("trans_result") != null && jsonObject.get("trans_result").toString().length() > 0) {
			JSONArray array = JSONArray.parseArray(jsonObject.get("trans_result").toString());
			JSONObject dst = JSONObject.parseObject(array.get(0).toString());
			object.put("zh-CN", dst.get("dst"));
			object.put("zh-HK", dst.get("dst"));
		}else {
			object.put("zh-CN", "");
			object.put("zh-HK", "");
		}
//		String str = XfWebOTS.enToCht(subtitle);
//		object.put("zh-CN", str);
//		object.put("zh-HK", str);
		object.put("en-US", subtitle);
		object.put("de", "");
		object.put("es", "");
		object.put("ar", "");
		object.put("bn", "");
		object.put("fil", "");
		object.put("fr-FR", "");
		object.put("hi-IN", "");
		object.put("it-IT", "");
		object.put("ja-JP", "");
		object.put("ko-KR", "");
		object.put("mn-MN", "");
		object.put("pt", "");
		object.put("ru", "");
		object.put("th-TH", "");
		object.put("uk-UA", "");
		object.put("vi", "");
		return object;
	}

	public List<String> formatNetwork(List<String> chains) {
		List<String> list = new ArrayList<String>();
		if (chains != null && chains.size() > 0) {
			for (int i = 0; i < chains.size(); i++) {
				Query query = new Query();
				query.addCriteria(Criteria.where("name").is(DappUtil.capitalizeFirstLetter(chains.get(i))));
				NetWorkEntity entity = mongoTemplate.findOne(query, NetWorkEntity.class);
				if (entity != null) {
					list.add(entity.getId());
				}
			}
		}
		return list;
	}

	public List<String> formstCategories(List<String> categoriesList) {
		List<String> list = new ArrayList<String>();
		if (categoriesList != null && categoriesList.size() > 0) {
			for (int i = 0; i < categoriesList.size(); i++) {
				Query query = new Query();
				query.addCriteria(Criteria.where("type").is("category"));
				query.addCriteria(Criteria.where("name").is(DappUtil.capitalizeFirstLetter(categoriesList.get(i))));
				DappTypeEntity entity = mongoTemplate.findOne(query, DappTypeEntity.class);
				if (entity != null) {
					list.add(entity.getId());
				}
			}
		}
		return list;
	}

	public List<String> formstTags(List<RpcDappTagsEntity> TagsList) {
		List<String> list = new ArrayList<String>();
		if (TagsList != null && TagsList.size() > 0) {
			for (int i = 0; i < TagsList.size(); i++) {
				Query query = new Query();
				query.addCriteria(Criteria.where("type").is("tag"));
				query.addCriteria(Criteria.where("name").is(DappUtil.capitalizeFirstLetter(TagsList.get(i).getName())));
				DappTypeEntity entity = mongoTemplate.findOne(query, DappTypeEntity.class);
				if (entity != null) {
					list.add(entity.getId());
				}
			}
		}
		this.tagAddTrendy(list);
		return list;
	}
	
	public List<String> addTags(List<RpcDappTagsEntity> TagsList) {
		List<String> list = new ArrayList<String>();
		if (TagsList != null && TagsList.size() > 0) {
			for (int i = 0; i < TagsList.size(); i++) {
				Query query = new Query();
				query.addCriteria(Criteria.where("type").is("tag"));
				query.addCriteria(Criteria.where("name").is(DappUtil.capitalizeFirstLetter(TagsList.get(i).getName())));
				DappTypeEntity entity = mongoTemplate.findOne(query, DappTypeEntity.class);
				if (entity != null) {
					list.add(entity.getId());
				}
			}
		}
		return list;
	}

	public void tagAddTrendy(List<String> list) {
		Query query = new Query();
		query.addCriteria(Criteria.where("type").is("tag"));
		query.addCriteria(Criteria.where("name").regex(".*Trendy.*", "i"));
		DappTypeEntity entity = mongoTemplate.findOne(query, DappTypeEntity.class);
		if (entity != null) {
			list.add(entity.getId());
		}
	}

	

	
	
	
	
	
	public static void main(String[] args) {
		String str = "solana";
		String result = DappUtil.capitalizeFirstLetter(str);
		System.out.println(result); // 输出: Solana
		String subtitle = "Chain Signature Protocol and multiplatform MPC Wallet";
		String simplifiedChinese = DappUtil.translate(subtitle, "en", "cht"); // 中文繁体体
		String content = DappUtil.unicodeToString(simplifiedChinese);
		JSONObject jsonObject = JSONObject.parseObject(content);
		JSONArray array = JSONArray.parseArray(jsonObject.get("trans_result").toString());
		JSONObject object = JSONObject.parseObject(array.get(0).toString());
		System.out.println(object.get("dst"));
		System.out.println(content);
	}

}
