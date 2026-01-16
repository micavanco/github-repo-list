package com.michaelolech.github;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class GithubRepoListApplication {

	public static void main(String[] args) {
		SpringApplication.run(GithubRepoListApplication.class, args);
	}

}
