package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ValidatedException extends Exception {
    public ValidatedException(String message) {
        super(message);
        log.error("Ой-ой ValidationException {}", message);
    }
}
