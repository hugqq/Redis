package com.ocrud.entity;

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

    public static final String RABBIT_EXCHANGE_NAME = "kill-exchange1";
    public static final String RABBIT_QUEUE_NAME ="kill-queue1";
    public static final String RABBIT_KEY_NAME ="kill-key1";


}
