package com.bin.tx.saga;

import com.bin.tx.saga.config.SagaConfig;
import com.bin.tx.saga.config.SagaDataSourceConfig;
import com.bin.tx.saga.core.SagaListener;
import com.bin.tx.saga.core.SagaManager;
import com.bin.tx.saga.core.SagaStepManager;
import com.bin.tx.saga.entity.SagaTransaction;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SagaTest {

    public static void main(String[] args) throws IOException {
        SagaConfig.load();



        SagaListener sagaListener = new SagaListener() {
            @Override
            public void pushSuccess(SagaTransaction globalTx) {
                System.out.println("saga 成功");
            }
        };
        SagaManager.registerSagaListener(sagaListener);

        SagaManager.startTransaction();



        Map<String,Object> param = new HashMap<>();
        param.put("saga","test");
        SagaStepManager.sagaRecord("text","/appName/service",param);




        SagaManager.endTransaction();

    }
}
