package com.michaelolech.github.repo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/{version}/repos", version = "v1")
public class RepoController {
    private final RepoService repoService;

    public RepoController(RepoService repoService) {
        this.repoService = repoService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<Repo>> getListOfRepositoriesByUserName(@PathVariable String username) {
        List<Repo> repos;

        repos = repoService.getRepos(username);

        return repos != null ?
                ResponseEntity.ok(repos) : ResponseEntity.notFound().build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<UserNotFoundResponse> handleNotFoundException(UserNotFoundException e) {
        return new ResponseEntity<>(
                new UserNotFoundResponse(e.getStatus().value(), e.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        return new ResponseEntity<>(
                e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
