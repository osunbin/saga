package com.bin.tx.saga.tm;

import com.bin.tx.saga.entity.TransactionRecord;

public interface Invoker {


    boolean compensate(TransactionRecord transactionRecord);
}
