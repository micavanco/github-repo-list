package com.michaelolech.github.repo;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class RepoClient {
    private final RepoHttpClient restClient;

    public RepoClient(RepoHttpClient restClient) {
        this.restClient = restClient;
    }

    public List<GitHubRepo> getListOfRepositories(final String username) {
        GitHubRepo[] repos = restClient.getUserRepos(username);

        if (repos == null) {
            throw new RuntimeException("Cannot parse response.");
        }

        return Arrays.asList(repos);
    }

    public List<GitHubBranch> getListOfBranches(final String username, final String repoName) {
        GitHubBranch[] branches = restClient.getUserRepoBranches(username, repoName);

        if (branches == null) {
            throw new RuntimeException("Cannot parse response.");
        }

        return Arrays.asList(branches);
    }
}
