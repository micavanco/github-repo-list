package com.michaelolech.github.repo;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.StructuredTaskScope;

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

        try (var scope = StructuredTaskScope.open()) {

            var tasks = filteredRepos.stream().map(repo ->
                        scope.fork(() -> {
                            var branches = repoClient.getListOfBranches(repo.owner().login(), repo.name());

                            List<RepoBranch> reposBranches = branches
                                    .stream()
                                    .map((b) -> new RepoBranch(b.name(), b.commit().sha())).toList();

                            return new Repo(repo.name(), repo.owner().login(), reposBranches);
                        })
                    )
                    .toList();

            scope.join();

            for (var t : tasks) {
                if (t.state() == StructuredTaskScope.Subtask.State.SUCCESS) {
                    repos.add(t.get());
                }
            }

            return repos;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
