package com.michaelolech.github.repo;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.client.EntityExchangeResult;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WireMockTest(httpPort = 8081)
@AutoConfigureRestTestClient
public class RepoIntegrationTests {

    @Autowired
    RestTestClient restTestClient;

    @Test
    public void gettingReposWithCorrectUsername_shouldReturnProperListOfRepositories() {
        EntityExchangeResult<Repo[]> result = restTestClient.get()
                .uri("/api/v1/repos/test")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(Repo[].class)
                .returnResult();

        if (result.getResponseBody() == null) {
            Assertions.fail();
        }

        List<Repo> repos = List.of(result.getResponseBody());

        Assertions.assertNotNull(repos);
        Assertions.assertEquals(2, repos.size());
        repos.forEach((repo) -> {
           Assertions.assertNotNull(repo);
           Assertions.assertEquals(4, repo.branches().size());
        });
    }

    @Test
    public void gettingReposWithIncorrectUsername_shouldThrowException() {
        EntityExchangeResult<UserNotFoundResponse> result = restTestClient.get()
                .uri("/api/v1/repos/incorrect_test")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(MediaType.APPLICATION_JSON)
                .expectBody(UserNotFoundResponse.class)
                .returnResult();

        if (result.getResponseBody() == null) {
            Assertions.fail();
        }

        var responseBody = result.getResponseBody();

        Assertions.assertEquals("User not found.", responseBody.message());
        Assertions.assertEquals(HttpStatus.NOT_FOUND.value(), responseBody.status());
    }
}
