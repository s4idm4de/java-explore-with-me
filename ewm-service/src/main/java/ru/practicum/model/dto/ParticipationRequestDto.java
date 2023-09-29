package ru.practicum.model.dto;

import ru.practicum.model.event.dto.EventRequestStatus;

public class ParticipationRequestDto {
    private String created;

    private Long event;

    private Long id;

    private Long requester;

    private EventRequestStatus status;
}
