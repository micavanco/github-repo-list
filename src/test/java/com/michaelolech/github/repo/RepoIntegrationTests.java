package com.michaelolech.github.repo;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@WireMockTest(httpPort = 8081)
public class RepoIntegrationTests {

    @Autowired
    private RepoClient repoClient;

    @Test
    public void sendingRequestWithCorrectUsername_shouldReturnListOfRepositories() {
        var repos = repoClient.getListOfRepositories("test");
        repos.forEach(repo -> System.out.println(repo.name()));
    }

}
