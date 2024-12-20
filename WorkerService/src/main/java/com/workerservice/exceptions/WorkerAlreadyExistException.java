package com.workerservice.exceptions;

import org.springframework.http.HttpStatus;

public class WorkerAlreadyExistException extends RuntimeException {
    private HttpStatus status;

    public WorkerAlreadyExistException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
