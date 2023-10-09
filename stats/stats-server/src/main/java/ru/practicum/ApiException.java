package ru.practicum;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ApiException extends Exception {
    public ApiException(String message) {
        super(message);
        log.error("Ой-ой ApiException {}", message);
    }
}
