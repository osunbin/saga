package com.bin.tx.saga.filter.web;

import com.bin.tx.saga.core.TransactionId;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SagaProviderInterceptor extends HandlerInterceptorAdapter {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1、获取渗透的请求头
        String txid =  request.getHeader(TransactionId.GLOBAL_TXID);
        if (null != txid) {
            TransactionId.setTransactionId(Long.valueOf(txid));
        }
        return true;
    }
}
