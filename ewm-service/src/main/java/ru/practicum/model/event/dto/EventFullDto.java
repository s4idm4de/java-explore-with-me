package ru.practicum.model.event.dto;

import lombok.*;
import ru.practicum.model.Location;
import ru.practicum.model.category.dto.CategoryDto;
import ru.practicum.model.user.dto.UserShortDto;

@Data
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {

    private String annotation;

    private CategoryDto category;

    private Long confirmedRequests;

    private String createdOn;

    private String description;

    private String eventDate;

    private Long id;

    private UserShortDto initiator;

    private Location location;

    private Boolean paid;

    private Long participantLimit = 0L;

    private String publishedOn;

    private Boolean requestModeration;

    private EventStatus state;

    private String title;

    private Long views;

}
