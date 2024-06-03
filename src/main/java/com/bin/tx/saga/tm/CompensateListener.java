package com.bin.tx.saga.tm;

import com.bin.tx.saga.entity.SagaTransaction;

public interface CompensateListener {


    void pushCompensate(SagaTransaction globalTx);

}
