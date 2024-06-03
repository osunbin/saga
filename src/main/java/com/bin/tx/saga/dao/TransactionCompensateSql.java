package com.bin.tx.saga.dao;

import com.bin.tx.saga.entity.TransactionCompensate;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface TransactionCompensateSql {

    /**
     * 根据事务组步骤反向生成 补偿事务步骤
     */
    @SqlUpdate("INSERT INTO t_txcompensate(id, txid,success, create_time, update_time, step) SELECT id,txid, 0, NOW(), NOW(), step FROM t_txrecord where txid = :txid ")
    void insertFromRecord(@Bind("txid") long txid);

    @SqlQuery("SELECT id, txid,success, create_time createTime, update_time updateTime, step FROM t_txcompensate WHERE txid = :txid")
    List<TransactionCompensate> selectCompensateListByTxid(@Bind("txid") long txid);

    @SqlQuery("SELECT count(id) from t_txcompensate where txid = :txid  and success = :success order by create_time")
    int selectCountCompensate(@Bind("txid") long txid,@Bind("success") boolean success);


    @SqlQuery("SELECT  id, txid,  success, create_time, update_time, step FROM t_txcompensate WHERE id =:id ")
    TransactionCompensate selectCompensate(@Bind("id") String id);

    @SqlUpdate("UPDATE t_txcompensate SET success = :success,update_time = :updateTime WHERE id =:id")
    void updateSuccess(@BindBean TransactionCompensate compensate);
}
