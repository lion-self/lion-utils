package com.lion.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * BigDecimalUtils 计算工具
 */
public class BigDecimalUtils {

	private BigDecimalUtils() {}

	/**
	  * 浮点数加法
	  */
	public static BigDecimal add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2);
    }

    /**
	  * 浮点数减法
	  */
	public static BigDecimal subtract(double v1,double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2);
    }

    /**
	  * 浮点数乘法
	  */
	public static BigDecimal multiply(double v1,double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2);
    }

    /**
	  * 浮点数除法
	  */
	public static BigDecimal divide(double v1,double v2, RoundingMode roundingMode) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, roundingMode);
    }
}
