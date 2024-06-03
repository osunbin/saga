package com.bin.tx.saga.core;

import com.bin.tx.saga.entity.SagaTransaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.bin.tx.saga.dao.SagaTransactionDao.sagaTransactionDao;

public class SagaManager {

    private static Logger logger = LoggerFactory.getLogger(SagaManager.class);


    private static SagaListener sagaListener;



    public static void registerSagaListener(SagaListener listener) {
        sagaListener = listener;
    }

    public static long  startAsyncTransaction() {
        long txid = TransactionId.getAsyncTransactionId();
        startTransaction(txid);
        return txid;
    }



    public static void  startTransaction() {
        long txid = TransactionId.createTransactionId();
        startTransaction(txid);
    }

    private static void  startTransaction(long txid) {
        logger.info("start saga transaction txid:{}",txid);

        SagaTransaction globalTx = SagaTransaction.start(txid);

        sagaTransactionDao.save(globalTx);

    }

    public static void endAsyncTransaction(long txid) {
        endTransaction(txid);
    }


    public static void endTransaction(){
        long txid = TransactionId.getTransactionId();
        endTransaction(txid);
        TransactionId.cleartxid();
    }

    private static void endTransaction(long txid) {
        SagaTransaction globalTx = sagaTransactionDao.query(txid);
        if (globalTx.isStart()) {
            globalTx.end();
            sagaTransactionDao.updateTxState(globalTx);
            logger.info("end saga transaction txid:{}",txid);
            if (sagaListener != null) {
                sagaListener.pushSuccess(globalTx);
            }
        }
    }
}
