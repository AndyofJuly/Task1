/*
Navicat MySQL Data Transfer

Source Server         : mysql1
Source Server Version : 50724
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50724
File Encoding         : 65001

Date: 2020-05-17 10:16:15
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for monster
-- ----------------------------
DROP TABLE IF EXISTS `monster`;
CREATE TABLE `monster` (
  `monsterid` int(100) NOT NULL,
  `monstername` varchar(20) DEFAULT NULL,
  `scenes` varchar(20) DEFAULT NULL,
  `alive` int(2) DEFAULT NULL,
  PRIMARY KEY (`monsterid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of monster
-- ----------------------------
INSERT INTO `monster` VALUES ('1', '史莱姆', '起始之地', '1');
INSERT INTO `monster` VALUES ('2', '飞鹰', '起始之地', '1');
INSERT INTO `monster` VALUES ('3', '小偷', '村子', '1');
INSERT INTO `monster` VALUES ('4', '恶人', '村子', '1');
INSERT INTO `monster` VALUES ('5', '逃兵', '城堡', '1');
INSERT INTO `monster` VALUES ('6', '叛军', '城堡', '1');
INSERT INTO `monster` VALUES ('7', '猛虎', '森林', '1');
INSERT INTO `monster` VALUES ('8', '毒蛇', '森林', '1');
INSERT INTO `monster` VALUES ('9', '恶龙', '未知世界', '1');
INSERT INTO `monster` VALUES ('10', '巨兽', '未知世界', '1');

-- ----------------------------
-- Table structure for npc
-- ----------------------------
DROP TABLE IF EXISTS `npc`;
CREATE TABLE `npc` (
  `npcid` int(100) NOT NULL AUTO_INCREMENT,
  `npcname` varchar(20) DEFAULT NULL,
  `scenes` varchar(20) DEFAULT NULL,
  `alive` int(2) DEFAULT NULL,
  PRIMARY KEY (`npcid`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of npc
-- ----------------------------
INSERT INTO `npc` VALUES ('1', '老爷爷', '起始之地', '1');
INSERT INTO `npc` VALUES ('2', '村长', '村子', '1');
INSERT INTO `npc` VALUES ('3', '铁匠', '村子', '1');
INSERT INTO `npc` VALUES ('4', '公主', '城堡', '1');
INSERT INTO `npc` VALUES ('5', '商人', '城堡', '1');
INSERT INTO `npc` VALUES ('6', '樵夫', '森林', '1');
INSERT INTO `npc` VALUES ('7', '猎户', '森林', '1');
INSERT INTO `npc` VALUES ('8', '守将', '未知世界', '1');

-- ----------------------------
-- Table structure for place
-- ----------------------------
DROP TABLE IF EXISTS `place`;
CREATE TABLE `place` (
  `placeid` int(20) NOT NULL AUTO_INCREMENT,
  `placename` varchar(20) NOT NULL,
  `xline` int(20) DEFAULT NULL,
  `yline` int(20) DEFAULT NULL,
  PRIMARY KEY (`placeid`),
  UNIQUE KEY `placename` (`placename`)
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of place
-- ----------------------------
INSERT INTO `place` VALUES ('1', '起始之地', '1', '1');
INSERT INTO `place` VALUES ('2', '村子', '1', '2');
INSERT INTO `place` VALUES ('3', '城堡', '1', '3');
INSERT INTO `place` VALUES ('4', '未知世界', '1', '4');
INSERT INTO `place` VALUES ('5', '森林', '2', '2');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `playid` int(10) unsigned zerofill NOT NULL AUTO_INCREMENT,
  `rolename` varchar(20) DEFAULT NULL,
  `placeid` int(20) DEFAULT NULL,
  `alive` int(2) DEFAULT NULL,
  PRIMARY KEY (`playid`),
  UNIQUE KEY `rolename` (`rolename`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('0000000001', 'andy', '5', '1');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) DEFAULT NULL,
  `password` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'zhaoyun', 'zhaoyun');
INSERT INTO `user` VALUES ('2', 'lvbu', 'lvbu');
INSERT INTO `user` VALUES ('3', 'kongming', 'kongming');
INSERT INTO `user` VALUES ('4', 'JJ', '15');
INSERT INTO `user` VALUES ('7', 'HE', '123456');
INSERT INTO `user` VALUES ('12', 'Abukuma', '123456');
INSERT INTO `user` VALUES ('16', 'hesdj', 'hsdfs');
INSERT INTO `user` VALUES ('17', 'skg', 'sdk');
INSERT INTO `user` VALUES ('18', 'ssss', 'sdddd');
INSERT INTO `user` VALUES ('19', 'beibei', '373737');
INSERT INTO `user` VALUES ('21', 'shuji', '222');
INSERT INTO `user` VALUES ('22', 'hah', '222');
INSERT INTO `user` VALUES ('23', 'tiger', '110');
INSERT INTO `user` VALUES ('24', 's', 'd');
INSERT INTO `user` VALUES ('25', 'm', 'm');
