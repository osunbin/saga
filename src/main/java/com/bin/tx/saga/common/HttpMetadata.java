package com.bin.tx.saga.common;

public class HttpMetadata {
    private String regProtocol;
    private String regAddress;
    private String appName;
    private String url;
    private boolean direct = false;

    /**
     * @param registerUrl   eureka://127.0.0.1:7080/eureka
     * @param compensateUrl http://appName/service/func
     */
    public static HttpMetadata buildHttpMetadata(String registerUrl, String compensateUrl) {
        HttpMetadata httpMetadata = new HttpMetadata();


        if (registerUrl == null || "".equals(registerUrl.trim())) {

            httpMetadata.setUrl(compensateUrl);
            httpMetadata.setDirect(true);
        } else {


            String[] compensateUrlInfo = compensateUrl.split("://");
            if (compensateUrlInfo.length == 2) {
                compensateUrl = compensateUrlInfo[1];
            }

            if (compensateUrl.startsWith("/")) {
                compensateUrl = compensateUrl.substring(1);
            }

            int pos = compensateUrl.indexOf("/");

            String appName = compensateUrl.substring(0, pos);

            String realUrl = compensateUrl.substring(pos);

            httpMetadata.setAppName(appName);
            httpMetadata.setUrl(realUrl);


            String[] registerInfos = registerUrl.split("://");
            if (registerInfos.length == 1) {
                httpMetadata.setRegAddress(registerUrl);
            } else if (registerInfos.length == 2) {
                httpMetadata.setRegProtocol(registerInfos[0]);
                httpMetadata.setRegAddress(registerInfos[1]);
            }
        }
        return httpMetadata;
    }


    public String getRegProtocol() {
        return regProtocol;
    }

    public void setRegProtocol(String regProtocol) {
        this.regProtocol = regProtocol;
    }

    public String getRegAddress() {
        return regAddress;
    }

    public void setRegAddress(String regAddress) {
        this.regAddress = regAddress;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isDirect() {
        return direct;
    }

    public void setDirect(boolean direct) {
        this.direct = direct;
    }
}
