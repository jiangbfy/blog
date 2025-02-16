package com.blog.common;

import org.springframework.data.redis.core.RedisTemplate;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public class RedisUtils {

    private static RedisTemplate<String, Object> redisTemplate;

    public static void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        RedisUtils.redisTemplate = redisTemplate;
    }

    public static void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public static Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public static void setWithExpire(String key, Object value, long expire) {
        redisTemplate.opsForValue().set(key, value, expire, TimeUnit.SECONDS);
    }

    public static long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    public static boolean checkExist(String key) {
        return redisTemplate.hasKey(key);
    }

    public static boolean del(String key) {
        return RedisUtils.checkExist(key) && redisTemplate.delete(key);
    }

    public static Set<String> getAllKeys(String pattern) {
        return redisTemplate.keys(pattern);
    }
}
