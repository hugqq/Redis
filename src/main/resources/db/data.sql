INSERT INTO `t_vouchers` (`id`,`title`, `thumbnail`, `amount`, `price`, `status`, `start_use_time`, `expire_time`, `stock`, `stock_left`, `description`, `clause`, `create_date`, `update_date`, `is_valid`)
VALUES (1, '代金券1', '缩略图1', 100, 10.00, 1, now(),  date_add( now(), INTERVAL 2 DAY) , 10, 10, '描述信息', '使用条款', now(), NULL, 0);
INSERT INTO `t_vouchers` (`id`,`title`, `thumbnail`, `amount`, `price`, `status`, `start_use_time`, `expire_time`, `stock`, `stock_left`, `description`, `clause`, `create_date`, `update_date`, `is_valid`)
VALUES (2, '代金券2', '缩略图2', 200, 20.00, 1, now(), date_add( now(), INTERVAL 2 DAY) , 10, 10, '描述信息', '使用条款', now(), NULL, 0);

INSERT INTO  `t_user` (`id`,`name`, `token`, `create_date`, `update_date`, `is_valid`) VALUES (1,'张三', 'zs', now(), NULL, 0);
INSERT INTO  `t_user` (`id`,`name`, `token`, `create_date`, `update_date`, `is_valid`) VALUES (2,'李四', 'ls', now(), NULL, 0);

INSERT INTO `t_seckill_vouchers` (`id`, `fk_voucher_id`, `name`, `start_time`, `end_time`, `is_valid`, `create_date`, `update_date`)
VALUES ( 1,1, '抢购活动1', now(), date_add( now(), INTERVAL 2 DAY) , 0, now(), NULL);

