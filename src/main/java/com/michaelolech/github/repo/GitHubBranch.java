package com.michaelolech.github.repo;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record GitHubBranch(String name, GitHubCommit commit) {
}
