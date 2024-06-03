package com.bin.tx.saga.filter.dubbo;

import com.bin.tx.saga.core.TransactionId;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.RpcInvocation;

/**
 *  dubbo 发送方
 */
@Activate(group = CommonConstants.CONSUMER)
public class SagaConsumerFilter implements Filter {


    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        // 1、将请求头进行渗透传递
        RpcInvocation rpcInvocation = (RpcInvocation)invocation;
        String txid = invocation.getAttachment(TransactionId.GLOBAL_TXID);

        if (txid == null && "".equals(txid.trim()) && null != TransactionId.getTransactionId()) {
            rpcInvocation.setAttachment(TransactionId.GLOBAL_TXID, TransactionId.getTransactionId() + "");
        }
        return invoker.invoke(rpcInvocation);
    }
}
