package org.utils.rest.restTemplate;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;
import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.CertificateException;

public class RestTemplateExtension {

    private final RestTemplate restTemplate;

    public RestTemplateExtension() {
        this.restTemplate = new RestTemplate(this.clientHttpRequestFactory());
    }

    public void setClientHttpRequestFactory(ClientHttpRequestFactory clientHttpRequestFactory) {
        this.restTemplate.setRequestFactory(clientHttpRequestFactory);
    }

    public UriComponentsBuilder uriBuilder(String scheme, String host) {
        return UriComponentsBuilder.newInstance().scheme(scheme).host(host);
    }

    public <T> RestTemplateCollector<T> get(UriComponentsBuilder uriBuilder, Class<T> responseT) {
        RestTemplateCollector<T> restTemplateCollector = new RestTemplateCollector<>();
        try {
            restTemplateCollector.setResponseEntity(this.restTemplate.getForEntity(uriBuilder.toUriString(), responseT));
            restTemplateCollector.setPassResponse(true);
        } catch (RestClientResponseException restClientResponseException) {
            restTemplateCollector.setPassResponse(false);
            restTemplateCollector.setRestClientResponseException(restClientResponseException);
        } catch (Exception exception) {
            restTemplateCollector.setPassResponse(false);
            restTemplateCollector.setGeneralException(exception);
        }
        return restTemplateCollector;
    }

    public <T> RestTemplateCollector<T> get(UriComponentsBuilder uriBuilder, HttpEntity<?> httpEntity, Class<T> responseT) {
        RestTemplateCollector<T> restTemplateCollector = new RestTemplateCollector<>();
        try {
            restTemplateCollector.setResponseEntity(this.restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.GET, httpEntity, responseT));
            restTemplateCollector.setPassResponse(true);
        } catch (RestClientResponseException restClientResponseException) {
            restTemplateCollector.setPassResponse(false);
            restTemplateCollector.setRestClientResponseException(restClientResponseException);
        } catch (Exception exception) {
            restTemplateCollector.setPassResponse(false);
            restTemplateCollector.setGeneralException(exception);
        }
        return restTemplateCollector;
    }

    public <T> RestTemplateCollector<T> put(UriComponentsBuilder uriBuilder, HttpEntity<?> httpEntity, Class<T> responseT) {
        RestTemplateCollector<T> restTemplateCollector = new RestTemplateCollector<>();
        try {
            restTemplateCollector.setResponseEntity(this.restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.PUT, httpEntity, responseT));
            restTemplateCollector.setPassResponse(true);
        } catch (RestClientResponseException restClientResponseException) {
            restTemplateCollector.setPassResponse(false);
            restTemplateCollector.setRestClientResponseException(restClientResponseException);
        } catch (Exception exception) {
            restTemplateCollector.setPassResponse(false);
            restTemplateCollector.setGeneralException(exception);
        }
        return restTemplateCollector;
    }

    public <T> RestTemplateCollector<T> post(UriComponentsBuilder uriBuilder, HttpEntity<?> httpEntity, Class<T> responseT) {
        RestTemplateCollector<T> restTemplateCollector = new RestTemplateCollector<>();
        try {
            restTemplateCollector.setResponseEntity(this.restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST, httpEntity, responseT));
            restTemplateCollector.setPassResponse(true);
        } catch (RestClientResponseException restClientResponseException) {
            restTemplateCollector.setPassResponse(false);
            restTemplateCollector.setRestClientResponseException(restClientResponseException);
        } catch (Exception exception) {
            restTemplateCollector.setPassResponse(false);
            restTemplateCollector.setGeneralException(exception);
        }
        return restTemplateCollector;
    }

    public ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setConnectionRequestTimeout(5000);
        factory.setReadTimeout(5000);
        return factory;
    }

    public ClientHttpRequestFactory clientSSLHttpRequestFactory() throws
            NoSuchAlgorithmException, KeyManagementException, KeyStoreException,
            IOException, UnrecoverableKeyException, CertificateException {

        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(new FileInputStream("keystore.jks"), "secret".toCharArray());

        SSLContext sslContextBuilder = new SSLContextBuilder()
                .loadTrustMaterial(null, new TrustSelfSignedStrategy())
                .loadKeyMaterial(keyStore, "password".toCharArray())
                .build();

        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContextBuilder);

        HttpClient httpClient = HttpClients
                .custom()
                .setSSLSocketFactory(socketFactory)
                .build();

        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
        factory.setConnectTimeout(5000);
        factory.setConnectionRequestTimeout(5000);
        factory.setReadTimeout(5000);

        return factory;
    }
}
