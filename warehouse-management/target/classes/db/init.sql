-- 创建数据库
CREATE DATABASE IF NOT EXISTS warehouse_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;

USE warehouse_db;

-- 用户表
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(100) NOT NULL COMMENT '密码',
  `real_name` varchar(50) DEFAULT NULL COMMENT '真实姓名',
  `phone` varchar(20) DEFAULT NULL COMMENT '电话',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `status` int(1) DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
  `role` int(1) DEFAULT 1 COMMENT '角色 1-普通用户 2-管理员',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` int(1) DEFAULT 0 COMMENT '删除标记 0-未删除 1-已删除',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 仓库表
DROP TABLE IF EXISTS `warehouse`;
CREATE TABLE `warehouse` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `warehouse_code` varchar(50) NOT NULL COMMENT '仓库编码',
  `warehouse_name` varchar(100) NOT NULL COMMENT '仓库名称',
  `address` varchar(200) DEFAULT NULL COMMENT '地址',
  `manager` varchar(50) DEFAULT NULL COMMENT '负责人',
  `phone` varchar(20) DEFAULT NULL COMMENT '电话',
  `status` int(1) DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` int(1) DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_warehouse_code` (`warehouse_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='仓库表';

-- 商品分类表
DROP TABLE IF EXISTS `product_category`;
CREATE TABLE `product_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `category_name` varchar(50) NOT NULL COMMENT '分类名称',
  `parent_id` bigint(20) DEFAULT 0 COMMENT '父分类ID',
  `sort` int(11) DEFAULT 0 COMMENT '排序',
  `status` int(1) DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` int(1) DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品分类表';

-- 商品表
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `product_code` varchar(50) NOT NULL COMMENT '商品编码',
  `product_name` varchar(100) NOT NULL COMMENT '商品名称',
  `category_id` bigint(20) DEFAULT NULL COMMENT '分类ID',
  `unit` varchar(20) DEFAULT NULL COMMENT '单位',
  `price` decimal(10,2) DEFAULT 0.00 COMMENT '单价',
  `specification` varchar(100) DEFAULT NULL COMMENT '规格',
  `brand` varchar(50) DEFAULT NULL COMMENT '品牌',
  `status` int(1) DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` int(1) DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_product_code` (`product_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- 库存表
DROP TABLE IF EXISTS `stock`;
CREATE TABLE `stock` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `warehouse_id` bigint(20) NOT NULL COMMENT '仓库ID',
  `product_id` bigint(20) NOT NULL COMMENT '商品ID',
  `quantity` decimal(10,2) DEFAULT 0.00 COMMENT '库存数量',
  `frozen_quantity` decimal(10,2) DEFAULT 0.00 COMMENT '冻结数量',
  `batch_no` varchar(50) DEFAULT NULL COMMENT '批次号',
  `location` varchar(50) DEFAULT NULL COMMENT '库位',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` int(1) DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `idx_warehouse_product` (`warehouse_id`, `product_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='库存表';

-- 供应商表
DROP TABLE IF EXISTS `supplier`;
CREATE TABLE `supplier` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `supplier_code` varchar(50) NOT NULL COMMENT '供应商编码',
  `supplier_name` varchar(100) NOT NULL COMMENT '供应商名称',
  `contact_person` varchar(50) DEFAULT NULL COMMENT '联系人',
  `phone` varchar(20) DEFAULT NULL COMMENT '电话',
  `address` varchar(200) DEFAULT NULL COMMENT '地址',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `status` int(1) DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` int(1) DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_supplier_code` (`supplier_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='供应商表';

-- 客户表
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `customer_code` varchar(50) NOT NULL COMMENT '客户编码',
  `customer_name` varchar(100) NOT NULL COMMENT '客户名称',
  `contact_person` varchar(50) DEFAULT NULL COMMENT '联系人',
  `phone` varchar(20) DEFAULT NULL COMMENT '电话',
  `address` varchar(200) DEFAULT NULL COMMENT '地址',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `status` int(1) DEFAULT 1 COMMENT '状态 0-禁用 1-启用',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` int(1) DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_customer_code` (`customer_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户表';

-- 入库单表
DROP TABLE IF EXISTS `inbound_order`;
CREATE TABLE `inbound_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_no` varchar(50) NOT NULL COMMENT '订单号',
  `warehouse_id` bigint(20) NOT NULL COMMENT '仓库ID',
  `supplier_id` bigint(20) DEFAULT NULL COMMENT '供应商ID',
  `operator_id` bigint(20) DEFAULT NULL COMMENT '操作员ID',
  `order_time` datetime DEFAULT NULL COMMENT '入库时间',
  `total_amount` decimal(10,2) DEFAULT 0.00 COMMENT '总金额',
  `status` int(1) DEFAULT 0 COMMENT '状态 0-待确认 1-已完成 2-已取消',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` int(1) DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='入库单表';

-- 入库单明细表
DROP TABLE IF EXISTS `inbound_order_item`;
CREATE TABLE `inbound_order_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_id` bigint(20) NOT NULL COMMENT '入库单ID',
  `product_id` bigint(20) NOT NULL COMMENT '商品ID',
  `quantity` decimal(10,2) DEFAULT 0.00 COMMENT '数量',
  `unit_price` decimal(10,2) DEFAULT 0.00 COMMENT '单价',
  `total_price` decimal(10,2) DEFAULT 0.00 COMMENT '总价',
  `batch_no` varchar(50) DEFAULT NULL COMMENT '批次号',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` int(1) DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='入库单明细表';

-- 出库单表
DROP TABLE IF EXISTS `outbound_order`;
CREATE TABLE `outbound_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_no` varchar(50) NOT NULL COMMENT '订单号',
  `warehouse_id` bigint(20) NOT NULL COMMENT '仓库ID',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '客户ID',
  `operator_id` bigint(20) DEFAULT NULL COMMENT '操作员ID',
  `order_time` datetime DEFAULT NULL COMMENT '出库时间',
  `total_amount` decimal(10,2) DEFAULT 0.00 COMMENT '总金额',
  `status` int(1) DEFAULT 0 COMMENT '状态 0-待确认 1-已完成 2-已取消',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` int(1) DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='出库单表';

-- 出库单明细表
DROP TABLE IF EXISTS `outbound_order_item`;
CREATE TABLE `outbound_order_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_id` bigint(20) NOT NULL COMMENT '出库单ID',
  `product_id` bigint(20) NOT NULL COMMENT '商品ID',
  `quantity` decimal(10,2) DEFAULT 0.00 COMMENT '数量',
  `unit_price` decimal(10,2) DEFAULT 0.00 COMMENT '单价',
  `total_price` decimal(10,2) DEFAULT 0.00 COMMENT '总价',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` int(1) DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='出库单明细表';

-- 审计日志表
DROP TABLE IF EXISTS `audit_log`;
CREATE TABLE `audit_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` varchar(50) DEFAULT NULL COMMENT '用户ID',
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `operation_type` varchar(20) DEFAULT NULL COMMENT '操作类型',
  `operation_module` varchar(50) DEFAULT NULL COMMENT '操作模块',
  `operation_content` varchar(500) DEFAULT NULL COMMENT '操作内容',
  `ip_address` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  `user_agent` varchar(500) DEFAULT NULL COMMENT '用户代理',
  `result` varchar(20) DEFAULT NULL COMMENT '操作结果',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` int(1) DEFAULT 0 COMMENT '删除标记',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_operation_type` (`operation_type`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='审计日志表';

-- 初始化数据
INSERT INTO `sys_user` (`username`, `password`, `real_name`, `phone`, `email`, `status`, `role`) VALUES
('admin', '123456', '管理员', '13800138000', 'admin@example.com', 1, 2),
('user', '123456', '普通用户', '13800138001', 'user@example.com', 1, 1);

INSERT INTO `warehouse` (`warehouse_code`, `warehouse_name`, `address`, `manager`, `phone`, `status`) VALUES
('WH001', '主仓库', '北京市朝阳区xxx路xxx号', '张三', '010-12345678', 1),
('WH002', '分仓库', '上海市浦东新区xxx路xxx号', '李四', '021-87654321', 1);

INSERT INTO `product_category` (`category_name`, `parent_id`, `sort`, `status`) VALUES
('电子产品', 0, 1, 1),
('办公用品', 0, 2, 1),
('日用品', 0, 3, 1);

INSERT INTO `product` (`product_code`, `product_name`, `category_id`, `unit`, `price`, `specification`, `brand`, `status`) VALUES
('P001', '笔记本电脑', 1, '台', 5999.00, '15.6寸', '联想', 1),
('P002', '台式电脑', 1, '台', 3999.00, 'i5处理器', '戴尔', 1),
('P003', '打印机', 2, '台', 1299.00, 'A4幅面', '惠普', 1),
('P004', '办公桌', 2, '张', 899.00, '1.4米', '全友', 1),
('P005', '洗手液', 3, '瓶', 15.00, '500ml', '蓝月亮', 1);

INSERT INTO `supplier` (`supplier_code`, `supplier_name`, `contact_person`, `phone`, `address`, `status`) VALUES
('S001', '北京科技有限公司', '王经理', '13900139000', '北京市海淀区xxx路xxx号', 1),
('S002', '上海贸易有限公司', '赵经理', '13900139001', '上海市黄浦区xxx路xxx号', 1);

INSERT INTO `customer` (`customer_code`, `customer_name`, `contact_person`, `phone`, `address`, `status`) VALUES
('C001', '广州销售有限公司', '陈经理', '13700137000', '广州市天河区xxx路xxx号', 1),
('C002', '深圳科技有限公司', '刘经理', '13700137001', '深圳市南山区xxx路xxx号', 1);
