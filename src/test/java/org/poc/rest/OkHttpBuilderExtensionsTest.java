package org.poc.rest;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rest.okHttp.OkHttpBuilderExtensions;
import org.rest.okHttp.ResponseCollector;
import java.util.Map;

@Slf4j
public class OkHttpBuilderExtensionsTest {
    private OkHttpBuilderExtensions okHttpBuilderExtensions;
    @BeforeEach
    public void init() {
        this.okHttpBuilderExtensions = new OkHttpBuilderExtensions();
    }

    @Test
    public void testGetRequest() {
        HttpUrl.Builder httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host("jsonplaceholder.typicode.com")
                .addPathSegment("comments")
                .addQueryParameter("postId","2");

        Request.Builder request = new Request.Builder()
                .get()
                .url(httpUrl.build())
                .header("Content-Type", "application/json");

        ResponseCollector optionalResponse = this.okHttpBuilderExtensions
                .withDebugMode(true)
                .withInterceptor(true)
                .withRequestBuilder(request)
                .build();

        if (optionalResponse.isPassRequest()) {
            log.info(optionalResponse.getResponseData().getHeadersMap().toString());
            log.info(optionalResponse.getResponseData().getResponseBody());
        }
    }

    @Test
    public void testPostRequest() {
        FormBody body = this.okHttpBuilderExtensions.setBodyMap(
                false, Map.of("title","foo","body","1","userId","1"));

        Request.Builder request = new Request.Builder()
                .url(new HttpUrl.Builder()
                        .scheme("http")
                        .host("jsonplaceholder.typicode.com")
                        .addPathSegment("posts")
                        .build())
                .headers(Headers.of(Map.of("Content-Type","application/json")))
                .post(body);

        ResponseCollector responseCollector = this.okHttpBuilderExtensions.withRequestBuilder(request).build();
        log.info(responseCollector.getResponseData().getResponseBody());
    }
}
