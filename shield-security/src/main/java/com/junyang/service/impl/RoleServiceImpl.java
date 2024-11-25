package com.junyang.service.impl;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.junyang.aop.SysLogAnnotation;
import com.junyang.base.BaseApiService;
import com.junyang.base.ResponseBase;
import com.junyang.constants.Constants;
import com.junyang.dao.system.SysRoleDao;
import com.junyang.dao.system.SysRoleMenuDao;
import com.junyang.entity.system.SysRoleEntity;
import com.junyang.entity.system.SysRoleMenuEntity;
import com.junyang.enums.RoleStateEnums;
import com.junyang.enums.RoleTypeEnums;
import com.junyang.service.RoleService;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


@RestController
@CrossOrigin
@Transactional
public class RoleServiceImpl extends BaseApiService implements RoleService{
	
	@Autowired
	private SysRoleDao sysRoleDao;
	
	@Autowired
	private SysRoleMenuDao sysRoleMenuDao;
	

	@Override
	@SysLogAnnotation(module = "角色接口", type = "POST", remark = "查询所有角色")
	public ResponseBase findAll(@RequestBody SysRoleEntity entity) {
		try {
			entity.setDelFlag(RoleStateEnums.ROLE_DEL_FLAG_NORMAL.getIndex());
			PageHelper.startPage(entity.getPageNum(), entity.getPageSize());
			List<SysRoleEntity> list = sysRoleDao.findList(entity);
			PageInfo<SysRoleEntity> info = new PageInfo<>(list);
			return setResultSuccess(info);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "角色接口", type = "GET", remark = "查询角色")
	public ResponseBase findById(Integer roleId) {
		SysRoleEntity entity = sysRoleDao.selectById(roleId);
		return setResultSuccess(entity);
	}

	@Override
	@SysLogAnnotation(module = "角色接口", type = "POST", remark = "新增角色")
	public ResponseBase add(@RequestBody SysRoleEntity entity) {
		try {
			SysRoleEntity roleEntity = sysRoleDao.findName(entity.getRoleName(),RoleStateEnums.ROLE_DEL_FLAG_NORMAL.getIndex());
			if(roleEntity != null) {
				return setResultError("角色名已存在");
			}else {
				entity.setCreateTime(new Date());
				sysRoleDao.insert(entity);
				if(entity.getMenuIds() != null && entity.getMenuIds().length > 0) {
					for (int i = 0; i < entity.getMenuIds().length; i++) {
						SysRoleMenuEntity roleMenuEntity = new SysRoleMenuEntity();
						roleMenuEntity.setCreateTime(new Date());
						roleMenuEntity.setRoleId(entity.getRoleId());
						roleMenuEntity.setMenuId(entity.getMenuIds()[i]);
						sysRoleMenuDao.insert(roleMenuEntity);
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
	@SysLogAnnotation(module = "角色接口", type = "POST", remark = "编辑角色信息")
	public ResponseBase update(@RequestBody SysRoleEntity entity) {
		try {
			SysRoleEntity roleEntity = sysRoleDao.selectById(entity.getRoleId());
			if(roleEntity != null) {
				SysRoleEntity role = sysRoleDao.findRoleIdName(entity.getRoleId(),entity.getRoleName(),RoleStateEnums.ROLE_DEL_FLAG_NORMAL.getIndex());
				if(role != null) {
					return setResultError("角色名已存在");
				}
				entity.setUpdateTime(new Date());
				int temp = sysRoleDao.updateById(entity);
				if(temp > 0) {
					if(entity.getMenuIds() != null && entity.getMenuIds().length > 0) {
						sysRoleMenuDao.deleteRoleId(entity.getRoleId());
						for (int i = 0; i <entity.getMenuIds().length; i++) {
							SysRoleMenuEntity roleMenuEntity = new SysRoleMenuEntity();
							roleMenuEntity.setCreateTime(new Date());
							roleMenuEntity.setRoleId(entity.getRoleId());
							roleMenuEntity.setMenuId(entity.getMenuIds()[i]);
							sysRoleMenuDao.insert(roleMenuEntity);
						}
					}
				}
				return setResultSuccess();
			}else {
				return setResultError(Constants.NULL_CONTETN);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "角色接口", type = "GET", remark = "删除角色")
	public ResponseBase delete(Integer roleId) {
		try {
			SysRoleEntity entity = sysRoleDao.selectById(roleId);
			if(entity != null) {
				entity.setDelFlag(RoleStateEnums.ROLE_DEL_FLAG_DELETE.getIndex());
				sysRoleDao.updateById(entity);
				return setResultSuccess();
			}else {
				return setResultError(Constants.NULL_CONTETN);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "角色接口", type = "POST", remark = "角色停用启用")
	public ResponseBase changeStatus(Integer roleId, String status) {
		try {
			SysRoleEntity entity = sysRoleDao.selectById(roleId);
			if(entity != null) {
				entity.setStatus(status);
				sysRoleDao.updateById(entity);
				return setResultSuccess();
			}else {
				return setResultError(Constants.NULL_CONTETN);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	@Override
	@SysLogAnnotation(module = "角色接口", type = "GET", remark = "查询正常状态下角色信息")
	public ResponseBase findNormalRole() {
		List<SysRoleEntity> list = sysRoleDao.findNormalRole(RoleStateEnums.ROLE_DEL_FLAG_NORMAL.getIndex(),
				RoleStateEnums.ROLE_STATUS_NORMAL.getIndex(),RoleTypeEnums.ADMIN.getIndex());
		return setResultSuccess(JSON.toJSON(list));
	}

}
