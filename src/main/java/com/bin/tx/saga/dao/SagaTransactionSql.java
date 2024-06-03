package com.bin.tx.saga.dao;

import com.bin.tx.saga.entity.SagaTransaction;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Date;
import java.util.List;

public interface SagaTransactionSql {

    @SqlUpdate("INSERT INTO t_saga_tx (txid,state,priority,attachment,create_time,update_time) VALUES(:txid,:state,:priority,:attachment,:createTime,:updateTime) ")
    void insert(@BindBean SagaTransaction globalTx);

    @SqlQuery("SELECT txid,state,priority,attachment,create_time createTime,update_time updateTime FROM t_saga_tx WHERE txid = :txid ")
    SagaTransaction select(long txid);

    @SqlUpdate("UPDATE SET state = :state, update_time =:updateTime FROM t_saga_tx WHERE txid = :txid ")
    void updateTxState(@BindBean SagaTransaction globalTx);


    @SqlQuery("SELECT  txid,state,priority,attachment,create_time createTime,update_time updateTime FROM t_saga_tx WHERE state = :state order by rand() limit 200")
    List<SagaTransaction> selectTransaction(@Bind("state") int state);


    @SqlQuery("SELECT  txid,state,priority,attachment,create_time createTime,update_time updateTime FROM t_saga_tx WHERE state = :state and create_time < :date  order by create_time ASC limit 500 ")
    List<SagaTransaction> selectUnFishedTransaction(@Bind("state") int state, @Bind("date") Date date);

}
