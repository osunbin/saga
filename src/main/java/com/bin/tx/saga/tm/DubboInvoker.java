package com.bin.tx.saga.tm;

import com.bin.tx.saga.common.DubboGenericServiceHelper;
import com.bin.tx.saga.common.JsonHelper;
import com.bin.tx.saga.entity.TransactionRecord;
import org.apache.dubbo.rpc.service.GenericService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class DubboInvoker implements Invoker{

    private static Logger logger = LoggerFactory.getLogger(DubboInvoker.class);


    @Override
    public boolean compensate(TransactionRecord record) {
        long txid = record.getTxid();
        String compensateUrl = record.getCompensateUrl();
        String registerUrl = record.getRegisterUrl();
        try {
            GenericService service = DubboGenericServiceHelper.getDubboService(registerUrl,compensateUrl);


            String compensateMethod = "";
            String params = record.getParams();
            Map<String, ?> parameterMap = JsonHelper.jsonToMap(params);
            service.$invoke(compensateMethod, new String[]{Map.class.getName()}, new Object[]{parameterMap});

            return true;
        }catch (Throwable t) {
            logger.error("txid:{} dubbo invoke compensate :{}",txid,compensateUrl,t);
            return false;
        }
    }

}
