package com.michaelolech.github.repo;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(accept = "application/json")
public interface RepoHttpClient {
    @GetExchange("/users/{username}/repos")
    GitHubRepo[] getUserRepos(@PathVariable String username);

    @GetExchange("/repos/{username}/{repoName}/branches")
    GitHubBranch[] getUserRepoBranches(@PathVariable String username, @PathVariable String repoName);
}
