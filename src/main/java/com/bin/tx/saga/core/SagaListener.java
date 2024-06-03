package com.bin.tx.saga.core;

import com.bin.tx.saga.entity.SagaTransaction;

public interface SagaListener {


    void pushSuccess(SagaTransaction globalTx);


}
