/*
 Navicat Premium Data Transfer

 Source Server         : 127.0.0.1
 Source Server Type    : MySQL
 Source Server Version : 80100 (8.1.0)
 Source Host           : localhost:3306
 Source Schema         : lo_orders

 Target Server Type    : MySQL
 Target Server Version : 80100 (8.1.0)
 File Encoding         : 65001

 Date: 07/09/2023 14:33:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for mq_message
-- ----------------------------
DROP TABLE IF EXISTS `mq_message`;
CREATE TABLE `mq_message` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '消息id',
  `message_type` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '消息类型代码: course_publish ,  media_test',
  `business_key1` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '关联业务信息',
  `business_key2` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '关联业务信息',
  `business_key3` varchar(512) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '关联业务信息',
  `execute_num` int unsigned NOT NULL DEFAULT '0' COMMENT '通知次数',
  `state` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '0' COMMENT '处理状态，0:初始，1:成功',
  `returnfailure_date` datetime DEFAULT NULL COMMENT '回复失败时间',
  `returnsuccess_date` datetime DEFAULT NULL COMMENT '回复成功时间',
  `returnfailure_msg` varchar(2048) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '回复失败内容',
  `execute_date` datetime DEFAULT NULL COMMENT '最近通知时间',
  `stage_state1` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '0' COMMENT '阶段1处理状态, 0:初始，1:成功',
  `stage_state2` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '0' COMMENT '阶段2处理状态, 0:初始，1:成功',
  `stage_state3` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '0' COMMENT '阶段3处理状态, 0:初始，1:成功',
  `stage_state4` char(1) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL DEFAULT '0' COMMENT '阶段4处理状态, 0:初始，1:成功',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of mq_message
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for mq_message_history
-- ----------------------------
DROP TABLE IF EXISTS `mq_message_history`;
CREATE TABLE `mq_message_history` (
  `id` bigint NOT NULL COMMENT '消息id',
  `message_type` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '消息类型代码',
  `business_key1` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '关联业务信息',
  `business_key2` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '关联业务信息',
  `business_key3` varchar(512) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '关联业务信息',
  `execute_num` int unsigned DEFAULT NULL COMMENT '通知次数',
  `state` int(10) unsigned zerofill DEFAULT NULL COMMENT '处理状态，0:初始，1:成功，2:失败',
  `returnfailure_date` datetime DEFAULT NULL COMMENT '回复失败时间',
  `returnsuccess_date` datetime DEFAULT NULL COMMENT '回复成功时间',
  `returnfailure_msg` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '回复失败内容',
  `execute_date` datetime DEFAULT NULL COMMENT '最近通知时间',
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
INSERT INTO `mq_message_history` (`id`, `message_type`, `business_key1`, `business_key2`, `business_key3`, `execute_num`, `state`, `returnfailure_date`, `returnsuccess_date`, `returnfailure_msg`, `execute_date`, `stage_state1`, `stage_state2`, `stage_state3`, `stage_state4`) VALUES (2, 'pay_result_notify', '19', '60201', NULL, 0, NULL, NULL, NULL, NULL, NULL, '0', '0', '0', '0');
COMMIT;

-- ----------------------------
-- Table structure for xc_orders
-- ----------------------------
DROP TABLE IF EXISTS `xc_orders`;
CREATE TABLE `xc_orders` (
  `id` bigint NOT NULL COMMENT '订单号',
  `total_price` float(8,2) NOT NULL COMMENT '总价',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `status` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '交易状态',
  `user_id` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户id',
  `order_type` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '订单类型',
  `order_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '订单名称',
  `order_descrip` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '订单描述',
  `order_detail` varchar(1024) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '订单明细json',
  `out_business_id` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '外部系统业务id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `orders_unioue` (`out_business_id`) USING BTREE COMMENT '外部系统的业务id'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of xc_orders
-- ----------------------------
BEGIN;
INSERT INTO `xc_orders` (`id`, `total_price`, `create_date`, `status`, `user_id`, `order_type`, `order_name`, `order_descrip`, `order_detail`, `out_business_id`) VALUES (1698624728209260544, 99.00, '2023-09-04 17:11:09', '600002', '52', '60201', 'Javascript之VueJS', '购买课程:Javascript之VueJS', '[{\"goodsId\":27,\"goodsType\":\"60201\",\"goodsName\":\"Javascript之VueJS\",\"goodsPrice\":99}]', '19');
COMMIT;

-- ----------------------------
-- Table structure for xc_orders_goods
-- ----------------------------
DROP TABLE IF EXISTS `xc_orders_goods`;
CREATE TABLE `xc_orders_goods` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint NOT NULL COMMENT '订单号',
  `goods_id` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '商品id',
  `goods_type` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '商品类型',
  `goods_name` varchar(100) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '商品名称',
  `goods_price` float(10,2) NOT NULL COMMENT '商品交易价，单位分',
  `goods_detail` varchar(1024) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '商品详情json',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of xc_orders_goods
-- ----------------------------
BEGIN;
INSERT INTO `xc_orders_goods` (`id`, `order_id`, `goods_id`, `goods_type`, `goods_name`, `goods_price`, `goods_detail`) VALUES (6, 1698624728209260544, '27', '60201', 'Javascript之VueJS', 99.00, NULL);
COMMIT;

-- ----------------------------
-- Table structure for xc_pay_record
-- ----------------------------
DROP TABLE IF EXISTS `xc_pay_record`;
CREATE TABLE `xc_pay_record` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `pay_no` bigint NOT NULL COMMENT '本系统支付交易号',
  `out_pay_no` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '第三方支付交易流水号',
  `out_pay_channel` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci DEFAULT NULL COMMENT '第三方支付渠道编号',
  `order_id` bigint NOT NULL COMMENT '商品订单号',
  `order_name` varchar(255) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '订单名称',
  `total_price` float(8,2) NOT NULL COMMENT '订单总价单位元',
  `currency` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '币种CNY',
  `create_date` datetime NOT NULL COMMENT '创建时间',
  `status` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '支付状态',
  `pay_success_time` datetime DEFAULT NULL COMMENT '支付成功时间',
  `user_id` varchar(32) CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci NOT NULL COMMENT '用户id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `pay_order_unioue2` (`pay_no`) USING BTREE COMMENT '本系统支付交易号',
  UNIQUE KEY `pay_order_unioue` (`out_pay_no`) USING BTREE COMMENT '第三方支付订单号'
) ENGINE=InnoDB AUTO_INCREMENT=1623527995601891334 DEFAULT CHARSET=utf8mb3 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of xc_pay_record
-- ----------------------------
BEGIN;
INSERT INTO `xc_pay_record` (`id`, `pay_no`, `out_pay_no`, `out_pay_channel`, `order_id`, `order_name`, `total_price`, `currency`, `create_date`, `status`, `pay_success_time`, `user_id`) VALUES (1623527995601891333, 1698646768184803328, '2023090422001435260500883781', 'Alipay', 1698624728209260544, 'Javascript之VueJS', 99.00, 'CNY', '2023-09-04 18:38:44', '601002', '2023-09-05 13:55:59', '52');
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
