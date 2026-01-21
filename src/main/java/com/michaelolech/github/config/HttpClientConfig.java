package com.michaelolech.github.config;

import com.michaelolech.github.repo.RepoHttpClient;
import com.michaelolech.github.repo.UserNotFoundException;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.springframework.web.service.registry.ImportHttpServices;

@Configuration
@ImportHttpServices(RepoHttpClient.class)
@EnableConfigurationProperties(RepoBaseUrl.class)
public class HttpClientConfig {

    @Bean
    RepoHttpClient restClient(RestClient.Builder builder, RepoBaseUrl urlProperties) {
        var restClient = builder
                .baseUrl(urlProperties.baseUrl())
                .defaultStatusHandler(
                        status -> status.value() == 404,
                        (_, res) -> {
                            throw new UserNotFoundException(res.getStatusCode(), "User not found.");
                        }
                )
                .defaultStatusHandler(
                        HttpStatusCode::is4xxClientError,
                        (_, _) -> {
                            throw new RuntimeException("Resource not available.");
                        }
                )
                .defaultStatusHandler(
                        HttpStatusCode::is5xxServerError,
                        (_, _) -> {
                            throw new RuntimeException("Remote service not available.");
                        }
                )
                .build();

        return HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build().createClient(RepoHttpClient.class);
    }
}
