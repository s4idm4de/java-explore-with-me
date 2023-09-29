package ru.practicum.model.event.dto;

import lombok.*;
import ru.practicum.model.Location;
import ru.practicum.model.dto.ActionStatus;

@Data
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventAdminRequest {

    private String annotation; //20-2000

    private Long category;

    private String description; //20-7000

    private String eventDate;

    private Location location;

    private Boolean paid;

    private Long participantLimit;

    private Boolean requestModeration;

    private ActionStatus stateAction;

    private String title; //3-120
}
