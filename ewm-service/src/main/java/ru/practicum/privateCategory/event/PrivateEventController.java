package ru.practicum.privateCategory.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.exception.ValidatedException;
import ru.practicum.model.event.dto.*;
import ru.practicum.model.request.dto.ParticipationRequestDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@Slf4j
public class PrivateEventController {
    @Autowired
    private final PrivateEventService privateEventService;

    public PrivateEventController(PrivateEventService privateEventService) {
        this.privateEventService = privateEventService;
    }

    @GetMapping
    public List<EventShortDto> getAllEvents(@PathVariable Long userId,
                                            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("private event controller getAllEvents userId {}, from {}, size {}", userId, from, size);
        return privateEventService.getAllEvents(userId, from, size);
    }

    @PostMapping
    public ResponseEntity<EventFullDto> postEvent(@PathVariable Long userId, @RequestBody @Validated NewEventDto newEventDto) {
        log.info("private event controller postEvent userId {}, newEventDto {}", userId, newEventDto);
        return new ResponseEntity<>(privateEventService.postEvent(userId, newEventDto), HttpStatus.CREATED);
    }

    @GetMapping(path = "/{eventId}")
    public EventFullDto getEvent(@PathVariable Long userId,
                                 @PathVariable Long eventId) {
        log.info("private event controller getEvent userId {}, eventId {}", userId, eventId);
        return privateEventService.getEvent(userId, eventId);
    }

    @PatchMapping(path = "/{eventId}")
    public EventFullDto patchEvent(@PathVariable Long userId,
                                   @PathVariable Long eventId,
                                   @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        log.info("private event controller patchEvent userId {}, eventId {}, updateEventUserRequest {}",
                userId, eventId, updateEventUserRequest);
        try {
            if (updateEventUserRequest.getDescription() != null && (updateEventUserRequest.getDescription().length() < 20
                    || updateEventUserRequest.getDescription().length() > 7000))
                throw new ValidatedException("wrong description");
            if (updateEventUserRequest.getAnnotation() != null && (updateEventUserRequest.getAnnotation().length() < 20
                    || updateEventUserRequest.getAnnotation().length() > 2000))
                throw new ValidatedException("wrong description");
            if (updateEventUserRequest.getTitle() != null && (updateEventUserRequest.getTitle().length() < 3
                    || updateEventUserRequest.getTitle().length() > 120))
                throw new ValidatedException("wrong description");
            return privateEventService.patchEvent(userId, eventId, updateEventUserRequest);
        } catch (ValidatedException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @GetMapping(path = "/{eventId}/requests")
    public List<ParticipationRequestDto> getRequests(@PathVariable Long userId,
                                                     @PathVariable Long eventId) {
        log.info("private event controller getRequest userId {}, eventId {}", userId, eventId);
        return privateEventService.getRequests(userId, eventId);
    }

    @PatchMapping(path = "/{eventId}/requests")
    public EventRequestStatusUpdateResult patchRequest(@PathVariable Long userId,
                                                       @PathVariable Long eventId,
                                                       @RequestBody @Validated EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.info("private event controller patchRequest userId {}, eventId {}, eventRequestStatusUpdateRequest {}",
                userId, eventId, eventRequestStatusUpdateRequest);
        return privateEventService.patchRequest(userId, eventId, eventRequestStatusUpdateRequest);
    }
}
