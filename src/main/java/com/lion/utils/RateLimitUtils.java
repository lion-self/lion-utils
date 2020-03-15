package com.lion.utils;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 限流工具
 */
public class RateLimitUtils {

    private static  Logger logger = LoggerFactory.getLogger(RateLimitUtils.class);

    /**
     * 单位时间内调用接口上限
     */
    private static long limits = 6000;

    private static final Object lock = new Object();

    private static LoadingCache<Long, AtomicLong> countCache = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.DAYS)
            .build(new CacheLoader<>() {
                @Override
                public AtomicLong load(Long aLong) throws Exception {
                    //System.out.println("调用次数已达上限，请稍后再请求..." + aLong);
                    return new AtomicLong(1L);
                }
            });

    /**
     * 过滤超出限流范围的请求
     */
    public static boolean filterRequestByCount(Long key) throws ExecutionException {
        synchronized (lock) {
            Long counter = countCache.get(key).get();
            if (countCache.get(key).getAndIncrement() > limits) {
                logger.info("当天第{}次访问，访问失败！", counter);
                return false;
            }
            logger.info("访问成功！当天第{}次访问", counter);
            return true;
        }
    }

    /**
     * 设置缓存上限
     */
    public static void setLimits(long limits) {
        RateLimitUtils.limits = limits;
    }

    /**
     * 设置限流时间周期
     * @param duration 持续时间
     * @param unit 单位
     */
    public static void setExpireTime(long duration, TimeUnit unit) {
        countCache =  CacheBuilder.newBuilder()
                .expireAfterWrite(duration, unit)
                .build(new CacheLoader<>() {
                    @Override
                    public AtomicLong load(Long aLong) throws Exception {
                        return new AtomicLong(1L);
                    }
                });
    }

}
