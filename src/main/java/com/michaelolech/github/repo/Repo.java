package com.michaelolech.github.repo;

import java.util.List;

public record Repo(String name, String ownerLogin, List<GitHubBranch> gitHubBranches) {
}
