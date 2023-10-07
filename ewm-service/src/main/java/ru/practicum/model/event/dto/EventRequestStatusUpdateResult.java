package ru.practicum.model.event.dto;

import lombok.*;
import ru.practicum.model.request.dto.ParticipationRequestDto;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateResult {
    private List<ParticipationRequestDto> confirmedRequests = new ArrayList<>(); //список???

    private List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();
}
