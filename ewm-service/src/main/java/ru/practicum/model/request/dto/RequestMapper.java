package ru.practicum.model.request.dto;

import ru.practicum.model.request.Request;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class RequestMapper {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static ParticipationRequestDto toParticipationRequestDto(Request request) {
        return ParticipationRequestDto.builder()
                .created(request.getCreated().format(formatter))
                .event(request.getEvent().getId())
                .id(request.getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .build();
    }

    public static List<ParticipationRequestDto> toParticipationRequestDto(Iterable<Request> requests) {
        List<ParticipationRequestDto> result = new ArrayList<>();

        for (Request request : requests) {
            result.add(toParticipationRequestDto(request));
        }

        return result;
    }
}
