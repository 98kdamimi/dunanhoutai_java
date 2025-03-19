package com.junyang.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
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
import com.github.pagehelper.PageInfo;
import com.junyang.aop.SysLogAnnotation;
import com.junyang.base.BaseApiService;
import com.junyang.base.ResponseBase;
import com.junyang.constants.Constants;
import com.junyang.entity.release.HardwareAddQueryEntity;
import com.junyang.entity.release.RekeaseAddQueryEntity;
import com.junyang.entity.response.RpcResponseEntity;
import com.junyang.entity.version.HardwareEntity;
import com.junyang.entity.version.HardwareEntity.Bootloader;
import com.junyang.entity.version.HardwareEntity.Firmware;
import com.junyang.entity.version.ReleaseEntity;
import com.junyang.entity.version.SoftwareEntity;
import com.junyang.entity.version.SoftwareEntity.Android;
import com.junyang.entity.version.SoftwareEntity.Google;
import com.junyang.entity.version.SoftwareEntity.IOS;
import com.junyang.enums.FilePathEnums;
import com.junyang.enums.ForceUpdateEnums;
import com.junyang.enums.HttpAddressEunms;
import com.junyang.enums.ReleaseStateEnums;
import com.junyang.query.PublicQueryEntity;
import com.junyang.service.ReleaseService;
import com.junyang.utils.GenericityUtil;
import com.junyang.utils.HttpUtil;
import com.junyang.utils.RedisUtil;
import org.springframework.data.mongodb.core.query.Update;

@RestController
@Transactional
@CrossOrigin
public class ReleaseServiceImpl extends BaseApiService implements ReleaseService {

	@Value("${http_url}")
	private String HTTP_URL;

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private UploadServiceImpl uploadServiceImpl;

	@Autowired
	private RedisUtil redisUtil;

	@Override
	@SysLogAnnotation(module = "版本管理管理", type = "POST", remark = "版本发布")
	public ResponseBase add(@RequestBody ReleaseEntity entity) {
		try {
			entity.getHardware().setOnlineState(ReleaseStateEnums.WAIT_LINE.getIndex());
			entity.getSoftware().setOnlineState(ReleaseStateEnums.WAIT_LINE.getIndex());
			GenericityUtil.setTokenDateStr(entity);
			mongoTemplate.insert(entity);
			// 更新通知
			redisUtil.set(Constants.MSG_KEY, JSON.toJSON(entity));
			return setResultSuccess();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "版本管理管理", type = "POST", remark = "软件版本列表查询")
	public ResponseBase softwareList(@RequestBody PublicQueryEntity entity) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("software").exists(true));
			if (entity.getStatus() != null && entity.getStatus().length() > 0) {
				query.addCriteria(Criteria.where("software.onlineState").is(Integer.parseInt(entity.getStatus())));
			}
			long totalCount = mongoTemplate.count(query, ReleaseEntity.class);// 总条数
			// 构建分页请求对象
			int pageNumber = Math.max(entity.getPageNumber() - 1, 0);
			PageRequest pageRequest = PageRequest.of(pageNumber, entity.getPageSize(),
					Sort.by(Sort.Direction.DESC, "createdAt"));
			query.with(pageRequest);
			List<ReleaseEntity> list = mongoTemplate.find(query, ReleaseEntity.class);
			// 获取总记录数
			PageInfo<ReleaseEntity> info = new PageInfo<>(list);
			info.setTotal(totalCount);
			return setResultSuccess(info);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "版本管理管理", type = "GET", remark = "软件版本上线")
	public ResponseBase onlineSoftware(String id, Integer forceUpdateLable) {
		try {
			ReleaseEntity entity = mongoTemplate.findById(id, ReleaseEntity.class);
			if (entity == null) {
				return setResultError(Constants.ERROR);
			}
			if (ForceUpdateEnums.ENFORCEMENT.getIndex().equals(forceUpdateLable)) {// 强制更新
				entity.getSoftware().getAndroid().setForceUpdateVersion(entity.getSoftware().getAndroid().getVersion());
				entity.getSoftware().getIos().setForceUpdateVersion(entity.getSoftware().getIos().getVersion());
				entity.setForceUpdateLable(ForceUpdateEnums.ENFORCEMENT.getIndex());
			}
//			ReleaseEntity releaseEntity = new ReleaseEntity();
//			BeanUtils.copyProperties(entity, releaseEntity);
//			releaseEntity.getSoftware().setOnlineState(null);
//			releaseEntity.getHardware().setOnlineState(null);
			String jsonParam = JSON.toJSONString(entity);
			String res = HttpUtil.sendPostRequest(HTTP_URL + HttpAddressEunms.ONLINE.getName(), jsonParam);
			RpcResponseEntity rpcResponse = JSONObject.parseObject(res, RpcResponseEntity.class);
			if (rpcResponse.getSuccess() != null && rpcResponse.getSuccess()) {
				entity.getSoftware().setOnlineState(ReleaseStateEnums.TOP_LINE.getIndex());
				// 创建查询条件，表示所有文档
				Query query = new Query();
				query.addCriteria(Criteria.where("software").exists(true));
				// 创建更新操作
				Update update = new Update();
				update.set("software.onlineState", ReleaseStateEnums.DOWN_LINE.getIndex());
				// 执行更新操作
				mongoTemplate.updateMulti(query, update, ReleaseEntity.class);

				// 执行更新操作
				mongoTemplate.save(entity);
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
	@SysLogAnnotation(module = "版本管理管理", type = "POST", remark = "硬件版本列表查询")
	public ResponseBase hardwareList(@RequestBody PublicQueryEntity entity) {
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("hardware").exists(true));
			if (entity.getStatus() != null && entity.getStatus().length() > 0) {
				query.addCriteria(Criteria.where("hardware.onlineState").is(Integer.parseInt(entity.getStatus())));
			}
			long totalCount = mongoTemplate.count(query, ReleaseEntity.class);// 总条数
			// 构建分页请求对象
			int pageNumber = Math.max(entity.getPageNumber() - 1, 0);
			PageRequest pageRequest = PageRequest.of(pageNumber, entity.getPageSize(),
					Sort.by(Sort.Direction.DESC, "createdAt"));
			query.with(pageRequest);
			List<ReleaseEntity> list = mongoTemplate.find(query, ReleaseEntity.class);
			// 获取总记录数
			PageInfo<ReleaseEntity> info = new PageInfo<>(list);
			info.setTotal(totalCount);
			return setResultSuccess(info);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "版本管理管理", type = "GET", remark = "硬件版本上线")
	public ResponseBase onlineHardware(String id) {
		try {
			ReleaseEntity entity = mongoTemplate.findById(id, ReleaseEntity.class);
			if (entity == null) {
				return setResultError(Constants.ERROR);
			}
//			ReleaseEntity releaseEntity = new ReleaseEntity();
//			BeanUtils.copyProperties(entity, releaseEntity);
//			releaseEntity.getSoftware().setOnlineState(null);
//			releaseEntity.getHardware().setOnlineState(null);
			String jsonParam = JSON.toJSONString(entity);
			String res = HttpUtil.sendPostRequest(HTTP_URL + HttpAddressEunms.ONLINE.getName(), jsonParam);
			RpcResponseEntity rpcResponse = JSONObject.parseObject(res, RpcResponseEntity.class);
			if (rpcResponse.getSuccess() != null && rpcResponse.getSuccess()) {
				// 创建查询条件，表示所有文档
				Query query = new Query();
				query.addCriteria(Criteria.where("hardware").exists(true));
				// 创建更新操作，将 status 字段更新为 newStatus
				Update update = new Update();
				update.set("hardware.onlineState", ReleaseStateEnums.DOWN_LINE.getIndex());
				// 执行更新操作
				mongoTemplate.updateMulti(query, update, ReleaseEntity.class);

				// 执行更新操作
				entity.getHardware().setOnlineState(ReleaseStateEnums.TOP_LINE.getIndex());
				mongoTemplate.save(entity);
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
	@SysLogAnnotation(module = "应用及硬件版本管理", type = "POST", remark = "新版本提示")
	public ResponseBase msgWarn() {
		Object obj = redisUtil.get(Constants.MSG_KEY);
		if (obj != null) {
			return setResultSuccess(obj);
		} else {
			return setResultSuccess();
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

	@Override
	@SysLogAnnotation(module = "版本管理管理", type = "POST", remark = "软件版本发布")
	public ResponseBase sysAdd(String dataStr, MultipartFile file) {
		try {
			if(dataStr.isEmpty()) {
				return setResultError(Constants.ERROR);
			}
			RekeaseAddQueryEntity entity = JSONObject.parseObject(dataStr, RekeaseAddQueryEntity.class);
			if (file != null) {
				ResponseBase base = uploadServiceImpl.fileUpload(file, FilePathEnums.APK.getIndex(), null);
				if (Constants.HTTP_RES_CODE_200 == base.getRtncode() && base.getData() != null) {
					entity.setAndroidUrl(base.getData().toString());
					// 格式化发布数据
					SoftwareEntity softwareEntity = new SoftwareEntity();
					softwareEntity.setOnlineState(ReleaseStateEnums.WAIT_LINE.getIndex());
					// 谷歌信息
					Google google = new Google();
					google.setUrl(entity.getGooglePlayUrl());
					google.setVersion(strToList(entity.getAndroidVersion()));
					// 安卓信息
					Android android = new Android();
					android.setUrl(base.getData().toString());
					android.setVersion(strToList(entity.getAndroidVersion()));
					android.setGooglePlay(entity.getGooglePlayUrl());
					android.setGoogle(google);
					softwareEntity.setAndroid(android);
					// ios信息
					IOS ios = new IOS();
					ios.setUrl(entity.getIosUrl());
					ios.setVersion(strToList(entity.getIosVersion()));
					softwareEntity.setIos(ios);

					ReleaseEntity releaseEntity = new ReleaseEntity();
					releaseEntity.setSoftware(softwareEntity);
					GenericityUtil.setTokenDateStr(releaseEntity);
					mongoTemplate.insert(releaseEntity);
					return setResultSuccess();
				} else {
					return setResultError("apk上传失败，请稍后再试");
				}
			} else {
				return setResultError("请上传androidAPK");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}


	@Override
	@SysLogAnnotation(module = "版本管理管理", type = "POST", remark = "硬件版本发布")
	public ResponseBase sysHardwareAdd(String dataStr,MultipartFile bootloaderFile,MultipartFile firmwareFile) {
		try {
			if(dataStr != null && dataStr.length() > 0) {
				HardwareAddQueryEntity entity = JSONObject.parseObject(dataStr,HardwareAddQueryEntity.class);
				if(bootloaderFile != null) {
					ResponseBase base = uploadServiceImpl.fileUpload(bootloaderFile, FilePathEnums.APK.getIndex(), null);
					if (Constants.HTTP_RES_CODE_200 == base.getRtncode() && base.getData() != null) {
						entity.setBootloaderUrl(base.getData().toString());
					}
				}
				if(firmwareFile != null) {
					ResponseBase base = uploadServiceImpl.fileUpload(firmwareFile, FilePathEnums.APK.getIndex(), null);
					if (Constants.HTTP_RES_CODE_200 == base.getRtncode() && base.getData() != null) {
						entity.setFirmwareUrl(base.getData().toString());
					}
				}
				//格式化数据
				HardwareEntity hardwareEntity = new HardwareEntity();
				hardwareEntity.setOnlineState(ReleaseStateEnums.WAIT_LINE.getIndex());
				//引导程序
				Bootloader bootloader = new Bootloader();
				bootloader.setRequired(false);
				bootloader.setFingerprint("");
				bootloader.setVersion(strToList(entity.getHardwareVersion()));
				bootloader.setUrl(entity.getBootloaderUrl());
				
				List<Bootloader> bootloaderList = new ArrayList<>();
				bootloaderList.add(bootloader);
				hardwareEntity.setBootloaders(bootloaderList);
				//固件信息
				Firmware firmware = new Firmware();
				firmware.setFingerprint("");
				firmware.setRequired(false);
				firmware.setUrl(entity.getFirmwareUrl());
				firmware.setVersion(strToList(entity.getHardwareVersion()));
				List<Firmware> firmwareList = new ArrayList<>();
				firmwareList.add(firmware);
				hardwareEntity.setFirmwares(firmwareList);
				//封装外层
				ReleaseEntity releaseEntity = new ReleaseEntity();
				releaseEntity.setHardware(hardwareEntity);
				GenericityUtil.setTokenDateStr(releaseEntity);
				mongoTemplate.insert(releaseEntity);
				return setResultSuccess();
			}else {
				return setResultError(Constants.ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	public List<Integer> strToList(String str) {
		List<Integer> versionList = Arrays.stream(str.split("\\.")) // 正确使用 "\\."
				.map(Integer::parseInt).collect(Collectors.toList());		
		return versionList;
	}



}
