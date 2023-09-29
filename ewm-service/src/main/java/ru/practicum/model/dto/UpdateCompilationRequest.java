package ru.practicum.model.dto;

import java.util.List;

public class UpdateCompilationRequest {

    private String description;

    private List<Long> events;

    private Boolean pinned;

    private String title; //1-50
}
