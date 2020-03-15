package com.lion.utils;

import java.util.Random;

public class StringUtils {

    private StringUtils() {

    }

    /**
     * 获取指定长度的随机数字字符串
     */
    public static String getRandom(int i) {
        Random random = new Random();
        if (i == 0) {
            return "";
        }
        String str = "";
        for (int k = 0; k < i; k++) {
            str = str + random.nextInt(9);
        }
        return str;
    }

    /**
     * 生成流水号
     */
    public static String geneTradeNo() {
        StringBuilder buffer = new StringBuilder(DateUtils.getCurrentDate("YYYYMMddHHmmssSSSS"));
        Random random = new Random();
        int num = (int) (random.nextDouble() * (99999 - 10000 + 1)) + 10000;
        buffer.append(num);
        return buffer.toString();
    }

    /**
     * 获取字符串的长度，一个汉字占两个字符
     */
    public static int getCharLength(String str) {
        int realLength = 0;
        int len = str.length();
        int charCode;
        for (int i = 0; i < len; i++) {
            charCode = str.charAt(i);
            if (charCode <= 128) {
                realLength += 1;
            } else {
                // 如果是中文则长度加2
                realLength += 2;
            }
        }
        return realLength;
    }




}
