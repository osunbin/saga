package com.bin.tx.saga.entity;


import java.util.Date;

/**
 *  补偿事务步骤对象
 *  t_txcompensate
 */
public class TransactionCompensate implements Comparable<TransactionCompensate>{


    private String id;

    private long txid;

    private boolean success;


    private Date createTime;


    private Date updateTime;

    private int step;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getTxid() {
        return txid;
    }

    public void setTxid(long txid) {
        this.txid = txid;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    /**
     *  降序
     *  新加入队列的data.compareTo(队列尾data)
     */
    @Override
    public int compareTo(TransactionCompensate transactionCompensate) {
        if (step > transactionCompensate.getStep()) {
            return -1; // 向前排序
        }
        if (step < transactionCompensate.getStep()) {
            return 1;  // 队列尾
        }
        return 0;      // 队列尾
    }
}
