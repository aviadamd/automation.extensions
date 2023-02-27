package org.rest.okHttp;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.http.HttpException;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.HttpStatusCodeException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
public class OkHttpBuilderExtensions {
    private boolean isWithInterceptor = false;

    private boolean isWithDebugMode = false;
    private Request.Builder requestBuilder = new Request.Builder();

    public OkHttpBuilderExtensions withDebugMode(boolean isWithInterceptor) {
        this.isWithInterceptor = isWithInterceptor;
        return this;
    }

    public OkHttpBuilderExtensions withInterceptor(boolean isWithInterceptor) {
        this.isWithInterceptor = isWithInterceptor;
        return this;
    }

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

    public ResponseCollector build() {
        try(Response response = this.okHttpClient().newCall(this.requestBuilder.build()).execute()) {
            return new ResponseCollector(response.isSuccessful(), response,"");
        } catch (HttpClientErrorException | HttpServerErrorException httpException) {
            if (!this.isWithDebugMode)
                Assertions.fail("error " + httpException.getResponseBodyAsString(), httpException);
            return new ResponseCollector(false, null, httpException.getResponseBodyAsString());
        } catch (Exception exception) {
            if (!this.isWithDebugMode)
                Assertions.fail(exception);
            return new ResponseCollector(false, null, exception.getMessage());
        }
    }
    private OkHttpClient okHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();

        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{ TRUST_ALL_CERTS }, new java.security.SecureRandom());
            builder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) TRUST_ALL_CERTS);
            builder.hostnameVerifier((s, sslSession) -> true);
            if (this.isWithInterceptor) builder.addInterceptor(new OkHttpLoggingInterceptor());
        } catch (Exception exception) {
            log.info("init okHttpClient error: " + exception.getMessage());
        }

        return builder.connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
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
