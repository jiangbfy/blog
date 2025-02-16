package com.blog.common;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;

public class CacheUtils {
    public static Cache<String, Object> cache = Caffeine.newBuilder().maximumSize(1000).expireAfterWrite(600, TimeUnit.SECONDS).build();

    public static void put(String key, Object obj) {
        cache.put(key, obj);
    }

    public static Object get(String key) { return cache.getIfPresent(key); }
}
