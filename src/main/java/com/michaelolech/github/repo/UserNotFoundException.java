package com.michaelolech.github.repo;

import org.springframework.http.HttpStatusCode;

public class UserNotFoundException extends RuntimeException {
    private final HttpStatusCode status;

    public UserNotFoundException(HttpStatusCode status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatusCode getStatus() {
        return status;
    }
}
