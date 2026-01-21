package com.michaelolech.github.repo;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record GitHubCommit(String sha) {
}
