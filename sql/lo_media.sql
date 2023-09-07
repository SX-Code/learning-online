/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 80100 (8.1.0)
 Source Host           : localhost:3306
 Source Schema         : lo_media

 Target Server Type    : MySQL
 Target Server Version : 80100 (8.1.0)
 File Encoding         : 65001

 Date: 07/09/2023 14:33:22
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for media_files
-- ----------------------------
DROP TABLE IF EXISTS `media_files`;
CREATE TABLE `media_files` (
  `id` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '文件id,md5值',
  `company_id` bigint DEFAULT NULL COMMENT '机构ID',
  `company_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '机构名称',
  `filename` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '文件名称',
  `file_type` varchar(12) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '文件类型（图片、文档，视频）',
  `tags` varchar(120) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '标签',
  `bucket` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '存储目录',
  `file_path` varchar(512) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '存储路径',
  `file_id` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '文件id',
  `url` varchar(1024) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '媒资文件访问地址',
  `username` varchar(60) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '上传人',
  `create_date` datetime DEFAULT NULL COMMENT '上传时间',
  `change_date` datetime DEFAULT NULL COMMENT '修改时间',
  `status` varchar(12) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT '1' COMMENT '状态,1:正常，0:不展示',
  `remark` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '备注',
  `audit_status` varchar(12) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '审核状态',
  `audit_mind` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '审核意见',
  `file_size` bigint DEFAULT NULL COMMENT '文件大小',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unique_fileid` (`file_id`) USING BTREE COMMENT '文件id唯一索引 '
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC COMMENT='媒资信息';

-- ----------------------------
-- Records of media_files
-- ----------------------------
BEGIN;
INSERT INTO `media_files` (`id`, `company_id`, `company_name`, `filename`, `file_type`, `tags`, `bucket`, `file_path`, `file_id`, `url`, `username`, `create_date`, `change_date`, `status`, `remark`, `audit_status`, `audit_mind`, `file_size`) VALUES ('1b55dc35a07dd66e97afc94f3056eb9d', 1232141425, NULL, '27course_template.html', '001003', NULL, 'mediafiles', 'course/27.html', '1b55dc35a07dd66e97afc94f3056eb9d', '/mediafiles/course/27.html', NULL, '2023-08-31 15:28:41', NULL, '1', NULL, '002003', NULL, 34916);
INSERT INTO `media_files` (`id`, `company_id`, `company_name`, `filename`, `file_type`, `tags`, `bucket`, `file_path`, `file_id`, `url`, `username`, `create_date`, `change_date`, `status`, `remark`, `audit_status`, `audit_mind`, `file_size`) VALUES ('1c191e165b4d4112df96cb28f4bb7103', 1232141425, NULL, '2course_template.html', '001003', NULL, 'mediafiles', 'course/2.html', '1c191e165b4d4112df96cb28f4bb7103', '/mediafiles/course/2.html', NULL, '2023-08-29 20:41:15', NULL, '1', NULL, '002003', NULL, 34436);
INSERT INTO `media_files` (`id`, `company_id`, `company_name`, `filename`, `file_type`, `tags`, `bucket`, `file_path`, `file_id`, `url`, `username`, `create_date`, `change_date`, `status`, `remark`, `audit_status`, `audit_mind`, `file_size`) VALUES ('5068e1e23619d41eb7e1c637537e51be', 1232141425, NULL, 'sparkle_your_name_am720p.mp4', '001002', '视频文件', 'video', '5/0/5068e1e23619d41eb7e1c637537e51be/5068e1e23619d41eb7e1c637537e51be.mp4', '5068e1e23619d41eb7e1c637537e51be', '/video/5/0/5068e1e23619d41eb7e1c637537e51be/5068e1e23619d41eb7e1c637537e51be.mp4', NULL, '2023-08-24 20:15:56', NULL, '1', NULL, '002003', NULL, 56516103);
INSERT INTO `media_files` (`id`, `company_id`, `company_name`, `filename`, `file_type`, `tags`, `bucket`, `file_path`, `file_id`, `url`, `username`, `create_date`, `change_date`, `status`, `remark`, `audit_status`, `audit_mind`, `file_size`) VALUES ('8fd2ef2f38e1b55b6cd12fc41c6ab05e', 1232141425, NULL, 'vue.png', '001001', NULL, 'mediafiles', '2023/08/31/8fd2ef2f38e1b55b6cd12fc41c6ab05e.png', '8fd2ef2f38e1b55b6cd12fc41c6ab05e', '/mediafiles/2023/08/31/8fd2ef2f38e1b55b6cd12fc41c6ab05e.png', NULL, '2023-08-31 14:55:52', NULL, '1', NULL, '002003', NULL, 27142);
INSERT INTO `media_files` (`id`, `company_id`, `company_name`, `filename`, `file_type`, `tags`, `bucket`, `file_path`, `file_id`, `url`, `username`, `create_date`, `change_date`, `status`, `remark`, `audit_status`, `audit_mind`, `file_size`) VALUES ('d4641d09375068faeeb0f64d256afdd2', 1232141425, NULL, '18course_template.html', '001003', NULL, 'mediafiles', 'course/18.html', 'd4641d09375068faeeb0f64d256afdd2', '/mediafiles/course/18.html', NULL, '2023-09-03 20:06:31', NULL, '1', NULL, '002003', NULL, 34435);
INSERT INTO `media_files` (`id`, `company_id`, `company_name`, `filename`, `file_type`, `tags`, `bucket`, `file_path`, `file_id`, `url`, `username`, `create_date`, `change_date`, `status`, `remark`, `audit_status`, `audit_mind`, `file_size`) VALUES ('e1d2f7f1433a5e671219ad5b39178b0c', 1232141425, NULL, '25course_template.html', '001003', NULL, 'mediafiles', 'course/25.html', 'e1d2f7f1433a5e671219ad5b39178b0c', '/mediafiles/course/25.html', NULL, '2023-08-31 15:30:40', NULL, '1', NULL, '002003', NULL, 36587);
COMMIT;

-- ----------------------------
-- Table structure for media_process
-- ----------------------------
DROP TABLE IF EXISTS `media_process`;
CREATE TABLE `media_process` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `file_id` varchar(120) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '文件标识',
  `filename` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '文件名称',
  `bucket` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '存储桶',
  `file_path` varchar(512) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '存储路径',
  `status` varchar(12) NOT NULL COMMENT '状态,1:未处理，2：处理成功  3处理失败 4处理中',
  `create_date` datetime NOT NULL COMMENT '上传时间',
  `finish_date` datetime DEFAULT NULL COMMENT '完成时间',
  `fail_count` int DEFAULT '0' COMMENT '失败次数',
  `url` varchar(1024) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '媒资文件访问地址',
  `errormsg` varchar(1024) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '失败原因',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unique_fileid` (`file_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of media_process
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for media_process_history
-- ----------------------------
DROP TABLE IF EXISTS `media_process_history`;
CREATE TABLE `media_process_history` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `file_id` varchar(120) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '文件标识',
  `filename` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '文件名称',
  `bucket` varchar(128) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '存储源',
  `status` varchar(12) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '状态,1:未处理，2：处理成功  3处理失败',
  `create_date` datetime NOT NULL COMMENT '上传时间',
  `finish_date` datetime NOT NULL COMMENT '完成时间',
  `url` varchar(1024) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '媒资文件访问地址',
  `fail_count` int DEFAULT '0' COMMENT '失败次数',
  `file_path` varchar(512) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '文件路径',
  `errormsg` varchar(1024) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '失败原因',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of media_process_history
-- ----------------------------
BEGIN;
INSERT INTO `media_process_history` (`id`, `file_id`, `filename`, `bucket`, `status`, `create_date`, `finish_date`, `url`, `fail_count`, `file_path`, `errormsg`) VALUES (1, '81d7ed5153316f5774885d3b4c07d8bc', 'Spring Security快速上手-创建工程.avi', 'video', '2', '2022-12-15 09:41:50', '2022-12-15 10:30:26', '/video/8/1/81d7ed5153316f5774885d3b4c07d8bc/81d7ed5153316f5774885d3b4c07d8bc.mp4', 0, '8/1/81d7ed5153316f5774885d3b4c07d8bc/81d7ed5153316f5774885d3b4c07d8bc.avi', NULL);
INSERT INTO `media_process_history` (`id`, `file_id`, `filename`, `bucket`, `status`, `create_date`, `finish_date`, `url`, `fail_count`, `file_path`, `errormsg`) VALUES (2, '18f919e23bedab97a78762c4875addc4', '垂直分库-插入和查询测试.avi', 'video', '2', '2022-12-15 09:45:18', '2022-12-15 10:30:11', '/video/1/8/18f919e23bedab97a78762c4875addc4/18f919e23bedab97a78762c4875addc4.mp4', 0, '1/8/18f919e23bedab97a78762c4875addc4/18f919e23bedab97a78762c4875addc4.avi', NULL);
INSERT INTO `media_process_history` (`id`, `file_id`, `filename`, `bucket`, `status`, `create_date`, `finish_date`, `url`, `fail_count`, `file_path`, `errormsg`) VALUES (3, 'efd2eacc4485946fc27feb0caef7506c', '读写分离-理解读写分离.avi', 'video', '2', '2022-12-15 09:45:19', '2022-12-15 10:31:04', '/video/e/f/efd2eacc4485946fc27feb0caef7506c/efd2eacc4485946fc27feb0caef7506c.mp4', 0, 'e/f/efd2eacc4485946fc27feb0caef7506c/efd2eacc4485946fc27feb0caef7506c.avi', NULL);
COMMIT;

-- ----------------------------
-- Table structure for mq_message
-- ----------------------------
DROP TABLE IF EXISTS `mq_message`;
CREATE TABLE `mq_message` (
  `id` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '消息id',
  `message_type` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '消息类型代码',
  `business_key1` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '关联业务信息',
  `business_key2` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '关联业务信息',
  `business_key3` varchar(512) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '关联业务信息',
  `mq_host` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '消息队列主机',
  `mq_port` int NOT NULL COMMENT '消息队列端口',
  `mq_virtualhost` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '消息队列虚拟主机',
  `mq_queue` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '队列名称',
  `inform_num` int unsigned NOT NULL COMMENT '通知次数',
  `state` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '处理状态，0:初始，1:成功',
  `returnfailure_date` datetime DEFAULT NULL COMMENT '回复失败时间',
  `returnsuccess_date` datetime DEFAULT NULL COMMENT '回复成功时间',
  `returnfailure_msg` varchar(2048) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '回复失败内容',
  `inform_date` datetime DEFAULT NULL COMMENT '最近通知时间',
  `stage_state1` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '阶段1处理状态, 0:初始，1:成功',
  `stage_state2` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '阶段2处理状态, 0:初始，1:成功',
  `stage_state3` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '阶段3处理状态, 0:初始，1:成功',
  `stage_state4` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '阶段4处理状态, 0:初始，1:成功',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of mq_message
-- ----------------------------
BEGIN;
INSERT INTO `mq_message` (`id`, `message_type`, `business_key1`, `business_key2`, `business_key3`, `mq_host`, `mq_port`, `mq_virtualhost`, `mq_queue`, `inform_num`, `state`, `returnfailure_date`, `returnsuccess_date`, `returnfailure_msg`, `inform_date`, `stage_state1`, `stage_state2`, `stage_state3`, `stage_state4`) VALUES ('f29a3149-7429-40be-8a4e-9909f32003b0', 'xc.mq.msgsync.coursepub', '111', NULL, NULL, '127.0.0.1', 5607, '/', 'xc.course.publish.queue', 0, '0', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
COMMIT;

-- ----------------------------
-- Table structure for mq_message_history
-- ----------------------------
DROP TABLE IF EXISTS `mq_message_history`;
CREATE TABLE `mq_message_history` (
  `id` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '消息id',
  `message_type` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '消息类型代码',
  `business_key1` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '关联业务信息',
  `business_key2` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '关联业务信息',
  `business_key3` varchar(512) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '关联业务信息',
  `mq_host` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '消息队列主机',
  `mq_port` int NOT NULL COMMENT '消息队列端口',
  `mq_virtualhost` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '消息队列虚拟主机',
  `mq_queue` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '队列名称',
  `inform_num` int(10) unsigned zerofill DEFAULT NULL COMMENT '通知次数',
  `state` int(10) unsigned zerofill DEFAULT NULL COMMENT '处理状态，0:初始，1:成功，2:失败',
  `returnfailure_date` datetime DEFAULT NULL COMMENT '回复失败时间',
  `returnsuccess_date` datetime DEFAULT NULL COMMENT '回复成功时间',
  `returnfailure_msg` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '回复失败内容',
  `inform_date` datetime DEFAULT NULL COMMENT '最近通知时间',
  `stage_state1` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `stage_state2` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `stage_state3` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  `stage_state4` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of mq_message_history
-- ----------------------------
BEGIN;
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
