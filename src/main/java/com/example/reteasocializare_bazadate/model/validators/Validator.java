package com.example.reteasocializare_bazadate.model.validators;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}