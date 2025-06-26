/*
 Navicat MySQL Dump SQL

 Source Server         : 本机
 Source Server Type    : MySQL
 Source Server Version : 50744 (5.7.44)
 Source Host           : localhost:3306
 Source Schema         : bms_01

 Target Server Type    : MySQL
 Target Server Version : 50744 (5.7.44)
 File Encoding         : 65001

 Date: 26/06/2025 21:57:05
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for battery_signal
-- ----------------------------
DROP TABLE IF EXISTS `battery_signal`;
CREATE TABLE `battery_signal` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `vid` varchar(16) NOT NULL COMMENT '车辆识别码',
  `chassis_number` bigint(20) NOT NULL COMMENT '车架编号',
  `signal_data` json NOT NULL COMMENT '信号数据JSON',
  `report_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '上报时间',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_vid_time` (`vid`,`report_time`),
  KEY `idx_chassis_time` (`chassis_number`,`report_time`),
  KEY `idx_report_time` (`report_time`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COMMENT='电池信号表';

-- ----------------------------
-- Table structure for battery_signal_202506
-- ----------------------------
DROP TABLE IF EXISTS `battery_signal_202506`;
CREATE TABLE `battery_signal_202506` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `vid` varchar(16) NOT NULL COMMENT '车辆识别码',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0-未处理，1-已处理',
  `chassis_number` bigint(20) NOT NULL COMMENT '车架编号',
  `signal_data` json NOT NULL COMMENT '信号数据JSON',
  `report_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '上报时间',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_vid_time` (`vid`,`report_time`),
  KEY `idx_chassis_time` (`chassis_number`,`report_time`),
  KEY `idx_report_time` (`report_time`)
) ENGINE=InnoDB AUTO_INCREMENT=45 DEFAULT CHARSET=utf8mb4 COMMENT='电池信号表';

-- ----------------------------
-- Table structure for message_log
-- ----------------------------
DROP TABLE IF EXISTS `message_log`;
CREATE TABLE `message_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `transaction_id` varchar(64) COLLATE utf8mb4_unicode_ci NOT NULL,
  `chassis_number` bigint(20) DEFAULT NULL,
  `status` varchar(20) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `transaction_id` (`transaction_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ----------------------------
-- Table structure for vehicle_info
-- ----------------------------
DROP TABLE IF EXISTS `vehicle_info`;
CREATE TABLE `vehicle_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID/车架编号',
  `vid` varchar(16) NOT NULL COMMENT '车辆识别码',
  `battery_type` tinyint(1) NOT NULL COMMENT '电池类型：1-三元电池，2-铁锂电池',
  `total_mileage` decimal(10,2) DEFAULT '0.00' COMMENT '总里程(km)',
  `battery_health` tinyint(3) DEFAULT '100' COMMENT '电池健康状态(%)',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态：0-停用，1-启用',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_vid` (`vid`),
  KEY `idx_created_time` (`created_time`),
  KEY `idx_battery_type` (`id`,`battery_type`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='车辆信息表';

-- ----------------------------
-- Table structure for warning_record
-- ----------------------------
DROP TABLE IF EXISTS `warning_record`;
CREATE TABLE `warning_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `signal_id` bigint(20) NOT NULL COMMENT '信号编号',
  `vid` varchar(16) NOT NULL COMMENT '车辆识别码',
  `chassis_number` bigint(20) NOT NULL COMMENT '车架编号',
  `battery_type` tinyint(1) NOT NULL COMMENT '电池类型',
  `rule_number` varchar(20) NOT NULL COMMENT '规则编号',
  `rule_name` varchar(100) NOT NULL COMMENT '规则名称',
  `warning_level` tinyint(1) NOT NULL COMMENT '预警等级',
  `signal_data` json NOT NULL COMMENT '触发预警的信号数据',
  `warning_desc` text COMMENT '预警描述',
  `status` tinyint(1) DEFAULT '0' COMMENT '处理状态：0-未处理，1-已处理',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_id_signal_id_rule_number` (`vid`,`signal_id`,`rule_number`) USING BTREE,
  KEY `idx_vid_time` (`vid`,`created_time`),
  KEY `idx_chassis_time` (`chassis_number`,`created_time`),
  KEY `idx_warning_level` (`warning_level`),
  KEY `idx_created_time` (`created_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预警记录表';

-- ----------------------------
-- Table structure for warning_rule
-- ----------------------------
DROP TABLE IF EXISTS `warning_rule`;
CREATE TABLE `warning_rule` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID/序号',
  `rule_number` int(11) NOT NULL COMMENT '规则编号',
  `rule_name` varchar(100) NOT NULL COMMENT '规则名称',
  `battery_type` tinyint(1) NOT NULL COMMENT '电池类型：1-三元电池，2-铁锂电池',
  `signal_type` varchar(20) NOT NULL COMMENT '信号类型：voltage,current',
  `rule_config` json NOT NULL COMMENT '规则配置JSON',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_battery_type` (`battery_type`),
  KEY `idx_signal_type` (`signal_type`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='预警规则表';

SET FOREIGN_KEY_CHECKS = 1;
