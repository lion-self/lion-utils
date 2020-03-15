package com.lion.formatter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * 数值类型格式化
 */
public class NumberFormatter {

    /**
     * 保留指定位小数，四舍五入
     */
    public static String decimalHalfUp(double num, int digitsLength) {
        double halfUpNum = new BigDecimal(num).setScale(digitsLength, RoundingMode.HALF_UP).doubleValue();
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        nf.setMinimumFractionDigits(digitsLength);
        nf.setMaximumFractionDigits(digitsLength);
        return nf.format(halfUpNum);
    }

    /**
     * 保留指定位小数，全舍
     */
    public static String decimalDown(double num, int digitsLength) {
        double downNum = new BigDecimal(num).setScale(digitsLength, RoundingMode.DOWN).doubleValue();
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        nf.setMinimumFractionDigits(digitsLength);
        nf.setMaximumFractionDigits(digitsLength);
        return nf.format(downNum);
    }

    /**
     * 保留指定位小数，全入
     */
    public static String decimalUp(double num, int digitsLength) {
        double upNum = new BigDecimal(num).setScale(digitsLength, RoundingMode.UP).doubleValue();
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(false);
        nf.setMinimumFractionDigits(digitsLength);
        nf.setMaximumFractionDigits(digitsLength);
        return nf.format(upNum);
    }

    /**
     * 保留指定位小数，每三位用逗号隔开，四舍五入
     */
    public static String halfupWithGroup(double num, int digitsLength) {
        double halfUoNum = new BigDecimal(num).setScale(digitsLength, RoundingMode.HALF_UP).doubleValue();;
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(true);
        nf.setMinimumFractionDigits(digitsLength);
        nf.setMaximumFractionDigits(digitsLength);
        return nf.format(halfUoNum);
    }

    /**
     * 保留指定位小数，每三位用逗号隔开，全舍
     */
    public static String downWithGroup(double num, int digitsLength) {
        double downNum = new BigDecimal(num).setScale(digitsLength, RoundingMode.DOWN).doubleValue();
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(true);
        nf.setMinimumFractionDigits(digitsLength);
        nf.setMaximumFractionDigits(digitsLength);
        return nf.format(downNum);
    }

    /**
     * 保留指定位小数，每三位用逗号隔开，全入
     */
    public static String upWithGroup(double num, int digitsLength) {
        double upNum = new BigDecimal(num).setScale(digitsLength, RoundingMode.UP).doubleValue();
        NumberFormat nf = NumberFormat.getInstance();
        nf.setGroupingUsed(true);
        nf.setMinimumFractionDigits(digitsLength);
        nf.setMaximumFractionDigits(digitsLength);
        return nf.format(upNum);
    }

    /**
     * 人民币货币格式
     */
    public static String formatRMB(double num) {
        Locale locale = new Locale("zh", "CN");
        NumberFormat currFmt = NumberFormat.getCurrencyInstance(locale);
        return currFmt.format(num);
    }

    /**
     * 美元货币格式
     */
    public static String formatDollar(double num) {
        Locale locale = new Locale("en", "US");
        NumberFormat currFmt = NumberFormat.getCurrencyInstance(locale);
        return currFmt.format(num);
    }

}
