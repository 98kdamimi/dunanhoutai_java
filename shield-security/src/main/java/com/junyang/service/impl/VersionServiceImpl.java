package com.junyang.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.junyang.aop.SysLogAnnotation;
import com.junyang.base.BaseApiService;
import com.junyang.base.ResponseBase;
import com.junyang.constants.Constants;
import com.junyang.dao.version.AndroidGoogleDao;
import com.junyang.dao.version.DigtalshieDao;
import com.junyang.dao.version.DigtalshieFirmwareDao;
import com.junyang.dao.version.VersionAndroidDao;
import com.junyang.dao.version.VersionDao;
import com.junyang.dao.version.VersionDigtalshieDao;
import com.junyang.dao.version.VersionDigtalshieFirmwareDao;
import com.junyang.dao.version.VersionIosDao;
import com.junyang.dao.version.VersionTypeDao;
import com.junyang.entity.release.VersionAndroidEntity;
import com.junyang.entity.release.VersionAndroidGoogleEntity;
import com.junyang.entity.release.VersionDigtalshieEntity;
import com.junyang.entity.release.VersionDigtalshieFirmwareEntity;
import com.junyang.entity.release.VersionEntity;
import com.junyang.entity.release.VersionIosEntity;
import com.junyang.entity.release.VersionTypeEntity;
import com.junyang.entity.response.RpcResponseEntity;
import com.junyang.enums.FilePathEnums;
import com.junyang.enums.HttpAddressEunms;
import com.junyang.enums.ReleaseStateEnums;
import com.junyang.enums.VersionTypeEnums;
import com.junyang.service.VersionService;
import com.junyang.utils.GenericityUtil;
import com.junyang.utils.HttpUtil;
import com.junyang.utils.RedisUtil;

@RestController
@Transactional
@CrossOrigin
public class VersionServiceImpl extends BaseApiService implements VersionService {

	@Autowired
	private VersionDao versionDao;

	@Autowired
	private VersionIosDao versionIosDao;

	@Autowired
	private VersionAndroidDao versionAndroidDao;

	@Autowired
	private AndroidGoogleDao androidGoogleDao;

	@Autowired
	private DigtalshieDao digtalshieDao;

	@Autowired
	private DigtalshieFirmwareDao digtalshieFirmwareDao;

	@Autowired
	private VersionTypeDao versionTypeDao;
	
	@Autowired
	private VersionDigtalshieFirmwareDao VersionDigtalshieFirmwareDao;
	
	@Autowired
	private VersionDigtalshieDao versionDigtalshieDao;
	
	@Autowired
	private UploadServiceImpl uploadServiceImpl;

	@Autowired
	private RedisUtil redisUtil;

	@Value("${http_url}")
	private String HTTP_URL;

	@Override
	@SysLogAnnotation(module = "应用及硬件版本管理", type = "POST", remark = "发版接口")
	public ResponseBase add(String versionStr,MultipartFile iosFile,MultipartFile androidFile,MultipartFile deviceFile) {
		try {
			if (versionStr != null && versionStr.length() > 0) {
				VersionEntity entity = JSONObject.parseObject(versionStr, VersionEntity.class);
				if(entity != null) {
					// 更新通知
					redisUtil.set(Constants.MSG_KEY, JSON.toJSON(entity));
					entity.setReleaseState(ReleaseStateEnums.WAIT_LINE.getIndex());
					GenericityUtil.setDate(entity);
					versionDao.insert(entity);
					// ios版本信息
					this.addIos(entity);
					// 安卓版本信息
					this.addAndroid(entity);
					// 设备信息
					this.addDigtalshie(entity);
					if(iosFile != null) {
						uploadServiceImpl.fileUpload(iosFile, FilePathEnums.APK.getIndex(),entity.getId().toString());
					}
					if(androidFile != null) {
						uploadServiceImpl.fileUpload(androidFile, FilePathEnums.APK.getIndex(),entity.getId().toString());
					}
					if(deviceFile != null) {
						uploadServiceImpl.fileUpload(deviceFile, FilePathEnums.APK.getIndex(),entity.getId().toString());
					}
				}
				return setResultSuccess();
			} else {
				return setResultError(Constants.NULL_CONTETN);
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	// ios版本信息新增
	public void addIos(VersionEntity entity) {
		try {
			VersionIosEntity iosEntity = entity.getIos();
			if (iosEntity != null) {
				iosEntity.setReleaseState(ReleaseStateEnums.WAIT_LINE.getIndex());
				iosEntity.setVersionId(entity.getId());
				String versionStr = this.formatList(iosEntity.getVersion());
				iosEntity.setIosVersion(versionStr);
				GenericityUtil.setDate(iosEntity);
				versionIosDao.insert(iosEntity);
				// 更新类型信息添加
				VersionTypeEntity typeEntity = new VersionTypeEntity();
				typeEntity.setVersionId(entity.getId());
				typeEntity.setVersionTypeId(VersionTypeEnums.IOS.getIndex());
				typeEntity.setVersionTypeName(VersionTypeEnums.IOS.getName());
				typeEntity.setVersionCode(versionStr);
				typeEntity.setViceId(iosEntity.getId());
				GenericityUtil.setDate(typeEntity);
				versionTypeDao.insert(typeEntity);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	// 安卓版本信息新增
	public void addAndroid(VersionEntity entity) {
		try {
			VersionAndroidEntity androidEntity = entity.getAndroid();
			if (androidEntity != null) {
				androidEntity.setReleaseState(ReleaseStateEnums.WAIT_LINE.getIndex());
				androidEntity.setVersionId(entity.getId());
				String versionStr = this.formatList(androidEntity.getVersion());
				androidEntity.setAndroidVersion(versionStr);
				GenericityUtil.setDate(androidEntity);
				versionAndroidDao.insert(androidEntity);
				// 更新类型信息添加
				VersionTypeEntity typeEntity = new VersionTypeEntity();
				typeEntity.setVersionId(entity.getId());
				typeEntity.setVersionTypeId(VersionTypeEnums.ANDROID.getIndex());
				typeEntity.setVersionTypeName(VersionTypeEnums.ANDROID.getName());
				typeEntity.setVersionCode(versionStr);
				typeEntity.setViceId(androidEntity.getId());
				GenericityUtil.setDate(typeEntity);
				versionTypeDao.insert(typeEntity);
				// 安卓Google Play版本信息
				VersionAndroidGoogleEntity androidGoogleEntity = androidEntity.getGoogle();
				if (androidGoogleEntity != null) {
					androidGoogleEntity.setAndroidVersionId(androidEntity.getId());
					androidGoogleEntity.setReleaseState(ReleaseStateEnums.WAIT_LINE.getIndex());
					String GoogleVersionStr = this.formatList(androidGoogleEntity.getVersion());
					androidGoogleEntity.setGoogleVersion(GoogleVersionStr);
					GenericityUtil.setDate(androidGoogleEntity);
					androidGoogleDao.insert(androidGoogleEntity);
					// 更新类型信息添加
					VersionTypeEntity type = new VersionTypeEntity();
					type.setVersionId(entity.getId());
					type.setVersionTypeId(VersionTypeEnums.GOOGLE.getIndex());
					type.setVersionTypeName(VersionTypeEnums.GOOGLE.getName());
					type.setVersionCode(versionStr);
					type.setViceId(androidGoogleEntity.getId());
					GenericityUtil.setDate(type);
					versionTypeDao.insert(type);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	// 设备版本信息新增
	public void addDigtalshie(VersionEntity entity) {
		try {
			VersionDigtalshieEntity digtalshieEntity = entity.getDigtalshield();
			if (digtalshieEntity != null) {
				digtalshieEntity.setVersionId(entity.getId());
				digtalshieEntity.setReleaseState(ReleaseStateEnums.WAIT_LINE.getIndex());
				GenericityUtil.setDate(digtalshieEntity);
				digtalshieDao.insert(digtalshieEntity);
				// 设备版本信息
				List<VersionDigtalshieFirmwareEntity> digtalshieFirmwareEntity = digtalshieEntity.getFirmware();
				if (digtalshieFirmwareEntity != null && digtalshieFirmwareEntity.size() > 0) {
					for (int i = 0; i < digtalshieFirmwareEntity.size(); i++) {
						digtalshieFirmwareEntity.get(i).setDigtalshieId(digtalshieEntity.getId());
						digtalshieFirmwareEntity.get(i).setReleaseState(ReleaseStateEnums.WAIT_LINE.getIndex());
						digtalshieFirmwareEntity.get(i)
								.setReleaseChangelog(digtalshieFirmwareEntity.get(i).getChangelog().toJSONString());
						String versionStr = this.formatList(digtalshieFirmwareEntity.get(i).getVersion());
						digtalshieFirmwareEntity.get(i).setFirmwareVersion(versionStr);
						GenericityUtil.setDate(digtalshieFirmwareEntity.get(i));
						digtalshieFirmwareDao.insert(digtalshieFirmwareEntity.get(i));
						// 更新类型信息添加
						VersionTypeEntity typeEntity = new VersionTypeEntity();
						typeEntity.setVersionId(entity.getId());
						typeEntity.setVersionTypeId(VersionTypeEnums.DIGTALSHIELD.getIndex());
						typeEntity.setVersionTypeName(VersionTypeEnums.DIGTALSHIELD.getName());
						typeEntity.setVersionCode(versionStr);
						typeEntity.setViceId(digtalshieFirmwareEntity.get(i).getId());
						GenericityUtil.setDate(typeEntity);
						versionTypeDao.insert(typeEntity);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	// 格式化list类型版本号返回字符串
	public String formatList(List<Integer> list) {
		if (list != null && list.size() > 0) {
			StringBuffer buffer = new StringBuffer();
			for (int i = 0; i < list.size(); i++) {
				buffer.append(list.get(i)).append(",");
			}
			return buffer.toString().substring(0, buffer.length() - 1);
		} else {
			return null;
		}
	}

	@Override
	@SysLogAnnotation(module = "应用及硬件版本管理", type = "POST", remark = "软件版本列表")
	public ResponseBase findList(@RequestBody VersionEntity entity) {
		PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
		List<VersionEntity> list = versionDao.findList(entity);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				// 更新版本集合
				List<VersionTypeEntity> versionTypeList = versionTypeDao.findVersionId(list.get(i).getId());
				if (versionTypeList != null && versionTypeList.size() > 0) {
					list.get(i).setVersionTypeList(versionTypeList);
				}
				// 更新ios内容
				VersionIosEntity ios = versionIosDao.findVersionId(list.get(i).getId());
				if (ios != null) {
					list.get(i).setIos(ios);
				}
				// 更新安卓内容
				VersionAndroidEntity androidEntity = versionAndroidDao.findVersionId(list.get(i).getId());
				if (androidEntity != null) {
					VersionAndroidGoogleEntity androidGoogleEntity = androidGoogleDao
							.findAndroid(androidEntity.getId());
					if (androidGoogleEntity != null) {
						androidEntity.setGoogle(androidGoogleEntity);
					}
					list.get(i).setAndroid(androidEntity);
				}
				// 设备版本信息
				VersionDigtalshieEntity digtalshieEntity = digtalshieDao.findVersionId(list.get(i).getId());
				if (digtalshieEntity != null) {
					List<VersionDigtalshieFirmwareEntity> firmware = digtalshieFirmwareDao
							.findDigtalshieId(digtalshieEntity.getId());
					if (firmware != null && firmware.size() > 0) {
						digtalshieEntity.setFirmware(firmware);
					}
					list.get(i).setDigtalshield(digtalshieEntity);
				}
			}
		}
		PageInfo<VersionEntity> info = new PageInfo<>(list);
		return setResultSuccess(info);
	}

	@Override
	@SysLogAnnotation(module = "应用及硬件版本管理", type = "POST", remark = "下线")
	public ResponseBase Offline(Integer id) {
		try {
			VersionEntity entity = versionDao.selectById(id);
			if (entity != null) {
				// 查询现有上线版本数量
				int temp = versionDao.findOnlineNum(ReleaseStateEnums.TOP_LINE.getIndex());
				if (temp > Constants.FIND_ONE) {
					this.OfflineOneMethod(entity);
					return setResultSuccess();
				} else {
					return setResultError("无法操作下线，需保证有一上线版本");
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
	@SysLogAnnotation(module = "应用及硬件版本管理", type = "POST", remark = "软件上线接口")
	public ResponseBase onlineSoftware(Integer id) {
		try {
			VersionEntity entity = versionDao.selectById(id);
			JSONObject jsonObject = new JSONObject();
			if (entity != null) {
				// 更新ios内容
				VersionIosEntity ios = versionIosDao.findVersionId(entity.getId());
				if (ios != null) {
//					ios.setVersion(this.strToList(ios.getIosVersion()));
//					entity.setIos(ios);
					JSONObject iosJson = new JSONObject();
					iosJson.put("url", ios.getUrl());
					iosJson.put("version", this.strToList(ios.getIosVersion()));
					jsonObject.put("ios", iosJson);
				}
				// 更新安卓内容
				VersionAndroidEntity androidEntity = versionAndroidDao.findVersionId(entity.getId());
				if (androidEntity != null) {
					JSONObject andeoidJson = new JSONObject();
					VersionAndroidGoogleEntity androidGoogleEntity = androidGoogleDao
							.findAndroid(androidEntity.getId());
					if (androidGoogleEntity != null) {
//						androidGoogleEntity.setVersion(this.strToList(androidGoogleEntity.getGoogleVersion()));
//						androidEntity.setGoogle(androidGoogleEntity);
						JSONObject googleJson = new JSONObject();
						googleJson.put("url", androidGoogleEntity.getUrl());
						googleJson.put("version", this.strToList(androidGoogleEntity.getGoogleVersion()));
						andeoidJson.put("google", googleJson);
					}
//					androidEntity.setVersion(this.strToList(androidEntity.getAndroidVersion()));
//					entity.setAndroid(androidEntity);
					andeoidJson.put("googlePlay", androidEntity.getGooglePlay());
					andeoidJson.put("url", androidEntity.getUrl());
					andeoidJson.put("version", this.strToList(androidEntity.getAndroidVersion()));
					jsonObject.put("android", andeoidJson);
				}
				//硬件信息
				VersionDigtalshieEntity digtalshieEntity = versionDigtalshieDao.findVersionId(entity.getId());
				if(digtalshieEntity != null) {
					JSONObject digtalshieJson = new JSONObject();
					List<VersionDigtalshieFirmwareEntity> list = 
							VersionDigtalshieFirmwareDao.findVersionId(digtalshieEntity.getId());
					List<JSONObject> listJson = new ArrayList<>();
					if(list != null && list.size() > 0) {
						for (int i = 0; i < list.size(); i++) {
//							list.get(i).setVersion(this.strToList(list.get(i).getFirmwareVersion()));
//							list.get(i).setChangelog(jsonObject);
							JSONObject releaseChangelog = JSONObject.parseObject(list.get(i).getReleaseChangelog());
							JSONObject firmwareJson = new JSONObject();
							if(list.get(i).getReleaseState() == 0) {
								firmwareJson.put("required", false);
							}else {
								firmwareJson.put("required", true);
							}
							firmwareJson.put("version", this.strToList(list.get(i).getFirmwareVersion()));
							firmwareJson.put("url", list.get(i).getUrl());
							firmwareJson.put("fingerprint", list.get(i).getFingerprint());
							firmwareJson.put("changelog", releaseChangelog);
							listJson.add(firmwareJson);
						}
//						digtalshieEntity.setFirmware(list);
						digtalshieJson.put("firmware", listJson);
					}
//					entity.setDigtalshield(digtalshieEntity);
					jsonObject.put("digtalshield", digtalshieJson);
				}
				String jsonParam = JSON.toJSONString(jsonObject);
				String res = HttpUtil.sendPostRequest(HTTP_URL+HttpAddressEunms.ONLINE.getName(), jsonParam);
				RpcResponseEntity rpcResponse = JSONObject.parseObject(res, RpcResponseEntity.class);
				if(rpcResponse.getSuccess() != null && rpcResponse.getSuccess()) {
					// 下线所有版本
					this.OfflineAllMethod();
					// 上线指定版本
					this.onlineOneMethod(entity);
					return setResultSuccess();
				}else {
					return setResultError(Constants.ERROR);
				}
//				// 下线所有版本
//				this.OfflineAllMethod();
//				// 上线指定版本
//				this.onlineOneMethod(entity);
//				return setResultSuccess();
			} else {
				return setResultError(Constants.ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	// 下线指定版本公共方法
	public void OfflineOneMethod(VersionEntity entity) {
		// 主表状态改为下线
		entity.setReleaseState(ReleaseStateEnums.DOWN_LINE.getIndex());
		versionDao.updateById(entity);
		// ios状态改为下线
		VersionIosEntity iosEntity = versionIosDao.findVersionId(entity.getId());
		if (iosEntity != null) {
			iosEntity.setReleaseState(ReleaseStateEnums.DOWN_LINE.getIndex());
			versionIosDao.updateById(iosEntity);
		}
		// 安卓状态改为下线
		VersionAndroidEntity androidEntity = versionAndroidDao.findVersionId(entity.getId());
		if (androidEntity != null) {
			androidEntity.setReleaseState(ReleaseStateEnums.DOWN_LINE.getIndex());
			versionAndroidDao.updateById(androidEntity);
			// 谷歌改为下线
			VersionAndroidGoogleEntity googleEntity = androidGoogleDao.findAndroid(androidEntity.getId());
			if (googleEntity != null) {
				googleEntity.setReleaseState(ReleaseStateEnums.DOWN_LINE.getIndex());
				androidGoogleDao.updateById(googleEntity);
			}
		}
		// 设备状态改为下线
		VersionDigtalshieEntity digtalshieEntity = digtalshieDao.findVersionId(entity.getId());
		if (digtalshieEntity != null) {
			digtalshieEntity.setReleaseState(ReleaseStateEnums.DOWN_LINE.getIndex());
			digtalshieDao.updateById(digtalshieEntity);
			List<VersionDigtalshieFirmwareEntity> list = digtalshieFirmwareDao
					.findDigtalshieId(digtalshieEntity.getId());
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					list.get(i).setReleaseState(ReleaseStateEnums.DOWN_LINE.getIndex());
					digtalshieFirmwareDao.updateById(list.get(i));
				}
			}
		}
	}

	// 下线所有版本公共方法
	public void OfflineAllMethod() {
		// 主表状态改为下线
		versionDao.updateState(ReleaseStateEnums.DOWN_LINE.getIndex());
		// ios状态改为下线
		versionIosDao.updateState(ReleaseStateEnums.DOWN_LINE.getIndex());
		// 安卓状态改为下线
		versionAndroidDao.updateState(ReleaseStateEnums.DOWN_LINE.getIndex());
		// 谷歌改为下线
		androidGoogleDao.updateState(ReleaseStateEnums.DOWN_LINE.getIndex());
	}

	// 上线指定版本公共方法
	public void onlineOneMethod(VersionEntity entity) {
		// 主表状态改为上线
		entity.setReleaseState(ReleaseStateEnums.TOP_LINE.getIndex());
		versionDao.updateById(entity);
		// ios状态改为上线
		VersionIosEntity iosEntity = versionIosDao.findVersionId(entity.getId());
		if (iosEntity != null) {
			iosEntity.setReleaseState(ReleaseStateEnums.TOP_LINE.getIndex());
			versionIosDao.updateById(iosEntity);
		}
		// 安卓状态改为上线
		VersionAndroidEntity androidEntity = versionAndroidDao.findVersionId(entity.getId());
		if (androidEntity != null) {
			androidEntity.setReleaseState(ReleaseStateEnums.TOP_LINE.getIndex());
			versionAndroidDao.updateById(androidEntity);
			// 谷歌改为上线
			VersionAndroidGoogleEntity googleEntity = androidGoogleDao.findAndroid(androidEntity.getId());
			if (googleEntity != null) {
				googleEntity.setReleaseState(ReleaseStateEnums.TOP_LINE.getIndex());
				androidGoogleDao.updateById(googleEntity);
			}
		}
	}
	
	//将字符串转换为list
	public List<Integer> strToList (String str) {
		List<Integer> versionList = Arrays.stream(str.split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
		return versionList;
	}

	@Override
	@SysLogAnnotation(module = "应用及硬件版本管理", type = "POST", remark = "新版本提示")
	public ResponseBase msgWarn() {
		Object obj = redisUtil.get(Constants.MSG_KEY);
		if(obj != null) {
			return setResultSuccess(obj);
		}else {
			return setResultSuccess();
		}
	}

	@Override
	@SysLogAnnotation(module = "应用及硬件版本管理", type = "POST", remark = "硬件版本列表")
	public ResponseBase hardwareFindList(@RequestBody VersionEntity entity) {
		PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
		List<VersionDigtalshieFirmwareEntity> list = VersionDigtalshieFirmwareDao.findList(entity);
		if(list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				JSONObject object = JSONObject.parseObject(list.get(i).getReleaseChangelog());
				list.get(i).setExplainContent(object.get("zh-CN").toString());
			}
		}
		PageInfo<VersionDigtalshieFirmwareEntity> info = new PageInfo<>(list);
		return setResultSuccess(info);
	}

	@Override
	@SysLogAnnotation(module = "应用及硬件版本管理", type = "POST", remark = "硬件上线接口")
	public ResponseBase onlineHardware(Integer id) {
		try {
			VersionDigtalshieFirmwareEntity entity = VersionDigtalshieFirmwareDao.selectById(id);
			if(entity != null) {
				//获取上级
				VersionDigtalshieEntity digtalshieEntity = versionDigtalshieDao.selectById(entity.getDigtalshieId());
				if(digtalshieEntity != null) {
					JSONObject digtalshieJson = new JSONObject();
					List<JSONObject> firmwareList = new ArrayList<>();
					JSONObject firmwareJson = new JSONObject();
					JSONObject releaseChangelog = JSONObject.parseObject(entity.getReleaseChangelog());
					firmwareJson.put("changelog", releaseChangelog);
					firmwareJson.put("fingerprint", entity.getFingerprint());
					firmwareJson.put("url", entity.getUrl());
					firmwareJson.put("version", this.strToList(entity.getFirmwareVersion()));
					if(entity.getReleaseState() == 0) {
						firmwareJson.put("required", false);
					}else {
						firmwareJson.put("required", true);
					}
					firmwareList.add(firmwareJson);
					digtalshieJson.put("firmware", firmwareList);
//					entity.setVersion(this.strToList(entity.getFirmwareVersion()));
//					entity.setChangelog(jsonObject);
//					List<VersionDigtalshieFirmwareEntity> list = new ArrayList<>();
//					list.add(entity);
//					digtalshieEntity.setFirmware(list);
					
					VersionEntity versionEntity = versionDao.selectById(digtalshieEntity.getVersionId());
					JSONObject jsonObject = new JSONObject();
					if(versionEntity != null) {
						// 更新ios内容
						VersionIosEntity ios = versionIosDao.findVersionId(entity.getId());
						if (ios != null) {
//							ios.setVersion(this.strToList(ios.getIosVersion()));
//							versionEntity.setIos(ios);
							JSONObject iosJson = new JSONObject();
							iosJson.put("url", ios.getUrl());
							iosJson.put("version", this.strToList(ios.getIosVersion()));
							jsonObject.put("ios", iosJson);
						}
						// 更新安卓内容
						VersionAndroidEntity androidEntity = versionAndroidDao.findVersionId(entity.getId());
						if (androidEntity != null) {
							JSONObject androidJson = new JSONObject();
							VersionAndroidGoogleEntity androidGoogleEntity = androidGoogleDao
									.findAndroid(androidEntity.getId());
							if (androidGoogleEntity != null) {
								JSONObject gooleJson = new JSONObject();
//								androidGoogleEntity.setVersion(this.strToList(androidGoogleEntity.getGoogleVersion()));
//								androidEntity.setGoogle(androidGoogleEntity);
								gooleJson.put("url", androidGoogleEntity.getUrl());
								gooleJson.put("version", this.strToList(androidGoogleEntity.getGoogleVersion()));
								androidJson.put("google", gooleJson);
							}
//							androidEntity.setVersion(this.strToList(androidEntity.getAndroidVersion()));
//							versionEntity.setAndroid(androidEntity);
							androidJson.put("googlePlay", androidEntity.getGooglePlay());
							androidJson.put("url", androidEntity.getUrl());
							androidJson.put("version", this.strToList(androidEntity.getAndroidVersion()));
							jsonObject.put("android", androidJson);
						}
//						versionEntity.setDigtalshield(digtalshieEntity);
						jsonObject.put("digtalshield", digtalshieJson);
					}
					String jsonParam = JSON.toJSONString(jsonObject);
					String res = HttpUtil.sendPostRequest(HTTP_URL+HttpAddressEunms.ONLINE.getName(), jsonParam);
					RpcResponseEntity rpcResponse = JSONObject.parseObject(res, RpcResponseEntity.class);
					if(rpcResponse.getSuccess() != null && rpcResponse.getSuccess()) {
//					下线所有版本
					VersionDigtalshieFirmwareDao.updateStateAll(ReleaseStateEnums.DOWN_LINE.getIndex());
					versionDigtalshieDao.updateStateAll(ReleaseStateEnums.DOWN_LINE.getIndex());
					//上线指定版本
					entity.setReleaseState(ReleaseStateEnums.TOP_LINE.getIndex());
					VersionDigtalshieFirmwareDao.updateById(entity);
					digtalshieEntity.setReleaseState(ReleaseStateEnums.TOP_LINE.getIndex());
					versionDigtalshieDao.updateById(digtalshieEntity);
					return setResultSuccess();
					}else {
						return setResultError(Constants.ERROR);
					}
					//下线所有版本
//					VersionDigtalshieFirmwareDao.updateStateAll(ReleaseStateEnums.DOWN_LINE.getIndex());
//					versionDigtalshieDao.updateStateAll(ReleaseStateEnums.DOWN_LINE.getIndex());
//					//上线指定版本
//					entity.setReleaseState(ReleaseStateEnums.TOP_LINE.getIndex());
//					VersionDigtalshieFirmwareDao.updateById(entity);
//					digtalshieEntity.setReleaseState(ReleaseStateEnums.TOP_LINE.getIndex());
//					versionDigtalshieDao.updateById(digtalshieEntity);
//					return setResultSuccess();
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
	public ResponseBase deleteMsg() {
		try {
			redisUtil.del(Constants.MSG_KEY);
			return setResultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	



}
