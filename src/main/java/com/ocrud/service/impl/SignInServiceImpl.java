package com.ocrud.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.ocrud.service.SignInService;
import com.ocrud.service.UserPointsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class SignInServiceImpl implements SignInService {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private DefaultRedisScript redisScript;
    @Autowired
    private UserPointsService tUserPointsService;

    /**
     * 用户签到
     * 获取登录用户信息
     * 根据日期获取当前是多少号（使用BITSET指令关注时，offset从0开始计算，0就代表1号）
     * 构建用户按月存储key（user:sign:用户id:月份）
     * 判断用户是否签到（GETBIT指令）
     * 用户签到（SETBIT）
     * 返回用户连续签到次数（BITFIELD key GET [u/i] type offset value， 获取从用户从当前日期开始到1号的所有签到状态，然后进行位移操作，获取连续签到天数）
     * @param userId  登录用户
     * @param dateStr 查询的日期，默认当天 yyyy-MM-dd
     * @return 连续签到次数
     */
    @Override
    public int doSign(Integer userId, String dateStr) {
        // 获取日期
        Date date = getDate(dateStr);
        // 获取日期对应的天数，多少号 从 0 开始
        int offset = DateUtil.dayOfMonth(date) - 1;
        // 构建 Key
        String signKey = buildSignKey(userId, date);
        // 查看是否已签到
        boolean isSigned = redisTemplate.opsForValue().getBit(signKey, offset);
        Assert.isFalse(isSigned, "当前日期已完成签到，无需再签");
        // 签到
        redisTemplate.opsForValue().setBit(signKey, offset, true);
        // 统计连续签到次数
        int count = getContinuousSignCount(userId, date);
        tUserPointsService.addPoints(count,userId);
        return count;
    }


    /**
     * 统计某月连续签到次数
     *
     * @param userId 用户ID
     * @param date   日期
     * @return 当月连续签到次数
     */
    @Override
    public int getContinuousSignCount(Integer userId, Date date) {
        // 获取日期对应的天数，多少号
        int dayOfMonth = DateUtil.dayOfMonth(date);
        // 构建 Key
        String signKey = buildSignKey(userId, date);
        // 命令：bitfield key get [u/i]offset value
        // 此命令就是get取出key对应的位图，指定value索引位开始，取offset位偏移量的二进制
        BitFieldSubCommands bitFieldSubCommands = BitFieldSubCommands.create().get(BitFieldSubCommands.BitFieldType.unsigned(dayOfMonth)).valueAt(0);
        List<Long> list = redisTemplate.opsForValue().bitField(signKey, bitFieldSubCommands);
        if (list == null || list.isEmpty()) {
            return 0;
        }
        int signCount = 0;
        long v = list.get(0) == null ? 0 : list.get(0);
        // 取低位连续不为0的个数即为连续签到次数，需考虑当天尚未签到的情况
        for (int i = dayOfMonth; i > 0; i--) {// i 表示位移次数
            // 右移再左移，如果等于自己说明最低位是 0，表示未签到
            if (v >> 1 << 1 == v) {
                // 低位为 0 且非当天说明连续签到中断了
                if (i != dayOfMonth) {
                    break;
                }
            } else {
                // 签到了 签到数加1
                signCount += 1;
            }
            // 右移一位并重新赋值，相当于把最右边一位去除
            v >>= 1;
        }
        return signCount;
    }


    /**
     * 获取用户签到次数
     *
     * @param userId  登录token
     * @param dateStr 查询的日期，默认当月 yyyy-MM-dd
     * @return 当前的签到次数
     */
    @Override
    public long getSignCount(Integer userId, String dateStr) {
        // 获取日期
        Date date = getDate(dateStr);
        // 构建 Key
        String signKey = buildSignKey(userId, date);
        // e.g. BITCOUNT user:sign:5:202011
        return (Long) redisTemplate.execute((RedisCallback<Long>) con -> con.bitCount(signKey.getBytes()));
    }


    /**
     * 获取当月签到情况
     * 获取登录用户信息
     * 构建Redis保存的Key
     * 获取月份的总天数（考虑2月闰、平年）
     * 通过BITFIELD指令获取当前月的所有签到数据
     * 遍历进行判断是否签到，并存入TreeMap方便排序
     * @param userId  登录token
     * @param dateStr 查询的日期，默认当月 yyyy-MM-dd
     * @return Key为签到日期，Value为签到状态的Map
     */
    @Override
    public Map<String, Boolean> getSignInfo(Integer userId, String dateStr) {
        // 获取日期
        Date date = getDate(dateStr);
        // 构建 Key
        String signKey = buildSignKey(userId, date);
        // 构建一个自动排序的 Map
        Map<String, Boolean> signInfo = new TreeMap<>();
        // 获取某月的总天数（考虑闰年）
        int dayOfMonth = DateUtil.lengthOfMonth(DateUtil.month(date) + 1, DateUtil.isLeapYear(DateUtil.dayOfYear(date)));
        // 命令：bitfield key get [u/i]offset value
        // 此命令就是get取出key对应的位图，指定value索引位开始，取offset位偏移量的二进制
        // 获取某月最后一天的数值（取无符号整数）
        BitFieldSubCommands bitFieldSubCommands = BitFieldSubCommands.create().get(BitFieldSubCommands.BitFieldType.unsigned(dayOfMonth)).valueAt(0);
        List<Long> list = redisTemplate.opsForValue().bitField(signKey, bitFieldSubCommands);
        if (list == null || list.isEmpty()) {
            return signInfo;
        }
        long v = list.get(0) == null ? 0 : list.get(0);
        // 由低位到高位进行遍历，为 0 表示未签，为 1 表示已签
        for (int i = dayOfMonth; i > 0; i--) {
            // 获取日期时间，比如 i = 31，最终拿到 yyyyMM31
            LocalDateTime dateTime = LocalDateTimeUtil.of(date).withDayOfMonth(i);
            // 先右移一位再左移一位，如果还不变那只能证明低位是 0，否则低位就是 1
            boolean flag = v >> 1 << 1 != v;
            // 构建一个 Key 为日期，value 为是否签到标记的有序 Map
            signInfo.put(DateUtil.format(dateTime, "yyyy-MM-dd"), flag);
            // 右移一位并重新赋值，相当于把最右边一位去除
            v >>= 1;
        }
        return signInfo;
    }


    /**
     * 获取首次签到日期
     *
     * @param userId
     * @param dateStr 查询的日期，默认当月 yyyy-MM-dd
     */
    @Override
    public Dict getFirstTimeSignInfo(Integer userId, String dateStr) {
        // 获取日期
        Date date = getDate(dateStr);
        // 构建 Key
        String signKey = buildSignKey(userId, date);
        Long execute = (Long) redisTemplate.execute((RedisCallback<Long>) con -> con.bitPos(signKey.getBytes(), true))+1;
        if (execute > -1) {
            LocalDateTime dateTime = LocalDateTimeUtil.of(date).withDayOfMonth(Math.toIntExact(execute));
            String format = DateUtil.format(dateTime, "yyyy-MM-dd");
            return Dict.create().set("data", format);
        } else {
            return Dict.create().set("data", "无数据");
        }
    }

    /**
     * 获取指定日期签到用户 默认今天
     */
    @Override
    public Dict getTodaySignUser(String dateStr){
        Set<String> keys = redisTemplate.keys("user:sign".concat("*"));
        List<Integer> userIds = Lists.newArrayList();
        if (CollUtil.isEmpty(keys)){
            return Dict.create().set("data", new ArrayList<>());
        }
        for (String key : keys) {
            // 获取日期
            Date date = getDate(dateStr);
            // 获取日期对应的天数，多少号 从 0 开始
            int offset = DateUtil.dayOfMonth(date) - 1;
            if (redisTemplate.opsForValue().getBit(key, offset)){
                String result = ReUtil.extractMulti(".*(\\d+):(\\d+)", key, "$1");
                userIds.add(Integer.valueOf(result));
            }
        }
        return Dict.create().set("data", userIds);
    }

    /**
     * 获取日期
     *
     * @param dateStr yyyy-MM-dd 默认当天
     * @return
     */
    private static Date getDate(String dateStr) {
        if (StrUtil.isBlank(dateStr)) {
            return new Date();
        }
        try {
            return DateUtil.parse(dateStr);
        } catch (Exception e) {
            throw new RuntimeException("请传入yyyy-MM-dd的日期格式");
        }
    }


    /**
     * 考虑到每月初需要重置连续签到次数，最简单的方式是按用户每月存一条签到数据（也可以每年存一条数据）。
     * Key的格式为user:sign:uid:yyyyMM，Value则采用长度为4个字节（32位）的位图（最大月份只有31天）。
     * 位图的每一位代表一天的签到，1表示已签，0表示未签。
     * 构建存储Key user:sign:dinerId:yyyyMM
     * e.g. user:sign:1:202204表示userId=1的食客在2022年04月的签到记录。
     *
     * @param userId
     * @return
     */
    private static String buildSignKey(int userId, Date date) {
        return String.format("user:sign:%d:%s", userId, DateUtil.format(date, "yyyyMM"));
    }
}
