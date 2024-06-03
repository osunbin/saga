package com.bin.tx.saga.entity;

import java.util.Date;

/**
 * t_saga_tx
 */
public class SagaTransaction implements Comparable<SagaTransaction> {


    private long txid;


    private int state;


    private int priority;

    private String attachment;

    private Date createTime;

    private Date updateTime;


    public static SagaTransaction start(long txid) {
        return start(txid, 2);
    }

    public static SagaTransaction start(long txid, int priority) {
        SagaTransaction transactionGroup =
                new SagaTransaction();
        transactionGroup.setTxid(txid);
        transactionGroup.setState(TransactionState.start.getState());
        transactionGroup.setPriority(priority);
        transactionGroup.setTime(new Date());
        return transactionGroup;
    }

    public void end() {
        state = TransactionState.end.getState();
        updateTime = new Date();
    }

    public boolean isStart() {
        if (TransactionState.start.getState() == state) return true;
        return false;
    }

    public void compensate() {
        this.state = TransactionState.compensate.getState();
    }

    public void compensateFinish() {
        this.state = TransactionState.compensateFinish.getState();
    }

    public long getTxid() {
        return txid;
    }

    public void setTxid(long txid) {
        this.txid = txid;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getAttachment() {
        return attachment;
    }

    public void setAttachment(String attachment) {
        this.attachment = attachment;
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

    public void updateTime()  {
        this.updateTime = new Date();
    }

    public void setTime(Date time) {
        this.createTime = this.updateTime = time;
    }

    @Override
    public int compareTo(SagaTransaction transactionGroup) {
        if (priority > transactionGroup.getPriority()) {
            return 1;
        }
        if (priority < transactionGroup.getPriority()) {
            return -1;
        }
        return 0;
    }
}
