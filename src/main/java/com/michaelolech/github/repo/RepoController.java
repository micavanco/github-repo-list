package com.michaelolech.github.repo;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/repos")
public class RepoController {

    @GetMapping("/{userName}")
    public List<Repo> getListOfRepositoriesByUserName(@PathVariable String userName) {
        return List.of(
                new Repo("Test", userName, List.of(new Branch("init", "12321")))
        );
    }
}
