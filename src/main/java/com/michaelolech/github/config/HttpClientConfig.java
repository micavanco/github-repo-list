package com.michaelolech.github.config;

import com.michaelolech.github.repo.RepoHttpClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.service.registry.ImportHttpServices;

@Configuration
@ImportHttpServices(RepoHttpClient.class)
@EnableConfigurationProperties(RepoBaseUrl.class)
public class HttpClientConfig {

    @Bean
    RestClient restClient(RestClient.Builder builder, RepoBaseUrl urlProperties) {
        return builder
                .baseUrl(urlProperties.baseUrl())
                .build();
    }
}
