package com.michaelolech.github.repo;

import com.michaelolech.github.config.RepoBaseUrl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Component
@EnableConfigurationProperties(RepoBaseUrl.class)
public class RepoClient {
    private final RestClient restClient;

    public RepoClient(RepoBaseUrl urlProperties) {
        this.restClient = RestClient.builder()
                .baseUrl(urlProperties.baseUrl())
                .build();
    }

    public List<GitHubRepo> getListOfRepositories(String username) {
        return restClient.get()
                .uri("/users/{username}/repos", username)
                .accept(MediaType.APPLICATION_JSON)
                .exchange((_, response) -> {
                    if (response.getStatusCode().isSameCodeAs(HttpStatusCode.valueOf(404))) {
                        throw new UserNotFoundException(response.getStatusCode(), "User not found.");
                    } else if (response.getStatusCode().is4xxClientError()) {
                        throw new RuntimeException("Resource not available.");
                    }
                    else {
                        GitHubRepo[] repos = response.bodyTo(GitHubRepo[].class);
                        return repos != null ? List.of(repos) : new ArrayList<>();
                    }
                });
    }

    public List<GitHubBranch> getListOfBranches(String username, String repoName) {
        return restClient.get()
                .uri("/repos/{username}/{repoName}/branches", username, repoName)
                .accept(MediaType.APPLICATION_JSON)
                .exchange((_, response) -> {
                    if (response.getStatusCode().is4xxClientError()) {
                        throw response.createException();
                    }
                    else {
                        GitHubBranch[] branches = response.bodyTo(GitHubBranch[].class);
                        return branches != null ? List.of(branches) : new ArrayList<>();
                    }
                });
    }
}
