package ru.practicum.model.event.dto;

import ru.practicum.model.Location;
import ru.practicum.model.category.dto.CategoryDto;
import ru.practicum.model.category.dto.CategoryMapper;
import ru.practicum.model.event.Event;
import ru.practicum.model.user.dto.UserMapper;
import ru.practicum.model.user.dto.UserShortDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventMapper {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static Event toEvent(NewEventDto newEventDto) {
        return Event.builder()
                .annotation(newEventDto.getAnnotation())
                .description(newEventDto.getDescription())
                .eventDate(LocalDateTime.parse(newEventDto.getEventDate(), formatter))
                .location(newEventDto.getLocation())
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .build();
    }

    public static Event toEvent(UpdateEventAdminRequest updateEventAdminRequest) {
        return Event.builder()
                .annotation(updateEventAdminRequest.getAnnotation())
                .description(updateEventAdminRequest.getDescription())
                .eventDate(LocalDateTime.parse(updateEventAdminRequest.getEventDate(), formatter))
                .location(updateEventAdminRequest.getLocation())
                .paid(updateEventAdminRequest.getPaid())
                .participantLimit(updateEventAdminRequest.getParticipantLimit())
                .requestModeration(updateEventAdminRequest.getRequestModeration())
                .build();
    }

    public static EventFullDto toEventFullDto(Event event) {
        return EventFullDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn().format(formatter))
                .description(event.getDescription())
                .eventDate(event.getEventDate().format(formatter))
                .id(event.getId())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .location(event.getLocation())
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn().format(formatter))
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }
}

