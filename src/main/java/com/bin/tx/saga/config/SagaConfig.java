package com.bin.tx.saga.config;

import com.bin.tx.saga.common.RedisClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class SagaConfig {

    private static  Properties prop;

    public static void load() throws IOException {
        load(null);
    }

    public static void load(String path) throws IOException {
        if (path == null || "".equals(path.trim()))
            path = "saga.properties";
        InputStream inputStream = SagaConfig.class.getResourceAsStream(path);
        Properties properties = new Properties();
        properties.load(inputStream);
        prop = properties;

        initEnv();
    }

    private static void initEnv() {
        SagaDataSourceConfig.initDataSource();
        RedisClient.init();
    }

    public static String getProp(String key) {
        if (prop == null)
            throw new RuntimeException("还未初始化配置");

      return   prop.getProperty(key);
    }
}
