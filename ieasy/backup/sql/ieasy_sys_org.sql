/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50522
Source Host           : localhost:3306
Source Database       : ieasy

Target Server Type    : MYSQL
Target Server Version : 50522
File Encoding         : 65001

Date: 2014-10-16 00:03:15
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for ieasy_sys_org
-- ----------------------------
DROP TABLE IF EXISTS `ieasy_sys_org`;
CREATE TABLE `ieasy_sys_org` (
  `id` varchar(255) NOT NULL,
  `createName` varchar(255) DEFAULT NULL,
  `created` datetime DEFAULT NULL,
  `modifyDate` datetime DEFAULT NULL,
  `modifyName` varchar(255) DEFAULT NULL,
  `iconCls` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `sn` varchar(255) DEFAULT NULL,
  `sort` int(11) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `type` varchar(255) DEFAULT NULL,
  `pid` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_obysj7u933bwsbe44hbbuuxlg` (`pid`),
  CONSTRAINT `FK_obysj7u933bwsbe44hbbuuxlg` FOREIGN KEY (`pid`) REFERENCES `ieasy_sys_org` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of ieasy_sys_org
-- ----------------------------
INSERT INTO `ieasy_sys_org` VALUES ('402881e5491486ed01491487636600a0', '系统初始化', '2014-10-15 23:57:37', null, null, 'icon-standard-application-view-list', '广东华智科技有限公司', 'GDHZ', '1', 'open', 'O', null);
INSERT INTO `ieasy_sys_org` VALUES ('402881e5491486ed0149148763ba00a1', '系统初始化', '2014-10-15 23:57:37', null, null, 'icon-standard-application-view-list', '日本支社', 'RBZS', '10', 'open', 'D', '402881e5491486ed01491487636600a0');
INSERT INTO `ieasy_sys_org` VALUES ('402881e5491486ed0149148763fc00a2', '系统初始化', '2014-10-15 23:57:37', null, null, 'icon-standard-application-view-list', '总裁办', 'ZCB', '9', 'open', 'D', '402881e5491486ed01491487636600a0');
INSERT INTO `ieasy_sys_org` VALUES ('402881e5491486ed01491487643f00a3', '系统初始化', '2014-10-15 23:57:37', null, null, 'icon-standard-application-view-list', '品质保证部', 'PZB', '8', 'open', 'D', '402881e5491486ed01491487636600a0');
INSERT INTO `ieasy_sys_org` VALUES ('402881e5491486ed01491487648200a4', '系统初始化', '2014-10-15 23:57:37', null, null, 'icon-standard-application-view-list', '系统集成部', 'XTJCB', '7', 'open', 'D', '402881e5491486ed01491487636600a0');
INSERT INTO `ieasy_sys_org` VALUES ('402881e5491486ed0149148764d900a5', '系统初始化', '2014-10-15 23:57:37', null, null, 'icon-standard-application-view-list', '项目管理部', 'XMGLB', '6', 'open', 'D', '402881e5491486ed01491487636600a0');
INSERT INTO `ieasy_sys_org` VALUES ('402881e5491486ed01491487653200a6', '系统初始化', '2014-10-15 23:57:37', null, null, 'icon-standard-application-view-list', '北京支社', 'BJZS', '5', 'open', 'O', '402881e5491486ed01491487636600a0');
INSERT INTO `ieasy_sys_org` VALUES ('402881e5491486ed01491487657500a7', '系统初始化', '2014-10-15 23:57:38', null, null, 'icon-standard-application-view-list', '北京开发部', 'BJKFB', '3', 'open', 'D', '402881e5491486ed01491487653200a6');
INSERT INTO `ieasy_sys_org` VALUES ('402881e5491486ed0149148765c200a8', '系统初始化', '2014-10-15 23:57:38', null, null, 'icon-standard-application-view-list', '北京市场部', 'BJSCB', '2', 'open', 'D', '402881e5491486ed01491487653200a6');
INSERT INTO `ieasy_sys_org` VALUES ('402881e5491486ed01491487660b00a9', '系统初始化', '2014-10-15 23:57:38', null, null, 'icon-standard-application-view-list', '北京管理部', 'BJGLB', '1', 'open', 'D', '402881e5491486ed01491487653200a6');
INSERT INTO `ieasy_sys_org` VALUES ('402881e5491486ed01491487666100aa', '系统初始化', '2014-10-15 23:57:38', null, null, 'icon-standard-application-view-list', '苏州支社', 'SZZS', '4', 'open', 'O', '402881e5491486ed01491487636600a0');
INSERT INTO `ieasy_sys_org` VALUES ('402881e5491486ed0149148766aa00ab', '系统初始化', '2014-10-15 23:57:38', null, null, 'icon-standard-application-view-list', '苏州综合管理部', 'SZZHGLB', '4', 'open', 'D', '402881e5491486ed01491487666100aa');
INSERT INTO `ieasy_sys_org` VALUES ('402881e5491486ed01491487670500ac', '系统初始化', '2014-10-15 23:57:38', null, null, 'icon-standard-application-view-list', '苏州品质管理部', 'SZPZGLB', '3', 'open', 'D', '402881e5491486ed01491487666100aa');
INSERT INTO `ieasy_sys_org` VALUES ('402881e5491486ed01491487674700ad', '系统初始化', '2014-10-15 23:57:38', null, null, 'icon-standard-application-view-list', '苏州系统集成部', 'SZXTJCB', '2', 'open', 'D', '402881e5491486ed01491487666100aa');
INSERT INTO `ieasy_sys_org` VALUES ('402881e5491486ed01491487678b00ae', '系统初始化', '2014-10-15 23:57:38', null, null, 'icon-standard-application-view-list', '苏州开发部', 'SZKFB', '1', 'open', 'D', '402881e5491486ed01491487666100aa');
INSERT INTO `ieasy_sys_org` VALUES ('402881e5491486ed0149148767f700af', '系统初始化', '2014-10-15 23:57:38', null, null, 'icon-standard-application-view-list', '综合管理本部', 'ZHGLB', '3', 'open', 'D', '402881e5491486ed01491487636600a0');
INSERT INTO `ieasy_sys_org` VALUES ('402881e5491486ed01491487684800b0', '系统初始化', '2014-10-15 23:57:38', null, null, 'icon-standard-application-view-list', '人力资源部', 'RLZYB', '4', 'open', 'D', '402881e5491486ed0149148767f700af');
INSERT INTO `ieasy_sys_org` VALUES ('402881e5491486ed01491487688d00b1', '系统初始化', '2014-10-15 23:57:38', null, null, 'icon-standard-application-view-list', '总务部', 'ZWB', '3', 'open', 'D', '402881e5491486ed0149148767f700af');
INSERT INTO `ieasy_sys_org` VALUES ('402881e5491486ed0149148768f300b2', '系统初始化', '2014-10-15 23:57:38', null, null, 'icon-standard-application-view-list', '采购部', 'CGB', '2', 'open', 'D', '402881e5491486ed0149148767f700af');
INSERT INTO `ieasy_sys_org` VALUES ('402881e5491486ed01491487693400b3', '系统初始化', '2014-10-15 23:57:38', null, null, 'icon-standard-application-view-list', '财务部', 'CWB', '1', 'open', 'D', '402881e5491486ed0149148767f700af');
INSERT INTO `ieasy_sys_org` VALUES ('402881e5491486ed01491487697600b4', '系统初始化', '2014-10-15 23:57:39', null, null, 'icon-standard-application-view-list', '第一开发本部', 'DYKFBB', '2', 'open', 'D', '402881e5491486ed01491487636600a0');
INSERT INTO `ieasy_sys_org` VALUES ('402881e5491486ed0149148769d200b5', '系统初始化', '2014-10-15 23:57:39', null, null, 'icon-standard-application-view-list', '第一开发本部开发一部', 'DYKFBBKFYB', '3', 'open', 'D', '402881e5491486ed01491487697600b4');
INSERT INTO `ieasy_sys_org` VALUES ('402881e5491486ed014914876a1400b6', '系统初始化', '2014-10-15 23:57:39', null, null, 'icon-standard-application-view-list', '第一开发本部开发二部', 'DYKFBBKFEB', '2', 'open', 'D', '402881e5491486ed01491487697600b4');
INSERT INTO `ieasy_sys_org` VALUES ('402881e5491486ed014914876a6800b7', '系统初始化', '2014-10-15 23:57:39', null, null, 'icon-standard-application-view-list', '第一开发本部开发三部', 'DYKFBBKFSB', '1', 'open', 'D', '402881e5491486ed01491487697600b4');
INSERT INTO `ieasy_sys_org` VALUES ('402881e5491486ed014914876aab00b8', '系统初始化', '2014-10-15 23:57:39', null, null, 'icon-standard-application-view-list', '第二开发本部', 'DEKFBB', '1', 'open', 'D', '402881e5491486ed01491487636600a0');
INSERT INTO `ieasy_sys_org` VALUES ('402881e5491486ed014914876afe00b9', '系统初始化', '2014-10-15 23:57:39', null, null, 'icon-standard-application-view-list', '第二开发本部开发一部', 'DEKFBBKFYB', '3', 'open', 'D', '402881e5491486ed014914876aab00b8');
INSERT INTO `ieasy_sys_org` VALUES ('402881e5491486ed014914876b5a00ba', '系统初始化', '2014-10-15 23:57:39', null, null, 'icon-standard-application-view-list', '第二开发本部开发二部', 'DEKFBBKFEB', '2', 'open', 'D', '402881e5491486ed014914876aab00b8');
INSERT INTO `ieasy_sys_org` VALUES ('402881e5491486ed014914876b9c00bb', '系统初始化', '2014-10-15 23:57:39', null, null, 'icon-standard-application-view-list', '第二开发本部开发三部', 'DEKFBBKFSB', '1', 'open', 'D', '402881e5491486ed014914876aab00b8');
