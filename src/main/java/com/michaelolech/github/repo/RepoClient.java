package com.michaelolech.github.repo;

import com.michaelolech.github.config.RepoBaseUrl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

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

                        if (repos == null) {
                            throw new RuntimeException("Cannot parse response.");
                        }

                        return List.of(repos);
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

                        if (branches == null) {
                            throw new RuntimeException("Cannot parse response.");
                        }

                        return List.of(branches);
                    }
                });
    }
}
