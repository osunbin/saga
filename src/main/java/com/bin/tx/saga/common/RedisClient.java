package com.bin.tx.saga.common;

import com.bin.tx.saga.config.SagaConfig;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisClient {


    /**
     * 服务器
     */
    protected final static String CACHE_HOST_KEY = "redis.host";

    /**
     * 服务器端口
     */
    protected final static String CACHE_PORT_KEY = "redis.port";



    /**
     * 密码
     */
    protected final static String CACHE_PWD_KEY = "cache.pwd";



    private static GenericObjectPoolConfig config = new GenericObjectPoolConfig();

    private static JedisPool jedisPool;

    public static void init() {
        String host = getRedisProp(CACHE_HOST_KEY,"127.0.0.1");
        int port = Integer.valueOf(getRedisProp(CACHE_PORT_KEY,"6379"));
        String pwd = getRedisProp(CACHE_PWD_KEY,"");
        pwd = null == pwd || "".equals(pwd.trim()) ? null : pwd;

        jedisPool = new JedisPool(config, host, port, 30000, pwd);

    }

    private static String getRedisProp(String key, String defaultProp) {
        String prop = SagaConfig.getProp(key);
        if (prop == null) {
            return defaultProp;
        }
        return prop;
    }

    public static Jedis getJedisPool() {
        return jedisPool.getResource();
    }
}
