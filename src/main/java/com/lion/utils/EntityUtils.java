package com.lion.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 类-内部操作
 */
public class EntityUtils {

    /**
     * 获取类中的所有属性
     */
    public static List<String> getField(Class entityClass, String... ignoreField) {
        List<String> resultFields = new ArrayList<>();
        List<String> ignoreList = new ArrayList<>();
        ignoreList.add("serialVersionUID");
        if (ignoreField != null) {
            ignoreList.addAll(Arrays.asList(ignoreField));
        }

        Class positionClass = entityClass;
        while (positionClass != Object.class) {
            Field[] fields = positionClass.getDeclaredFields();
            for (Field field : fields) {
                String fieldName = field.getName();
                if (!ignoreList.contains(fieldName)) {
                    resultFields.add(fieldName);
                }
            }
            positionClass = positionClass.getSuperclass();
        }
        return resultFields;
    }
}
