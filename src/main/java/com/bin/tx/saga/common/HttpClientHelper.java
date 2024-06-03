package com.bin.tx.saga.common;


import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class HttpClientHelper {


    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static final HttpClientHelper HTTP_CLIENT = new HttpClientHelper();



    private OkHttpClient client;

    private HttpClientHelper() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.writeTimeout(10, TimeUnit.SECONDS);
        client = builder.build();
    }



    /**
     * Post string.
     *
     * @param url  the url
     * @param json the json
     * @return the string
     * @throws IOException the io exception
     */
    public String post(final String url, final String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        return client.newCall(request).execute().body().string();

    }


    public <T> T post(final String url, final String json, final Class<T> classOfT) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        assert response.body() != null;
        final String result = response.body().string();
        return JsonHelper.fromJson(result, classOfT);

    }



    public String post(final String url, final Map<String, String> params) throws IOException {
        String json =  JsonHelper.toJson(params);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        return client.newCall(request).execute().body().string();
    }


}
