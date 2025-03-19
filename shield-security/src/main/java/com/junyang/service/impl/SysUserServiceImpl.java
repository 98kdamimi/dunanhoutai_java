package com.junyang.service.impl;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import com.junyang.entity.system.SysUserEntity;
import com.junyang.entity.system.SysUserPowerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.junyang.aop.SysLogAnnotation;
import com.junyang.base.BaseApiService;
import com.junyang.base.FileResponse;
import com.junyang.base.ResponseBase;
import com.junyang.constants.Constants;
import com.junyang.dao.system.SysUserDao;
import com.junyang.enums.ImageTypeEnums;
import com.junyang.enums.RoleTypeEnums;
import com.junyang.enums.UserStateEnums;
import com.junyang.filter.JWTAuthenticationFilter;
import com.junyang.service.SysUserService;
import com.junyang.utils.FileUploadUtil;
import com.junyang.utils.GoogleAuthenticatorUtil;
import com.junyang.utils.RedisUtil;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
@Transactional
@CrossOrigin
public class SysUserServiceImpl extends BaseApiService implements SysUserService {

	@Autowired
	private SysUserDao sysUserDao;
	
	@Autowired
	RedisUtil redisUtil;
	
	@Value("${filePath}")
	private String filePath;
	
	@Value("${DEFAULT_PASSWORD}")
	private String default_password;

	
	@Override
	@SysLogAnnotation(module = "用户管理", type = "POST", remark = "用户列表分页查询未注销用户")
	public ResponseBase findList(@RequestBody SysUserEntity entity) {
		PageHelper.startPage(entity.getPageNumber(), entity.getPageSize());
		if (entity.getRoleId() != null && entity.getRoleId().size() < 1) {
			entity.setRoleId(null);
		}
//		entity.setDeleteState(UserStateEnums.LOGOUT.getIndex());
//		entity.setAdminRole(RoleTypeEnums.ADMIN.getIndex());
		List<SysUserEntity> list = sysUserDao.findNotDeleteAll(entity);
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				list.get(i).setUserStateNmae(UserStateEnums.getValue(list.get(i).getUserState()));
				list.get(i).setRoleList(sysUserDao.findUserRole(list.get(i).getId()));
			}
		}
		PageInfo<SysUserEntity> pageInfo = new PageInfo<>(list);
		return setResultSuccess(pageInfo);
	}

	@Override
	@SysLogAnnotation(module = "用户管理", type = "POST", remark = "用户注册")
	public ResponseBase signUp(String user, MultipartFile file) {
		try {
			SysUserEntity entity = JSON.parseObject(user, SysUserEntity.class);
			List<SysUserEntity> userEntity = sysUserDao.findByAcctiveAndTel(entity.getAcctive(), entity.getTel());//,UserStateEnums.NORMAL.getIndex()
			if (userEntity != null && userEntity.size() > 0) {
				return setResultError("此账号已存在");
			} else {
				String password;
				entity.setUserState(UserStateEnums.NORMAL.getIndex());
				if (entity.getPassword() != null && entity.getPassword().length() > 0) {
					password = entity.getPassword();
					entity.setPassword(DigestUtils.md5DigestAsHex((entity.getPassword()).getBytes()));
				} else {
					password = default_password;
					entity.setPassword(DigestUtils.md5DigestAsHex((password).getBytes()));
				}
				System.out.println(entity.getGoogleSecretkey());
				if(entity.getGoogleSecretkey() == null || entity.getGoogleSecretkey().isEmpty()) {
					entity.setGoogleSecretkey(Constants.user_googleKey);
				}
				if (file != null) {
					FileResponse res = FileUploadUtil.uploadFile(file, filePath, ImageTypeEnums.TOUXIANG.getName());
					if (Constants.HTTP_RES_CODE_200.equals(res.getCode())) {
						entity.setHeadPic(res.getUrl());
					}
				}
				sysUserDao.insert(entity);
				if (entity.getRoleId() != null && entity.getRoleId().size() > 0) {
					for (int i = 0; i < entity.getRoleId().size(); i++) {
						sysUserDao.addUserOrRole(entity.getId(), entity.getRoleId().get(i));
					}
				}
				return setResultSuccess(JSON.toJSON(entity), Constants.SUCCESS);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
         
	


	@Override
	@SysLogAnnotation(module = "用户管理", type = "GET", remark = "通过token获取用户信息")
	public ResponseBase findToken(HttpServletRequest request) {
		String tokenStr = request.getHeader(Constants.HEADER_AUTH);
		UsernamePasswordAuthenticationToken token = JWTAuthenticationFilter.getAuthentication(request);
		String username = token.getName();
		Collection<GrantedAuthority> powerList = token.getAuthorities();
		SysUserEntity entity = sysUserDao.findByAcctiveState(username, UserStateEnums.NORMAL.getIndex());
		redisUtil.set(tokenStr, JSON.toJSONString(entity), Constants.REDIS_EXPIRE_TIME);
		entity.setRoleList(sysUserDao.findUserRole(entity.getId()));
		List<SysUserPowerEntity> newPowerList = new ArrayList<>();
		for (GrantedAuthority grantedAuthority : powerList) {
			SysUserPowerEntity powerEntity = new SysUserPowerEntity();
			powerEntity.setAuthority(grantedAuthority.getAuthority());
			newPowerList.add(powerEntity);
		}
		entity.setPower(newPowerList);
		boolean temp = false;
		if (entity.getRoleList() != null && entity.getRoleList().size() > 0) {
			for (int i = 0; i < entity.getRoleList().size(); i++) {
				if (RoleTypeEnums.ADMIN.getIndex().equals(entity.getRoleList().get(i).getRoleId())) {// 管理员
					temp = true;
				}
			}
		}
		if (temp) {
			List<String> roles = new ArrayList<String>();
			roles.add(Constants.ADMIN_STR);
			entity.setRoles(roles);
			List<String> perms = new ArrayList<String>();
			perms.add("*:*:*");
			entity.setPermissions(perms);
		} else {
			entity.setRoles(sysUserDao.findRoleKey(entity.getId()));
			entity.setPermissions(sysUserDao.findPermissions(entity.getId()));
		}
		
		entity.setLoginTime(new Date());
		sysUserDao.updateById(entity);
		return setResultSuccess(JSON.toJSON(entity));
	}

	
	@Override
	@SysLogAnnotation(module = "用户管理", type = "GET", remark = "删除用户信息")
	public ResponseBase delUser(@PathVariable("userId") Integer userId) {
		try {
			SysUserEntity entity = sysUserDao.findById(userId);
			if (entity != null) {
				sysUserDao.updateState(userId, UserStateEnums.LOGOUT.getIndex());
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
	@SysLogAnnotation(module = "用户管理", type = "POST", remark = "改变用户账号状态")
	public ResponseBase updateUserState(Integer userId, Integer newState) {
		try {
			SysUserEntity entity = sysUserDao.findById(userId);
			if (entity != null) {
				entity.setUserState(newState);
				sysUserDao.updateById(entity);
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
	@SysLogAnnotation(module = "用户管理", type = "POST", remark = "编辑用户信息")
	public ResponseBase updateUser(String user, MultipartFile file) {
		try {
			SysUserEntity entity = JSON.parseObject(user, SysUserEntity.class);
			SysUserEntity userEntity = sysUserDao.findById(entity.getId());
			if (userEntity != null) {
				List<SysUserEntity> userSysUserEntity = sysUserDao.findByNoAcctiveAndTel(entity.getAcctive(),
						entity.getTel(), entity.getId(), UserStateEnums.NORMAL.getIndex());
				if (userSysUserEntity != null && userSysUserEntity.size() > 0) {
					return setResultError("账号信息已存在");
				}
				if (file != null) {
					FileResponse res = FileUploadUtil.uploadFile(file, filePath, ImageTypeEnums.TOUXIANG.getName());
					if (Constants.HTTP_RES_CODE_200.equals(res.getCode())) {
						entity.setHeadPic(res.getUrl());
					}
				}
				sysUserDao.updateById(entity);
				if (entity.getRoleId() != null && entity.getRoleId().size() > 0) {
					sysUserDao.delteRole(userEntity.getId());
					for (int i = 0; i < entity.getRoleId().size(); i++) {
						sysUserDao.addUserOrRole(userEntity.getId(), entity.getRoleId().get(i));
					}
				}
				SysUserEntity result = sysUserDao.findById(entity.getId());
				return setResultSuccess(result);
			} else {
				return setResultError(Constants.ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}



	@Override
	@SysLogAnnotation(module = "用户管理", type = "POST", remark = "重置密码")
	public ResponseBase resetUserPwd(@RequestParam("userId") Integer userId) {
		try {
			SysUserEntity entity = sysUserDao.findById(userId);
			if (entity != null) {
				String pwd = DigestUtils.md5DigestAsHex((default_password).getBytes());
				sysUserDao.resetUserPwd(userId, pwd);
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
	@SysLogAnnotation(module = "用户管理", type = "POST", remark = "修改密码")
	public ResponseBase resetPwd(@RequestParam("userId") Integer userId, @RequestParam("password") String password) {
		try {
			SysUserEntity entity = sysUserDao.findById(userId);
			if (entity != null) {
				String pwd = DigestUtils.md5DigestAsHex((password).getBytes());
				sysUserDao.resetUserPwd(userId, pwd);
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
	@SysLogAnnotation(module = "用户管理", type = "POST", remark = "校验账号密码")
	public ResponseBase verifyPwd(Integer userId, String password) {
		try {
			SysUserEntity entity = sysUserDao.findById(userId);
			if (entity != null && entity.getPassword().equals(DigestUtils.md5DigestAsHex((password).getBytes()))) {
				return setResultSuccess(1, "校验通过");
			} else {
				return setResultError(0, "原密码不正确");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}



	@Override
	public ResponseBase updateDeviceModel(@RequestBody SysUserEntity entity) {
		if (entity.getId() != null) {
			SysUserEntity e = sysUserDao.findById(entity.getId());
			if (e != null) {
				sysUserDao.updateById(entity);
				return setResultSuccess();
			} else {
				return setResultError(Constants.NULL_CONTETN);
			}
		}else {
			return setResultError(Constants.NULL_PARAMS);
		}
	}
	
	@Override
	@SysLogAnnotation(module = "用户管理", type = "GET", remark = "发版Token查询")
	public ResponseBase generateToken() {
		try {
			String subject = "root";
			String token = Jwts.builder()
					.setSubject(subject)
					.setExpiration(new Date(System.currentTimeMillis() + Constants.REDIS_EXPIRE_TIME))
					.signWith(SignatureAlgorithm.HS512, Constants.SIGNING_KEY).compact();
			if (subject.indexOf(",") == -1) {
				redisUtil.set(Constants.APP_PACKAGE_NAME + subject, Constants.AUTH_HEADER_START_WITH + token);
			} else {
				redisUtil.set(Constants.APP_PACKAGE_NAME + subject.substring(0, subject.indexOf(",")), Constants.AUTH_HEADER_START_WITH + token);
			}
			List<Map<String, Object>> list =new ArrayList<>();
			Map<String, Object> map = new HashMap<>();
			map.put("token", Constants.AUTH_HEADER_START_WITH + token);
			list.add(map);
			return setResultSuccess(list, Constants.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase upGoogleSecretkey(Integer userId,String googleSecretkey) {
		try {
			SysUserEntity userEntity = sysUserDao.selectById(userId);
			if(userEntity != null) {
				userEntity.setGoogleSecretkey(googleSecretkey);
				sysUserDao.updateById(userEntity);
				return setResultSuccess();
			}
			return setResultError(Constants.ERROR);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	public ResponseBase IssueGoogleSecretkey(String userName) {
		try {
			String key = GoogleAuthenticatorUtil.createKey(userName).getKey();
			return setResultSuccess(key, Constants.SUCCESS);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	

	


}
