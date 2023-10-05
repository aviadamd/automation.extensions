package org.utils.rest.okHttp;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class OkHttpBuilderExtensions {
    private Request.Builder requestBuilder = new Request.Builder();

    public OkHttpBuilderExtensions withRequestBuilder(Request.Builder requestBuilder) {
        this.requestBuilder = requestBuilder;
        return this;
    }

    public FormBody setBodyMap(boolean isAddEncoded, Map<String, String> bodyMap) {
        FormBody.Builder builder = new FormBody.Builder();
        if (isAddEncoded) {
            bodyMap.forEach(builder::addEncoded);
        } else bodyMap.forEach(builder::add);
        return builder.build();
    }

    public ResponseHandler execute() {
        OkHttpLoggingInterceptor okHttpLoggingInterceptor = new OkHttpLoggingInterceptor();

        try {

            this.okHttpClient()
                    .addInterceptor(okHttpLoggingInterceptor)
                    .build()
                    .newCall(this.requestBuilder.build())
                    .execute();

        } catch (IOException ioException) {
            throw new RuntimeException(ioException);
        }

        return okHttpLoggingInterceptor.getResponse();
    }

    private OkHttpClient.Builder okHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();

        try {

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{ TRUST_ALL_CERTS }, new java.security.SecureRandom());

            builder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) TRUST_ALL_CERTS)
                    .hostnameVerifier((s, sslSession) -> true)
                    .connectTimeout(10, TimeUnit.SECONDS)
                    .writeTimeout(10, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS);

        } catch (Exception exception) {
            throw new RuntimeException("init okHttpClient error ", exception);
        }

        return builder;
    }

    private final TrustManager TRUST_ALL_CERTS = new X509TrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) {}

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) {}

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[] {};
        }
    };

}
