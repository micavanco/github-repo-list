package com.michaelolech.github.repo;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class RepoService {
    private final RepoClient repoClient;

    public RepoService(RepoClient repoClient) {
        this.repoClient = repoClient;
    }

    public List<Repo> getRepos(String username) {
        System.out.println("Getting repos for " + username);
        List<GitHubRepo> gitHubRepos = repoClient.getListOfRepositories(username);
        System.out.println(gitHubRepos.size());

        return gitHubRepos.stream()
                .map(repo -> new Repo(repo.name(), repo.owner().login(), new ArrayList<>()))
                .toList();
    }
}
