package ru.practicum.model.event.dto;

import lombok.*;
import ru.practicum.model.dto.ParticipationRequestDto;

@Data
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateResult {
    private ParticipationRequestDto confirmedRequests; //список???

    private ParticipationRequestDto rejectedRequests;
}
