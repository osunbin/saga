package com.bin.tx.saga.common;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class JsonHelper {

    private static final Gson gson = new Gson();

    public static String toJson(Object resp) {
        return gson.toJson(resp);
    }


    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }

    public static <T> T fromJson(String json, Type type) {
        return gson.fromJson(json, type);
    }


    public static <T> List<T> strToList(String gsonString, Class<T> cls) {
        return gson.fromJson(gsonString, new TypeToken<List<T>>() {
        }.getType());
    }


    public static Map<String, ?> jsonToMap(String data) {
        Type mapType = new TypeToken<Map<String, Object>>() {
        }.getType();
        return gson.fromJson(data, mapType);
    }


    public static void main(String[] args) {
        String registerUrl = "nacos://1231";
        int p = registerUrl.indexOf("://");
        String protocol = registerUrl.substring(0,p);
        String url = registerUrl.substring(p + 3);
        System.out.println(protocol);
        System.out.println(url);
    }


}