package com.lion.utils;

/**
 * 计算方法执行时间的类。
 * 使用：在方法调用前调用Profiler.begin()，在方法调用后调用Profiler.getTimeDuration()
 */
public class ExecutionAnalysisUtils {

    private ExecutionAnalysisUtils() {}

    private static final ThreadLocal<Long> TIME_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 开始执行方法的时间
     */
    public static void begin() {
        TIME_THREAD_LOCAL.set(System.currentTimeMillis());
    }

    /**
     * 输出方法执行时间
     */
    public static Long getTimeDuration() {
        return System.currentTimeMillis() - TIME_THREAD_LOCAL.get();
    }

}
