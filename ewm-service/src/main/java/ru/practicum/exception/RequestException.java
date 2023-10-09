package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestException extends Exception {
    public RequestException(String message) {
        super(message);
        log.error("Aй-aй RequestException {}", message);
    }
}
