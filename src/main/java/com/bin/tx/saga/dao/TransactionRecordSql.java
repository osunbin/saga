package com.bin.tx.saga.dao;

import com.bin.tx.saga.entity.TransactionRecord;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface TransactionRecordSql {

    @SqlUpdate("INSERT INTO t_txrecord (id,txid,method_name,params,compensate_url,register_url,service_name,step,version,create_time) " +
            " VALUES(:id,:txid,:methodName,:params,:compensateUrl,:registerUrl,:serviceName, COALESCE((SELECT COUNT(txid) FROM t_txrecord t WHERE t.txid = :txid ), 0) + 1 ,:version,:createTime) ")
    void insert(@BindBean TransactionRecord record);

    @SqlQuery("SELECT id,txid,method_name methodName,params,compensate_url compensateUrl,register_url registerUrl,service_name serviceName,step,version,create_time createTime FROM t_txrecord WHERE id =:id")
    TransactionRecord selectRecord(@Bind("id") String id);
}
