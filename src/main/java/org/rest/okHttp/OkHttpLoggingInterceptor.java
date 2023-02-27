package org.rest.okHttp;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class OkHttpLoggingInterceptor implements Interceptor {
    private final Logger logger = LoggerFactory.getLogger(OkHttpLoggingInterceptor.class);

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        long t1 = System.nanoTime();
        logger.info("Sending request start at " + t1);
        logger.info("Sending request url " + request.url());
        logger.info("Sending request headers " + request.headers());

        try (Response response = chain.proceed(request)) {
            long t2 = System.nanoTime();

            logger.info("Received response for " + response.request().url() + " in " + (t2 - t1) / 1e6d);
            logger.info("Received response code " + response.code());
            logger.info("Received response headers " + response.headers());

            try (ResponseBody responseBody = response.peekBody(Long.MAX_VALUE)) {
                logger.info("Received response body " + responseBody.string());
            } catch (Exception exception) {

            }
        } catch (Exception exception) {
            logger.error(exception.getMessage());
        }

        return chain.proceed(request);
    }
}
