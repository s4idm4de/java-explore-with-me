package ru.practicum.model.dto;

import ru.practicum.model.event.dto.EventShortDto;

import java.util.List;

public class CompilationDto {
    private List<EventShortDto> events;

    private Long id;

    private Boolean pinned;

    private String title;
}
