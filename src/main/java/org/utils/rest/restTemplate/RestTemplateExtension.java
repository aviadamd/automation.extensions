package org.utils.rest.restTemplate;

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
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class RestTemplateExtension {
    private final RestTemplate restTemplate;

    static { disableSSLVerification(); }

    public RestTemplateExtension() {
        this.restTemplate = this.restTemplate();
    }

    public UriComponentsBuilder uriBuilder(String scheme, String host) {
        return UriComponentsBuilder.newInstance().scheme(scheme).host(host);
    }

    public <T> ResponseAdapter<T> get(UriComponentsBuilder uriBuilder, Class<T> responseT) {
        ResponseAdapter<T> responseAdapter = new ResponseAdapter<>();
        try {
            final String uri = uriBuilder.toUriString();
            responseAdapter.setResponse(this.restTemplate.getForEntity(uri, responseT));
            responseAdapter.setPassResponse(true);
        } catch (RestClientResponseException restClientResponseException) {
            responseAdapter.setPassResponse(false);
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
            responseAdapter.setResponse(this.restTemplate.exchange(uri, HttpMethod.GET, httpEntity, responseT));
            responseAdapter.setPassResponse(true);
        } catch (RestClientResponseException restClientResponseException) {
            responseAdapter.setPassResponse(false);
            responseAdapter.setResponseException(restClientResponseException);
        } catch (Exception exception) {
            responseAdapter.setPassResponse(false);
            responseAdapter.setGeneralException(exception);
        }
        return responseAdapter;
    }

    public <T> ResponseAdapter<T> put(UriComponentsBuilder uriBuilder, HttpEntity<?> httpEntity, Class<T> responseT) {
        ResponseAdapter<T> responseAdapter = new ResponseAdapter<>();
        try {
            final String uri = uriBuilder.toUriString();
            responseAdapter.setResponse(this.restTemplate.exchange(uri, HttpMethod.PUT, httpEntity, responseT));
            responseAdapter.setPassResponse(true);
        } catch (RestClientResponseException restClientResponseException) {
            responseAdapter.setPassResponse(false);
            responseAdapter.setResponseException(restClientResponseException);
        } catch (Exception exception) {
            responseAdapter.setPassResponse(false);
            responseAdapter.setGeneralException(exception);
        }
        return responseAdapter;
    }

    public <T> ResponseAdapter<T> post(UriComponentsBuilder uriBuilder, HttpEntity<?> httpEntity, Class<T> responseT) {
        ResponseAdapter<T> responseAdapter = new ResponseAdapter<>();
        try {
            final String uri = uriBuilder.toUriString();
            responseAdapter.setResponse(this.restTemplate.exchange(uri, HttpMethod.POST, httpEntity, responseT));
            responseAdapter.setPassResponse(true);
        } catch (RestClientResponseException restClientResponseException) {
            responseAdapter.setPassResponse(false);
            responseAdapter.setResponseException(restClientResponseException);
        } catch (Exception exception) {
            responseAdapter.setPassResponse(false);
            responseAdapter.setGeneralException(exception);
        }
        return responseAdapter;
    }

    private RestTemplate restTemplate() {
        try {
            TrustStrategy trustStrategy = ((x509Certificates, s) -> true);
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, trustStrategy).build();
            SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
            CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(csf).build();
            HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
            requestFactory.setHttpClient(httpClient);
            requestFactory.setConnectTimeout(5000);
            requestFactory.setConnectionRequestTimeout(5000);
            requestFactory.setReadTimeout(5000);
            return new RestTemplate(requestFactory);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
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
}
