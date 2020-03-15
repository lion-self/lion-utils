package com.lion.utils;

import com.lion.formatter.Str2NumFormatter;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * IP地址处理
 */
public class IPUtils {

    private IPUtils() {}

    /**
     * 校验是否IP地址
     */
    public static boolean isIP(String s) {
        if (s == null || s.isBlank()) {
            return false;
        }
        String str = "(2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2}(\\.((2(5[0-5]|[0-4]\\d))|[0-1]?\\d{1,2})){3}";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(s);
        return m.matches();
    }

    /**
     * 校验是否公网IP
     */
    public static boolean isPubicNetwork(String s) {
        if (!isIP(s)) {
            return false;
        }
        if ("0.0.0.0".equals(s) || "1.1.1.1".equals(s)) {
            return false;
        }
        String[] ipArray = s.split(".");
        int a1 = Str2NumFormatter.toInt(ipArray[0]);
        // 10.0.0.0 ~ 10.255.255.255
        if (a1 == 10) {
            return false;
        }
        int a2 = Str2NumFormatter.toInt(ipArray[1]);
        // 172.16.0.0 ~ 172.31.255.255
        if (a1 == 172) {
            if (a2 >= 16 && a2 <= 31) {
                return false;
            }
        }
        // 192.168.0.0 ~ 192.168.255.255
        if (a1 == 192) {
            if (a2 == 168) {
                return false;
            }
        }
        return true;
    }

    /**
     * IP 转为 long 类型
     */
    public static Long convertIpToLong(String ip) {
        String[] ipArray = ip.split("\\.");
        StringBuilder sb = new StringBuilder();
        for (String str: ipArray) {
            sb.append((str.length()<3) ? 0 + str : str);
        }
        return Long.parseLong(sb.toString());
    }
}
