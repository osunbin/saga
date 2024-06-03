package com.bin.tx.saga;

import com.bin.tx.saga.tm.SagaTransactionJob;

public class SagaTmApp {

    public static void main(String[] args) {
        SagaTransactionJob sagaTransactionJob = new SagaTransactionJob();
        sagaTransactionJob.registerListener(null);
        sagaTransactionJob.start();
    }
}
