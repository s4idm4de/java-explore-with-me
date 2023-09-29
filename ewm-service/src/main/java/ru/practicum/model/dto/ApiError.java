package ru.practicum.model.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ApiError {
    private List<String> errors;

    private String message;

    private String reason;

    private ApiErrorStatus status;

    private LocalDateTime timestamp;
}
