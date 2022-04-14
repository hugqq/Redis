package com.ocrud.entity;

/**
 * @author glow
 */
public class Constant {

    public static final String REDIS_VOUCHER_KEY = "vouchers:";
    /**
     * 某个用户关注了谁
     */
    public static final String REDIS_FOLLOWING_KEY = "following:";
    /**
     * keyId是哪个用户的粉丝
     */
    public static final String REDIS_FOLLOWERS_KEY = "followers:";
    /**
     * feeds key
     */
    public static final String REDIS_FOLLOWING_FEEDS_KEY = "followingFeeds:";
    /**
     * points key
     */
    public static final String REDIS_POINTS_KEY = "points:";

    /**
     * rabbitmq exchange
     */
    public static final String RABBIT_EXCHANGE_NAME = "kill-exchange1";
    /**
     * rabbitmq queue
     */
    public static final String RABBIT_QUEUE_NAME ="kill-queue1";
    /**
     * rabbitmq route key
     */
    public static final String RABBIT_KEY_NAME ="kill-key1";


}
