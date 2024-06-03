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


    public static long startAsyncSaga() {
        long txid = TransactionId.getAsyncTransactionId();
        startTransaction(txid);
        return txid;
    }


    public static void startSaga() {
        long txid = TransactionId.createTransactionId();
        startTransaction(txid);
    }

    private static void startTransaction(long txid) {
        logger.info("start saga transaction txid:{}", txid);

        SagaTransaction globalTx = SagaTransaction.start(txid);

        sagaTransactionDao.save(globalTx);

    }


    public static void sagaSuccess(long txid) {
        endTransaction(txid, true);
    }

    public static void sagaFail(long txid) {
        endTransaction(txid, false);
    }

    public static void sagaSuccess() {
        endTransaction(true);
    }

    public static void sagaFail() {
        endTransaction(false);
    }


    private static void endTransaction(boolean success) {
        long txid = TransactionId.getTransactionId();
        endTransaction(txid, success);
        TransactionId.cleartxid();
    }

    private static void endTransaction(long txid, boolean success) {
        SagaTransaction globalTx = sagaTransactionDao.query(txid);
        if (!globalTx.isStart()) return;

        globalTx.execSuccess(success);
        sagaTransactionDao.updateTxState(globalTx);
        logger.info("end saga transaction  txid:{} success:{}", txid, success);

        if (success && sagaListener != null) {
            sagaListener.pushSuccess(globalTx);
        }

    }

}