package com.bin.tx.saga.dao;

import com.bin.tx.saga.config.SagaDataSourceConfig;
import com.bin.tx.saga.entity.TransactionCompensate;
import com.bin.tx.saga.entity.TransactionState;
import org.jdbi.v3.core.Jdbi;

import java.util.List;

public class TransactionCompensateDao {


    public static TransactionCompensateDao transactionCompensateDao;

    private Jdbi jdbi;

    public TransactionCompensateDao(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    static  {
        transactionCompensateDao = new TransactionCompensateDao(SagaDataSourceConfig.getJdbi());
    }




    public List<TransactionCompensate> queryCompensateListByTxid(long txid) {
       return jdbi.withExtension(TransactionCompensateSql.class,dao -> dao.selectCompensateListByTxid(txid));
    }

    public boolean stillHaveUnFinshedCompensationInTransaction(long txid) {
        return 0 < jdbi.withExtension(TransactionCompensateSql.class,dao -> dao.selectCountCompensate(txid, TransactionState.start.getState()));
    }

    public TransactionCompensate queryCompensate(String id) {
       return jdbi.withExtension(TransactionCompensateSql.class,dao -> dao.selectCompensate(id));
    }

    public void updateSuccess(TransactionCompensate transactionCompensate) {
        jdbi.useExtension(TransactionCompensateSql.class, dao -> dao.updateSuccess(transactionCompensate));
    }
}
