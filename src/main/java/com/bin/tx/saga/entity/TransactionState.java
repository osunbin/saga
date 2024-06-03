package com.bin.tx.saga.entity;

public enum TransactionState {
    /**
     * 开始
     */
    start(0),
    /**
     * 结束
     */
    end(1),
    /**
     * 补偿
     */
    compensate(2),
    /**
     * 补偿完成
     */
    compensateFinish(3);

    private int state;

    private TransactionState(int state) {
        this.state = state;
    }

    public int getState() {
        return state;
    }
}
