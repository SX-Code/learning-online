/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 80100 (8.1.0)
 Source Host           : localhost:3306
 Source Schema         : lo_learning

 Target Server Type    : MySQL
 Target Server Version : 80100 (8.1.0)
 File Encoding         : 65001

 Date: 07/09/2023 14:33:16
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for xc_choose_course
-- ----------------------------
DROP TABLE IF EXISTS `xc_choose_course`;
CREATE TABLE `xc_choose_course` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `course_id` bigint NOT NULL COMMENT '课程id',
  `course_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '课程名称',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
  `company_id` bigint NOT NULL COMMENT '机构id',
  `order_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '选课类型',
  `create_date` datetime NOT NULL COMMENT '添加时间',
  `course_price` float(10,2) NOT NULL COMMENT '课程价格',
  `valid_days` int NOT NULL COMMENT '课程有效期(天)',
  `status` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '选课状态',
  `validtime_start` datetime NOT NULL COMMENT '开始服务时间',
  `validtime_end` datetime NOT NULL COMMENT '结束服务时间',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of xc_choose_course
-- ----------------------------
BEGIN;
INSERT INTO `xc_choose_course` (`id`, `course_id`, `course_name`, `user_id`, `company_id`, `order_type`, `create_date`, `course_price`, `valid_days`, `status`, `validtime_start`, `validtime_end`, `remarks`) VALUES (18, 18, 'java零基础入门v2.0', '52', 1232141425, '700001', '2023-09-03 20:37:31', 0.00, 365, '701001', '2023-09-03 20:37:31', '2024-09-02 20:37:31', NULL);
INSERT INTO `xc_choose_course` (`id`, `course_id`, `course_name`, `user_id`, `company_id`, `order_type`, `create_date`, `course_price`, `valid_days`, `status`, `validtime_start`, `validtime_end`, `remarks`) VALUES (19, 27, 'Javascript之VueJS', '52', 1232141425, '700002', '2023-09-03 21:11:47', 99.00, 365, '701001', '2023-09-03 21:11:47', '2024-09-02 21:11:47', NULL);
COMMIT;

-- ----------------------------
-- Table structure for xc_course_tables
-- ----------------------------
DROP TABLE IF EXISTS `xc_course_tables`;
CREATE TABLE `xc_course_tables` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `choose_course_id` bigint NOT NULL COMMENT '选课记录id',
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
  `course_id` bigint NOT NULL COMMENT '课程id',
  `company_id` bigint NOT NULL COMMENT '机构id',
  `course_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '课程名称',
  `course_type` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '课程类型',
  `create_date` datetime NOT NULL COMMENT '添加时间',
  `validtime_start` datetime DEFAULT NULL COMMENT '开始服务时间',
  `validtime_end` datetime NOT NULL COMMENT '到期时间',
  `update_date` datetime DEFAULT NULL COMMENT '更新时间',
  `remarks` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `course_tables_unique` (`user_id`,`course_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of xc_course_tables
-- ----------------------------
BEGIN;
INSERT INTO `xc_course_tables` (`id`, `choose_course_id`, `user_id`, `course_id`, `company_id`, `course_name`, `course_type`, `create_date`, `validtime_start`, `validtime_end`, `update_date`, `remarks`) VALUES (12, 18, '52', 18, 1232141425, 'java零基础入门v2.0', '700001', '2023-09-03 20:37:31', '2023-09-03 20:37:31', '2024-09-02 20:37:31', '2023-09-03 20:37:31', NULL);
INSERT INTO `xc_course_tables` (`id`, `choose_course_id`, `user_id`, `course_id`, `company_id`, `course_name`, `course_type`, `create_date`, `validtime_start`, `validtime_end`, `update_date`, `remarks`) VALUES (13, 19, '52', 27, 1232141425, 'Javascript之VueJS', '700002', '2023-09-03 21:11:47', '2023-09-03 21:11:47', '2024-09-02 21:11:47', '2023-09-05 14:05:11', NULL);
COMMIT;

-- ----------------------------
-- Table structure for xc_learn_record
-- ----------------------------
DROP TABLE IF EXISTS `xc_learn_record`;
CREATE TABLE `xc_learn_record` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `course_id` bigint NOT NULL COMMENT '课程id',
  `course_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '课程名称',
  `user_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户id',
  `learn_date` datetime DEFAULT NULL COMMENT '最近学习时间',
  `learn_length` bigint DEFAULT NULL COMMENT '学习时长',
  `teachplan_id` bigint DEFAULT NULL COMMENT '章节id',
  `teachplan_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '章节名称',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `learn_record_unique` (`course_id`,`user_id`,`teachplan_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of xc_learn_record
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
