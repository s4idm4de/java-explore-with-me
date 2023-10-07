package ru.practicum.model.request.dto;

import lombok.*;
import ru.practicum.model.request.EventRequestStatus;

@Data
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequestDto {
    private String created;

    private Long event;

    private Long id;

    private Long requester;

    private EventRequestStatus status;
}
