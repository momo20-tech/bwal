/*
Navicat MySQL Data Transfer

Source Server         : nmg
Source Server Version : 50721
Source Host           : localhost:3306
Source Database       : nmgstu

Target Server Type    : MYSQL
Target Server Version : 50721
File Encoding         : 65001

Date: 2025-01-19 22:41:00
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `workuser`
-- ----------------------------
DROP TABLE IF EXISTS `workuser`;
CREATE TABLE `workuser` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(50) NOT NULL DEFAULT '123',
  `email` varchar(50) DEFAULT NULL,
  `phone` varchar(50) DEFAULT NULL,
  `nickname` varchar(50) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `createdtime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of workuser
-- ----------------------------
INSERT INTO `workuser` VALUES ('1', '王武', '1', '15326060267@163.com', '15326060267', '1', '黑龙江', '2025-01-19 20:31:51');
INSERT INTO `workuser` VALUES ('2', '2', '2', '15326060267@163.com', '15326060267', '2', '黑龙江', '2025-01-19 20:33:31');
INSERT INTO `workuser` VALUES ('3', '3', '3333', '15326060267@163.com', '15326060267', '3', '黑龙江', '2025-01-19 20:36:16');
INSERT INTO `workuser` VALUES ('4', '晓梅', '666', '22', '22', '美美美', '新疆', '2025-01-19 22:01:11');
INSERT INTO `workuser` VALUES ('6', '管理员', '123', '@1547', '15249685741', 'zhangyu', '北京', '2025-01-18 16:39:08');
INSERT INTO `workuser` VALUES ('7', '数据二', '123', '@154', '15249685741', 'zhangyu', '北京', '2025-01-18 16:39:08');
INSERT INTO `workuser` VALUES ('8', '数据3', '123', '@154', '15249685741', 'zhangyu', '北京', '2025-01-18 16:39:08');
INSERT INTO `workuser` VALUES ('10', '数据4', '123', '@154', '15249685741', 'zhangyu', '北京', '2025-01-18 16:39:08');
INSERT INTO `workuser` VALUES ('11', '数据5', '123', '@154', '15249685741', 'zhangyu', '北京', '2025-01-18 16:39:08');
INSERT INTO `workuser` VALUES ('12', '数据6', '123', '@154', '15249685741', 'zhangyu', '北京', '2025-01-18 16:39:08');
INSERT INTO `workuser` VALUES ('13', '数据7', '154', '52', '147', 'nnnnnn', '北京', '2025-01-18 16:39:08');
INSERT INTO `workuser` VALUES ('24', '数据11', '123', '15326060267@163.com', '15326060267', '刘小霄超级傻', '内蒙古', '2025-01-19 00:14:25');
