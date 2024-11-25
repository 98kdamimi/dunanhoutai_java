/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50717
Source Host           : localhost:3306
Source Database       : shield_security

Target Server Type    : MYSQL
Target Server Version : 50717
File Encoding         : 65001

Date: 2024-11-25 11:17:03
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) NOT NULL COMMENT '菜单名称',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父菜单ID',
  `order_num` int(4) DEFAULT '0' COMMENT '显示顺序',
  `path` varchar(200) DEFAULT '' COMMENT '路由地址',
  `component` varchar(255) DEFAULT NULL COMMENT '组件路径',
  `query` varchar(255) DEFAULT NULL COMMENT '路由参数',
  `is_frame` int(1) DEFAULT '1' COMMENT '是否为外链（0是 1否）',
  `is_cache` int(1) DEFAULT '0' COMMENT '是否缓存（0缓存 1不缓存）',
  `menu_type` char(1) DEFAULT '' COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` char(1) DEFAULT '0' COMMENT '菜单状态（0显示 1隐藏）',
  `status` char(1) DEFAULT '0' COMMENT '菜单状态（0正常 1停用）',
  `perms` varchar(100) DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) DEFAULT '#' COMMENT '菜单图标',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2298 DEFAULT CHARSET=utf8 COMMENT='菜单权限表';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('1', '系统管理', '0', '12', 'system', null, '', '1', '0', 'M', '0', '0', '', 'system', 'admin', '2023-05-26 17:37:31', '', null, '系统管理目录');
INSERT INTO `sys_menu` VALUES ('100', '用户管理', '1', '1', 'user', 'system/user/index', '', '1', '0', 'C', '0', '0', 'system:user:list', 'user', 'admin', '2023-05-26 17:37:31', '', null, '用户管理菜单');
INSERT INTO `sys_menu` VALUES ('101', '角色管理', '1', '2', 'role', 'system/role/index', '', '1', '0', 'C', '0', '0', 'system:role:list', 'peoples', 'admin', '2023-05-26 17:37:31', '', null, '角色管理菜单');
INSERT INTO `sys_menu` VALUES ('102', '菜单管理', '1', '3', 'menu', 'system/menu/index', '', '1', '0', 'C', '0', '0', 'system:menu:list', 'tree-table', 'admin', '2023-05-26 17:37:31', '', null, '菜单管理菜单');
INSERT INTO `sys_menu` VALUES ('1000', '用户查询', '100', '1', '', '', '', '1', '0', 'F', '0', '0', 'system:user:query', '#', 'admin', '2023-05-26 17:37:31', '', null, '');
INSERT INTO `sys_menu` VALUES ('1001', '用户新增', '100', '2', '', '', '', '1', '0', 'F', '0', '0', 'system:user:add', '#', 'admin', '2023-05-26 17:37:31', '', null, '');
INSERT INTO `sys_menu` VALUES ('1002', '用户修改', '100', '3', '', '', '', '1', '0', 'F', '0', '0', 'system:user:edit', '#', 'admin', '2023-05-26 17:37:31', '', null, '');
INSERT INTO `sys_menu` VALUES ('1003', '用户删除', '100', '4', '', '', '', '1', '0', 'F', '0', '0', 'system:user:remove', '#', 'admin', '2023-05-26 17:37:31', '', null, '');
INSERT INTO `sys_menu` VALUES ('1004', '用户导出', '100', '5', '', '', '', '1', '0', 'F', '0', '0', 'system:user:export', '#', 'admin', '2023-05-26 17:37:31', '', null, '');
INSERT INTO `sys_menu` VALUES ('1005', '用户导入', '100', '6', '', '', '', '1', '0', 'F', '0', '0', 'system:user:import', '#', 'admin', '2023-05-26 17:37:31', '', null, '');
INSERT INTO `sys_menu` VALUES ('1006', '重置密码', '100', '7', '', '', '', '1', '0', 'F', '0', '0', 'system:user:resetPwd', '#', 'admin', '2023-05-26 17:37:31', '', null, '');
INSERT INTO `sys_menu` VALUES ('1007', '角色查询', '101', '1', '', '', '', '1', '0', 'F', '0', '0', 'system:role:query', '#', 'admin', '2023-05-26 17:37:31', '', null, '');
INSERT INTO `sys_menu` VALUES ('1008', '角色新增', '101', '2', '', '', '', '1', '0', 'F', '0', '0', 'system:role:add', '#', 'admin', '2023-05-26 17:37:31', '', null, '');
INSERT INTO `sys_menu` VALUES ('1009', '角色修改', '101', '3', '', '', '', '1', '0', 'F', '0', '0', 'system:role:edit', '#', 'admin', '2023-05-26 17:37:31', '', null, '');
INSERT INTO `sys_menu` VALUES ('1010', '角色删除', '101', '4', '', '', '', '1', '0', 'F', '0', '0', 'system:role:remove', '#', 'admin', '2023-05-26 17:37:31', '', null, '');
INSERT INTO `sys_menu` VALUES ('1011', '角色导出', '101', '5', '', '', '', '1', '0', 'F', '0', '0', 'system:role:export', '#', 'admin', '2023-05-26 17:37:31', '', null, '');
INSERT INTO `sys_menu` VALUES ('1012', '菜单查询', '102', '1', '', '', '', '1', '0', 'F', '0', '0', 'system:menu:query', '#', 'admin', '2023-05-26 17:37:31', '', null, '');
INSERT INTO `sys_menu` VALUES ('1013', '菜单新增', '102', '2', '', '', '', '1', '0', 'F', '0', '0', 'system:menu:add', '#', 'admin', '2023-05-26 17:37:31', '', null, '');
INSERT INTO `sys_menu` VALUES ('1014', '菜单修改', '102', '3', '', '', '', '1', '0', 'F', '0', '0', 'system:menu:edit', '#', 'admin', '2023-05-26 17:37:31', '', null, '');
INSERT INTO `sys_menu` VALUES ('1015', '菜单删除', '102', '4', '', '', '', '1', '0', 'F', '0', '0', 'system:menu:remove', '#', 'admin', '2023-05-26 17:37:31', '', null, '');
INSERT INTO `sys_menu` VALUES ('2262', '字典管理', '0', '13', 'dict', null, null, '1', '0', 'M', '1', '0', null, 'dict', 'root', '2023-11-25 10:25:20', '', null, '');
INSERT INTO `sys_menu` VALUES ('2285', '主链管理', '0', '1', 'netWork', 'netWork/index', null, '1', '0', 'C', '0', '0', 'netWork:index:list', 'international', 'csz1', '2024-11-15 09:01:01', '', null, '');
INSERT INTO `sys_menu` VALUES ('2286', '推送消息管理', '0', '10', 'message', 'message/index', null, '1', '0', 'C', '0', '0', 'message:index', 'wechat', 'csz1', '2024-11-16 16:32:27', '', null, '');
INSERT INTO `sys_menu` VALUES ('2287', 'Dapp发现页管理', '0', '9', 'dapp', 'dapp/index', null, '1', '0', 'C', '0', '0', 'dapp:index', 'row', 'csz1', '2024-11-16 17:58:44', '', null, '');
INSERT INTO `sys_menu` VALUES ('2288', '上传资源管理', '0', '8', 'fileUplod', 'fileUplod/index', null, '1', '0', 'C', '0', '0', 'fileUplod:index', 'documentation', 'csz1', '2024-11-17 09:54:23', '', null, '');
INSERT INTO `sys_menu` VALUES ('2289', '代币管理', '0', '2', 'token', 'token/index', null, '1', '0', 'C', '0', '0', 'token:index:list', 'money', 'csz1', '2024-11-17 15:30:53', '', null, '');
INSERT INTO `sys_menu` VALUES ('2290', '证书管理', '0', '7', 'certificate', 'certificate/index', null, '1', '0', 'C', '0', '0', 'certificate:index', 'documentation', 'csz1', '2024-11-17 16:02:30', '', null, '');
INSERT INTO `sys_menu` VALUES ('2292', '第三方代币管理', '0', '3', 'platformToken', 'platformToken/index', null, '1', '0', 'C', '0', '0', 'platformToken:index', 'edit', 'csz1', '2024-11-20 10:42:01', '', null, '');
INSERT INTO `sys_menu` VALUES ('2293', '版本管理', '0', '6', 'version', null, null, '1', '0', 'M', '0', '0', null, 'bug', 'csz1', '2024-11-21 09:54:03', '', null, '');
INSERT INTO `sys_menu` VALUES ('2294', '软件版本管理', '2293', '1', 'software', 'software/index', null, '1', '0', 'C', '0', '0', 'software:index', 'phone', 'csz1', '2024-11-21 09:55:33', '', null, '');
INSERT INTO `sys_menu` VALUES ('2295', '硬件版本管理', '2293', '2', 'hardware', 'hardware/index', null, '1', '0', 'C', '0', '0', 'hardware:index', 'redis', 'csz1', '2024-11-21 09:56:47', '', null, '');
INSERT INTO `sys_menu` VALUES ('2296', '发版Token管理', '0', '11', 'releaseToken', 'releaseToken/index', null, '1', '0', 'C', '0', '0', 'releaseToken:index', 'eye-open', 'csz1', '2024-11-22 09:21:33', '', null, '');
INSERT INTO `sys_menu` VALUES ('2297', '日志管理', '1', '4', 'logIndex', 'logIndex/index', null, '1', '0', 'C', '0', '0', 'logIndex:index', 'list', 'csz1', '2024-11-25 09:29:11', '', null, '');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '角色ID',
  `role_name` varchar(30) NOT NULL COMMENT '角色名称',
  `role_key` varchar(100) NOT NULL COMMENT '角色权限字符串',
  `role_sort` int(4) NOT NULL COMMENT '显示顺序',
  `data_scope` char(1) DEFAULT '1' COMMENT '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  `menu_check_strictly` tinyint(1) DEFAULT '1' COMMENT '菜单树选择项是否关联显示',
  `dept_check_strictly` tinyint(1) DEFAULT '1' COMMENT '部门树选择项是否关联显示',
  `status` char(1) NOT NULL COMMENT '角色状态（0正常 1停用）',
  `del_flag` char(1) DEFAULT '0' COMMENT '删除标志（0代表存在 2代表删除）',
  `create_by` varchar(64) DEFAULT '' COMMENT '创建者',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(64) DEFAULT '' COMMENT '更新者',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='角色信息表';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '超级管理员', 'admin', '0', '1', '1', '1', '0', '0', 'admin', '2023-05-26 17:37:31', '', null, null);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `role_id` int(9) DEFAULT NULL COMMENT '角色id',
  `menu_id` int(9) DEFAULT NULL COMMENT '菜单id',
  `create_user` int(9) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `seq_role_id` (`role_id`) USING BTREE,
  KEY `seq_menu_id` (`menu_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=28465 DEFAULT CHARSET=utf8 COMMENT='角色菜单';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES ('26610', '2', '2251', null, '2023-11-16 16:06:05');
INSERT INTO `sys_role_menu` VALUES ('26611', '2', '2252', null, '2023-11-16 16:06:05');
INSERT INTO `sys_role_menu` VALUES ('26612', '2', '2253', null, '2023-11-16 16:06:05');
INSERT INTO `sys_role_menu` VALUES ('26613', '2', '1', null, '2023-11-16 16:06:05');
INSERT INTO `sys_role_menu` VALUES ('26614', '2', '100', null, '2023-11-16 16:06:05');
INSERT INTO `sys_role_menu` VALUES ('26615', '2', '1000', null, '2023-11-16 16:06:05');
INSERT INTO `sys_role_menu` VALUES ('26616', '2', '1001', null, '2023-11-16 16:06:05');
INSERT INTO `sys_role_menu` VALUES ('26617', '2', '1002', null, '2023-11-16 16:06:05');
INSERT INTO `sys_role_menu` VALUES ('26618', '2', '1003', null, '2023-11-16 16:06:05');
INSERT INTO `sys_role_menu` VALUES ('26619', '2', '1004', null, '2023-11-16 16:06:05');
INSERT INTO `sys_role_menu` VALUES ('26620', '2', '1005', null, '2023-11-16 16:06:05');
INSERT INTO `sys_role_menu` VALUES ('26621', '2', '1006', null, '2023-11-16 16:06:05');
INSERT INTO `sys_role_menu` VALUES ('26622', '2', '101', null, '2023-11-16 16:06:05');
INSERT INTO `sys_role_menu` VALUES ('26623', '2', '1007', null, '2023-11-16 16:06:05');
INSERT INTO `sys_role_menu` VALUES ('26624', '2', '1008', null, '2023-11-16 16:06:05');
INSERT INTO `sys_role_menu` VALUES ('26625', '2', '1009', null, '2023-11-16 16:06:05');
INSERT INTO `sys_role_menu` VALUES ('26626', '2', '1010', null, '2023-11-16 16:06:05');
INSERT INTO `sys_role_menu` VALUES ('26627', '2', '1011', null, '2023-11-16 16:06:05');
INSERT INTO `sys_role_menu` VALUES ('26628', '2', '102', null, '2023-11-16 16:06:05');
INSERT INTO `sys_role_menu` VALUES ('26629', '2', '1012', null, '2023-11-16 16:06:05');
INSERT INTO `sys_role_menu` VALUES ('26630', '2', '1013', null, '2023-11-16 16:06:05');
INSERT INTO `sys_role_menu` VALUES ('26631', '2', '1014', null, '2023-11-16 16:06:05');
INSERT INTO `sys_role_menu` VALUES ('26632', '2', '1015', null, '2023-11-16 16:06:05');
INSERT INTO `sys_role_menu` VALUES ('26633', '2', '2254', null, '2023-11-16 16:06:05');
INSERT INTO `sys_role_menu` VALUES ('26634', '2', '2256', null, '2023-11-16 16:06:05');
INSERT INTO `sys_role_menu` VALUES ('28037', '32', '2267', null, '2023-12-15 17:45:38');
INSERT INTO `sys_role_menu` VALUES ('28038', '32', '2257', null, '2023-12-15 17:45:38');
INSERT INTO `sys_role_menu` VALUES ('28039', '32', '2259', null, '2023-12-15 17:45:38');
INSERT INTO `sys_role_menu` VALUES ('28040', '32', '2260', null, '2023-12-15 17:45:38');
INSERT INTO `sys_role_menu` VALUES ('28041', '32', '1', null, '2023-12-15 17:45:38');
INSERT INTO `sys_role_menu` VALUES ('28042', '32', '100', null, '2023-12-15 17:45:38');
INSERT INTO `sys_role_menu` VALUES ('28043', '32', '1000', null, '2023-12-15 17:45:38');
INSERT INTO `sys_role_menu` VALUES ('28044', '32', '1001', null, '2023-12-15 17:45:38');
INSERT INTO `sys_role_menu` VALUES ('28045', '32', '1002', null, '2023-12-15 17:45:38');
INSERT INTO `sys_role_menu` VALUES ('28046', '32', '1003', null, '2023-12-15 17:45:38');
INSERT INTO `sys_role_menu` VALUES ('28047', '32', '1004', null, '2023-12-15 17:45:38');
INSERT INTO `sys_role_menu` VALUES ('28048', '32', '1005', null, '2023-12-15 17:45:38');
INSERT INTO `sys_role_menu` VALUES ('28049', '32', '1006', null, '2023-12-15 17:45:38');
INSERT INTO `sys_role_menu` VALUES ('28050', '32', '101', null, '2023-12-15 17:45:38');
INSERT INTO `sys_role_menu` VALUES ('28051', '32', '1007', null, '2023-12-15 17:45:38');
INSERT INTO `sys_role_menu` VALUES ('28052', '32', '1008', null, '2023-12-15 17:45:38');
INSERT INTO `sys_role_menu` VALUES ('28053', '32', '1009', null, '2023-12-15 17:45:38');
INSERT INTO `sys_role_menu` VALUES ('28054', '32', '1010', null, '2023-12-15 17:45:38');
INSERT INTO `sys_role_menu` VALUES ('28055', '32', '1011', null, '2023-12-15 17:45:38');
INSERT INTO `sys_role_menu` VALUES ('28056', '32', '102', null, '2023-12-15 17:45:38');
INSERT INTO `sys_role_menu` VALUES ('28057', '32', '1012', null, '2023-12-15 17:45:38');
INSERT INTO `sys_role_menu` VALUES ('28058', '32', '1013', null, '2023-12-15 17:45:38');
INSERT INTO `sys_role_menu` VALUES ('28059', '32', '1014', null, '2023-12-15 17:45:38');
INSERT INTO `sys_role_menu` VALUES ('28060', '32', '1015', null, '2023-12-15 17:45:38');
INSERT INTO `sys_role_menu` VALUES ('28061', '32', '2262', null, '2023-12-15 17:45:38');
INSERT INTO `sys_role_menu` VALUES ('28062', '32', '2263', null, '2023-12-15 17:45:38');
INSERT INTO `sys_role_menu` VALUES ('28063', '32', '2264', null, '2023-12-15 17:45:38');
INSERT INTO `sys_role_menu` VALUES ('28064', '32', '2265', null, '2023-12-15 17:45:38');
INSERT INTO `sys_role_menu` VALUES ('28065', '32', '2266', null, '2023-12-15 17:45:38');
INSERT INTO `sys_role_menu` VALUES ('28066', '32', '2261', null, '2023-12-15 17:45:38');
INSERT INTO `sys_role_menu` VALUES ('28067', '32', '2281', null, '2023-12-15 17:45:38');
INSERT INTO `sys_role_menu` VALUES ('28068', '32', '2275', null, '2023-12-15 17:45:38');
INSERT INTO `sys_role_menu` VALUES ('28069', '32', '2268', null, '2023-12-15 17:45:38');
INSERT INTO `sys_role_menu` VALUES ('28070', '32', '2269', null, '2023-12-15 17:45:38');
INSERT INTO `sys_role_menu` VALUES ('28071', '32', '2270', null, '2023-12-15 17:45:38');
INSERT INTO `sys_role_menu` VALUES ('28072', '32', '2271', null, '2023-12-15 17:45:38');
INSERT INTO `sys_role_menu` VALUES ('28073', '32', '2272', null, '2023-12-15 17:45:38');
INSERT INTO `sys_role_menu` VALUES ('28074', '32', '2273', null, '2023-12-15 17:45:38');
INSERT INTO `sys_role_menu` VALUES ('28075', '32', '2277', null, '2023-12-15 17:45:38');
INSERT INTO `sys_role_menu` VALUES ('28076', '32', '2278', null, '2023-12-15 17:45:38');
INSERT INTO `sys_role_menu` VALUES ('28077', '32', '2279', null, '2023-12-15 17:45:38');
INSERT INTO `sys_role_menu` VALUES ('28160', '31', '2267', null, '2023-12-15 17:47:46');
INSERT INTO `sys_role_menu` VALUES ('28161', '31', '2257', null, '2023-12-15 17:47:46');
INSERT INTO `sys_role_menu` VALUES ('28162', '31', '2258', null, '2023-12-15 17:47:46');
INSERT INTO `sys_role_menu` VALUES ('28163', '31', '2270', null, '2023-12-15 17:47:46');
INSERT INTO `sys_role_menu` VALUES ('28164', '31', '2271', null, '2023-12-15 17:47:46');
INSERT INTO `sys_role_menu` VALUES ('28165', '31', '2272', null, '2023-12-15 17:47:46');
INSERT INTO `sys_role_menu` VALUES ('28166', '31', '2273', null, '2023-12-15 17:47:46');
INSERT INTO `sys_role_menu` VALUES ('28167', '31', '2282', null, '2023-12-15 17:47:46');
INSERT INTO `sys_role_menu` VALUES ('28168', '31', '2276', null, '2023-12-15 17:47:46');
INSERT INTO `sys_role_menu` VALUES ('28169', '33', '2267', null, '2023-12-15 17:47:58');
INSERT INTO `sys_role_menu` VALUES ('28170', '33', '2257', null, '2023-12-15 17:47:58');
INSERT INTO `sys_role_menu` VALUES ('28171', '33', '2258', null, '2023-12-15 17:47:58');
INSERT INTO `sys_role_menu` VALUES ('28172', '33', '2270', null, '2023-12-15 17:47:58');
INSERT INTO `sys_role_menu` VALUES ('28173', '33', '2271', null, '2023-12-15 17:47:58');
INSERT INTO `sys_role_menu` VALUES ('28174', '33', '2272', null, '2023-12-15 17:47:58');
INSERT INTO `sys_role_menu` VALUES ('28175', '33', '2273', null, '2023-12-15 17:47:58');
INSERT INTO `sys_role_menu` VALUES ('28176', '33', '2282', null, '2023-12-15 17:47:58');
INSERT INTO `sys_role_menu` VALUES ('28177', '33', '2276', null, '2023-12-15 17:47:58');
INSERT INTO `sys_role_menu` VALUES ('28178', '36', '2257', null, '2023-12-15 18:19:19');
INSERT INTO `sys_role_menu` VALUES ('28179', '36', '2258', null, '2023-12-15 18:19:19');
INSERT INTO `sys_role_menu` VALUES ('28180', '36', '2259', null, '2023-12-15 18:19:19');
INSERT INTO `sys_role_menu` VALUES ('28181', '36', '2260', null, '2023-12-15 18:19:19');
INSERT INTO `sys_role_menu` VALUES ('28182', '36', '2261', null, '2023-12-15 18:19:19');
INSERT INTO `sys_role_menu` VALUES ('28183', '36', '2281', null, '2023-12-15 18:19:19');
INSERT INTO `sys_role_menu` VALUES ('28184', '36', '1', null, '2023-12-15 18:19:19');
INSERT INTO `sys_role_menu` VALUES ('28185', '36', '100', null, '2023-12-15 18:19:19');
INSERT INTO `sys_role_menu` VALUES ('28186', '36', '1000', null, '2023-12-15 18:19:19');
INSERT INTO `sys_role_menu` VALUES ('28187', '36', '1001', null, '2023-12-15 18:19:19');
INSERT INTO `sys_role_menu` VALUES ('28188', '36', '1002', null, '2023-12-15 18:19:19');
INSERT INTO `sys_role_menu` VALUES ('28189', '36', '1003', null, '2023-12-15 18:19:19');
INSERT INTO `sys_role_menu` VALUES ('28190', '36', '1004', null, '2023-12-15 18:19:19');
INSERT INTO `sys_role_menu` VALUES ('28191', '36', '1005', null, '2023-12-15 18:19:19');
INSERT INTO `sys_role_menu` VALUES ('28192', '36', '1006', null, '2023-12-15 18:19:19');
INSERT INTO `sys_role_menu` VALUES ('28193', '36', '101', null, '2023-12-15 18:19:19');
INSERT INTO `sys_role_menu` VALUES ('28194', '36', '1007', null, '2023-12-15 18:19:19');
INSERT INTO `sys_role_menu` VALUES ('28195', '36', '1008', null, '2023-12-15 18:19:19');
INSERT INTO `sys_role_menu` VALUES ('28196', '36', '1009', null, '2023-12-15 18:19:19');
INSERT INTO `sys_role_menu` VALUES ('28197', '36', '1010', null, '2023-12-15 18:19:19');
INSERT INTO `sys_role_menu` VALUES ('28198', '36', '1011', null, '2023-12-15 18:19:19');
INSERT INTO `sys_role_menu` VALUES ('28199', '36', '102', null, '2023-12-15 18:19:19');
INSERT INTO `sys_role_menu` VALUES ('28200', '36', '1012', null, '2023-12-15 18:19:19');
INSERT INTO `sys_role_menu` VALUES ('28201', '36', '1013', null, '2023-12-15 18:19:19');
INSERT INTO `sys_role_menu` VALUES ('28202', '36', '1014', null, '2023-12-15 18:19:19');
INSERT INTO `sys_role_menu` VALUES ('28203', '36', '1015', null, '2023-12-15 18:19:19');
INSERT INTO `sys_role_menu` VALUES ('28204', '36', '2262', null, '2023-12-15 18:19:19');
INSERT INTO `sys_role_menu` VALUES ('28205', '36', '2263', null, '2023-12-15 18:19:19');
INSERT INTO `sys_role_menu` VALUES ('28206', '36', '2264', null, '2023-12-15 18:19:19');
INSERT INTO `sys_role_menu` VALUES ('28207', '36', '2265', null, '2023-12-15 18:19:19');
INSERT INTO `sys_role_menu` VALUES ('28208', '36', '2266', null, '2023-12-15 18:19:19');
INSERT INTO `sys_role_menu` VALUES ('28251', '3', '2267', null, '2023-12-19 14:37:20');
INSERT INTO `sys_role_menu` VALUES ('28252', '3', '2257', null, '2023-12-19 14:37:20');
INSERT INTO `sys_role_menu` VALUES ('28253', '3', '2259', null, '2023-12-19 14:37:20');
INSERT INTO `sys_role_menu` VALUES ('28254', '3', '2260', null, '2023-12-19 14:37:20');
INSERT INTO `sys_role_menu` VALUES ('28255', '3', '2261', null, '2023-12-19 14:37:20');
INSERT INTO `sys_role_menu` VALUES ('28256', '3', '2281', null, '2023-12-19 14:37:20');
INSERT INTO `sys_role_menu` VALUES ('28257', '3', '1', null, '2023-12-19 14:37:20');
INSERT INTO `sys_role_menu` VALUES ('28258', '3', '100', null, '2023-12-19 14:37:20');
INSERT INTO `sys_role_menu` VALUES ('28259', '3', '1000', null, '2023-12-19 14:37:20');
INSERT INTO `sys_role_menu` VALUES ('28260', '3', '1001', null, '2023-12-19 14:37:20');
INSERT INTO `sys_role_menu` VALUES ('28261', '3', '1002', null, '2023-12-19 14:37:20');
INSERT INTO `sys_role_menu` VALUES ('28262', '3', '1003', null, '2023-12-19 14:37:20');
INSERT INTO `sys_role_menu` VALUES ('28263', '3', '1004', null, '2023-12-19 14:37:20');
INSERT INTO `sys_role_menu` VALUES ('28264', '3', '1005', null, '2023-12-19 14:37:20');
INSERT INTO `sys_role_menu` VALUES ('28265', '3', '1006', null, '2023-12-19 14:37:20');
INSERT INTO `sys_role_menu` VALUES ('28266', '3', '101', null, '2023-12-19 14:37:20');
INSERT INTO `sys_role_menu` VALUES ('28267', '3', '1007', null, '2023-12-19 14:37:20');
INSERT INTO `sys_role_menu` VALUES ('28268', '3', '1008', null, '2023-12-19 14:37:20');
INSERT INTO `sys_role_menu` VALUES ('28269', '3', '1009', null, '2023-12-19 14:37:20');
INSERT INTO `sys_role_menu` VALUES ('28270', '3', '1010', null, '2023-12-19 14:37:20');
INSERT INTO `sys_role_menu` VALUES ('28271', '3', '1011', null, '2023-12-19 14:37:20');
INSERT INTO `sys_role_menu` VALUES ('28272', '3', '102', null, '2023-12-19 14:37:20');
INSERT INTO `sys_role_menu` VALUES ('28273', '3', '1012', null, '2023-12-19 14:37:20');
INSERT INTO `sys_role_menu` VALUES ('28274', '3', '1013', null, '2023-12-19 14:37:20');
INSERT INTO `sys_role_menu` VALUES ('28275', '3', '1014', null, '2023-12-19 14:37:20');
INSERT INTO `sys_role_menu` VALUES ('28276', '3', '1015', null, '2023-12-19 14:37:20');
INSERT INTO `sys_role_menu` VALUES ('28277', '3', '2262', null, '2023-12-19 14:37:20');
INSERT INTO `sys_role_menu` VALUES ('28278', '3', '2263', null, '2023-12-19 14:37:20');
INSERT INTO `sys_role_menu` VALUES ('28279', '3', '2264', null, '2023-12-19 14:37:20');
INSERT INTO `sys_role_menu` VALUES ('28280', '3', '2265', null, '2023-12-19 14:37:20');
INSERT INTO `sys_role_menu` VALUES ('28281', '3', '2266', null, '2023-12-19 14:37:20');
INSERT INTO `sys_role_menu` VALUES ('28282', '3', '2275', null, '2023-12-19 14:37:20');
INSERT INTO `sys_role_menu` VALUES ('28283', '3', '2268', null, '2023-12-19 14:37:20');
INSERT INTO `sys_role_menu` VALUES ('28284', '3', '2269', null, '2023-12-19 14:37:20');
INSERT INTO `sys_role_menu` VALUES ('28285', '3', '2270', null, '2023-12-19 14:37:20');
INSERT INTO `sys_role_menu` VALUES ('28286', '3', '2271', null, '2023-12-19 14:37:20');
INSERT INTO `sys_role_menu` VALUES ('28287', '3', '2272', null, '2023-12-19 14:37:20');
INSERT INTO `sys_role_menu` VALUES ('28288', '3', '2273', null, '2023-12-19 14:37:20');
INSERT INTO `sys_role_menu` VALUES ('28289', '3', '2282', null, '2023-12-19 14:37:20');
INSERT INTO `sys_role_menu` VALUES ('28290', '3', '2277', null, '2023-12-19 14:37:20');
INSERT INTO `sys_role_menu` VALUES ('28291', '3', '2278', null, '2023-12-19 14:37:20');
INSERT INTO `sys_role_menu` VALUES ('28292', '3', '2279', null, '2023-12-19 14:37:20');
INSERT INTO `sys_role_menu` VALUES ('28378', '35', '2267', null, '2023-12-22 08:31:04');
INSERT INTO `sys_role_menu` VALUES ('28379', '35', '2257', null, '2023-12-22 08:31:04');
INSERT INTO `sys_role_menu` VALUES ('28380', '35', '2259', null, '2023-12-22 08:31:04');
INSERT INTO `sys_role_menu` VALUES ('28381', '35', '2260', null, '2023-12-22 08:31:04');
INSERT INTO `sys_role_menu` VALUES ('28382', '35', '2261', null, '2023-12-22 08:31:04');
INSERT INTO `sys_role_menu` VALUES ('28383', '35', '2281', null, '2023-12-22 08:31:04');
INSERT INTO `sys_role_menu` VALUES ('28384', '35', '1', null, '2023-12-22 08:31:04');
INSERT INTO `sys_role_menu` VALUES ('28385', '35', '100', null, '2023-12-22 08:31:04');
INSERT INTO `sys_role_menu` VALUES ('28386', '35', '1000', null, '2023-12-22 08:31:04');
INSERT INTO `sys_role_menu` VALUES ('28387', '35', '1001', null, '2023-12-22 08:31:04');
INSERT INTO `sys_role_menu` VALUES ('28388', '35', '1002', null, '2023-12-22 08:31:04');
INSERT INTO `sys_role_menu` VALUES ('28389', '35', '1003', null, '2023-12-22 08:31:04');
INSERT INTO `sys_role_menu` VALUES ('28390', '35', '1004', null, '2023-12-22 08:31:04');
INSERT INTO `sys_role_menu` VALUES ('28391', '35', '1005', null, '2023-12-22 08:31:04');
INSERT INTO `sys_role_menu` VALUES ('28392', '35', '1006', null, '2023-12-22 08:31:04');
INSERT INTO `sys_role_menu` VALUES ('28393', '35', '101', null, '2023-12-22 08:31:04');
INSERT INTO `sys_role_menu` VALUES ('28394', '35', '1007', null, '2023-12-22 08:31:04');
INSERT INTO `sys_role_menu` VALUES ('28395', '35', '1008', null, '2023-12-22 08:31:04');
INSERT INTO `sys_role_menu` VALUES ('28396', '35', '1009', null, '2023-12-22 08:31:04');
INSERT INTO `sys_role_menu` VALUES ('28397', '35', '1010', null, '2023-12-22 08:31:04');
INSERT INTO `sys_role_menu` VALUES ('28398', '35', '1011', null, '2023-12-22 08:31:04');
INSERT INTO `sys_role_menu` VALUES ('28399', '35', '102', null, '2023-12-22 08:31:04');
INSERT INTO `sys_role_menu` VALUES ('28400', '35', '1012', null, '2023-12-22 08:31:04');
INSERT INTO `sys_role_menu` VALUES ('28401', '35', '1013', null, '2023-12-22 08:31:04');
INSERT INTO `sys_role_menu` VALUES ('28402', '35', '1014', null, '2023-12-22 08:31:04');
INSERT INTO `sys_role_menu` VALUES ('28403', '35', '1015', null, '2023-12-22 08:31:04');
INSERT INTO `sys_role_menu` VALUES ('28404', '35', '2262', null, '2023-12-22 08:31:04');
INSERT INTO `sys_role_menu` VALUES ('28405', '35', '2263', null, '2023-12-22 08:31:04');
INSERT INTO `sys_role_menu` VALUES ('28406', '35', '2264', null, '2023-12-22 08:31:04');
INSERT INTO `sys_role_menu` VALUES ('28407', '35', '2265', null, '2023-12-22 08:31:04');
INSERT INTO `sys_role_menu` VALUES ('28408', '35', '2266', null, '2023-12-22 08:31:04');
INSERT INTO `sys_role_menu` VALUES ('28409', '35', '2275', null, '2023-12-22 08:31:04');
INSERT INTO `sys_role_menu` VALUES ('28410', '35', '2268', null, '2023-12-22 08:31:04');
INSERT INTO `sys_role_menu` VALUES ('28411', '35', '2269', null, '2023-12-22 08:31:04');
INSERT INTO `sys_role_menu` VALUES ('28412', '35', '2270', null, '2023-12-22 08:31:04');
INSERT INTO `sys_role_menu` VALUES ('28413', '35', '2271', null, '2023-12-22 08:31:04');
INSERT INTO `sys_role_menu` VALUES ('28414', '35', '2272', null, '2023-12-22 08:31:04');
INSERT INTO `sys_role_menu` VALUES ('28415', '35', '2273', null, '2023-12-22 08:31:04');
INSERT INTO `sys_role_menu` VALUES ('28416', '35', '2282', null, '2023-12-22 08:31:04');
INSERT INTO `sys_role_menu` VALUES ('28417', '35', '2277', null, '2023-12-22 08:31:04');
INSERT INTO `sys_role_menu` VALUES ('28418', '35', '2278', null, '2023-12-22 08:31:04');
INSERT INTO `sys_role_menu` VALUES ('28419', '35', '2279', null, '2023-12-22 08:31:04');
INSERT INTO `sys_role_menu` VALUES ('28420', '34', '2267', null, '2024-05-06 10:52:55');
INSERT INTO `sys_role_menu` VALUES ('28421', '34', '2257', null, '2024-05-06 10:52:55');
INSERT INTO `sys_role_menu` VALUES ('28422', '34', '2258', null, '2024-05-06 10:52:55');
INSERT INTO `sys_role_menu` VALUES ('28423', '34', '2259', null, '2024-05-06 10:52:55');
INSERT INTO `sys_role_menu` VALUES ('28424', '34', '2260', null, '2024-05-06 10:52:55');
INSERT INTO `sys_role_menu` VALUES ('28425', '34', '2280', null, '2024-05-06 10:52:55');
INSERT INTO `sys_role_menu` VALUES ('28426', '34', '2261', null, '2024-05-06 10:52:55');
INSERT INTO `sys_role_menu` VALUES ('28427', '34', '2281', null, '2024-05-06 10:52:55');
INSERT INTO `sys_role_menu` VALUES ('28428', '34', '1', null, '2024-05-06 10:52:55');
INSERT INTO `sys_role_menu` VALUES ('28429', '34', '100', null, '2024-05-06 10:52:55');
INSERT INTO `sys_role_menu` VALUES ('28430', '34', '1000', null, '2024-05-06 10:52:55');
INSERT INTO `sys_role_menu` VALUES ('28431', '34', '1001', null, '2024-05-06 10:52:55');
INSERT INTO `sys_role_menu` VALUES ('28432', '34', '1002', null, '2024-05-06 10:52:55');
INSERT INTO `sys_role_menu` VALUES ('28433', '34', '1003', null, '2024-05-06 10:52:55');
INSERT INTO `sys_role_menu` VALUES ('28434', '34', '1004', null, '2024-05-06 10:52:55');
INSERT INTO `sys_role_menu` VALUES ('28435', '34', '1005', null, '2024-05-06 10:52:55');
INSERT INTO `sys_role_menu` VALUES ('28436', '34', '1006', null, '2024-05-06 10:52:55');
INSERT INTO `sys_role_menu` VALUES ('28437', '34', '101', null, '2024-05-06 10:52:55');
INSERT INTO `sys_role_menu` VALUES ('28438', '34', '1007', null, '2024-05-06 10:52:55');
INSERT INTO `sys_role_menu` VALUES ('28439', '34', '1008', null, '2024-05-06 10:52:55');
INSERT INTO `sys_role_menu` VALUES ('28440', '34', '1009', null, '2024-05-06 10:52:55');
INSERT INTO `sys_role_menu` VALUES ('28441', '34', '1010', null, '2024-05-06 10:52:55');
INSERT INTO `sys_role_menu` VALUES ('28442', '34', '1011', null, '2024-05-06 10:52:55');
INSERT INTO `sys_role_menu` VALUES ('28443', '34', '102', null, '2024-05-06 10:52:55');
INSERT INTO `sys_role_menu` VALUES ('28444', '34', '1012', null, '2024-05-06 10:52:55');
INSERT INTO `sys_role_menu` VALUES ('28445', '34', '1013', null, '2024-05-06 10:52:55');
INSERT INTO `sys_role_menu` VALUES ('28446', '34', '1014', null, '2024-05-06 10:52:55');
INSERT INTO `sys_role_menu` VALUES ('28447', '34', '1015', null, '2024-05-06 10:52:55');
INSERT INTO `sys_role_menu` VALUES ('28448', '34', '2262', null, '2024-05-06 10:52:55');
INSERT INTO `sys_role_menu` VALUES ('28449', '34', '2263', null, '2024-05-06 10:52:55');
INSERT INTO `sys_role_menu` VALUES ('28450', '34', '2264', null, '2024-05-06 10:52:55');
INSERT INTO `sys_role_menu` VALUES ('28451', '34', '2265', null, '2024-05-06 10:52:55');
INSERT INTO `sys_role_menu` VALUES ('28452', '34', '2266', null, '2024-05-06 10:52:55');
INSERT INTO `sys_role_menu` VALUES ('28453', '34', '2284', null, '2024-05-06 10:52:55');
INSERT INTO `sys_role_menu` VALUES ('28454', '34', '2275', null, '2024-05-06 10:52:55');
INSERT INTO `sys_role_menu` VALUES ('28455', '34', '2268', null, '2024-05-06 10:52:55');
INSERT INTO `sys_role_menu` VALUES ('28456', '34', '2269', null, '2024-05-06 10:52:55');
INSERT INTO `sys_role_menu` VALUES ('28457', '34', '2270', null, '2024-05-06 10:52:55');
INSERT INTO `sys_role_menu` VALUES ('28458', '34', '2271', null, '2024-05-06 10:52:55');
INSERT INTO `sys_role_menu` VALUES ('28459', '34', '2272', null, '2024-05-06 10:52:55');
INSERT INTO `sys_role_menu` VALUES ('28460', '34', '2273', null, '2024-05-06 10:52:55');
INSERT INTO `sys_role_menu` VALUES ('28461', '34', '2274', null, '2024-05-06 10:52:55');
INSERT INTO `sys_role_menu` VALUES ('28462', '34', '2277', null, '2024-05-06 10:52:55');
INSERT INTO `sys_role_menu` VALUES ('28463', '34', '2278', null, '2024-05-06 10:52:55');
INSERT INTO `sys_role_menu` VALUES ('28464', '34', '2279', null, '2024-05-06 10:52:55');

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `acctive` varchar(80) DEFAULT NULL COMMENT '账户',
  `password` varchar(80) DEFAULT NULL COMMENT '密码',
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `tel` varchar(80) DEFAULT NULL COMMENT '手机号',
  `head_pic` varchar(120) DEFAULT NULL COMMENT '头像',
  `user_state` int(2) DEFAULT NULL COMMENT '用户状态',
  `os_version` varchar(255) DEFAULT NULL COMMENT '安卓操作系统版本',
  `app_version` varchar(255) DEFAULT NULL COMMENT 'app版本',
  `device_model` varchar(255) DEFAULT NULL COMMENT '设备型号',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `create_user` int(11) DEFAULT NULL COMMENT '创建人',
  `login_time` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `google_secretkey` varchar(280) DEFAULT NULL,
  `google_auth_lable` int(2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='用户';

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'csz', 'e10adc3949ba59abbe56e057f20f883e', 'csz1', '15621349306', null, '1', null, null, null, '2022-04-14 09:29:44', '1', '2024-11-25 10:59:36', null, null);
INSERT INTO `sys_user` VALUES ('2', 'admin', 'e10adc3949ba59abbe56e057f20f883e', 'admin', '15621349307', null, '1', '8.1.0', '1.0.2', 'Redmi5Plus', '2022-04-14 09:29:44', '1', '2024-11-17 19:10:32', null, null);
INSERT INTO `sys_user` VALUES ('3', 'root', 'e6e407b1edb2cca3def82992c8ef32d9', 'root', '15621349388', null, '1', null, null, null, '2024-11-22 09:53:04', '1', null, null, null);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` int(9) NOT NULL AUTO_INCREMENT,
  `user_id` int(9) DEFAULT NULL COMMENT '用户id',
  `role_id` int(9) DEFAULT NULL COMMENT '角色id',
  `create_user` int(9) DEFAULT NULL COMMENT '创建人',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `seq_user_id` (`user_id`) USING BTREE,
  KEY `seq_role_id` (`role_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2108 DEFAULT CHARSET=utf8 COMMENT='用户角色 ';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('1', '1', '1', '1', '2023-05-29 11:21:15');
INSERT INTO `sys_user_role` VALUES ('2106', '2', '1', '1', '2023-11-08 15:45:55');
INSERT INTO `sys_user_role` VALUES ('2107', '3', '1', null, '2024-11-22 09:53:04');

-- ----------------------------
-- Table structure for version_android
-- ----------------------------
DROP TABLE IF EXISTS `version_android`;
CREATE TABLE `version_android` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `version_id` int(11) DEFAULT NULL,
  `android_url` varchar(380) DEFAULT NULL,
  `android_version` varchar(180) DEFAULT NULL,
  `android_googlePlay` varchar(380) DEFAULT NULL,
  `release_state` int(2) DEFAULT NULL,
  `set_time` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of version_android
-- ----------------------------
INSERT INTO `version_android` VALUES ('21', '23', 'https://静态资源/apk/v1.0.0/wallet-1.0.0-android.apk', '1,0,0', 'https://play.google.com/store/apps/details?id=digtalshield.app.wallet', '1', '2024-11-21 10:59:04', '2024-11-21 10:59:19');
INSERT INTO `version_android` VALUES ('22', '24', 'https://静态资源/apk/v1.0.0/wallet-1.0.0-android.apk', '1,0,0', 'https://play.google.com/store/apps/details?id=digtalshield.app.wallet', '1', '2024-11-21 10:59:13', '2024-11-21 11:08:57');
INSERT INTO `version_android` VALUES ('23', '25', 'https://静态资源/apk/v1.0.0/wallet-1.0.0-android.apk', '1,0,0', 'https://play.google.com/store/apps/details?id=digtalshield.app.wallet', '1', '2024-11-21 10:59:23', '2024-11-21 11:13:20');
INSERT INTO `version_android` VALUES ('24', '26', 'https://静态资源/apk/v1.0.0/wallet-1.0.0-android.apk', '1,0,0', 'https://play.google.com/store/apps/details?id=digtalshield.app.wallet', '1', '2024-11-21 11:09:07', '2024-11-21 11:14:15');
INSERT INTO `version_android` VALUES ('25', '27', 'https://静态资源/apk/v1.0.0/wallet-1.0.0-android.apk', '1,0,0', 'https://play.google.com/store/apps/details?id=digtalshield.app.wallet', '1', '2024-11-21 11:13:25', '2024-11-21 11:17:20');
INSERT INTO `version_android` VALUES ('26', '28', 'https://静态资源/apk/v1.0.0/wallet-1.0.0-android.apk', '1,0,0', 'https://play.google.com/store/apps/details?id=digtalshield.app.wallet', '1', '2024-11-21 11:14:21', '2024-11-21 11:17:20');
INSERT INTO `version_android` VALUES ('27', '29', 'https://静态资源/apk/v1.0.0/wallet-1.0.0-android.apk', '1,0,0', 'https://play.google.com/store/apps/details?id=digtalshield.app.wallet', '1', '2024-11-21 11:17:13', '2024-11-21 11:20:47');
INSERT INTO `version_android` VALUES ('28', '30', 'https://静态资源/apk/v1.0.0/wallet-1.0.0-android.apk', '1,0,0', 'https://play.google.com/store/apps/details?id=digtalshield.app.wallet', '1', '2024-11-21 11:19:58', '2024-11-21 11:45:41');
INSERT INTO `version_android` VALUES ('29', '31', 'https://静态资源/apk/v1.0.0/wallet-1.0.0-android.apk', '1,0,0', 'https://play.google.com/store/apps/details?id=digtalshield.app.wallet', '1', '2024-11-21 11:20:50', '2024-11-21 11:45:41');
INSERT INTO `version_android` VALUES ('30', '32', 'https://静态资源/apk/v1.0.0/wallet-1.0.0-android.apk', '1,0,0', 'https://play.google.com/store/apps/details?id=digtalshield.app.wallet', '1', '2024-11-21 11:42:15', '2024-11-21 11:45:41');
INSERT INTO `version_android` VALUES ('31', '33', 'https://静态资源/apk/v1.0.0/wallet-1.0.0-android.apk', '1,0,0', 'https://play.google.com/store/apps/details?id=digtalshield.app.wallet', '1', '2024-11-21 11:44:12', '2024-11-22 10:40:35');
INSERT INTO `version_android` VALUES ('32', '34', 'https://静态资源/apk/v1.0.0/wallet-1.0.0-android.apk', '1,0,0', 'https://play.google.com/store/apps/details?id=digtalshield.app.wallet', '1', '2024-11-21 11:45:53', '2024-11-22 10:40:35');
INSERT INTO `version_android` VALUES ('33', '35', 'https://静态资源/apk/v1.0.0/wallet-1.0.0-android.apk', '1,0,0', 'https://play.google.com/store/apps/details?id=digtalshield.app.wallet', '1', '2024-11-22 10:03:34', '2024-11-22 10:40:35');
INSERT INTO `version_android` VALUES ('34', '36', 'https://静态资源/apk/v1.0.0/wallet-1.0.0-android.apk', '1,0,0', 'https://play.google.com/store/apps/details?id=digtalshield.app.wallet', '1', '2024-11-22 10:04:05', '2024-11-22 10:40:35');
INSERT INTO `version_android` VALUES ('35', '37', 'https://静态资源/apk/v1.0.0/wallet-1.0.0-android.apk', '1,0,0', 'https://play.google.com/store/apps/details?id=digtalshield.app.wallet', '1', '2024-11-22 10:15:23', '2024-11-22 10:40:35');
INSERT INTO `version_android` VALUES ('36', '38', 'https://静态资源/apk/v1.0.0/wallet-1.0.0-android.apk', '1,0,0', 'https://play.google.com/store/apps/details?id=digtalshield.app.wallet', '2', '2024-11-22 10:22:47', '2024-11-22 10:40:35');
INSERT INTO `version_android` VALUES ('37', '39', 'https://静态资源/apk/v1.0.0/wallet-1.0.0-android.apk', '1,0,0', 'https://play.google.com/store/apps/details?id=digtalshield.app.wallet', '0', '2024-11-22 10:40:58', '2024-11-22 10:40:58');

-- ----------------------------
-- Table structure for version_android_google
-- ----------------------------
DROP TABLE IF EXISTS `version_android_google`;
CREATE TABLE `version_android_google` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `android_version_id` int(11) DEFAULT NULL,
  `google_url` varchar(380) DEFAULT NULL,
  `google_version` varchar(180) DEFAULT NULL,
  `release_state` int(2) DEFAULT NULL,
  `set_time` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of version_android_google
-- ----------------------------
INSERT INTO `version_android_google` VALUES ('21', '21', 'https://play.google.com/store/apps/details?id=digtalshield.wallet', '1,0,0', '1', '2024-11-21 10:59:04', '2024-11-21 10:59:19');
INSERT INTO `version_android_google` VALUES ('22', '22', 'https://play.google.com/store/apps/details?id=digtalshield.wallet', '1,0,0', '1', '2024-11-21 10:59:13', '2024-11-21 11:08:57');
INSERT INTO `version_android_google` VALUES ('23', '23', 'https://play.google.com/store/apps/details?id=digtalshield.wallet', '1,0,0', '1', '2024-11-21 10:59:23', '2024-11-21 11:13:20');
INSERT INTO `version_android_google` VALUES ('24', '24', 'https://play.google.com/store/apps/details?id=digtalshield.wallet', '1,0,0', '1', '2024-11-21 11:09:07', '2024-11-21 11:14:15');
INSERT INTO `version_android_google` VALUES ('25', '25', 'https://play.google.com/store/apps/details?id=digtalshield.wallet', '1,0,0', '1', '2024-11-21 11:13:25', '2024-11-21 11:17:20');
INSERT INTO `version_android_google` VALUES ('26', '26', 'https://play.google.com/store/apps/details?id=digtalshield.wallet', '1,0,0', '1', '2024-11-21 11:14:21', '2024-11-21 11:17:20');
INSERT INTO `version_android_google` VALUES ('27', '27', 'https://play.google.com/store/apps/details?id=digtalshield.wallet', '1,0,0', '1', '2024-11-21 11:17:13', '2024-11-21 11:20:47');
INSERT INTO `version_android_google` VALUES ('28', '28', 'https://play.google.com/store/apps/details?id=digtalshield.wallet', '1,0,0', '1', '2024-11-21 11:19:58', '2024-11-21 11:45:41');
INSERT INTO `version_android_google` VALUES ('29', '29', 'https://play.google.com/store/apps/details?id=digtalshield.wallet', '1,0,0', '1', '2024-11-21 11:20:50', '2024-11-21 11:45:41');
INSERT INTO `version_android_google` VALUES ('30', '30', 'https://play.google.com/store/apps/details?id=digtalshield.wallet', '1,0,0', '1', '2024-11-21 11:42:15', '2024-11-21 11:45:41');
INSERT INTO `version_android_google` VALUES ('31', '31', 'https://play.google.com/store/apps/details?id=digtalshield.wallet', '1,0,0', '1', '2024-11-21 11:44:12', '2024-11-22 10:40:35');
INSERT INTO `version_android_google` VALUES ('32', '32', 'https://play.google.com/store/apps/details?id=digtalshield.wallet', '1,0,0', '1', '2024-11-21 11:45:53', '2024-11-22 10:40:35');
INSERT INTO `version_android_google` VALUES ('33', '33', 'https://play.google.com/store/apps/details?id=digtalshield.wallet', '1,0,0', '1', '2024-11-22 10:03:34', '2024-11-22 10:40:35');
INSERT INTO `version_android_google` VALUES ('34', '34', 'https://play.google.com/store/apps/details?id=digtalshield.wallet', '1,0,0', '1', '2024-11-22 10:04:05', '2024-11-22 10:40:35');
INSERT INTO `version_android_google` VALUES ('35', '35', 'https://play.google.com/store/apps/details?id=digtalshield.wallet', '1,0,0', '1', '2024-11-22 10:15:23', '2024-11-22 10:40:35');
INSERT INTO `version_android_google` VALUES ('36', '36', 'https://play.google.com/store/apps/details?id=digtalshield.wallet', '1,0,0', '2', '2024-11-22 10:22:47', '2024-11-22 10:40:35');
INSERT INTO `version_android_google` VALUES ('37', '37', 'https://play.google.com/store/apps/details?id=digtalshield.wallet', '1,0,0', '0', '2024-11-22 10:40:58', '2024-11-22 10:40:58');

-- ----------------------------
-- Table structure for version_db
-- ----------------------------
DROP TABLE IF EXISTS `version_db`;
CREATE TABLE `version_db` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `version_content` text,
  `release_state` int(2) DEFAULT NULL,
  `set_time` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of version_db
-- ----------------------------
INSERT INTO `version_db` VALUES ('23', null, '1', '2024-11-21 10:59:04', '2024-11-21 10:59:19');
INSERT INTO `version_db` VALUES ('24', null, '1', '2024-11-21 10:59:13', '2024-11-21 11:08:57');
INSERT INTO `version_db` VALUES ('25', null, '1', '2024-11-21 10:59:23', '2024-11-21 11:13:20');
INSERT INTO `version_db` VALUES ('26', null, '1', '2024-11-21 11:09:07', '2024-11-21 11:14:15');
INSERT INTO `version_db` VALUES ('27', null, '1', '2024-11-21 11:13:25', '2024-11-21 11:17:20');
INSERT INTO `version_db` VALUES ('28', null, '1', '2024-11-21 11:14:21', '2024-11-21 11:17:20');
INSERT INTO `version_db` VALUES ('29', null, '1', '2024-11-21 11:17:13', '2024-11-21 11:20:47');
INSERT INTO `version_db` VALUES ('30', null, '1', '2024-11-21 11:19:58', '2024-11-21 11:45:41');
INSERT INTO `version_db` VALUES ('31', null, '1', '2024-11-21 11:20:50', '2024-11-21 11:45:41');
INSERT INTO `version_db` VALUES ('32', null, '1', '2024-11-21 11:42:15', '2024-11-21 11:45:41');
INSERT INTO `version_db` VALUES ('33', null, '1', '2024-11-21 11:44:12', '2024-11-22 10:40:35');
INSERT INTO `version_db` VALUES ('34', null, '1', '2024-11-21 11:45:53', '2024-11-22 10:40:35');
INSERT INTO `version_db` VALUES ('35', null, '1', '2024-11-22 10:03:34', '2024-11-22 10:40:35');
INSERT INTO `version_db` VALUES ('36', null, '1', '2024-11-22 10:04:05', '2024-11-22 10:40:35');
INSERT INTO `version_db` VALUES ('37', null, '1', '2024-11-22 10:15:23', '2024-11-22 10:40:35');
INSERT INTO `version_db` VALUES ('38', null, '2', '2024-11-22 10:22:47', '2024-11-22 10:22:47');
INSERT INTO `version_db` VALUES ('39', null, '0', '2024-11-22 10:40:58', '2024-11-22 10:40:58');

-- ----------------------------
-- Table structure for version_digtalshie
-- ----------------------------
DROP TABLE IF EXISTS `version_digtalshie`;
CREATE TABLE `version_digtalshie` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `version_id` int(11) DEFAULT NULL,
  `digtalshie_id` varchar(280) DEFAULT NULL,
  `digtalshie_name` varchar(280) DEFAULT NULL,
  `release_state` int(2) DEFAULT NULL,
  `set_time` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of version_digtalshie
-- ----------------------------
INSERT INTO `version_digtalshie` VALUES ('21', '23', null, null, '1', '2024-11-21 10:59:04', '2024-11-21 11:08:46');
INSERT INTO `version_digtalshie` VALUES ('22', '24', null, null, '1', '2024-11-21 10:59:13', '2024-11-21 11:08:46');
INSERT INTO `version_digtalshie` VALUES ('23', '25', null, null, '1', '2024-11-21 10:59:23', '2024-11-21 11:13:00');
INSERT INTO `version_digtalshie` VALUES ('24', '26', null, null, '1', '2024-11-21 11:09:07', '2024-11-21 11:13:40');
INSERT INTO `version_digtalshie` VALUES ('25', '27', null, null, '1', '2024-11-21 11:13:25', '2024-11-21 11:14:31');
INSERT INTO `version_digtalshie` VALUES ('26', '28', null, null, '1', '2024-11-21 11:14:21', '2024-11-21 11:17:27');
INSERT INTO `version_digtalshie` VALUES ('27', '29', null, null, '1', '2024-11-21 11:17:13', '2024-11-21 11:20:06');
INSERT INTO `version_digtalshie` VALUES ('28', '30', null, null, '1', '2024-11-21 11:19:58', '2024-11-21 11:45:45');
INSERT INTO `version_digtalshie` VALUES ('29', '31', null, null, '1', '2024-11-21 11:20:50', '2024-11-21 11:45:45');
INSERT INTO `version_digtalshie` VALUES ('30', '32', null, null, '1', '2024-11-21 11:42:15', '2024-11-21 11:45:45');
INSERT INTO `version_digtalshie` VALUES ('31', '33', null, null, '1', '2024-11-21 11:44:12', '2024-11-22 10:40:42');
INSERT INTO `version_digtalshie` VALUES ('32', '34', null, null, '1', '2024-11-21 11:45:53', '2024-11-22 10:40:42');
INSERT INTO `version_digtalshie` VALUES ('33', '35', null, null, '1', '2024-11-22 10:03:34', '2024-11-22 10:40:42');
INSERT INTO `version_digtalshie` VALUES ('34', '36', null, null, '1', '2024-11-22 10:04:05', '2024-11-22 10:40:42');
INSERT INTO `version_digtalshie` VALUES ('35', '37', null, null, '1', '2024-11-22 10:15:23', '2024-11-22 10:40:42');
INSERT INTO `version_digtalshie` VALUES ('36', '38', null, null, '2', '2024-11-22 10:22:47', '2024-11-22 10:22:47');
INSERT INTO `version_digtalshie` VALUES ('37', '39', null, null, '0', '2024-11-22 10:40:58', '2024-11-22 10:40:58');

-- ----------------------------
-- Table structure for version_digtalshie_firmware
-- ----------------------------
DROP TABLE IF EXISTS `version_digtalshie_firmware`;
CREATE TABLE `version_digtalshie_firmware` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `digtalshie_Id` int(11) DEFAULT NULL,
  `firmware_required` tinyint(1) DEFAULT NULL,
  `firmware_version` varchar(180) DEFAULT NULL,
  `firmware_url` varchar(380) DEFAULT NULL,
  `firmware_fingerprint` text,
  `release_state` int(2) DEFAULT NULL,
  `release_changelog` text,
  `set_time` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of version_digtalshie_firmware
-- ----------------------------
INSERT INTO `version_digtalshie_firmware` VALUES ('21', '21', '0', '1,0,0', 'https://静态资源/bin/v1.0.0/1.0.0-Release.zip', '', '1', '{\"en-US\":\"\",\"zh-CN\":\"版本说明\"}', '2024-11-21 10:59:04', '2024-11-21 11:08:46');
INSERT INTO `version_digtalshie_firmware` VALUES ('22', '22', '0', '1,0,0', 'https://静态资源/bin/v1.0.0/1.0.0-Release.zip', '', '1', '{\"en-US\":\"\",\"zh-CN\":\"版本说明\"}', '2024-11-21 10:59:13', '2024-11-21 11:08:46');
INSERT INTO `version_digtalshie_firmware` VALUES ('23', '23', '0', '1,0,0', 'https://静态资源/bin/v1.0.0/1.0.0-Release.zip', '', '1', '{\"en-US\":\"\",\"zh-CN\":\"版本说明\"}', '2024-11-21 10:59:23', '2024-11-21 11:13:00');
INSERT INTO `version_digtalshie_firmware` VALUES ('24', '24', '0', '1,0,0', 'https://静态资源/bin/v1.0.0/1.0.0-Release.zip', '', '1', '{\"en-US\":\"\",\"zh-CN\":\"版本说明\"}', '2024-11-21 11:09:07', '2024-11-21 11:13:40');
INSERT INTO `version_digtalshie_firmware` VALUES ('25', '25', '0', '1,0,0', 'https://静态资源/bin/v1.0.0/1.0.0-Release.zip', '', '1', '{\"en-US\":\"\",\"zh-CN\":\"版本说明\"}', '2024-11-21 11:13:25', '2024-11-21 11:14:31');
INSERT INTO `version_digtalshie_firmware` VALUES ('26', '26', '0', '1,0,0', 'https://静态资源/bin/v1.0.0/1.0.0-Release.zip', '', '1', '{\"en-US\":\"\",\"zh-CN\":\"版本说明\"}', '2024-11-21 11:14:21', '2024-11-21 11:17:27');
INSERT INTO `version_digtalshie_firmware` VALUES ('27', '27', '0', '1,0,0', 'https://静态资源/bin/v1.0.0/1.0.0-Release.zip', '', '1', '{\"en-US\":\"\",\"zh-CN\":\"版本说明\"}', '2024-11-21 11:17:13', '2024-11-21 11:20:06');
INSERT INTO `version_digtalshie_firmware` VALUES ('28', '28', '0', '1,0,0', 'https://静态资源/bin/v1.0.0/1.0.0-Release.zip', '', '1', '{\"en-US\":\"\",\"zh-CN\":\"版本说明\"}', '2024-11-21 11:19:58', '2024-11-21 11:45:45');
INSERT INTO `version_digtalshie_firmware` VALUES ('29', '29', '0', '1,0,0', 'https://静态资源/bin/v1.0.0/1.0.0-Release.zip', '', '1', '{\"en-US\":\"\",\"zh-CN\":\"版本说明\"}', '2024-11-21 11:20:50', '2024-11-21 11:45:45');
INSERT INTO `version_digtalshie_firmware` VALUES ('30', '30', '0', '1,0,0', 'https://静态资源/bin/v1.0.0/1.0.0-Release.zip', '', '1', '{\"en-US\":\"\",\"zh-CN\":\"版本说明\"}', '2024-11-21 11:42:15', '2024-11-21 11:45:45');
INSERT INTO `version_digtalshie_firmware` VALUES ('31', '31', '0', '1,0,0', 'https://静态资源/bin/v1.0.0/1.0.0-Release.zip', '', '1', '{\"en-US\":\"\",\"zh-CN\":\"版本说明\"}', '2024-11-21 11:44:12', '2024-11-22 10:40:42');
INSERT INTO `version_digtalshie_firmware` VALUES ('32', '32', '0', '1,0,0', 'https://静态资源/bin/v1.0.0/1.0.0-Release.zip', '', '1', '{\"en-US\":\"\",\"zh-CN\":\"版本说明\"}', '2024-11-21 11:45:53', '2024-11-22 10:40:42');
INSERT INTO `version_digtalshie_firmware` VALUES ('33', '33', '0', '1,0,0', 'https://静态资源/bin/v1.0.0/1.0.0-Release.zip', '', '1', '{\"en-US\":\"\",\"zh-CN\":\"版本说明\"}', '2024-11-22 10:03:34', '2024-11-22 10:40:42');
INSERT INTO `version_digtalshie_firmware` VALUES ('34', '34', '0', '1,0,0', 'https://静态资源/bin/v1.0.0/1.0.0-Release.zip', '', '1', '{\"en-US\":\"\",\"zh-CN\":\"版本说明\"}', '2024-11-22 10:04:05', '2024-11-22 10:40:42');
INSERT INTO `version_digtalshie_firmware` VALUES ('35', '35', '0', '1,0,0', 'https://静态资源/bin/v1.0.0/1.0.0-Release.zip', '', '1', '{\"en-US\":\"\",\"zh-CN\":\"版本说明\"}', '2024-11-22 10:15:23', '2024-11-22 10:40:42');
INSERT INTO `version_digtalshie_firmware` VALUES ('36', '36', '0', '1,0,0', 'https://静态资源/bin/v1.0.0/1.0.0-Release.zip', '', '2', '{\"en-US\":\"\",\"zh-CN\":\"版本说明\"}', '2024-11-22 10:22:47', '2024-11-22 10:22:47');
INSERT INTO `version_digtalshie_firmware` VALUES ('37', '37', '0', '1,0,0', 'https://静态资源/bin/v1.0.0/1.0.0-Release.zip', '', '0', '{\"en-US\":\"\",\"zh-CN\":\"版本说明\"}', '2024-11-22 10:40:58', '2024-11-22 10:40:58');

-- ----------------------------
-- Table structure for version_ios
-- ----------------------------
DROP TABLE IF EXISTS `version_ios`;
CREATE TABLE `version_ios` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `version_id` int(11) DEFAULT NULL,
  `ios_url` varchar(380) DEFAULT NULL,
  `ios_version` varchar(180) DEFAULT NULL,
  `release_state` int(2) DEFAULT NULL,
  `set_time` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of version_ios
-- ----------------------------
INSERT INTO `version_ios` VALUES ('23', '23', 'https://apps.apple.com/us/app/digtalshield-wallet/idXXXXXXX', '1,0,0', '1', '2024-11-21 10:59:04', '2024-11-21 10:59:19');
INSERT INTO `version_ios` VALUES ('24', '24', 'https://apps.apple.com/us/app/digtalshield-wallet/idXXXXXXX', '1,0,0', '1', '2024-11-21 10:59:13', '2024-11-21 11:08:57');
INSERT INTO `version_ios` VALUES ('25', '25', 'https://apps.apple.com/us/app/digtalshield-wallet/idXXXXXXX', '1,0,0', '1', '2024-11-21 10:59:23', '2024-11-21 11:13:20');
INSERT INTO `version_ios` VALUES ('26', '26', 'https://apps.apple.com/us/app/digtalshield-wallet/idXXXXXXX', '1,0,0', '1', '2024-11-21 11:09:07', '2024-11-21 11:14:15');
INSERT INTO `version_ios` VALUES ('27', '27', 'https://apps.apple.com/us/app/digtalshield-wallet/idXXXXXXX', '1,0,0', '1', '2024-11-21 11:13:25', '2024-11-21 11:17:20');
INSERT INTO `version_ios` VALUES ('28', '28', 'https://apps.apple.com/us/app/digtalshield-wallet/idXXXXXXX', '1,0,0', '1', '2024-11-21 11:14:21', '2024-11-21 11:17:20');
INSERT INTO `version_ios` VALUES ('29', '29', 'https://apps.apple.com/us/app/digtalshield-wallet/idXXXXXXX', '1,0,0', '1', '2024-11-21 11:17:13', '2024-11-21 11:20:47');
INSERT INTO `version_ios` VALUES ('30', '30', 'https://apps.apple.com/us/app/digtalshield-wallet/idXXXXXXX', '1,0,0', '1', '2024-11-21 11:19:58', '2024-11-21 11:45:41');
INSERT INTO `version_ios` VALUES ('31', '31', 'https://apps.apple.com/us/app/digtalshield-wallet/idXXXXXXX', '1,0,0', '1', '2024-11-21 11:20:50', '2024-11-21 11:45:41');
INSERT INTO `version_ios` VALUES ('32', '32', 'https://apps.apple.com/us/app/digtalshield-wallet/idXXXXXXX', '1,0,0', '1', '2024-11-21 11:42:15', '2024-11-21 11:45:41');
INSERT INTO `version_ios` VALUES ('33', '33', 'https://apps.apple.com/us/app/digtalshield-wallet/idXXXXXXX', '1,0,0', '1', '2024-11-21 11:44:12', '2024-11-22 10:40:35');
INSERT INTO `version_ios` VALUES ('34', '34', 'https://apps.apple.com/us/app/digtalshield-wallet/idXXXXXXX', '1,0,0', '1', '2024-11-21 11:45:53', '2024-11-22 10:40:35');
INSERT INTO `version_ios` VALUES ('35', '35', 'https://apps.apple.com/us/app/digtalshield-wallet/idXXXXXXX', '1,0,0', '1', '2024-11-22 10:03:34', '2024-11-22 10:40:35');
INSERT INTO `version_ios` VALUES ('36', '36', 'https://apps.apple.com/us/app/digtalshield-wallet/idXXXXXXX', '1,0,0', '1', '2024-11-22 10:04:05', '2024-11-22 10:40:35');
INSERT INTO `version_ios` VALUES ('37', '37', 'https://apps.apple.com/us/app/digtalshield-wallet/idXXXXXXX', '1,0,0', '1', '2024-11-22 10:15:23', '2024-11-22 10:40:35');
INSERT INTO `version_ios` VALUES ('38', '38', 'https://apps.apple.com/us/app/digtalshield-wallet/idXXXXXXX', '1,0,0', '2', '2024-11-22 10:22:47', '2024-11-22 10:40:35');
INSERT INTO `version_ios` VALUES ('39', '39', 'https://apps.apple.com/us/app/digtalshield-wallet/idXXXXXXX', '1,0,0', '0', '2024-11-22 10:40:58', '2024-11-22 10:40:58');

-- ----------------------------
-- Table structure for version_type
-- ----------------------------
DROP TABLE IF EXISTS `version_type`;
CREATE TABLE `version_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `version_id` int(11) DEFAULT NULL,
  `vice_id` int(11) DEFAULT NULL,
  `version_type_id` int(2) DEFAULT NULL,
  `version_type_name` varchar(120) DEFAULT NULL,
  `version_code` varchar(180) DEFAULT NULL,
  `set_time` datetime DEFAULT NULL,
  `gmt_modified` datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=146 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of version_type
-- ----------------------------
INSERT INTO `version_type` VALUES ('78', '23', '23', '1', 'IOS', '1,0,0', '2024-11-21 10:59:04', '2024-11-21 10:59:04');
INSERT INTO `version_type` VALUES ('79', '23', '21', '2', '安卓', '1,0,0', '2024-11-21 10:59:04', '2024-11-21 10:59:04');
INSERT INTO `version_type` VALUES ('80', '23', '21', '3', '谷歌', '1,0,0', '2024-11-21 10:59:04', '2024-11-21 10:59:04');
INSERT INTO `version_type` VALUES ('81', '23', '21', '4', '设备', '1,0,0', '2024-11-21 10:59:04', '2024-11-21 10:59:04');
INSERT INTO `version_type` VALUES ('82', '24', '24', '1', 'IOS', '1,0,0', '2024-11-21 10:59:13', '2024-11-21 10:59:13');
INSERT INTO `version_type` VALUES ('83', '24', '22', '2', '安卓', '1,0,0', '2024-11-21 10:59:13', '2024-11-21 10:59:13');
INSERT INTO `version_type` VALUES ('84', '24', '22', '3', '谷歌', '1,0,0', '2024-11-21 10:59:13', '2024-11-21 10:59:13');
INSERT INTO `version_type` VALUES ('85', '24', '22', '4', '设备', '1,0,0', '2024-11-21 10:59:13', '2024-11-21 10:59:13');
INSERT INTO `version_type` VALUES ('86', '25', '25', '1', 'IOS', '1,0,0', '2024-11-21 10:59:23', '2024-11-21 10:59:23');
INSERT INTO `version_type` VALUES ('87', '25', '23', '2', '安卓', '1,0,0', '2024-11-21 10:59:23', '2024-11-21 10:59:23');
INSERT INTO `version_type` VALUES ('88', '25', '23', '3', '谷歌', '1,0,0', '2024-11-21 10:59:23', '2024-11-21 10:59:23');
INSERT INTO `version_type` VALUES ('89', '25', '23', '4', '设备', '1,0,0', '2024-11-21 10:59:23', '2024-11-21 10:59:23');
INSERT INTO `version_type` VALUES ('90', '26', '26', '1', 'IOS', '1,0,0', '2024-11-21 11:09:07', '2024-11-21 11:09:07');
INSERT INTO `version_type` VALUES ('91', '26', '24', '2', '安卓', '1,0,0', '2024-11-21 11:09:07', '2024-11-21 11:09:07');
INSERT INTO `version_type` VALUES ('92', '26', '24', '3', '谷歌', '1,0,0', '2024-11-21 11:09:07', '2024-11-21 11:09:07');
INSERT INTO `version_type` VALUES ('93', '26', '24', '4', '设备', '1,0,0', '2024-11-21 11:09:07', '2024-11-21 11:09:07');
INSERT INTO `version_type` VALUES ('94', '27', '27', '1', 'IOS', '1,0,0', '2024-11-21 11:13:25', '2024-11-21 11:13:25');
INSERT INTO `version_type` VALUES ('95', '27', '25', '2', '安卓', '1,0,0', '2024-11-21 11:13:25', '2024-11-21 11:13:25');
INSERT INTO `version_type` VALUES ('96', '27', '25', '3', '谷歌', '1,0,0', '2024-11-21 11:13:25', '2024-11-21 11:13:25');
INSERT INTO `version_type` VALUES ('97', '27', '25', '4', '设备', '1,0,0', '2024-11-21 11:13:25', '2024-11-21 11:13:25');
INSERT INTO `version_type` VALUES ('98', '28', '28', '1', 'IOS', '1,0,0', '2024-11-21 11:14:21', '2024-11-21 11:14:21');
INSERT INTO `version_type` VALUES ('99', '28', '26', '2', '安卓', '1,0,0', '2024-11-21 11:14:21', '2024-11-21 11:14:21');
INSERT INTO `version_type` VALUES ('100', '28', '26', '3', '谷歌', '1,0,0', '2024-11-21 11:14:21', '2024-11-21 11:14:21');
INSERT INTO `version_type` VALUES ('101', '28', '26', '4', '设备', '1,0,0', '2024-11-21 11:14:21', '2024-11-21 11:14:21');
INSERT INTO `version_type` VALUES ('102', '29', '29', '1', 'IOS', '1,0,0', '2024-11-21 11:17:13', '2024-11-21 11:17:13');
INSERT INTO `version_type` VALUES ('103', '29', '27', '2', '安卓', '1,0,0', '2024-11-21 11:17:13', '2024-11-21 11:17:13');
INSERT INTO `version_type` VALUES ('104', '29', '27', '3', '谷歌', '1,0,0', '2024-11-21 11:17:13', '2024-11-21 11:17:13');
INSERT INTO `version_type` VALUES ('105', '29', '27', '4', '设备', '1,0,0', '2024-11-21 11:17:13', '2024-11-21 11:17:13');
INSERT INTO `version_type` VALUES ('106', '30', '30', '1', 'IOS', '1,0,0', '2024-11-21 11:19:58', '2024-11-21 11:19:58');
INSERT INTO `version_type` VALUES ('107', '30', '28', '2', '安卓', '1,0,0', '2024-11-21 11:19:58', '2024-11-21 11:19:58');
INSERT INTO `version_type` VALUES ('108', '30', '28', '3', '谷歌', '1,0,0', '2024-11-21 11:19:58', '2024-11-21 11:19:58');
INSERT INTO `version_type` VALUES ('109', '30', '28', '4', '设备', '1,0,0', '2024-11-21 11:19:58', '2024-11-21 11:19:58');
INSERT INTO `version_type` VALUES ('110', '31', '31', '1', 'IOS', '1,0,0', '2024-11-21 11:20:50', '2024-11-21 11:20:50');
INSERT INTO `version_type` VALUES ('111', '31', '29', '2', '安卓', '1,0,0', '2024-11-21 11:20:50', '2024-11-21 11:20:50');
INSERT INTO `version_type` VALUES ('112', '31', '29', '3', '谷歌', '1,0,0', '2024-11-21 11:20:50', '2024-11-21 11:20:50');
INSERT INTO `version_type` VALUES ('113', '31', '29', '4', '设备', '1,0,0', '2024-11-21 11:20:50', '2024-11-21 11:20:50');
INSERT INTO `version_type` VALUES ('114', '32', '32', '1', 'IOS', '1,0,0', '2024-11-21 11:42:15', '2024-11-21 11:42:15');
INSERT INTO `version_type` VALUES ('115', '32', '30', '2', '安卓', '1,0,0', '2024-11-21 11:42:15', '2024-11-21 11:42:15');
INSERT INTO `version_type` VALUES ('116', '32', '30', '3', '谷歌', '1,0,0', '2024-11-21 11:42:15', '2024-11-21 11:42:15');
INSERT INTO `version_type` VALUES ('117', '32', '30', '4', '设备', '1,0,0', '2024-11-21 11:42:15', '2024-11-21 11:42:15');
INSERT INTO `version_type` VALUES ('118', '33', '33', '1', 'IOS', '1,0,0', '2024-11-21 11:44:12', '2024-11-21 11:44:12');
INSERT INTO `version_type` VALUES ('119', '33', '31', '2', '安卓', '1,0,0', '2024-11-21 11:44:12', '2024-11-21 11:44:12');
INSERT INTO `version_type` VALUES ('120', '33', '31', '3', '谷歌', '1,0,0', '2024-11-21 11:44:12', '2024-11-21 11:44:12');
INSERT INTO `version_type` VALUES ('121', '33', '31', '4', '设备', '1,0,0', '2024-11-21 11:44:12', '2024-11-21 11:44:12');
INSERT INTO `version_type` VALUES ('122', '34', '34', '1', 'IOS', '1,0,0', '2024-11-21 11:45:53', '2024-11-21 11:45:53');
INSERT INTO `version_type` VALUES ('123', '34', '32', '2', '安卓', '1,0,0', '2024-11-21 11:45:53', '2024-11-21 11:45:53');
INSERT INTO `version_type` VALUES ('124', '34', '32', '3', '谷歌', '1,0,0', '2024-11-21 11:45:53', '2024-11-21 11:45:53');
INSERT INTO `version_type` VALUES ('125', '34', '32', '4', '设备', '1,0,0', '2024-11-21 11:45:53', '2024-11-21 11:45:53');
INSERT INTO `version_type` VALUES ('126', '35', '35', '1', 'IOS', '1,0,0', '2024-11-22 10:03:34', '2024-11-22 10:03:34');
INSERT INTO `version_type` VALUES ('127', '35', '33', '2', '安卓', '1,0,0', '2024-11-22 10:03:34', '2024-11-22 10:03:34');
INSERT INTO `version_type` VALUES ('128', '35', '33', '3', '谷歌', '1,0,0', '2024-11-22 10:03:34', '2024-11-22 10:03:34');
INSERT INTO `version_type` VALUES ('129', '35', '33', '4', '设备', '1,0,0', '2024-11-22 10:03:34', '2024-11-22 10:03:34');
INSERT INTO `version_type` VALUES ('130', '36', '36', '1', 'IOS', '1,0,0', '2024-11-22 10:04:05', '2024-11-22 10:04:05');
INSERT INTO `version_type` VALUES ('131', '36', '34', '2', '安卓', '1,0,0', '2024-11-22 10:04:05', '2024-11-22 10:04:05');
INSERT INTO `version_type` VALUES ('132', '36', '34', '3', '谷歌', '1,0,0', '2024-11-22 10:04:05', '2024-11-22 10:04:05');
INSERT INTO `version_type` VALUES ('133', '36', '34', '4', '设备', '1,0,0', '2024-11-22 10:04:05', '2024-11-22 10:04:05');
INSERT INTO `version_type` VALUES ('134', '37', '37', '1', 'IOS', '1,0,0', '2024-11-22 10:15:23', '2024-11-22 10:15:23');
INSERT INTO `version_type` VALUES ('135', '37', '35', '2', '安卓', '1,0,0', '2024-11-22 10:15:23', '2024-11-22 10:15:23');
INSERT INTO `version_type` VALUES ('136', '37', '35', '3', '谷歌', '1,0,0', '2024-11-22 10:15:23', '2024-11-22 10:15:23');
INSERT INTO `version_type` VALUES ('137', '37', '35', '4', '设备', '1,0,0', '2024-11-22 10:15:23', '2024-11-22 10:15:23');
INSERT INTO `version_type` VALUES ('138', '38', '38', '1', 'IOS', '1,0,0', '2024-11-22 10:22:47', '2024-11-22 10:22:47');
INSERT INTO `version_type` VALUES ('139', '38', '36', '2', '安卓', '1,0,0', '2024-11-22 10:22:47', '2024-11-22 10:22:47');
INSERT INTO `version_type` VALUES ('140', '38', '36', '3', '谷歌', '1,0,0', '2024-11-22 10:22:47', '2024-11-22 10:22:47');
INSERT INTO `version_type` VALUES ('141', '38', '36', '4', '设备', '1,0,0', '2024-11-22 10:22:47', '2024-11-22 10:22:47');
INSERT INTO `version_type` VALUES ('142', '39', '39', '1', 'IOS', '1,0,0', '2024-11-22 10:40:58', '2024-11-22 10:40:58');
INSERT INTO `version_type` VALUES ('143', '39', '37', '2', '安卓', '1,0,0', '2024-11-22 10:40:58', '2024-11-22 10:40:58');
INSERT INTO `version_type` VALUES ('144', '39', '37', '3', '谷歌', '1,0,0', '2024-11-22 10:40:58', '2024-11-22 10:40:58');
INSERT INTO `version_type` VALUES ('145', '39', '37', '4', '设备', '1,0,0', '2024-11-22 10:40:58', '2024-11-22 10:40:58');
