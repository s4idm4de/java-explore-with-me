package ru.practicum.model.event.dto;

import ru.practicum.model.category.dto.CategoryMapper;
import ru.practicum.model.event.Event;
import ru.practicum.model.request.ActionStatus;
import ru.practicum.model.user.dto.UserMapper;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class EventMapper {
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Event toEvent(NewEventDto newEventDto) {
        @Valid Event event = Event.builder()
                .annotation(newEventDto.getAnnotation())
                .description(newEventDto.getDescription())
                .eventDate(LocalDateTime.parse(newEventDto.getEventDate(), formatter))
                .location(newEventDto.getLocation())
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .requestModeration(newEventDto.getRequestModeration())
                .title(newEventDto.getTitle())
                .confirmedRequests(0L)
                .views(0L)
                .build();
        return event;
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
                .publishedOn(event.getPublishedOn() == null ? "" : event.getPublishedOn().format(formatter))
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static EventShortDto toEventShortDto(Event event) {
        return EventShortDto.builder()
                .annotation(event.getAnnotation())
                .category(CategoryMapper.toCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate().format(formatter))
                .id(event.getId())
                .initiator(UserMapper.toUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static Event toEvent(UpdateEventAdminRequest updateEventAdminRequest, Event event) {
        @Valid Event eventOut = Event.builder()
                .annotation(updateEventAdminRequest.getAnnotation() != null ? updateEventAdminRequest.getAnnotation() : event.getAnnotation())
                .description(updateEventAdminRequest.getDescription() != null ? updateEventAdminRequest.getDescription() : event.getDescription())
                .eventDate(updateEventAdminRequest.getEventDate() != null ? LocalDateTime.parse(updateEventAdminRequest.getEventDate(), formatter) : event.getEventDate())
                .location(updateEventAdminRequest.getLocation() != null ? updateEventAdminRequest.getLocation() : event.getLocation())
                .paid(updateEventAdminRequest.getPaid() != null ? updateEventAdminRequest.getPaid() : event.getPaid())
                .participantLimit(updateEventAdminRequest.getParticipantLimit() != null ? updateEventAdminRequest.getParticipantLimit() : event.getParticipantLimit())
                .requestModeration(updateEventAdminRequest.getRequestModeration() != null ? updateEventAdminRequest.getRequestModeration() : event.getRequestModeration())
                .title(updateEventAdminRequest.getTitle() != null ? updateEventAdminRequest.getTitle() : event.getTitle())
                .id(event.getId())
                .state(updateEventAdminRequest.getStateAction() == null ? event.getState() : changeStatus(updateEventAdminRequest.getStateAction()))
                .views(event.getViews())
                .createdOn(event.getCreatedOn())
                .category(event.getCategory())
                .initiator(event.getInitiator())
                .confirmedRequests(event.getConfirmedRequests())
                .publishedOn(updateEventAdminRequest.getStateAction() != null && updateEventAdminRequest.getStateAction().equals(ActionStatus.PUBLISH_EVENT) ? LocalDateTime.now() : null)
                .build();
        return eventOut;
    }

    public static Event toEvent(UpdateEventUserRequest updateEventUserRequest, Event event) {
        @Valid Event eventOut = Event.builder()
                .annotation(updateEventUserRequest.getAnnotation() != null ? updateEventUserRequest.getAnnotation() : event.getAnnotation())
                .description(updateEventUserRequest.getDescription() != null ? updateEventUserRequest.getDescription() : event.getDescription())
                .eventDate(updateEventUserRequest.getEventDate() != null ? LocalDateTime.parse(updateEventUserRequest.getEventDate(), formatter) : event.getEventDate())
                .location(updateEventUserRequest.getLocation() != null ? updateEventUserRequest.getLocation() : event.getLocation())
                .paid(updateEventUserRequest.getPaid() != null ? updateEventUserRequest.getPaid() : event.getPaid())
                .participantLimit(updateEventUserRequest.getParticipantLimit() != null ? updateEventUserRequest.getParticipantLimit() : event.getParticipantLimit())
                .requestModeration(updateEventUserRequest.getRequestModeration() != null ? updateEventUserRequest.getRequestModeration() : event.getRequestModeration())
                .title(updateEventUserRequest.getTitle() != null ? updateEventUserRequest.getTitle() : event.getTitle())
                .id(event.getId())
                .state(updateEventUserRequest.getStateAction() == null ? event.getState() : changeStatus(updateEventUserRequest.getStateAction()))
                .views(event.getViews())
                .createdOn(event.getCreatedOn())
                .category(event.getCategory())
                .initiator(event.getInitiator())
                .confirmedRequests(event.getConfirmedRequests())
                .publishedOn(event.getPublishedOn())
                .build();
        return eventOut;
    }

    public static List<EventFullDto> toEventFullDto(Iterable<Event> events) {
        List<EventFullDto> result = new ArrayList<>();

        for (Event event : events) {
            result.add(toEventFullDto(event));
        }

        return result;
    }

    public static List<EventShortDto> toEventShortDto(Iterable<Event> events) {
        List<EventShortDto> result = new ArrayList<>();

        for (Event event : events) {
            result.add(toEventShortDto(event));
        }

        return result;
    }

    private static EventStatus changeStatus(ActionStatus actionStatus) {
        if (actionStatus.equals(ActionStatus.PUBLISH_EVENT)) {
            return EventStatus.PUBLISHED;
        } else if (actionStatus.equals(ActionStatus.REJECT_EVENT)) {
            return EventStatus.CANCELED;
        } else if (actionStatus.equals(ActionStatus.SEND_TO_REVIEW)) {
            return EventStatus.PENDING;
        } else {
            return EventStatus.CANCELED;
        }
    }
}

