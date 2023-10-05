package org.component.rest;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.utils.rest.okHttp.OkHttpBuilderExtensions;
import org.utils.rest.okHttp.ResponseHandler;
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

        ResponseHandler optionalResponse = this.okHttpBuilderExtensions
                .withRequestBuilder(request)
                .execute();
    }

    @Test
    public void testPostRequest() {
        Map<String, String> bodyMap = Map.of("title", "foo", "body", "1", "userId", "1");
        FormBody formBody = this.okHttpBuilderExtensions.setBodyMap(false, bodyMap);

        Request.Builder request = new Request.Builder()
                .url(new HttpUrl.Builder()
                        .scheme("http")
                        .host("jsonplaceholder.typicode.com")
                        .addPathSegment("posts")
                        .build())
                .headers(Headers.of(Map.of("Content-Type","application/json")))
                .post(formBody);

        ResponseHandler responseHandler = this.okHttpBuilderExtensions
                .withRequestBuilder(request)
                .execute();
    }
}
