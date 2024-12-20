package com.workerservice.exceptions;

public class WorkerNotFoundException extends Exception {
    public WorkerNotFoundException(String message) {
        super(message);
    }
}
