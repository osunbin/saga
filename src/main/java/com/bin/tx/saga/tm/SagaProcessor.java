package com.bin.tx.saga.tm;

import com.bin.tx.saga.entity.SagaTransaction;
import com.bin.tx.saga.entity.TransactionCompensate;
import com.bin.tx.saga.entity.TransactionRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

import static com.bin.tx.saga.dao.SagaTransactionDao.sagaTransactionDao;
import static com.bin.tx.saga.dao.TransactionCompensateDao.transactionCompensateDao;
import static com.bin.tx.saga.dao.TransactionRecordDao.transactionRecordDao;

public class SagaProcessor {

    private static Logger logger = LoggerFactory.getLogger(SagaProcessor.class);

    private Invoker invoker = new InvokerFactory();

    private CompensateListener compensateListener;




    public void registerListener(CompensateListener compensateListener) {
        this.compensateListener = compensateListener;
    }

    public void compensate(SagaTransaction globalTx) {
        logger.info(" processor trigger txid=" + globalTx.getTxid());
        globalTx.compensate();
        globalTx.updateTime();
        sagaTransactionDao.updateCompensate(globalTx);
    }


    public void execCompensate(SagaTransaction globalTx) throws Exception {
        List<TransactionCompensate> transactionCompensates =
                transactionCompensateDao.queryCompensateListByTxid(globalTx.getTxid());
        PriorityBlockingQueue<TransactionCompensate> queue =
                new PriorityBlockingQueue<>(transactionCompensates);

        while (queue.peek() != null) {
            if (!compensateRecord(queue.take())) {
                break;
            }
        }
        compensateHook(globalTx);
    }

    public boolean compensateRecord(TransactionCompensate transactionCompensate) {
        long txid = transactionCompensate.getTxid();
        String id = transactionCompensate.getId();
        logger.info(" processor compensate record txid:{}  actionid: {}",txid, id);
        TransactionRecord transactionRecord =  transactionRecordDao.queryRecord(id);

        boolean compensateSuccess = invoker.compensate(transactionRecord);
        if (compensateSuccess) {
            transactionCompensate.setSuccess(true);
            transactionCompensate.setUpdateTime(new Date());
            transactionCompensateDao.updateSuccess(transactionCompensate);
        }
        return compensateSuccess;
    }

    private void compensateHook(SagaTransaction globalTx) {
        long txid = globalTx.getTxid();
        logger.info(" processor compensate hook txid :{}", txid);
        boolean finishAllCompensate =
                !transactionCompensateDao.stillHaveUnFinshedCompensationInTransaction(txid);
        if (finishAllCompensate) {
            globalTx.compensateFinish();
            globalTx.updateTime();
            sagaTransactionDao.updateTxState(globalTx);
            logger.info("compensate finish transaction txid :{}", txid);
            if (compensateListener != null)
                compensateListener.pushCompensate(globalTx);
        }
    }

}
