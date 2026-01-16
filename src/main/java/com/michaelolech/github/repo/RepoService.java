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
        List<GitHubRepo> gitHubRepos = repoClient.getListOfRepositories(username);
        List<Repo> repos = new ArrayList<>();

        if (gitHubRepos.isEmpty()) {
            return Collections.emptyList();
        }

        List<GitHubRepo> filteredRepos = gitHubRepos.stream().filter(r -> !r.fork()).toList();

        for (GitHubRepo gitHubRepo : filteredRepos) {
            List<GitHubBranch> branches = repoClient
                    .getListOfBranches(gitHubRepo.owner().login(), gitHubRepo.name());
            List<RepoBranch> reposBranches = branches
                    .stream()
                    .map((b) -> new RepoBranch(b.name(), b.commit().sha())).toList();
            Repo repo = new Repo(gitHubRepo.name(), gitHubRepo.owner().login(), reposBranches);

            repos.add(repo);
        }

        return repos;
    }
}
