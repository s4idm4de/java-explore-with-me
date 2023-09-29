package ru.practicum.model.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public class NewCompilationDto {
    private List<Long> events;

    private Boolean pinned = false;

    @NotBlank
    @NotNull
    private String title;
}
