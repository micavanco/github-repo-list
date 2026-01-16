package com.michaelolech.github.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "repo.url")
public record RepoBaseUrl(String baseUrl) {
}