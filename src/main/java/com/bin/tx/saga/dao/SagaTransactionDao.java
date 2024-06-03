package com.bin.tx.saga.dao;

import com.bin.tx.saga.config.SagaDataSourceConfig;
import com.bin.tx.saga.entity.SagaTransaction;
import com.bin.tx.saga.entity.TransactionState;
import org.jdbi.v3.core.Jdbi;

import java.util.Date;
import java.util.List;

public class SagaTransactionDao {

    public static SagaTransactionDao sagaTransactionDao;

    private Jdbi jdbi;

    public SagaTransactionDao(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    static  {
        sagaTransactionDao = new SagaTransactionDao(SagaDataSourceConfig.getJdbi());
    }

    public void save(SagaTransaction globalTx) {
        jdbi.useExtension(SagaTransactionSql.class, dao -> dao.insert(globalTx));
    }

    public SagaTransaction query(long txid) {
       return jdbi.withExtension(SagaTransactionSql.class, dao -> dao.select(txid));
    }

    public void updateCompensate(SagaTransaction globalTx) {
        jdbi.useTransaction(transactionHandle -> {
            TransactionCompensateSql compensateDao = transactionHandle.attach(TransactionCompensateSql.class);
            compensateDao.insertFromRecord(globalTx.getTxid());
            SagaTransactionSql transactionDao = transactionHandle.attach(SagaTransactionSql.class);
            transactionDao.updateTxState(globalTx);
        });
    }

    public void updateTxState(SagaTransaction globalTx) {
        jdbi.useExtension(SagaTransactionSql.class, dao -> dao.updateTxState(globalTx));
    }

    public List<SagaTransaction> queryTransactionList() {
       return jdbi.withExtension(SagaTransactionSql.class, dao -> dao.selectTransaction(TransactionState.compensate.getState()));
    }



    public  List<SagaTransaction> queryUnFishedTransaction(int intervals) {

        return jdbi.withExtension(SagaTransactionSql.class, dao ->
                dao.selectUnFishedTransaction(
                        TransactionState.start.getState(),
                        new Date(System.currentTimeMillis() - intervals * 1000)));
    }


}
