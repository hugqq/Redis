DROP TABLE IF EXISTS `t_vouchers`;
CREATE TABLE `t_vouchers` (
                              `id`  int(10) NOT NULL AUTO_INCREMENT ,
                              `title`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '代金券标题' ,
                              `thumbnail`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '缩略图' ,
                              `amount`  int(11) NULL DEFAULT NULL COMMENT '抵扣金额' ,
                              `price`  decimal(10,2) NULL DEFAULT NULL COMMENT '售价' ,
                              `status`  int(10) NULL DEFAULT NULL COMMENT '-1=过期 0=下架 1=上架' ,
                              `start_use_time`  datetime NULL DEFAULT NULL COMMENT '开始使用时间' ,
                              `expire_time`  datetime NULL DEFAULT NULL COMMENT '过期时间' ,
                              `stock`  int(11) NULL DEFAULT 0 COMMENT '库存' ,
                              `stock_left`  int(11) NULL DEFAULT 0 COMMENT '剩余数量' ,
                              `description`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '描述信息' ,
                              `clause`  varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '使用条款' ,
                              `create_date`  datetime NULL DEFAULT NULL ,
                              `update_date`  datetime NULL DEFAULT NULL ,
                              `remarks`  datetime NULL DEFAULT NULL ,
                              `is_valid` int DEFAULT '0',
                              PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci AUTO_INCREMENT=1 ROW_FORMAT=COMPACT;

DROP TABLE IF EXISTS `t_orders`;
CREATE TABLE `t_orders` (
                            `id` int NOT NULL AUTO_INCREMENT,
                            `order_no` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '订单号',
                            `fk_voucher_id` int DEFAULT NULL COMMENT '优惠券id',
                            `fk_user_id` int DEFAULT NULL COMMENT '用户id',
                            `fk_seckill_id` int DEFAULT NULL COMMENT '如果是抢购订单时，抢购订单的id',
                            `status` tinyint(1) DEFAULT NULL COMMENT '订单状态：-1=已取消 0=未支付 1=已支付 2=已消费 3=已过期',
                            `order_type` int DEFAULT NULL COMMENT '订单类型：0=正常订单 1=抢购订单',
                            `create_date` datetime DEFAULT NULL,
                            `update_date` datetime DEFAULT NULL,
                            `is_valid` int DEFAULT '0',
                            PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT;

DROP TABLE IF EXISTS `t_seckill_vouchers`;
CREATE TABLE `t_seckill_vouchers` (
                                      `id` int NOT NULL AUTO_INCREMENT,
                                      `fk_voucher_id` int DEFAULT NULL,
                                      `name` varchar(255) COLLATE utf8mb4_general_ci DEFAULT NULL,
                                      `amount` int DEFAULT NULL COMMENT '库存',
                                      `start_time` datetime DEFAULT NULL,
                                      `end_time` datetime DEFAULT NULL,
                                      `create_date` datetime DEFAULT NULL,
                                      `update_date` datetime DEFAULT NULL,
                                      `is_valid` int DEFAULT '0',
                                      PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=COMPACT;

DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
                          `id` int NOT NULL AUTO_INCREMENT,
                          `name` varchar(255) DEFAULT NULL COMMENT '用户姓名',
                          `token` varchar(255) DEFAULT NULL COMMENT '伪token',
                          `create_date` datetime DEFAULT NULL,
                          `update_date` datetime DEFAULT NULL,
                          `is_valid` int DEFAULT '0',
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

DROP TABLE IF EXISTS `t_follow`;
CREATE TABLE `t_follow` (
                            `id`  int(11) NOT NULL AUTO_INCREMENT ,
                            `user_id`  int(11) NULL DEFAULT NULL COMMENT '用户' ,
                            `follow_user_id`  int(11) NULL DEFAULT NULL COMMENT '粉丝' ,
                            `is_followed` int DEFAULT '0' COMMENT '0 关注 1取消关注',
                            `is_valid`  INT(1) NULL DEFAULT '0' ,
                            `create_date`  datetime NULL DEFAULT NULL ,
                            `update_date`  datetime NULL DEFAULT NULL ,
                            PRIMARY KEY (`id`)
)
    ENGINE=InnoDB
DEFAULT CHARACTER SET=utf8 COLLATE=utf8_general_ci
AUTO_INCREMENT=1
ROW_FORMAT=COMPACT;;