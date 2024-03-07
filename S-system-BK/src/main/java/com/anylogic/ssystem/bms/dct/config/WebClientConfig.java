package com.anylogic.ssystem.bms.dct.config;


import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.extern.slf4j.Slf4j;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.SSLException;

@Slf4j
@Configuration
public class WebClientConfig {

    @Value("${shield-viewer-url}")
    private String shieldViewerUrl;

    @Bean
    public WebClient poolWebClientForViewer() throws SSLException {
        SslContext context = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        HttpClient httpClient = HttpClient.create().secure(provider -> provider.sslContext(context));

        return WebClient
                .builder()
                .baseUrl(shieldViewerUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1))
                .build();

    }

    @Value("${convert-pdf-url}")
    private String convertPDFUrl;
    @Bean
    public WebClient poolWebClientForConvertPDF() throws SSLException {
        SslContext context = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
        HttpClient httpClient = HttpClient.create().secure(provider -> provider.sslContext(context));


        return WebClient
                .builder()
                .baseUrl(convertPDFUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(-1))
                .build();

    }

}
