package ru.practicum.model.event.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.lang.Nullable;
import ru.practicum.model.Location;
import ru.practicum.model.request.ActionStatus;

@Data
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventAdminRequest {

    @Nullable
    @Length(min = 20, max = 2000)
    private String annotation; //20-2000

    private Long category;

    @Nullable
    @Length(min = 20, max = 7000)
    private String description; //20-7000

    private String eventDate;

    private Location location;

    private Boolean paid;

    private Long participantLimit;

    private Boolean requestModeration;

    private ActionStatus stateAction;

    @Nullable
    @Length(min = 3, max = 120)
    private String title;
}
