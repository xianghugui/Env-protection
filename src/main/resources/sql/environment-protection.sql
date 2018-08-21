/*
Navicat MySQL Data Transfer

Source Server         : 118.31.250.119
Source Server Version : 50721
Source Host           : 118.31.250.119:3306
Source Database       : environment-protection

Target Server Type    : MYSQL
Target Server Version : 50721
File Encoding         : 65001

Date: 2018-08-21 09:13:36
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for parameter
-- ----------------------------
DROP TABLE IF EXISTS `parameter`;
CREATE TABLE `parameter` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `temperature` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '温度',
  `humidity` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '湿度',
  `HCHO` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '甲醛',
  `TVOC` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '总挥发性有机物',
  `CO_two` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '二氧化碳',
  `PM_two_point_five` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'PM2.5',
  `PM_one_point_zero` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'PM1.0',
  `illumination` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '光照',
  `windSpeed` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '风速',
  `windDirection` varchar(10) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT '风向',
  `deviceId` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `createTime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
