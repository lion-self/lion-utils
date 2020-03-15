package com.lion.utils;

import java.util.Calendar;

/**
 * 身份证号码解析工具
 */
public class IdentityCardUtils {

    private IdentityCardUtils() {}

    private static final String IDENTITY_CARD_REG = "^\\d{6}(((19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])\\d{3}([0-9]|x|X))|(\\d{2}(0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])\\d{3}))$";
    private static final String GENDER_MALE = "male";
    private static final String GENDER_FEMALE = "female";
    private static final String UNKNOWN = "unknown";

    /**
     * 判断是否身份证号
     */
    public static boolean isIdentityCard(String identityCard) {
        if (identityCard == null || identityCard.isBlank()) {
            return false;
        }
        return identityCard.matches(IDENTITY_CARD_REG)
                || (identityCard.length() == 17 && identityCard.substring(0, 15).matches(IDENTITY_CARD_REG));
    }

    /**
     * 获取生日
     */
    public String fetchBirthday(String identityCard) {
        if (!isIdentityCard(identityCard)) {
            return UNKNOWN;
        }
        TempBirthDay tempBirthDay = fetchTempBirthday(identityCard);
        return tempBirthDay.year + '-' + tempBirthDay.month + '-' + tempBirthDay.day;
    }

    /**
     * 获取性别
     */
    public String fetchGender(String identityCard) {
        if (!isIdentityCard(identityCard)) {
            return UNKNOWN;
        }
        int idxSexStart = 16;
        if (identityCard.length() == 15) {
            idxSexStart = 14;
        }
        String idxSexStr = identityCard.substring(idxSexStart, idxSexStart + 1);
        int idxGender = Integer.parseInt(idxSexStr) % 2;
        return  (idxGender == 1) ? GENDER_MALE : GENDER_FEMALE;
    }

    /**
     * 获取年龄
     */
    public Integer fetchAge(String identityCard) {
        if (!isIdentityCard(identityCard)) {
            return null;
        }
        TempBirthDay tempBirthDay = fetchTempBirthday(identityCard);
        Calendar certificateCal = Calendar.getInstance();
        Calendar currentTimeCal = Calendar.getInstance();
        certificateCal.set(Integer.parseInt(tempBirthDay.year), Integer.parseInt(tempBirthDay.month) - 1, Integer.parseInt(tempBirthDay.day));
        int yearAge = (currentTimeCal.get(Calendar.YEAR)) - (certificateCal.get(Calendar.YEAR));
        certificateCal.set(currentTimeCal.get(Calendar.YEAR), Integer.parseInt(tempBirthDay.month) - 1, Integer.parseInt(tempBirthDay.day));
        int monthFloor = (currentTimeCal.before(certificateCal) ? 1 : 0);
        return yearAge - monthFloor;
    }


    private TempBirthDay fetchTempBirthday(String identityCard) {
        int birthYearSpan = 4;
        if (identityCard.length() == 15) {
            birthYearSpan = 2;
        }
        String year = (birthYearSpan == 2 ? "19" : "") + identityCard.substring(6, 6 + birthYearSpan);
        String month = identityCard.substring(6 + birthYearSpan, 6 + birthYearSpan + 2);
        String day = identityCard.substring(8 + birthYearSpan, 8 + birthYearSpan + 2);
        TempBirthDay a = new TempBirthDay(year, month, day);
        return a;
    }

    private class TempBirthDay {
        String year;
        String month;
        String day;

        TempBirthDay(String year, String month, String day) {
            this.year = year;
            this.month = month;
            this.day = day;
        }
    }
}
