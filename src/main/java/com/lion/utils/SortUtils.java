package com.lion.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

import static com.lion.formatter.StringFormatter.supplementZero2Str;

public class SortUtils {

    private SortUtils() {}

    private static Logger logger = LoggerFactory.getLogger(SortUtils.class);

    /**
     * 将 list 中的对象按指定域的值升序排序
     * @param list 待排序的列表
     * @param isAsc 是否升序
     * @param source 调用此方法的对象
     * @param sortFieldName 用于排序的域

     */
    public static <E> void sort2(List<E> list, boolean isAsc, Object source, final String ...sortFieldName) {
        list.sort((e1, e2) -> {
            int ret = 0;
            for (String fieldName : sortFieldName) {
                try {
                    ret = SortUtils.compare(fieldName, isAsc, e1, e2, source);
                } catch (NoSuchFieldException e) {
                    logger.error("类中不存在该属性", e);
                } catch (IllegalAccessException e) {
                    logger.error("非法访问类中属性", e);
                }
                if (ret != 0)
                    break;
            }
            return ret;
        });
    }

    /**
     * 将 list 中的对象按指定域的值以指定的方式排序
     * @param list 待排序的列表
     * @param sortFieldNames 用于排序的域
     * @param isAscOrders 对应 sortFieldNames，说明每个域是否升序排序
     * @param source 调用此方法的对象
     */
    public static <E> void sort(List<E> list, final String[] sortFieldNames, final boolean[] isAscOrders, Object source) {
        if (sortFieldNames.length != isAscOrders.length) {
            throw new IllegalArgumentException("属性数组元素个数和升降序数组元素个数不相等!");
        }
        list.sort((e1, e2) -> {
            int ret = 0;
            for (int index = 0; index < sortFieldNames.length; index++) {
                try {
                    ret = SortUtils.compare(sortFieldNames[index], isAscOrders[index], e1, e2, source);
                    if (ret != 0)
                        break;
                } catch (NoSuchFieldException e) {
                    logger.error("类中不存在该属性", e);
                } catch (IllegalAccessException e) {
                    logger.error("非法访问类中属性", e);
                }
            }
            return ret;
        });
    }

    /**
     * 比较两个类中某个元素属性的大小
     * @param fieldName 被比较的域名
     * @param isAsc 是否升序
     * @param a 被比较的对象
     * @param b 被比较的对象
     * @param source 调用此方法的对象
     */
    public static <E> int compare(final String fieldName, final boolean isAsc, E a, E b, Object source) throws NoSuchFieldException, IllegalAccessException {
        int ret;
        Object v1 = getFieldValue(a, fieldName, source);
        Object v2 = getFieldValue(b, fieldName, source);
        String str1 = v1.toString();
        String str2 = v2.toString();
        if (v1 instanceof Number && v2 instanceof Number) {
            int maxLen = Math.max(str1.length(), str2.length());
            str1 = supplementZero2Str((Number) v1, maxLen);
            str2 = supplementZero2Str((Number) v2, maxLen);
        } else if (v1 instanceof Date && v2 instanceof Date) {
            long time1 = ((Date) v1).getTime();
            long time2 = ((Date) v2).getTime();
            int maxLen = String.valueOf(Math.max(time1, time2)).length();
            str1 = supplementZero2Str(time1, maxLen);
            str2 = supplementZero2Str(time2, maxLen);
        }
        if (isAsc) {
            ret = str1.compareTo(str2);
        } else {
            ret = str2.compareTo(str1);
        }
        return ret;
    }


    private static Object getFieldValue(Object target, String fieldName, Object source) throws NoSuchFieldException, IllegalAccessException {
        Field field = target.getClass().getDeclaredField(fieldName);
        Object object ;
        boolean accessible = field.canAccess(source);
        if (!accessible) {
            //如果是source不可访问的属性，需要修改其访问属性为可访问
            field.setAccessible(true);
            object = field.get(target);
            //还原private、protected访问属性
            field.setAccessible(false);
            return object;
        }
        object = field.get(target);
        return object;
    }

}
