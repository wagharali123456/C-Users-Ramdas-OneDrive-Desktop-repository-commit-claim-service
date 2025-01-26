package com.org.fms.appconfig;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        // Create custom HttpClient
        HttpClient httpClient = HttpClients.custom()
            .setDefaultRequestConfig(createRequestConfig())  // Add timeout settings
            .setConnectionManager(createConnectionManager())  // Connection pooling
            .build();

        // Create and return the RestTemplate with the custom HttpClient
        return new RestTemplate(new HttpComponentsClientHttpRequestFactory(httpClient));
    }

    // Set timeouts (connection timeout, read timeout)
    private RequestConfig createRequestConfig() {
        return RequestConfig.custom()
            .setConnectTimeout(10000)  // Connection timeout (10 seconds)
            .setSocketTimeout(20000)   // Read timeout (20 seconds)
            .build();
    }

    // Configure connection pooling to improve performance under load
    private PoolingHttpClientConnectionManager createConnectionManager() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(200);  // Max total connections
        connectionManager.setDefaultMaxPerRoute(20);  // Max connections per route (server)
        return connectionManager;
    }
}
