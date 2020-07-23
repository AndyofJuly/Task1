/*
Navicat MySQL Data Transfer

Source Server         : mysql1
Source Server Version : 50724
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50724
File Encoding         : 65001

Date: 2020-07-23 14:08:23
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for achievement
-- ----------------------------
DROP TABLE IF EXISTS `achievement`;
CREATE TABLE `achievement` (
  `playid` int(11) NOT NULL,
  `achievementlist` varchar(30) DEFAULT NULL,
  `killMonsterThief` int(11) DEFAULT NULL,
  `levelUp` int(11) DEFAULT NULL,
  `talkNpc` int(11) DEFAULT NULL,
  `getBestEquip` int(11) DEFAULT NULL,
  `passDungeons` int(11) DEFAULT NULL,
  `sumEquipLevel` int(11) DEFAULT NULL,
  `addFriend` int(11) DEFAULT NULL,
  `firstJoinTeam` int(11) DEFAULT NULL,
  `firstJoinUnion` int(11) DEFAULT NULL,
  `firstTrade` int(11) DEFAULT NULL,
  `firstPkSuccess` int(11) DEFAULT NULL,
  `sumMoney` int(11) DEFAULT NULL,
  `completeTask` int(11) DEFAULT NULL,
  `killMonsterWicked` int(11) DEFAULT NULL,
  `levelUpB` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of achievement
-- ----------------------------
INSERT INTO `achievement` VALUES ('1', '010000000000001', '0', '21', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '21');
INSERT INTO `achievement` VALUES ('16', '000000000000000', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `achievement` VALUES ('17', '000000000000000', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `achievement` VALUES ('37', '000000000000000', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0', '0');

-- ----------------------------
-- Table structure for bodyequipment
-- ----------------------------
DROP TABLE IF EXISTS `bodyequipment`;
CREATE TABLE `bodyequipment` (
  `playid` int(11) NOT NULL,
  `weapon` int(11) DEFAULT NULL,
  `circlet` int(11) DEFAULT NULL,
  `necklace` int(11) DEFAULT NULL,
  `body` int(11) DEFAULT NULL,
  `head` int(11) DEFAULT NULL,
  `foot` int(11) DEFAULT NULL,
  PRIMARY KEY (`playid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of bodyequipment
-- ----------------------------
INSERT INTO `bodyequipment` VALUES ('1', '33009', '0', '0', '0', '0', '0');
INSERT INTO `bodyequipment` VALUES ('16', '0', '0', '0', '0', '0', '0');
INSERT INTO `bodyequipment` VALUES ('17', '0', '0', '0', '0', '0', '0');

-- ----------------------------
-- Table structure for equipment
-- ----------------------------
DROP TABLE IF EXISTS `equipment`;
CREATE TABLE `equipment` (
  `equipid` int(11) NOT NULL,
  `staticid` int(11) NOT NULL,
  `dura` int(11) NOT NULL,
  PRIMARY KEY (`equipid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of equipment
-- ----------------------------
INSERT INTO `equipment` VALUES ('33009', '3002', '88');
INSERT INTO `equipment` VALUES ('33010', '3001', '80');
INSERT INTO `equipment` VALUES ('33011', '3005', '100');

-- ----------------------------
-- Table structure for package
-- ----------------------------
DROP TABLE IF EXISTS `package`;
CREATE TABLE `package` (
  `playid` int(11) NOT NULL,
  `goodsid` int(11) DEFAULT NULL,
  `num` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of package
-- ----------------------------
INSERT INTO `package` VALUES ('1', '33010', '1');
INSERT INTO `package` VALUES ('1', '2002', '7');

-- ----------------------------
-- Table structure for record
-- ----------------------------
DROP TABLE IF EXISTS `record`;
CREATE TABLE `record` (
  `playid` int(11) NOT NULL,
  `goodsid` int(11) DEFAULT NULL,
  `num` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of record
-- ----------------------------

-- ----------------------------
-- Table structure for recordid
-- ----------------------------
DROP TABLE IF EXISTS `recordid`;
CREATE TABLE `recordid` (
  `id` int(11) NOT NULL,
  `maxUnionid` int(11) DEFAULT NULL,
  `maxEquipid` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of recordid
-- ----------------------------
INSERT INTO `recordid` VALUES ('1', '40002', '33015');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `playid` int(100) NOT NULL AUTO_INCREMENT,
  `rolename` varchar(20) DEFAULT NULL,
  `placeid` int(20) DEFAULT NULL,
  `alive` int(2) DEFAULT NULL,
  `careerid` int(10) DEFAULT NULL,
  `hp` int(11) DEFAULT NULL,
  `mp` int(11) DEFAULT NULL,
  `atk` int(11) DEFAULT NULL,
  `money` int(11) DEFAULT NULL,
  `def` int(11) DEFAULT NULL,
  `unionid` int(11) DEFAULT NULL,
  `nowlevel` int(11) DEFAULT NULL,
  `userid` int(11) DEFAULT NULL,
  PRIMARY KEY (`playid`),
  UNIQUE KEY `rolename` (`rolename`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', 'andy', '10003', '1', '5001', '100', '50', '10', '264', '10', '40001', '21', '1');
INSERT INTO `role` VALUES ('16', 'kk', '10006', '1', '5004', '70', '60', '4', '500', '4', '0', '1', '2');
INSERT INTO `role` VALUES ('17', 'aa', '10006', '1', '5002', '70', '60', '3', '500', '6', '0', '1', '1');
INSERT INTO `role` VALUES ('36', 'qq', '10001', '1', '5003', '50', '70', '4', '500', '4', '0', '1', '19');
INSERT INTO `role` VALUES ('37', 'vv', '10006', '1', '5004', '70', '60', '4', '220', '4', '0', '1', '20');

-- ----------------------------
-- Table structure for unionmember
-- ----------------------------
DROP TABLE IF EXISTS `unionmember`;
CREATE TABLE `unionmember` (
  `unionid` int(11) DEFAULT NULL,
  `playid` int(11) DEFAULT NULL,
  `grade` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of unionmember
-- ----------------------------
INSERT INTO `unionmember` VALUES ('40001', '1', '1');

-- ----------------------------
-- Table structure for unions
-- ----------------------------
DROP TABLE IF EXISTS `unions`;
CREATE TABLE `unions` (
  `unionid` int(11) NOT NULL,
  `unionname` varchar(255) DEFAULT NULL,
  `money` int(11) DEFAULT NULL,
  PRIMARY KEY (`unionid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of unions
-- ----------------------------
INSERT INTO `unions` VALUES ('40001', '凌云会', '10');

-- ----------------------------
-- Table structure for unionstore
-- ----------------------------
DROP TABLE IF EXISTS `unionstore`;
CREATE TABLE `unionstore` (
  `unionid` int(11) NOT NULL,
  `goodsid` int(11) DEFAULT NULL,
  `num` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of unionstore
-- ----------------------------
INSERT INTO `unionstore` VALUES ('40001', '2002', '1');
INSERT INTO `unionstore` VALUES ('40001', '33011', '1');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `userid` int(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(20) DEFAULT NULL,
  `password` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`userid`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'm', 'm');
INSERT INTO `user` VALUES ('2', 't', 't');
INSERT INTO `user` VALUES ('19', 'e', 'e');
INSERT INTO `user` VALUES ('20', 'v', 'v');
