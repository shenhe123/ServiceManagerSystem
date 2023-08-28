package com.jssg.servicemanagersystem.utils;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Json工具类
 */

public class JsonUtils {


    public static String toJson(Object object) {
        return new Gson().toJson(object);
    }


    public static <T> T fromJson(String json, Class<T> classOfT) {
        return new Gson().fromJson(json, classOfT);
    }


    public static <T> List<T> getListFromJson(String json, Class<T> classOfT) {
        Type type = new ParameterizedTypeImpl(classOfT);
        List<T> list =  new Gson().fromJson(json, type);
        return list;
    }

    public  static  <T> List<T> parseAbstractResponse(String json, TypeToken type) {
        return new GsonBuilder()
                .create()
                .fromJson(json, type.getType());
    }

    private static class ParameterizedTypeImpl implements ParameterizedType {
        Class clazz;

        public ParameterizedTypeImpl(Class clz) {
            clazz = clz;
        }
        @Override
        public Type[] getActualTypeArguments() {
            //返回实际类型组成的数据
            return new Type[]{clazz};
        }
        @Override
        public Type getRawType() {
            //返回原生类型，即HashMap
            return List.class;
        }
        @Override
        public Type getOwnerType() {
            //返回Type对象
            return null;
        }
    }





}
