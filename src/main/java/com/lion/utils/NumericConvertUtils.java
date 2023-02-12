package com.lion.utils;

/**
 * 进制转换工具
 * 最大支持 62 进制的转换
 */
public class NumericConvertUtils {

    /** 在进制表示中的字符集合
     * 0-Z分别用于表示最大为62进制的符号；
     * 每个字符所在的位置下标是该字符对应的十进制数值
     */
    private static final char[] DIGITS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};

    /**
     * 将十进制的数字转换为指定进制的字符串
     * @param number 十进制的数字
     * @param seed   指定的进制
     * @return 指定进制的字符串
     */
    public static String toOtherNumberSystem(long number, int seed) {
        if (number < 0) {
            number = ((long) 2 * 0x7fffffff) + number + 2;
        }

        // 用于存储转化后的字符串
        char[] buf = new char[32];
        int charPos = 32;
        // 使用 --a 运算符，将余数从数组的最大位数开始存放
        while ((number / seed) > 0) {
            buf[--charPos] = DIGITS[(int) (number % seed)];
            number /= seed;
        }
        buf[--charPos] = DIGITS[(int) (number % seed)];
        return new String(buf, charPos, (32 - charPos));
    }

    /**
     * 将其它进制的数字（字符串形式）转换为十进制的数字
     * @param number 其它进制的数字（字符串形式）
     * @param seed   指定的进制，也就是参数str的原始进制
     * @return 十进制的数字
     */
    public static long toDecimalNumber(String number, int seed) {
        char[] charBuf = number.toCharArray();
        if (seed == 10) {
            return Long.parseLong(number);
        }
        // result 是转化结果，base 是原进制的0次方
        long result = 0, base = 1;
        for (int i = charBuf.length - 1; i >= 0; i--) {
            int index = 0;
            for (int j = 0, length = DIGITS.length; j < length; j++) {
                //找到对应字符的下标，对应的下标才是具体的数值
                if (DIGITS[j] == charBuf[i]) {
                    index = j;
                }
            }
            // 累加操作
            result += index * base;
            // 原进制的位数次方
            base *= seed;
        }
        return result;
    }
}




