package ru.practicum.model.event.dto;

import lombok.*;
import ru.practicum.model.request.EventRequestStatus;

import java.util.List;

@Data
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;

    private EventRequestStatus status;
}
