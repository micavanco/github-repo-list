package com.michaelolech.github.repo;

public record GitHubRepo(String name, GitHubRepoOwner owner, Boolean fork) {
}
