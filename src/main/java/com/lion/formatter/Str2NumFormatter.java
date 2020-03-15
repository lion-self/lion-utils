package com.lion.formatter;

import com.lion.utils.RegularUtils;

/**
 * 字符串转换成数值类型
 */
public class Str2NumFormatter {

    private Str2NumFormatter() {}

    /**
     * 转为int
     */
    public static int toInt(String s) {
        if (s == null || s.isBlank() || RegularUtils.isJustNumber(s)) {
            throw new NumberFormatException("error input str：" + s);
        }
        return Integer.valueOf(s);
    }

    /**
     * 转为 long
     */
    public static long toLong(String s) {
        if (s == null || s.isBlank() || RegularUtils.isJustNumber(s)) {
            throw new NumberFormatException("error input str：" + s);
        }
        return Long.valueOf(s);
    }

    /**
     * 转为float
     */
    public static float toFloat(String s) {
        if (s == null || s.isBlank() || RegularUtils.isNumber(s)) {
            throw new NumberFormatException("error input str：" + s);
        }
        return Float.parseFloat(s);
    }


    /**
     * 转为double
     */
    public static double toDouble(String s) {
        if (s == null || s.isBlank() || RegularUtils.isNumber(s)) {
            throw new NumberFormatException("error input str：" + s);
        }
        return Double.parseDouble(s);
    }



}
