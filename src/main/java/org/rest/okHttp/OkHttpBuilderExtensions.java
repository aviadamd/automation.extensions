package org.rest.okHttp;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Slf4j
public class OkHttpBuilderExtensions {
    private OkHttpClient okHttpClientInstance;
    private Request.Builder requestBuilder = new Request.Builder();

    public OkHttpBuilderExtensions setRequestBuilder(Request.Builder requestBuilder) {
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
        Optional<Response> response = Optional.empty();

        try {

            if (this.okHttpClientInstance == null) {
                this.okHttpClientInstance = this.okHttpClient();
            }

            response = Optional.ofNullable(Observable.just(this.okHttpClientInstance.newCall(this.requestBuilder.build()).execute())
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.newThread())
                    .blockingFirst());

            if (response.isPresent()) {
                if (response.get().isSuccessful()) {
                    return new ResponseCollector(true, response.get(), "");
                } else return new ResponseCollector(false, response.get(), "");
            }
        } catch (Exception exception) {
            return new ResponseCollector(false, response.orElse(null), exception.getMessage());
        }

        return new ResponseCollector(false, null, "");
    }
    private OkHttpClient okHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();

        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{ TRUST_ALL_CERTS }, new java.security.SecureRandom());
            builder.sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) TRUST_ALL_CERTS);
            builder.hostnameVerifier((s, sslSession) -> true);
        } catch (Exception exception) {
            log.info("init okHttpClient ssl trust factory error: " + exception.getMessage());
        }

        try {
            builder.addInterceptor(new OkHttpLoggingInterceptor());
        } catch (Exception exception) {
            log.info("init okHttpClient addInterceptor error: " + exception.getMessage());
        }

        return builder.connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
    }

    private final TrustManager TRUST_ALL_CERTS = new X509TrustManager() {
        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
            Arrays.asList(chain).forEach(print -> log.info("checkClientTrusted.getSigAlgName" + print.getSigAlgName()));
        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
            Arrays.asList(chain).forEach(print -> log.info("checkServerTrusted.getSigAlgName" + print.getSigAlgName() + ", authType " + authType));
        }

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[] {};
        }
    };

}
