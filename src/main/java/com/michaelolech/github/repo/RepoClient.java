package com.michaelolech.github.repo;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

@Component
public class RepoClient {
    private final RestClient restClient;

    public RepoClient() {
        this.restClient = RestClient.builder()
                .baseUrl("https://api.github.com")
                .build();
    }

    public List<GitHubRepo> getListOfRepositories(String username) {
        return restClient.get()
                .uri("/users/{username}/repos", username)
                .accept(MediaType.APPLICATION_JSON)
                .exchange((_, response) -> {
                    if (response.getStatusCode().is4xxClientError()) {
                        throw new UserNotFoundException(response.getStatusCode(), "User not found.");
                    }
                    else {
                        GitHubRepo[] repos = response.bodyTo(GitHubRepo[].class);
                        return repos != null ? List.of(repos) : new ArrayList<>();
                    }
                });
    }
}
