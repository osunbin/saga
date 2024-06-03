package com.bin.tx.saga.tm;

import com.bin.tx.saga.entity.TransactionRecord;

public class InvokerFactory implements Invoker{


    private static final String DUBBO = "dubbo";
    private static final String HTTP = "http";
    private static final String MQ = "mq";

    private Invoker dubboInvoker = new DubboInvoker();

    private Invoker httpInvoker = new HttpInvoker();

    public boolean compensate(TransactionRecord transactionRecord) {
        boolean result = false;
        String compensateUrl = transactionRecord.getCompensateUrl();
        int p = compensateUrl.indexOf("://");
        if (p <= 0) return result;
        String protocol = compensateUrl.substring(0,p);

        switch (protocol) {
            case DUBBO:
                result = dubboInvoker.compensate(transactionRecord);
                break;
            case HTTP:
                result = httpInvoker.compensate(transactionRecord);
                break;
            case MQ:
                break;
        }
        return result;
    }


}
