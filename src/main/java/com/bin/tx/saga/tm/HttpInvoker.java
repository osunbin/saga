package com.bin.tx.saga.tm;

import com.bin.tx.saga.common.HttpCenterHelper;
import com.bin.tx.saga.common.HttpMetadata;
import com.bin.tx.saga.entity.TransactionRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.bin.tx.saga.common.HttpClientHelper.HTTP_CLIENT;

public class HttpInvoker implements Invoker{

    private static Logger logger = LoggerFactory.getLogger(HttpInvoker.class);


    /**
     *   http://127.0.0.1:8090/appName/service
     */
    @Override
    public boolean compensate(TransactionRecord transactionRecord) {
        long txid = transactionRecord.getTxid();
        String registerUrl = transactionRecord.getRegisterUrl();
        String compensateUrl = transactionRecord.getCompensateUrl();



        HttpMetadata httpMetadata =
                HttpMetadata.buildHttpMetadata(registerUrl, compensateUrl);
        String url = httpMetadata.getUrl();
        if (!httpMetadata.isDirect()) {

            //  http://127.0.0.1:8090/appName/service";
            url = HttpCenterHelper.getAddress(httpMetadata);
        }

        String params = transactionRecord.getParams();

        try {
            HTTP_CLIENT.post(url,params);
        } catch (IOException e) {
            logger.error("txid:{} http invoker {} error",txid,compensateUrl,e);
            return false;
        }
        return true;
    }



}
