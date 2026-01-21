package com.michaelolech.github.repo;

import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public record Repo(String name, String ownerLogin, List<RepoBranch> branches) {
}
