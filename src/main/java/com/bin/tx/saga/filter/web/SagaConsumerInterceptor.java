package com.bin.tx.saga.filter.web;

import com.bin.tx.saga.core.TransactionId;
import feign.RequestInterceptor;
import feign.RequestTemplate;

import java.util.Collection;
import java.util.Map;

public class SagaConsumerInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {


        Map<String, Collection<String>> headers = requestTemplate.headers();
        Collection<String> txids = headers.get(TransactionId.GLOBAL_TXID);
        if (txids != null && txids.size() > 0) return;

        // 1、获取渗透的请求头
        Long txid = TransactionId.getTransactionId();
        if (null != txid && txid > 0) {
            requestTemplate.header(TransactionId.GLOBAL_TXID, txid + "");
        }

    }
}
