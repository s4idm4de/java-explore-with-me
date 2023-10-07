package ru.practicum.model.compilation.dto;

import lombok.*;
import ru.practicum.model.event.dto.EventShortDto;

import java.util.List;

@Data
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDto {
    private List<EventShortDto> events;

    private Long id;

    private Boolean pinned;

    private String title;
}
