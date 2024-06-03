package com.bin.tx.saga.tm;


import com.bin.tx.saga.entity.SagaTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.bin.tx.saga.dao.SagaTransactionDao.sagaTransactionDao;
import static java.util.concurrent.Executors.defaultThreadFactory;

public class SagaTransactionJob {

    private static Logger logger = LoggerFactory.getLogger(SagaTransactionJob.class);





    private final ScheduledExecutorService scheService;

    private SagaProcessor processor;


    public SagaTransactionJob() {
        processor = new SagaProcessor();

        scheService = Executors.newScheduledThreadPool(2, (run) -> {
            Thread thread = defaultThreadFactory().newThread(run);
            thread.setName("saga transaction job scheduled-" + run.getClass().getSimpleName());
            return thread;
        });


    }

    public void registerListener(CompensateListener compensateListener) {
        processor.registerListener(compensateListener);
    }

    public void start() {
        scheService.scheduleWithFixedDelay(() -> timeoutCompensate(), 1, 5, TimeUnit.SECONDS);
        scheService.scheduleWithFixedDelay(() -> execCompensate(), 1, 1, TimeUnit.SECONDS);
    }

    /**
     *  补偿
     */
    public void execCompensate() {

        try {
            List<SagaTransaction> list = sagaTransactionDao.queryTransactionList();
            logger.info("fetch transaction size :{} ", list.size());
            for (SagaTransaction transaction : list) {
                long txid = transaction.getTxid();
                logger.info("processor compensate  txid :{}", txid);
                try (RedisLock redisLock =
                             new RedisLock("lock:sagas:" + txid)) {
                    if (redisLock.acquire(30)) {
                        processor.execCompensate(transaction);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("addTxError ", e);
        }
    }


    public void timeoutCompensate() {
        List<SagaTransaction> list = null;
        try {
            // 1 小时
            list = sagaTransactionDao.queryUnFishedTransaction(600);
        } catch (Exception e) {
            logger.error("queryUnFishedTransaction err ", e);
        }
        if (list == null || list.isEmpty()) return;

        for (SagaTransaction globalTx : list) {
            try {
                logger.info("timeout compensate txid=" + globalTx.getTxid());
                globalTx.compensate();
                sagaTransactionDao.updateCompensate(globalTx);
            } catch (Exception e) {
                logger.error(" trigger compensate err", e);
            }
        }

    }

}
