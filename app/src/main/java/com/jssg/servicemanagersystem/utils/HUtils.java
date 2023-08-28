package com.jssg.servicemanagersystem.utils;

import android.os.Looper;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

/**
 * Created by gongdongyang on 2018/7/19.
 */

public class HUtils {

    public static <T> T checkNotNull(T t, String message) {
        if (t == null) {
            throw new NullPointerException(message);
        }
        return t;
    }

    public static boolean checkMain() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }

    public static RequestBody createJson(String jsonString) {
        checkNotNull(jsonString, "json not null!");
        return RequestBody.create(jsonString, okhttp3.MediaType.parse("application/json; charset=utf-8"));
//        return RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), jsonString);
    }


    public static <T> RequestBody createRequestBody(String key, T value) {
        Map<String, T> map = new HashMap<>();
        map.put(key, value);
        return createJson(JsonUtils.toJson(map));
    }


    public static RequestBody createRequestBodyMap(Map map) {
        return createJson(JsonUtils.toJson(map));
    }

    public static RequestBody createRequestBody(Map<String, String> map) {
        return createJson(JsonUtils.toJson(map));
    }


    public static void close(Closeable close) {
        if (close != null) {
            try {
                closeThrowException(close);
            } catch (IOException ignored) {
            }
        }
    }

    public static void closeThrowException(Closeable close) throws IOException {
        if (close != null) {
            close.close();
        }
    }


    public static Class getClass(Type type, int i) {
        if (type instanceof ParameterizedType) { // 处理泛型类型
            return getGenericClass((ParameterizedType) type, i);
        } else if (type instanceof TypeVariable) {
            return getClass(((TypeVariable) type).getBounds()[0], 0); // 处理泛型擦拭对象
        } else {// class本身也是type，强制转型
            return (Class) type;
        }
    }

    public static Type getType(Type type, int i) {
        if (type instanceof ParameterizedType) { // 处理泛型类型
            return getGenericType((ParameterizedType) type, i);
        } else if (type instanceof TypeVariable) {
            return getType(((TypeVariable) type).getBounds()[0], 0); // 处理泛型擦拭对象
        } else {// class本身也是type，强制转型
            return type;
        }
    }


    public static Class getGenericClass(ParameterizedType parameterizedType, int i) {
        Type genericClass = parameterizedType.getActualTypeArguments()[i];
        if (genericClass instanceof ParameterizedType) { // 处理多级泛型
            return (Class) ((ParameterizedType) genericClass).getRawType();
        } else if (genericClass instanceof GenericArrayType) { // 处理数组泛型
            return (Class) ((GenericArrayType) genericClass).getGenericComponentType();
        } else if (genericClass instanceof TypeVariable) { // 处理泛型擦拭对象
            return getClass(((TypeVariable) genericClass).getBounds()[0], 0);
        } else {
            return (Class) genericClass;
        }
    }

    public static Type getGenericType(ParameterizedType parameterizedType, int i) {
        Type genericType = parameterizedType.getActualTypeArguments()[i];
        if (genericType instanceof ParameterizedType) { // 处理多级泛型
            return ((ParameterizedType) genericType).getRawType();
        } else if (genericType instanceof GenericArrayType) { // 处理数组泛型
            return ((GenericArrayType) genericType).getGenericComponentType();
        } else if (genericType instanceof TypeVariable) { // 处理泛型擦拭对象
            return getClass(((TypeVariable) genericType).getBounds()[0], 0);
        } else {
            return genericType;
        }
    }

}
