package ru.practicum.model.event.dto;

import lombok.*;
import ru.practicum.model.Location;

@Data
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {
    private String annotation; //minLength 20, max 2000

    private Long category;

    private String description; //20-7000

    private String eventDate;

    private Location location;

    private Boolean paid = false;

    private Long participantLimit = 0L;

    private Boolean requestModeration = true;

    private String title; //3-120
}
