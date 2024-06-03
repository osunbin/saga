package com.bin.tx.saga.core;

import com.bin.tx.saga.common.JsonHelper;
import com.bin.tx.saga.entity.SagaTransaction;
import com.bin.tx.saga.entity.TransactionRecord;



import static com.bin.tx.saga.core.LocalAppMetadata.EMPTY;
import static com.bin.tx.saga.dao.SagaTransactionDao.sagaTransactionDao;
import static com.bin.tx.saga.dao.TransactionRecordDao.transactionRecordDao;

public class SagaStepManager {


    private static  LocalAppMetadata localAppMetadata = EMPTY;



    public static void registerLocalAppMetadata(LocalAppMetadata appMetadata) {
        localAppMetadata = appMetadata;
    }



    public static void sagaRecord(String methodName,String compensateUrl,Object param) {
        TransactionRecord record = new TransactionRecord();
        record.setMethodName(methodName);
        record.setParams(JsonHelper.toJson(param));
        record.setCompensateUrl(compensateUrl);
        sagaRecord(record);
    }

    public static void sagaRecord(TransactionRecord record) {
        Long txid = TransactionId.getTransactionId();
        if (txid == null || txid == 0) return;


        record.setId(TransactionId.getLocalTransactionId());
        record.setTxid(txid);


        if (localAppMetadata != null) {
            record.setServiceName(localAppMetadata.getLocalAppName());
            record.setRegisterUrl(localAppMetadata.getRegisterUrl());
        }

        transactionRecordDao.save(record);
    }


    public static void sagaRecordFail() {
        Long txid = TransactionId.getTransactionId();
        if (txid == null || txid == 0) return;
        SagaTransaction globalTx = sagaTransactionDao.query(txid);
        if (!globalTx.isStart()) return;

        globalTx.execSuccess(false);
        sagaTransactionDao.updateTxState(globalTx);
    }

}
