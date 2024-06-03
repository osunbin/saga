package com.bin.tx.saga.dao;

import com.bin.tx.saga.config.SagaDataSourceConfig;
import com.bin.tx.saga.entity.TransactionRecord;
import org.jdbi.v3.core.Jdbi;

public class TransactionRecordDao {

    public static TransactionRecordDao transactionRecordDao;

    private Jdbi jdbi;

    public TransactionRecordDao(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    static {
        transactionRecordDao = new TransactionRecordDao(SagaDataSourceConfig.getJdbi());
    }


    public void save(TransactionRecord record) {

        jdbi.useExtension(TransactionRecordSql.class, dao -> dao.insert(record));
    }


    public TransactionRecord queryRecord(String id) {
       return jdbi.withExtension(TransactionRecordSql.class, dao -> dao.selectRecord(id));
    }
}
