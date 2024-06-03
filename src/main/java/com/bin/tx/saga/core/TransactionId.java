package com.bin.tx.saga.core;

import java.math.BigInteger;
import java.util.UUID;

public class TransactionId {

    private static ThreadLocal<Long> context = new ThreadLocal<Long>();



    public static final String GLOBAL_TXID = "txid";

    public static long createTransactionId() {
        long txid = new BigInteger(UUID.randomUUID().toString().replaceAll("-", ""), 16).longValue();
        context.set(txid);
        return txid;
    }

    public static long getAsyncTransactionId() {
        return new BigInteger(UUID.randomUUID().toString().replaceAll("-", ""), 16).longValue();
    }

    public static String getLocalTransactionId() {
       return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static Long getTransactionId() {
        return context.get();
    }

    public static void setTransactionId(Long txid) {
        context.set(txid);
    }

    public static void cleartxid() {
        context.remove();
    }



}
