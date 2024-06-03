package com.bin.tx.saga.entity;

import java.util.Date;

/**
 *  t_txrecord
 */
public class TransactionRecord {


    private String id;


    private long txid;



    /**
     * 本地 微服务名
     */
    private String serviceName;


    /**
     *  记录是那个方法调用
     */
    private String methodName;





    /**
     *  兼容 http  dubbo
     *  dubbo:补偿类名(接口类名)/补偿方法名
     *  http: url
     */
    private String compensateUrl;


    /**
     *  json 格式的参数
     */
    private String params;


    /**
     * 方式名：服务注册协议名[zk、nacos、eureka...]
     */
    private String registerUrl;




    private int step;

    private Date createTime = new Date();


    private String version;


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



    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }


    public String getCompensateUrl() {
        return compensateUrl;
    }

    public void setCompensateUrl(String compensateUrl) {
        this.compensateUrl = compensateUrl;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }


    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getRegisterUrl() {
        return registerUrl;
    }

    public void setRegisterUrl(String registerUrl) {
        this.registerUrl = registerUrl;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("TransactionRecord{");
        sb.append("id='").append(id).append('\'');
        sb.append(", txid=").append(txid);
        sb.append(", registerUrl='").append(registerUrl).append('\'');
        sb.append(", methodName='").append(methodName).append('\'');
        sb.append(", compensateUrl='").append(compensateUrl).append('\'');
        sb.append(", serviceName='").append(serviceName).append('\'');
        sb.append(", params='").append(params).append('\'');
        sb.append(", step=").append(step);
        sb.append(", createTime=").append(createTime);
        sb.append(", version='").append(version).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
