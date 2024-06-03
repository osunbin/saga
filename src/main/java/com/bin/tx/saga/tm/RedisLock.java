package com.bin.tx.saga.tm;


import com.bin.tx.saga.common.RedisClient;
import com.bin.tx.saga.config.SagaConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.params.SetParams;

import java.util.concurrent.ThreadLocalRandom;

public class RedisLock implements AutoCloseable {


    private Jedis jedis;
    private final String lockKey;
    private boolean lock = false;
    private boolean useLock = false;


    public RedisLock(String lock) {
        this.lockKey = lock;
        String use = SagaConfig.getProp("use.redis.lock");
        if (use != null && !"".equals(use)) {
            if (Boolean.getBoolean(use)) {
                useLock = true;
            }

        }
        if (useLock)
          jedis = RedisClient.getJedisPool();
    }

    public boolean acquire(long second) {
        if (!useLock) return true;
        return acquireLock(second);
    }

    public boolean acquireLock(long second) {

        String value = Thread.currentThread().getName() + ThreadLocalRandom.current().nextInt(500);

        SetParams setParams = SetParams.setParams().ex(second).nx();
        String setex = jedis.set(lockKey, value, setParams);
        if ("OK".equals(setex)) {
            lock = true;
        }
        return lock;
    }

    @Override
    public void close() {
        if (!useLock) return;
        jedisClose();
    }

    private void jedisClose() {
        if (lock)  jedis.del(lockKey);
        jedis.close();
    }
}
