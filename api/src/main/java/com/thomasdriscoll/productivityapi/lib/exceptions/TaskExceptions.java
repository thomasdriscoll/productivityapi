package com.thomasdriscoll.productivityapi.lib.exceptions;

import org.springframework.http.HttpStatus;

//This is just a sample enum for exceptions; delete!
public enum TaskExceptions {
    TESTING_EXCEPTIONS(HttpStatus.BAD_REQUEST, "You done goofed");

    private final HttpStatus status;
    private final String message;

    TaskExceptions(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
