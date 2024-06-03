package com.bin.tx.saga.common;

import org.apache.dubbo.config.ReferenceConfig;
import org.apache.dubbo.config.RegistryConfig;
import org.apache.dubbo.config.bootstrap.DubboBootstrap;
import org.apache.dubbo.rpc.model.ApplicationModel;
import org.apache.dubbo.rpc.service.GenericService;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DubboGenericServiceHelper {

    /**
     * 默认消费者名称--前缀
     */
    private static final String CONSUMER = "generic-consumer";

    public static Map<String, Map<String,ReferenceConfig<GenericService>>> references = new HashMap<>();




    public static GenericService getDubboService(String registerUrl,  String compensateUrl) {


        String[] urls = registerUrl.split("/");
        int urlParamSize = urls.length;

        String address = urls[0];
        String group = null;
        if (urlParamSize == 2)
            group = urls[1];


        // zookeeper://127.0.0.1:2181/group
        Map<String, ReferenceConfig<GenericService>> referenceMap = references.get(address);
        if (referenceMap == null) {
            synchronized (DubboGenericServiceHelper.class) {
                if (referenceMap == null) {
                    referenceMap = new HashMap<>();
                    references.put(address,referenceMap);
                }
            }
        }

        int p = compensateUrl.indexOf("://");
        if (p <= 0) throw new RuntimeException(compensateUrl + " 缺少协议");
        String protocol = compensateUrl.substring(0,p);
        String serviceInfo = compensateUrl.substring(p +3);
        String[] services = serviceInfo.split("/");
        String serviceGroup = null;
        String appName = null;
        String service = null;
        if (services.length == 3) {
            serviceGroup = services[0];
            appName = services[1];
            service = services[2];
        } else {
            appName = services[0];
            service = services[1];
        }

        // dubbo://group/appName/service
        ReferenceConfig<GenericService> genericServices = referenceMap.get(service);
        if (genericServices == null) {
            synchronized (DubboGenericServiceHelper.class) {
                if (genericServices == null) {
                    // 连接注册中心配置
                    RegistryConfig registryConfig = new RegistryConfig();
                    registryConfig.setAddress(address);
                    registryConfig.setRegister(false);
                    if (group != null)
                      registryConfig.setGroup(group);



                    // 泛化引用配置
                    genericServices = new ReferenceConfig<>();
                    genericServices.setGeneric("true"); // 声明为泛化接口
                    genericServices.setInterface(service);
                    genericServices.setRegistry(registryConfig);
                    genericServices.setProtocol(protocol);


                    referenceMap.put(service,genericServices);
                }
            }
        }


        return genericServices.get();
    }

}
