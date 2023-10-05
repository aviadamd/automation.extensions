package org.utils.rest.okHttp;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class OkHttpLoggingInterceptor implements Interceptor {

    private ResponseHandler responseHandler;

    private final Logger logger = LoggerFactory.getLogger(OkHttpLoggingInterceptor.class);
    public ResponseHandler getResponse() {
        return this.responseHandler;
    }

    @Override
    public @NotNull Response intercept(Chain chain) throws IOException {
        this.responseHandler = new ResponseHandler();

        Request request = chain.request();

        this.responseHandler.setRequest(request);

        try (Response response = chain.proceed(request)) {

            this.responseHandler.setCode(response.code());
            this.responseHandler.setHeadersMap(response.headers().toMultimap());

            try (ResponseBody responseBody = response.peekBody(Long.MAX_VALUE)) {
                this.responseHandler.setResponseBody(responseBody.string());
            } catch (Exception exception) {
                this.responseHandler.setResponseBody("");
            }

        } catch (Exception exception) {
            this.responseHandler.setException(exception.getMessage());
        }

        logger.debug(responseHandler.printPretty());

        return chain.proceed(request);
    }


}
