/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50518
Source Host           : localhost:3306
Source Database       : ieasy

Target Server Type    : MYSQL
Target Server Version : 50518
File Encoding         : 65001

Date: 2014-10-22 12:02:09
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for ieasy_sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `ieasy_sys_dict`;
CREATE TABLE `ieasy_sys_dict` (
  `id` varchar(255) NOT NULL,
  `createName` varchar(255) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `modifyDate` datetime DEFAULT NULL,
  `modifyName` varchar(255) DEFAULT NULL,
  `dictCode` varchar(255) DEFAULT NULL,
  `dictIndex` int(11) DEFAULT NULL,
  `dictName` varchar(255) DEFAULT NULL,
  `remark` varchar(255) DEFAULT NULL,
  `selected` varchar(255) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `pid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_78ymqqcvkya9lwh5bgno981ar` (`pid`),
  CONSTRAINT `FK_78ymqqcvkya9lwh5bgno981ar` FOREIGN KEY (`pid`) REFERENCES `ieasy_sys_dict` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ieasy_sys_dict
-- ----------------------------
INSERT INTO `ieasy_sys_dict` VALUES ('402881e549335206014933523dab00f7', '系统初始化', '2014-10-21 23:27:48', null, null, null, null, '人力资源', null, null, '1', 'open', 'SZX', null);
INSERT INTO `ieasy_sys_dict` VALUES ('402881e549335206014933523dcb00f8', '系统初始化', '2014-10-21 23:27:48', null, null, 'YGZT', null, '员工状态', null, null, '13', 'open', 'LX', '402881e549335206014933523dab00f7');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e549335206014933523de900f9', '系统初始化', '2014-10-21 23:27:48', null, null, null, '1', '退休', null, 'false', '3', 'open', 'LB', '402881e549335206014933523dcb00f8');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e549335206014933523e0100fa', '系统初始化', '2014-10-21 23:27:48', null, null, null, '2', '离职', null, 'false', '2', 'open', 'LB', '402881e549335206014933523dcb00f8');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e549335206014933523e2000fb', '系统初始化', '2014-10-21 23:27:48', null, null, null, '3', '在职', null, 'true', '1', 'open', 'LB', '402881e549335206014933523dcb00f8');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e549335206014933523e3d00fc', '系统初始化', '2014-10-21 23:27:48', null, null, 'YGLX', null, '员工类型', null, null, '12', 'open', 'LX', '402881e549335206014933523dab00f7');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e549335206014933523e5700fd', '系统初始化', '2014-10-21 23:27:48', null, null, null, '1', '试用期员工', null, 'false', '4', 'open', 'LB', '402881e549335206014933523e3d00fc');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e549335206014933523e7c00fe', '系统初始化', '2014-10-21 23:27:48', null, null, null, '2', '临时工', null, 'false', '3', 'open', 'LB', '402881e549335206014933523e3d00fc');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e549335206014933523ea000ff', '系统初始化', '2014-10-21 23:27:48', null, null, null, '3', '合同工', null, 'false', '2', 'open', 'LB', '402881e549335206014933523e3d00fc');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e549335206014933523eb90100', '系统初始化', '2014-10-21 23:27:48', null, null, null, '4', '正式工', null, 'true', '1', 'open', 'LB', '402881e549335206014933523e3d00fc');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e549335206014933523ed30101', '系统初始化', '2014-10-21 23:27:48', null, null, 'ZJLX', null, '证件类型', null, null, '11', 'open', 'LX', '402881e549335206014933523dab00f7');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e549335206014933523eeb0102', '系统初始化', '2014-10-21 23:27:48', null, null, null, '1', '身份证', null, 'true', '6', 'open', 'LB', '402881e549335206014933523ed30101');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e549335206014933523f070103', '系统初始化', '2014-10-21 23:27:48', null, null, null, '2', '技师证', null, 'false', '5', 'open', 'LB', '402881e549335206014933523ed30101');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e549335206014933523f260104', '系统初始化', '2014-10-21 23:27:48', null, null, null, '3', '毕业证', null, 'false', '4', 'open', 'LB', '402881e549335206014933523ed30101');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e549335206014933523f400105', '系统初始化', '2014-10-21 23:27:48', null, null, null, '4', '军官证', null, 'false', '3', 'open', 'LB', '402881e549335206014933523ed30101');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e549335206014933523f5a0106', '系统初始化', '2014-10-21 23:27:48', null, null, null, '5', '驾驶证', null, 'false', '2', 'open', 'LB', '402881e549335206014933523ed30101');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e549335206014933523f7e0107', '系统初始化', '2014-10-21 23:27:48', null, null, null, '6', '护照', null, 'false', '1', 'open', 'LB', '402881e549335206014933523ed30101');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e549335206014933523f9c0108', '系统初始化', '2014-10-21 23:27:48', null, null, 'XB', null, '性别', null, null, '10', 'open', 'LX', '402881e549335206014933523dab00f7');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e549335206014933523fb40109', '系统初始化', '2014-10-21 23:27:48', null, null, null, '1', '女', null, 'false', '2', 'open', 'LB', '402881e549335206014933523f9c0108');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e549335206014933523fd5010a', '系统初始化', '2014-10-21 23:27:48', null, null, null, '2', '男', null, 'true', '1', 'open', 'LB', '402881e549335206014933523f9c0108');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e549335206014933523fed010b', '系统初始化', '2014-10-21 23:27:48', null, null, 'HYZK', null, '婚姻状况', null, null, '9', 'open', 'LX', '402881e549335206014933523dab00f7');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e549335206014933524018010c', '系统初始化', '2014-10-21 23:27:48', null, null, null, '1', '离异', null, 'false', '3', 'open', 'LB', '402881e549335206014933523fed010b');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e549335206014933524030010d', '系统初始化', '2014-10-21 23:27:48', null, null, null, '2', '未婚', null, 'true', '2', 'open', 'LB', '402881e549335206014933523fed010b');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e54933520601493352404a010e', '系统初始化', '2014-10-21 23:27:48', null, null, null, '3', '已婚', null, 'false', '1', 'open', 'LB', '402881e549335206014933523fed010b');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e549335206014933524062010f', '系统初始化', '2014-10-21 23:27:48', null, null, 'JKZK', null, '健康状况', null, null, '8', 'open', 'LX', '402881e549335206014933523dab00f7');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e54933520601493352408f0110', '系统初始化', '2014-10-21 23:27:48', null, null, null, '1', '良好', null, 'false', '3', 'open', 'LB', '402881e549335206014933524062010f');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e5493352060149335240b00111', '系统初始化', '2014-10-21 23:27:48', null, null, null, '2', '一般', null, 'true', '2', 'open', 'LB', '402881e549335206014933524062010f');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e5493352060149335240d10112', '系统初始化', '2014-10-21 23:27:48', null, null, null, '3', '较差', null, 'false', '1', 'open', 'LB', '402881e549335206014933524062010f');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e5493352060149335240f30113', '系统初始化', '2014-10-21 23:27:48', null, null, 'ZZMM', null, '政治面貌', null, null, '7', 'open', 'LX', '402881e549335206014933523dab00f7');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e5493352060149335241140114', '系统初始化', '2014-10-21 23:27:49', null, null, null, '1', '其它党派', null, 'false', '4', 'open', 'LB', '402881e5493352060149335240f30113');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e54933520601493352412c0115', '系统初始化', '2014-10-21 23:27:49', null, null, null, '2', '共产党员', null, 'false', '3', 'open', 'LB', '402881e5493352060149335240f30113');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e54933520601493352414d0116', '系统初始化', '2014-10-21 23:27:49', null, null, null, '3', '团员', null, 'false', '2', 'open', 'LB', '402881e5493352060149335240f30113');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e5493352060149335241650117', '系统初始化', '2014-10-21 23:27:49', null, null, null, '4', '群众', null, 'true', '1', 'open', 'LB', '402881e5493352060149335240f30113');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e5493352060149335241860118', '系统初始化', '2014-10-21 23:27:49', null, null, 'HKXZ', null, '户口性质', null, null, '6', 'open', 'LX', '402881e549335206014933523dab00f7');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e54933520601493352419f0119', '系统初始化', '2014-10-21 23:27:49', null, null, null, '1', '农业', null, 'false', '2', 'open', 'LB', '402881e5493352060149335241860118');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e5493352060149335241c1011a', '系统初始化', '2014-10-21 23:27:49', null, null, null, '2', '非农', null, 'true', '1', 'open', 'LB', '402881e5493352060149335241860118');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e5493352060149335241da011b', '系统初始化', '2014-10-21 23:27:49', null, null, 'ZC', null, '职称', null, null, '5', 'open', 'LX', '402881e549335206014933523dab00f7');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e5493352060149335241f2011c', '系统初始化', '2014-10-21 23:27:49', null, null, null, '1', '高级讲师', null, 'false', '8', 'open', 'LB', '402881e5493352060149335241da011b');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e54933520601493352421b011d', '系统初始化', '2014-10-21 23:27:49', null, null, null, '2', '讲师', null, 'true', '7', 'open', 'LB', '402881e5493352060149335241da011b');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e549335206014933524236011e', '系统初始化', '2014-10-21 23:27:49', null, null, null, '3', '培训师', null, 'false', '6', 'open', 'LB', '402881e5493352060149335241da011b');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e549335206014933524253011f', '系统初始化', '2014-10-21 23:27:49', null, null, null, '4', '经济师', null, 'false', '5', 'open', 'LB', '402881e5493352060149335241da011b');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e5493352060149335242710120', '系统初始化', '2014-10-21 23:27:49', null, null, null, '5', '人力资源师', null, 'false', '4', 'open', 'LB', '402881e5493352060149335241da011b');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e5493352060149335242890121', '系统初始化', '2014-10-21 23:27:49', null, null, null, '6', '会计师', null, 'false', '3', 'open', 'LB', '402881e5493352060149335241da011b');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e5493352060149335242a60122', '系统初始化', '2014-10-21 23:27:49', null, null, null, '7', '高级工程师', null, 'false', '2', 'open', 'LB', '402881e5493352060149335241da011b');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e5493352060149335242e60123', '系统初始化', '2014-10-21 23:27:49', null, null, null, '8', '工程师', null, 'false', '1', 'open', 'LB', '402881e5493352060149335241da011b');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e54933520601493352430f0124', '系统初始化', '2014-10-21 23:27:49', null, null, 'XL', null, '学历', null, null, '4', 'open', 'LX', '402881e549335206014933523dab00f7');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e5493352060149335243270125', '系统初始化', '2014-10-21 23:27:49', null, null, null, '1', '博士后', null, 'false', '8', 'open', 'LB', '402881e54933520601493352430f0124');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e5493352060149335243400126', '系统初始化', '2014-10-21 23:27:49', null, null, null, '2', '博士', null, 'false', '7', 'open', 'LB', '402881e54933520601493352430f0124');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e5493352060149335243590127', '系统初始化', '2014-10-21 23:27:49', null, null, null, '3', '硕士', null, 'false', '6', 'open', 'LB', '402881e54933520601493352430f0124');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e5493352060149335243720128', '系统初始化', '2014-10-21 23:27:49', null, null, null, '4', '本科', null, 'true', '5', 'open', 'LB', '402881e54933520601493352430f0124');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e54933520601493352438b0129', '系统初始化', '2014-10-21 23:27:49', null, null, null, '5', '大专', null, 'false', '4', 'open', 'LB', '402881e54933520601493352430f0124');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e5493352060149335243a8012a', '系统初始化', '2014-10-21 23:27:49', null, null, null, '6', '高中', null, 'false', '3', 'open', 'LB', '402881e54933520601493352430f0124');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e5493352060149335243c8012b', '系统初始化', '2014-10-21 23:27:49', null, null, null, '7', '初中', null, 'false', '2', 'open', 'LB', '402881e54933520601493352430f0124');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e5493352060149335243f6012c', '系统初始化', '2014-10-21 23:27:49', null, null, null, '8', '小学', null, 'false', '1', 'open', 'LB', '402881e54933520601493352430f0124');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e549335206014933524413012d', '系统初始化', '2014-10-21 23:27:49', null, null, 'ZCDJ', null, '职称等级', null, null, '3', 'open', 'LX', '402881e549335206014933523dab00f7');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e549335206014933524434012e', '系统初始化', '2014-10-21 23:27:49', null, null, null, '1', '普通职员', null, 'true', '3', 'open', 'LB', '402881e549335206014933524413012d');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e549335206014933524456012f', '系统初始化', '2014-10-21 23:27:49', null, null, null, '2', '高级职员', null, 'false', '2', 'open', 'LB', '402881e549335206014933524413012d');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e54933520601493352448f0130', '系统初始化', '2014-10-21 23:27:49', null, null, null, '3', '核心员工', null, 'false', '1', 'open', 'LB', '402881e549335206014933524413012d');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e5493352060149335244b10131', '系统初始化', '2014-10-21 23:27:49', null, null, 'JDL-DBMTYPE', null, '稼动率-到部门类型', null, null, '2', 'open', 'LX', '402881e549335206014933523dab00f7');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e5493352060149335244d20132', '系统初始化', '2014-10-21 23:27:49', null, null, null, '1', '在职', null, 'false', '6', 'open', 'LB', '402881e5493352060149335244b10131');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e5493352060149335244f40133', '系统初始化', '2014-10-21 23:27:50', null, null, null, '2', '转入', null, 'false', '5', 'open', 'LB', '402881e5493352060149335244b10131');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e5493352060149335245150134', '系统初始化', '2014-10-21 23:27:50', null, null, null, '3', '新增', null, 'false', '4', 'open', 'LB', '402881e5493352060149335244b10131');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e54933520601493352453f0135', '系统初始化', '2014-10-21 23:27:50', null, null, null, '4', '试用', null, 'false', '3', 'open', 'LB', '402881e5493352060149335244b10131');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e5493352060149335245600136', '系统初始化', '2014-10-21 23:27:50', null, null, null, '5', '停薪留职返回', null, 'false', '2', 'open', 'LB', '402881e5493352060149335244b10131');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e5493352060149335245940137', '系统初始化', '2014-10-21 23:27:50', null, null, null, '6', '返聘', null, 'false', '1', 'open', 'LB', '402881e5493352060149335244b10131');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e5493352060149335245b40138', '系统初始化', '2014-10-21 23:27:50', null, null, 'JDL-LBMTYPE', null, '稼动率-离部门类型', null, null, '1', 'open', 'LX', '402881e549335206014933523dab00f7');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e5493352060149335245e60139', '系统初始化', '2014-10-21 23:27:50', null, null, null, '1', '转出-到开发部', null, 'false', '4', 'open', 'LB', '402881e5493352060149335245b40138');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e549335206014933524605013a', '系统初始化', '2014-10-21 23:27:50', null, null, null, '2', '转出-到非开发部', null, 'false', '3', 'open', 'LB', '402881e5493352060149335245b40138');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e54933520601493352461e013b', '系统初始化', '2014-10-21 23:27:50', null, null, null, '3', '离职', null, 'false', '2', 'open', 'LB', '402881e5493352060149335245b40138');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e549335206014933524637013c', '系统初始化', '2014-10-21 23:27:50', null, null, null, '4', '停薪留职', null, 'false', '1', 'open', 'LB', '402881e5493352060149335245b40138');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e54933520601493352464f013d', '系统初始化', '2014-10-21 23:27:50', null, null, null, null, '项目管理', null, null, '1', 'open', 'XMGL', null);
INSERT INTO `ieasy_sys_dict` VALUES ('402881e549335206014933524669013e', '系统初始化', '2014-10-21 23:27:50', null, null, 'KFRYJS', null, '开发人员角色', null, null, '10', 'open', 'LX', '402881e54933520601493352464f013d');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e54933520601493352468c013f', '系统初始化', '2014-10-21 23:27:50', null, null, null, '2', 'BSE', null, 'false', '9', 'open', 'LB', '402881e549335206014933524669013e');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e5493352060149335246ad0140', '系统初始化', '2014-10-21 23:27:50', null, null, null, '2', '通訳', null, 'false', '8', 'open', 'LB', '402881e549335206014933524669013e');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e5493352060149335246cc0141', '系统初始化', '2014-10-21 23:27:50', null, null, null, '1', 'PM', null, 'false', '7', 'open', 'LB', '402881e549335206014933524669013e');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e5493352060149335246e80142', '系统初始化', '2014-10-21 23:27:50', null, null, null, '2', 'PL', null, 'false', '6', 'open', 'LB', '402881e549335206014933524669013e');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e5493352060149335247090143', '系统初始化', '2014-10-21 23:27:50', null, null, null, '3', 'SE', null, 'false', '5', 'open', 'LB', '402881e549335206014933524669013e');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e54933520601493352472a0144', '系统初始化', '2014-10-21 23:27:50', null, null, null, '2', 'PG', null, 'false', '4', 'open', 'LB', '402881e549335206014933524669013e');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e5493352060149335247540145', '系统初始化', '2014-10-21 23:27:50', null, null, null, '2', '初级PG1', null, 'false', '3', 'open', 'LB', '402881e549335206014933524669013e');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e5493352060149335247860146', '系统初始化', '2014-10-21 23:27:50', null, null, null, '2', '初级PG2', null, 'false', '2', 'open', 'LB', '402881e549335206014933524669013e');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e5493352060149335247b70147', '系统初始化', '2014-10-21 23:27:50', null, null, null, '2', '初级PG3', null, 'false', '1', 'open', 'LB', '402881e549335206014933524669013e');
INSERT INTO `ieasy_sys_dict` VALUES ('402881e5493352060149335247d90148', '系统初始化', '2014-10-21 23:27:50', null, null, null, null, '系统管理', null, null, '1', 'open', 'SZX', null);
INSERT INTO `ieasy_sys_dict` VALUES ('40288483493578880149358b478e0001', null, '2014-10-22 09:49:20', '2014-10-22 11:57:25', '苏佩钰', 'XM-LX', null, '项目类型', '', null, '9', 'open', 'LX', '402881e54933520601493352464f013d');
INSERT INTO `ieasy_sys_dict` VALUES ('40288483493578880149358d2e7a0002', null, '2014-10-22 09:51:25', null, '苏佩钰', null, null, '案件', '', 'false', '6', null, 'LB', '40288483493578880149358b478e0001');
INSERT INTO `ieasy_sys_dict` VALUES ('4028848349358e60014935916e630001', null, '2014-10-22 09:56:03', null, '苏佩钰', null, null, '项目委托', '', 'false', '5', null, 'LB', '40288483493578880149358b478e0001');
INSERT INTO `ieasy_sys_dict` VALUES ('4028848349358e600149359192f50002', null, '2014-10-22 09:56:13', null, '苏佩钰', null, null, '人员派遣', '', 'false', '4', null, 'LB', '40288483493578880149358b478e0001');
INSERT INTO `ieasy_sys_dict` VALUES ('4028848349358e6001493591d3360003', null, '2014-10-22 09:56:29', null, '苏佩钰', null, null, '保守', '', 'false', '3', null, 'LB', '40288483493578880149358b478e0001');
INSERT INTO `ieasy_sys_dict` VALUES ('4028848349358e600149359201cf0004', null, '2014-10-22 09:56:41', null, '苏佩钰', null, null, '软硬件采购', '', 'false', '2', null, 'LB', '40288483493578880149358b478e0001');
INSERT INTO `ieasy_sys_dict` VALUES ('4028848349358e60014935925beb0005', null, '2014-10-22 09:57:04', null, '苏佩钰', null, null, '公司内部业务', '', 'false', '1', null, 'LB', '40288483493578880149358b478e0001');
INSERT INTO `ieasy_sys_dict` VALUES ('4028848349358e600149359753960006', null, '2014-10-22 10:02:30', null, '苏佩钰', 'XM-PSZT', null, '项目合同评审状态', '', null, '8', '', 'LX', '402881e54933520601493352464f013d');
INSERT INTO `ieasy_sys_dict` VALUES ('4028848349358e600149359883100007', null, '2014-10-22 10:03:47', '2014-10-22 10:05:57', '苏佩钰', null, null, '未评审', '', 'false', '2', null, 'LB', '4028848349358e600149359753960006');
INSERT INTO `ieasy_sys_dict` VALUES ('4028848349358e6001493598dff80008', null, '2014-10-22 10:04:11', null, '苏佩钰', null, null, '已评审', '', 'false', '1', null, 'LB', '4028848349358e600149359753960006');
INSERT INTO `ieasy_sys_dict` VALUES ('4028848349358e600149359b72b10009', null, '2014-10-22 10:07:00', null, '苏佩钰', 'XM-SZZT', null, '受注状态', '', null, '7', '', 'LX', '402881e54933520601493352464f013d');
INSERT INTO `ieasy_sys_dict` VALUES ('4028848349358e600149359c0858000a', null, '2014-10-22 10:07:38', null, '苏佩钰', null, null, '已受注', '', 'false', '2', null, 'LB', '4028848349358e600149359b72b10009');
INSERT INTO `ieasy_sys_dict` VALUES ('4028848349358e600149359c24ae000b', null, '2014-10-22 10:07:45', null, '苏佩钰', null, null, '未受注', '', 'false', '1', null, 'LB', '4028848349358e600149359b72b10009');
INSERT INTO `ieasy_sys_dict` VALUES ('4028848349358e600149359d49d7000c', null, '2014-10-22 10:09:00', null, '苏佩钰', 'XM-JXZT', null, '结项状态', '', null, '6', '', 'LX', '402881e54933520601493352464f013d');
INSERT INTO `ieasy_sys_dict` VALUES ('4028848349358e600149359d93f6000d', null, '2014-10-22 10:09:19', null, '苏佩钰', null, null, '已结项', '', 'false', '2', null, 'LB', '4028848349358e600149359d49d7000c');
INSERT INTO `ieasy_sys_dict` VALUES ('4028848349358e600149359da84f000e', null, '2014-10-22 10:09:25', null, '苏佩钰', null, null, '未结项', '', 'false', '1', null, 'LB', '4028848349358e600149359d49d7000c');
INSERT INTO `ieasy_sys_dict` VALUES ('4028848349358e600149359e3341000f', null, '2014-10-22 10:10:00', null, '苏佩钰', 'XM-JSZT', null, '结算状态', '', null, '5', '', 'LX', '402881e54933520601493352464f013d');
INSERT INTO `ieasy_sys_dict` VALUES ('4028848349358e600149359e698f0010', null, '2014-10-22 10:10:14', null, '苏佩钰', null, null, '已结算', '', 'false', '2', null, 'LB', '4028848349358e600149359e3341000f');
INSERT INTO `ieasy_sys_dict` VALUES ('4028848349358e600149359e7dd10011', null, '2014-10-22 10:10:19', null, '苏佩钰', null, null, '未结算', '', 'false', '1', null, 'LB', '4028848349358e600149359e3341000f');
INSERT INTO `ieasy_sys_dict` VALUES ('4028848349358e60014935a3cd1f0012', null, '2014-10-22 10:16:07', null, '苏佩钰', 'XM-GJJB', null, '跟进级别', '', null, '4', '', 'LX', '402881e54933520601493352464f013d');
INSERT INTO `ieasy_sys_dict` VALUES ('4028848349358e60014935a4091e0013', null, '2014-10-22 10:16:23', null, '苏佩钰', null, null, 'A', '', 'false', '4', null, 'LB', '4028848349358e60014935a3cd1f0012');
INSERT INTO `ieasy_sys_dict` VALUES ('4028848349358e60014935a4196b0014', null, '2014-10-22 10:16:27', null, '苏佩钰', null, null, 'B', '', 'false', '3', null, 'LB', '4028848349358e60014935a3cd1f0012');
INSERT INTO `ieasy_sys_dict` VALUES ('4028848349358e60014935a4282d0015', null, '2014-10-22 10:16:31', null, '苏佩钰', null, null, 'C', '', 'false', '2', null, 'LB', '4028848349358e60014935a3cd1f0012');
INSERT INTO `ieasy_sys_dict` VALUES ('4028848349358e60014935a437000016', null, '2014-10-22 10:16:34', null, '苏佩钰', null, null, 'D', '', 'false', '1', null, 'LB', '4028848349358e60014935a3cd1f0012');
INSERT INTO `ieasy_sys_dict` VALUES ('402884834935df2d014935fc4eab0001', null, '2014-10-22 11:52:48', null, '苏佩钰', 'XM-LXQF', null, '类型区分', '', null, '3', '', 'LX', '402881e54933520601493352464f013d');
INSERT INTO `ieasy_sys_dict` VALUES ('402884834935df2d014935fc827e0002', null, '2014-10-22 11:53:01', '2014-10-22 12:00:42', '苏佩钰', null, null, '项目', '', 'false', '2', null, 'LB', '402884834935df2d014935fc4eab0001');
INSERT INTO `ieasy_sys_dict` VALUES ('402884834935df2d014935fc98d60003', null, '2014-10-22 11:53:07', '2014-10-22 12:00:33', '苏佩钰', null, null, '提案', '', 'false', '1', null, 'LB', '402884834935df2d014935fc4eab0001');
