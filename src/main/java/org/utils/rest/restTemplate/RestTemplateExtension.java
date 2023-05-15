package org.utils.rest.restTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Objects;

@Slf4j
public class RestTemplateExtension {
    private final RestTemplate restTemplate;

    static { disableSSLVerification(); }
    public RestTemplateExtension() { this.restTemplate = this.restTemplate(); }
    public UriComponentsBuilder uriBuilder(String scheme, String host) {
        return UriComponentsBuilder.newInstance().scheme(scheme).host(host);
    }
    public <T> ResponseAdapter<T> get(UriComponentsBuilder uriBuilder, Class<T> responseT) {
        ResponseAdapter<T> responseAdapter = new ResponseAdapter<>();
        try {
            final String uri = uriBuilder.toUriString();
            ResponseEntity<T> responseEntity = this.restTemplate.getForEntity(uri, responseT);
            responseAdapter.setResponse(responseEntity);
            responseAdapter.setPassResponse(true);
            responseAdapter.setResponseCode(responseEntity.getStatusCode().value());
        } catch (RestClientResponseException restClientResponseException) {
            responseAdapter.setPassResponse(false);
            responseAdapter.setResponseCode(restClientResponseException.getRawStatusCode());
            responseAdapter.setResponseException(restClientResponseException);
        } catch (Exception exception) {
            responseAdapter.setPassResponse(false);
            responseAdapter.setGeneralException(exception);
        }
        return responseAdapter;
    }
    public <T> ResponseAdapter<T> get(UriComponentsBuilder uriBuilder, HttpEntity<?> httpEntity, Class<T> responseT) {
        ResponseAdapter<T> responseAdapter = new ResponseAdapter<>();
        try {
            final String uri = uriBuilder.toUriString();
            ResponseEntity<T> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, httpEntity, responseT);
            responseAdapter.setResponse(responseEntity);
            responseAdapter.setPassResponse(true);
            responseAdapter.setResponseCode(responseEntity.getStatusCode().value());
        } catch (RestClientResponseException restClientResponseException) {
            responseAdapter.setPassResponse(false);
            responseAdapter.setResponseCode(restClientResponseException.getRawStatusCode());
            responseAdapter.setResponseException(restClientResponseException);
        } catch (Exception exception) {
            responseAdapter.setPassResponse(false);
            responseAdapter.setGeneralException(exception);
            responseAdapter.setResponseCode(0);
        }
        return responseAdapter;
    }

    public <T> ResponseAdapter<T> put(UriComponentsBuilder uriBuilder, HttpEntity<?> httpEntity, Class<T> responseT) {
        ResponseAdapter<T> responseAdapter = new ResponseAdapter<>();
        try {
            final String uri = uriBuilder.toUriString();
            ResponseEntity<T> responseEntity = this.restTemplate.exchange(uri, HttpMethod.POST, httpEntity, responseT);
            responseAdapter.setResponse(responseEntity);
            responseAdapter.setPassResponse(true);
            responseAdapter.setResponseCode(responseEntity.getStatusCode().value());
        } catch (RestClientResponseException restClientResponseException) {
            responseAdapter.setPassResponse(false);
            responseAdapter.setResponseCode(restClientResponseException.getRawStatusCode());
            responseAdapter.setResponseException(restClientResponseException);
        } catch (Exception exception) {
            responseAdapter.setPassResponse(false);
            responseAdapter.setGeneralException(exception);
            responseAdapter.setResponseCode(0);
        }
        return responseAdapter;
    }

    public <T> ResponseAdapter<T> post(UriComponentsBuilder uriBuilder, HttpEntity<?> httpEntity, Class<T> responseT) {
        ResponseAdapter<T> responseAdapter = new ResponseAdapter<>();
        try {
            final String uri = uriBuilder.toUriString();
            ResponseEntity<T> responseEntity = this.restTemplate.exchange(uri, HttpMethod.POST, httpEntity, responseT);
            responseAdapter.setResponse(responseEntity);
            responseAdapter.setPassResponse(true);
            responseAdapter.setResponseCode(responseEntity.getStatusCode().value());
        } catch (RestClientResponseException restClientResponseException) {
            responseAdapter.setPassResponse(false);
            responseAdapter.setResponseCode(restClientResponseException.getRawStatusCode());
            responseAdapter.setResponseException(restClientResponseException);
        } catch (Exception exception) {
            responseAdapter.setPassResponse(false);
            responseAdapter.setResponseCode(0);
            responseAdapter.setGeneralException(exception);
        }
        return responseAdapter;
    }
    public void printResponseExceptions(ResponseAdapter<JsonNode> responseAdapter) {
        if (responseAdapter != null && !responseAdapter.isPassResponse() && responseAdapter.getResponseException() != null) {
            log.error(responseAdapter.getResponseException().getMessage());
            log.error(responseAdapter.getResponseException().getStatusText());
            log.error(responseAdapter.getResponseException().getResponseBodyAsString());
            log.error(Objects.requireNonNull(responseAdapter.getResponseException().getResponseHeaders()).toString());
        } else {
            if (responseAdapter != null && responseAdapter.getGeneralException() != null) {
                log.error(responseAdapter.getGeneralException().getMessage());
            }
        }
    }

    private RestTemplate restTemplate() {
        try {
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            requestFactory.setHttpClient(this.httpClient());
            requestFactory.setConnectTimeout(5000);
            requestFactory.setConnectionRequestTimeout(5000);
            requestFactory.setReadTimeout(5000);
            return new RestTemplate(requestFactory);
        } catch (Exception exception) {
            throw new RuntimeException("rest template instance creation error " + exception.getMessage(), exception);
        }
    }

    private static void disableSSLVerification() {
        try {

            SSLContext sslContexts = SSLContext.getInstance("SSL");
            sslContexts.init(null, new TrustManager[] { new X509TrustManager() {
                @Override public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {}
                @Override public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {}
                @Override
                public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
            }}, new SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sslContexts.getSocketFactory());
            HostnameVerifier hostnameVerifier = (hostName, session) -> true;
            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

        } catch (Exception ignore) {}
    }

    private CloseableHttpClient httpClient() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        TrustStrategy trustStrategy = ((x509Certificates, s) -> true);
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, trustStrategy).build();
        SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
        return HttpClients.custom().setSSLSocketFactory(csf).build();
    }
}
