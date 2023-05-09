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
        this.restTemplate = new RestTemplate(this.httpRequestFactory(null));
    }

    public void setClientHttpRequestFactory(ClientHttpRequestFactory clientHttpRequestFactory) {
        this.restTemplate.setRequestFactory(clientHttpRequestFactory);
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

    public ClientHttpRequestFactory clientSSLHttpRequestFactory() throws NoSuchAlgorithmException,
            KeyManagementException, KeyStoreException, IOException, UnrecoverableKeyException, CertificateException {
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(new FileInputStream("keystore.jks"), "secret".toCharArray());
        SSLContext sslContextBuilder = this.sslContext(keyStore);
        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContextBuilder);
        HttpClient httpClient = this.httpClient(socketFactory);
        return this.httpRequestFactory(httpClient);
    }

    private SSLContext sslContext(KeyStore keyStore) throws NoSuchAlgorithmException,
            KeyStoreException, UnrecoverableKeyException, KeyManagementException {
        return new SSLContextBuilder()
                .loadTrustMaterial(null, new TrustSelfSignedStrategy())
                .loadKeyMaterial(keyStore, "password".toCharArray())
                .build();
    }
    private HttpClient httpClient(SSLConnectionSocketFactory socketFactory) {
        return HttpClients
                .custom()
                .setSSLSocketFactory(socketFactory)
                .build();
    }
    private HttpComponentsClientHttpRequestFactory httpRequestFactory(HttpClient httpClient) {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory;

        if (httpClient != null) {
            clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        } else clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();

        clientHttpRequestFactory.setConnectTimeout(5000);
        clientHttpRequestFactory.setConnectionRequestTimeout(5000);
        clientHttpRequestFactory.setReadTimeout(5000);
        return clientHttpRequestFactory;
    }

}
