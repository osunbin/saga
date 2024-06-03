package com.bin.tx.saga.filter.dubbo;

import com.bin.tx.saga.core.TransactionId;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;

@Activate(group = CommonConstants.PROVIDER)
public class SagaProviderFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        // 1、获取渗透的请求头
        String txid = invocation.getAttachment(TransactionId.GLOBAL_TXID);
        if (null != txid) {
            TransactionId.setTransactionId(Long.valueOf(txid));
        }
        return invoker.invoke(invocation);
    }
}