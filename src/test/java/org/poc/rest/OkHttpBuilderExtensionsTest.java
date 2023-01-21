package org.poc.rest;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.rest.okHttp.OkHttpBuilderExtensions;
import org.rest.okHttp.ResponseCollector;
import java.io.IOException;
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
                .setRequestBuilder(request)
                .build();

        if (optionalResponse.isPassRequest()) {
            log.info(optionalResponse.toString());
            log.info(optionalResponse.getResponse().headers().toString());
        }
    }

    @Test
    public void testPostRequest() throws IOException {
        Headers headers = Headers.of(Map.of("Content-Type","application/json"));
        FormBody body = this.okHttpBuilderExtensions.setBodyMap(false, Map.of("title","foo","body","1","userId","1"));

        Request.Builder request = new Request.Builder()
                .url(new HttpUrl.Builder()
                        .scheme("http")
                        .host("jsonplaceholder.typicode.com")
                        .addPathSegment("posts")
                        .build())
                .headers(headers)
                .post(body);

        ResponseCollector responseCollector = this.okHttpBuilderExtensions.setRequestBuilder(request).build();
        try (ResponseBody responseBody = responseCollector.getResponse().peekBody(Long.MAX_VALUE)) {
            log.info(responseBody.string());
        }
    }
}
