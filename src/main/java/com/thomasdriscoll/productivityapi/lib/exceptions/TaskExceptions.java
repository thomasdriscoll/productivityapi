package com.thomasdriscoll.productivityapi.lib.exceptions;

import org.springframework.http.HttpStatus;

//This is just a sample enum for exceptions; delete!
public enum TaskExceptions {
    TASK_FAILED_TO_SAVE(HttpStatus.BAD_REQUEST, "Task failed to save"),
    INVALID_TASK_PRIORITY(HttpStatus.BAD_REQUEST, "Invalid task priority"),
    INVALID_TASK_TYPE(HttpStatus.BAD_REQUEST, "Invalid task type"),
    INVALID_STATUS(HttpStatus.BAD_REQUEST, "Invalid status for task"),
    TASK_ID_NOT_FOUND(HttpStatus.NOT_FOUND, "Task ID not found");

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
