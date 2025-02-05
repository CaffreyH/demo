// src/main/java/com/example/transactionservice/exception/ResourceNotFoundException.java
package com.example.demo.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}