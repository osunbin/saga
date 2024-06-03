package com.bin.tx.saga.tm;


import com.bin.tx.saga.common.RedisClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.concurrent.ThreadLocalRandom;

public class RedisLock implements AutoCloseable {


    private Jedis jedis;
    private final String lockKey;
    private boolean lock;
    public RedisLock(String lock) {
        this.lockKey = lock;
        jedis = RedisClient.getJedisPool();
    }


    public boolean acquire(long second) {
        String value = Thread.currentThread().getName() + ThreadLocalRandom.current().nextInt(500);

        SetParams setParams = SetParams.setParams().ex(second).nx();
        String setex = jedis.set(lockKey, value, setParams);
        if ("OK".equals(setex)) {
            lock = true;
            return true;
        }
        lock = false;
        return false;
    }

    @Override
    public void close() {
        if (lock)  jedis.del(lockKey);
        jedis.close();
    }
}
