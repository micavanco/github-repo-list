package com.michaelolech.github.repo;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;

@SpringBootTest
@WireMockTest(httpPort = 8081)
public class RepoIntegrationTests {

    @Autowired
    private RepoService repoService;

    @Test
    public void gettingReposWithCorrectUsername_shouldReturnProperListOfRepositories() {
        var repos = repoService.getRepos("test");

        Assertions.assertNotNull(repos);
        Assertions.assertEquals(2, repos.size());
        repos.forEach((repo) -> {
           Assertions.assertNotNull(repo);
           Assertions.assertEquals(4, repo.branches().size());
        });
    }

    @Test
    public void gettingReposWithIncorrectUsername_shouldThrowException() {
        try {
            repoService.getRepos("incorrect_test");
            Assertions.fail("Should have thrown exception");
        } catch (UserNotFoundException e) {
            Assertions.assertEquals("User not found.", e.getMessage());
            Assertions.assertEquals(HttpStatusCode.valueOf(404), e.getStatus());
        } catch (Exception e) {
            Assertions.fail("Should have thrown exception");
        }
    }
}
