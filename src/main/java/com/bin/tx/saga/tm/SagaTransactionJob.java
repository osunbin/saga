package com.bin.tx.saga.tm;


import com.bin.tx.saga.entity.SagaTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.bin.tx.saga.dao.SagaTransactionDao.sagaTransactionDao;
import static java.util.concurrent.Executors.defaultThreadFactory;

public class SagaTransactionJob {

    private static Logger logger = LoggerFactory.getLogger(SagaTransactionJob.class);


    public static PriorityBlockingQueue<SagaTransaction> queue
            = new PriorityBlockingQueue<>();


    private final ScheduledExecutorService scheService;

    private SagaProcessor processor;


    public SagaTransactionJob(int poolSize) {
        processor = new SagaProcessor();

        scheService = Executors.newScheduledThreadPool(poolSize, (run) -> {
            Thread thread = defaultThreadFactory().newThread(run);
            thread.setName("saga transaction job scheduled-" + run.getClass().getSimpleName());
            return thread;
        });


    }

    public void registerListener(CompensateListener compensateListener) {
        processor.registerListener(compensateListener);
    }

    public void start() {
        scheService.scheduleWithFixedDelay(() -> fetchCompensateToQueue(), 1, 1, TimeUnit.SECONDS);
        scheService.scheduleWithFixedDelay(() -> compensate(), 1, 1, TimeUnit.SECONDS);
        scheService.scheduleWithFixedDelay(() -> execCompensate(), 1, 1, TimeUnit.SECONDS);
    }

    /**
     * 每个1秒 获取200个随机事务组(需要补偿) 添加 事务队列中
     */
    public void fetchCompensateToQueue() {
        try (RedisLock redisLock = new RedisLock("lock:sagas")) {
            if (redisLock.acquire(5)) {
                try {
                    List<SagaTransaction> list = sagaTransactionDao.queryTransactionList();
                    logger.info("add transaction size :{} ", list.size());
                    for (SagaTransaction transaction : list) {
                        logger.info("add transaction  txid : {} ", transaction.getTxid());
                        queue.offer(transaction);
                    }
                } catch (Exception e) {
                    logger.error("addTxError ", e);
                }
            }
        }
    }

    /**
     * 每个1秒 随机获取 500个 1小时未释放的 事务组列表
     */
    public void compensate() {
        List<SagaTransaction> list = null;
        try {
            list = sagaTransactionDao.queryUnFishedTransaction(600);
        } catch (Exception e) {
            logger.error("queryUnFishedTransaction err ", e);
        }
        if (list == null || list.isEmpty()) return;

        for (SagaTransaction globalTx : list) {
            try {
                processor.compensate(globalTx);
            } catch (Exception e) {
                logger.error(" trigger compensate err", e);
            }
        }

    }

    /**
     * 每个1秒，从事务队列取出一个事务进行反向补偿
     */
    public void execCompensate() {
        while (queue.peek() != null) {
            SagaTransaction transaction = null;
            try {
                transaction = queue.take();
            } catch (InterruptedException e) {
            }
            if (transaction == null) continue;

            long txid = transaction.getTxid();


            logger.info("processor compensate  txid :{}", txid);
            try {
                try (RedisLock redisLock = new RedisLock("lock:sagas:" + txid)) {
                    if (redisLock.acquire(10)) {
                        processor.execCompensate(transaction);
                    }
                }
            } catch (Exception e) {
                logger.error("compensate error", e);
            }
        }
    }

}
