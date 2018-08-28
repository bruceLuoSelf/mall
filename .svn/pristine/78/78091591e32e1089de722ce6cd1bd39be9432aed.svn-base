package com.wzitech.gamegold.common.utils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by ljn on 2018/3/13.
 */
public class ParamUtils {

    public ParamUtils() {
    }

    public static <T> Map<String, Object> convertMap(T obj) {
        HashMap params = new HashMap();
        Class objClass = obj.getClass();

        ArrayList fieldList;
        for(fieldList = new ArrayList(); objClass != null; objClass = objClass.getSuperclass()) {
            fieldList.addAll(Arrays.asList(objClass.getDeclaredFields()));
        }

        Iterator var4 = fieldList.iterator();

        while(var4.hasNext()) {
            Field field = (Field)var4.next();
            String name = field.getName();

            try {
                field.setAccessible(true);
                Object e = field.get(obj);
                if(e != null && !e.toString().isEmpty()) {
                    params.put(name, e);
                }
            } catch (IllegalAccessException var8) {
                ;
            }
        }

        return params;
    }
}
