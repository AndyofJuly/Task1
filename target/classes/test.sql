/*
Navicat MySQL Data Transfer

Source Server         : mysql1
Source Server Version : 50724
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50724
File Encoding         : 65001

Date: 2020-07-02 21:53:24
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for achievement
-- ----------------------------
DROP TABLE IF EXISTS `achievement`;
CREATE TABLE `achievement` (
  `playid` int(11) NOT NULL,
  `achievementlist` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of achievement
-- ----------------------------
INSERT INTO `achievement` VALUES ('1', '0000000010010');
INSERT INTO `achievement` VALUES ('16', '0000000011010');
INSERT INTO `achievement` VALUES ('17', '0000000010010');

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
INSERT INTO `bodyequipment` VALUES ('1', '3001', '3003', '0', '0', '0', '0');

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
INSERT INTO `package` VALUES ('1', '2001', '5');
INSERT INTO `package` VALUES ('1', '2002', '1');
INSERT INTO `package` VALUES ('16', '2001', '2');
INSERT INTO `package` VALUES ('16', '2002', '1');
INSERT INTO `package` VALUES ('17', '2001', '1');

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
INSERT INTO `record` VALUES ('1', '2002', '1');
INSERT INTO `record` VALUES ('2', '2002', '0');
INSERT INTO `record` VALUES ('16', '2001', '0');
INSERT INTO `record` VALUES ('17', '2001', '0');
INSERT INTO `record` VALUES ('1', '2001', '2');
INSERT INTO `record` VALUES ('1', '2002', '1');
INSERT INTO `record` VALUES ('1', '2001', '1');
INSERT INTO `record` VALUES ('1', '2001', '2');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `playid` int(10) NOT NULL AUTO_INCREMENT,
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
  PRIMARY KEY (`playid`),
  UNIQUE KEY `rolename` (`rolename`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', 'andy', '10002', '1', '5001', '40', '50', '10', '161', '5', '0', '1');
INSERT INTO `role` VALUES ('16', 'kk', '10006', '1', '5003', '50', '70', '20', '66', '3', '0', '1');
INSERT INTO `role` VALUES ('17', 'aa', '10002', '1', '5001', '50', '50', '10', '71', '3', '0', '1');

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
INSERT INTO `unions` VALUES ('1', '逍遥派', '58');

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
INSERT INTO `unionstore` VALUES ('1', '2001', '2');

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
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
