package com.bin.tx.saga.common;

import com.netflix.appinfo.ApplicationInfoManager;
import com.netflix.appinfo.EurekaInstanceConfig;
import com.netflix.appinfo.InstanceInfo;
import com.netflix.appinfo.MyDataCenterInstanceConfig;
import com.netflix.discovery.DefaultEurekaClientConfig;
import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.EurekaClient;
import com.netflix.discovery.shared.Application;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class HttpCenterHelper {

    private static EurekaClient eurekaClient;


    public static String getAddress(HttpMetadata httpMetadata) {
        String regProtocol = httpMetadata.getRegProtocol();

        if (regProtocol == null || "eureka".equals(regProtocol)) {
            String address = getUrlFromEureka(httpMetadata.getRegAddress(), httpMetadata.getAppName());
            return address + httpMetadata.getUrl();
        } else if ("nacos".equals(regProtocol)) {


        }

        return "";
    }


    private static String getUrlFromEureka(String ipAddr, String appName) {
        if (eurekaClient == null) {
            synchronized (HttpCenterHelper.class) {
                if (eurekaClient == null) {
                    eurekaClient = createEurekaClient(ipAddr);
                }
            }
        }

        Application application = eurekaClient.getApplication(appName);
        List<InstanceInfo> instances = application.getInstances();
        int size = instances.size();
        int index = ThreadLocalRandom.current().nextInt(size);
        InstanceInfo instanceInfo = instances.get(index);
        String hostName = instanceInfo.getHostName();
        int port = instanceInfo.getPort();
        return "http://" + hostName + ":" + port;
    }

    private static EurekaClient createEurekaClient(String ipAddr) {
        InstanceInfo instanceInfo = InstanceInfo.Builder.newBuilder()
                .setInstanceId("saga-1") // 实例id。在同一个应用appName的范围内是惟一的
                .setHostName("saga-" + ipAddr)
                .setIPAddr(ipAddr)
                .setAppName("saga")
                .build();

        return new DiscoveryClient(
                new ApplicationInfoManager(new MyDataCenterInstanceConfig(), instanceInfo),
                new DefaultEurekaClientConfig());
    }

}
