package com.thomasdriscoll.productivityapi.lib.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum UserExceptions {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found");

    private final HttpStatus status;
    private final String message;

    UserExceptions(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
