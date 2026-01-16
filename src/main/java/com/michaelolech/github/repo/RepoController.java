package com.michaelolech.github.repo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/repos")
public class RepoController {
    private final RepoService repoService;

    public RepoController(RepoService repoService) {
        this.repoService = repoService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getListOfRepositoriesByUserName(@PathVariable String username) {
        List<Repo> repos;

        try {
            repos = repoService.getRepos(username);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(
                    new UserNotFoundResponse(e.getStatus().value(), e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        } catch (RuntimeException e) {
            return new ResponseEntity<>(
                    e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }

        return repos != null ?
                ResponseEntity.ok(repos) : ResponseEntity.notFound().build();
    }
}
