package com.lion.formatter;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * 字符串格式化工具
 */
public class StringFormatter {

    private StringFormatter() {}

    private static final char UNDER_LINE = '_';

    /**
     * 转为大写
     */
    public static String upperCase(String source) {
        if (source == null || source.isBlank()) {
            return "";
        }
        return source.toUpperCase(Locale.US);
    }

    /**
     * 转为小写
     */
    public static String lowerCase(String source) {
        if (source == null || source.isBlank()) {
            return "";
        }
        return source.toLowerCase(Locale.US);
    }

    /**
     * 下划线转小驼峰
     */
    public static String replaceUnderline2SmallHump(String source) {
        if (source == null) {
            return "";
        }
        long start = System.currentTimeMillis();
        source = source.replaceFirst("^_+", "");
        char[] chars = source.toCharArray();
        boolean needChange = false;
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < chars.length; i ++) {
            if (chars[i] == UNDER_LINE) {
                needChange = true;
                continue;
            }
            if (needChange) {
                if (Character.isLowerCase(chars[i])) {
                    result.append((char)(chars[i] - 32));
                } else {
                    result.append(chars[i]);
                }
                needChange = false;
                continue;
            }
            if(Character.isUpperCase(chars[i])) {
                result.append(Character.toLowerCase(chars[i]));
                continue;
            }
            result.append(chars[i]);
        }

        System.out.println("method2: " + (System.currentTimeMillis() - start));
        return result.toString();
    }

    /**
     * 下划线转大驼峰
     */
    public static String replaceUnderline2BigHump(String source) {
        String smallHump = replaceUnderline2SmallHump(source);
        char[] chars = smallHump.toCharArray();
        if (Character.isLowerCase(chars[0])) {
            chars[0] = Character.toUpperCase(chars[0]);
        }
        return new String(chars);
    }

    /**
     * 驼峰转下划线
     */
    public static String replaceHump2Underline(String source) {
        if (source == null) {
            return "";
        }
        char[] sourcesChars = source.toCharArray();
        if (Character.isUpperCase(sourcesChars[0])) {
            sourcesChars[0] = Character.toLowerCase(sourcesChars[0]);
        }
        StringBuilder resultSb = new StringBuilder();
        for (int i = 0; i < sourcesChars.length; i++) {
            if (Character.isUpperCase(sourcesChars[i])) {
                resultSb.append(UNDER_LINE).append(Character.toLowerCase(sourcesChars[i]));
            } else {
                resultSb.append(sourcesChars[i]);
            }
        }
        return resultSb.toString();
    }

    /**
     * 驼峰转下划线+大写
     */
    public static String replaceHump2UpperUnderline(String source) {
        if (source == null) {
            return "";
        }
        char[] sourcesChars = source.toCharArray();
        StringBuilder resultSb = new StringBuilder();
        if (Character.isLowerCase(sourcesChars[0])) {
            resultSb.append(Character.toUpperCase(sourcesChars[0]));
        } else {
            resultSb.append(sourcesChars[0]);
        }

        for (int i = 1; i < sourcesChars.length; i++) {
            if (Character.isUpperCase(sourcesChars[i])) {
                resultSb.append(UNDER_LINE).append(sourcesChars[i]);
            } else {
                resultSb.append(Character.toUpperCase(sourcesChars[i]));
            }
        }
        return resultSb.toString();
    }

    /**
     * 字符串脱敏
     */
    public static String desensitize(String source, int pre, int mask) {
        if (source == null || source.isBlank()) {
            return source;
        }
        int n = source.length();
        if (pre > n) {
            return source;
        }
        String preStr = source.substring(0, pre);
        if (pre + mask > n) {
            return preStr + "*".repeat(Math.max(0, n - pre));
        }
        String suffix =  source.substring(pre + mask, n);
        return preStr + "*".repeat(Math.max(0, mask)) + suffix;
    }

    /**
     * 传入数字以及指定长度，长度不足的数字前补0
     */
    public static String supplementZero2Str(Number number, int length) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        nf.setMaximumIntegerDigits(length);
        nf.setMinimumIntegerDigits(length);
        return nf.format(number);
    }

}
