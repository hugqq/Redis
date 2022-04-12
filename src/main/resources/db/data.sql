INSERT INTO `t_vouchers` (`id`,`title`, `thumbnail`, `amount`, `price`, `status`, `start_use_time`, `expire_time`, `stock`, `stock_left`, `description`, `clause`, `create_date`, `update_date`, `is_valid`)
VALUES (1, '代金券1', '缩略图1', 100, 10.00, 1, now(),  date_add( now(), INTERVAL 2 DAY) , 10, 10, '描述信息', '使用条款', now(), NULL, 0);
INSERT INTO `t_vouchers` (`id`,`title`, `thumbnail`, `amount`, `price`, `status`, `start_use_time`, `expire_time`, `stock`, `stock_left`, `description`, `clause`, `create_date`, `update_date`, `is_valid`)
VALUES (2, '代金券2', '缩略图2', 200, 20.00, 1, now(), date_add( now(), INTERVAL 2 DAY) , 10, 10, '描述信息', '使用条款', now(), NULL, 0);


INSERT INTO `t_seckill_vouchers` (`id`, `fk_voucher_id`, `name`, `start_time`, `end_time`, `is_valid`, `create_date`, `update_date`)
VALUES ( 1,1, '抢购活动1', now(), date_add( now(), INTERVAL 2 DAY) , 0, now(), NULL);

INSERT INTO  `t_user` (`id`,`name`, `token`, `create_date`, `update_date`, `is_valid`) VALUES (1,'刘一', 'zs', now(), NULL, 0);
INSERT INTO  `t_user` (`id`,`name`, `token`, `create_date`, `update_date`, `is_valid`) VALUES (2,'陈二', 'zs', now(), NULL, 0);
INSERT INTO  `t_user` (`id`,`name`, `token`, `create_date`, `update_date`, `is_valid`) VALUES (3,'张三', 'zs', now(), NULL, 0);
INSERT INTO  `t_user` (`id`,`name`, `token`, `create_date`, `update_date`, `is_valid`) VALUES (4,'李四', 'ls', now(), NULL, 0);
INSERT INTO  `t_user` (`id`,`name`, `token`, `create_date`, `update_date`, `is_valid`) VALUES (5,'王五', 'ww', now(), NULL, 0);
INSERT INTO  `t_user` (`id`,`name`, `token`, `create_date`, `update_date`, `is_valid`) VALUES (6,'赵六', 'zl', now(), NULL, 0);
INSERT INTO  `t_user` (`id`,`name`, `token`, `create_date`, `update_date`, `is_valid`) VALUES (7,'孙七', 'sq', now(), NULL, 0);
INSERT INTO  `t_user` (`id`,`name`, `token`, `create_date`, `update_date`, `is_valid`) VALUES (8,'周八', 'zb', now(), NULL, 0);
INSERT INTO  `t_user` (`id`,`name`, `token`, `create_date`, `update_date`, `is_valid`) VALUES (9,'吴九', 'wj', now(), NULL, 0);
INSERT INTO  `t_user` (`id`,`name`, `token`, `create_date`, `update_date`, `is_valid`) VALUES (10,'郑十', 'zs', now(), NULL, 0);

INSERT INTO `t_feeds` (`id`, `content`, `fk_user_id`, `praise_amount`, `comment_amount`, `create_date`, `update_date`, `is_valid`) VALUES (1, '啦啦啦啦啦1', 1, 0, 0, now(), NULL, 0);
INSERT INTO `t_feeds` (`id`, `content`, `fk_user_id`, `praise_amount`, `comment_amount`, `create_date`, `update_date`, `is_valid`) VALUES (2, '啦啦啦啦啦2', 1, 0, 0, now(), NULL, 0);




