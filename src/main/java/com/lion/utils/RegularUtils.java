package com.lion.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则校验工具
 */
public class RegularUtils {

    /**
     * 手机号校验
     */
    public static boolean isMobileNO(String s) {
        if (s == null || s.isBlank()) {
            return false;
        }
        Pattern p = Pattern.compile("^((13[0-9])|(14[0-9])|(15[^4,\\D])|(16[0-9])|(17[0-9])|(18[0-9])|(19[0-9]))\\d{8}$");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    /**
     * 带区号的电话号码验证
     */
    public static boolean isPhone(String s) {
        if (s == null || s.isBlank()) {
            return false;
        }
        Pattern p1 = Pattern.compile("^[0][0-9]{2,3}-[0-9]{5,10}$");
        Matcher m = p1.matcher(s);
        return m.matches();
    }

    /**
     * 邮箱校验
     */
    public static boolean isEmail(String s) {
        if (s == null || s.isBlank()) {
            return false;
        }
        String str = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(s);
        return m.matches();
    }

    /**
     * 银行卡号校验
     */
    public static boolean isAccount(String s) {
        if (s == null || s.isBlank()) {
            return false;
        }
        String str = "^[1-9][0-9]{14,18}$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(s);
        return m.matches();
    }

    /**
     * 校验是否含汉字
     */
    public static boolean isContainChinese(String s) {
        if (s == null || s.isBlank()) {
            return false;
        }
        String str = "[\u4E00-\u9FA5]+";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(s);
        return m.find();
    }

    /**
     * 校验是否只包含数字
     */
    public static boolean isJustNumber(String s) {
        if (s == null || s.isBlank()) {
            return false;
        }
        Pattern p = Pattern.compile("^\\d*$");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    /**
     * 校验是否是一个数值
     */
    public static boolean isNumber(String s) {
        if (s == null || s.isBlank()) {
            return false;
        }
        Pattern p = Pattern.compile("^[0-9]+(\\.[0-9]+)?$");
        Matcher m = p.matcher(s);
        return m.matches();
    }

    /**
     * 验证字符串长度是否在指定长度之间，闭区间 [ ]
     */
    public static boolean checkLengthBetween(String str, int length1, int length2) {
        return !str.isEmpty() && str.trim().length() >= length1 && str.trim().length() <= length2;
    }

    /**
     * 通用密码校验
     * 包含数字，大写字母，小写字母
     * 6 ~ 16位字符
     */
    public static boolean checkPwd(String pwd) {
        return pwd.matches("[0-9A-Za-z]*") && checkLengthBetween(pwd, 6, 16);
    }

    /**
     * 判断字符串中间是否含有空格
     */
    public static boolean hasSpace(String str) {
        int index = str.trim().indexOf(" ");
        return index > 0;
    }

    /**
     * 验证字符串是否是指定长度
     */
    public static boolean checkLength(String str, int length) {
        return !str.isEmpty() && str.trim().length() == length;
    }
}
