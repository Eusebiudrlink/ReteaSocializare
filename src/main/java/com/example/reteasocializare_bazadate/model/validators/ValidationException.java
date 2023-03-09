package com.example.reteasocializare_bazadate.model.validators;

public class ValidationException extends RuntimeException {
    public ValidationException() {
    }

    public ValidationException(String message) {
        super(message);
    }

}
