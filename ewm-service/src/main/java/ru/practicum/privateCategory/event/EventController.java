package ru.practicum.privateCategory.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.dto.*;
import ru.practicum.model.event.dto.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/events")
@Slf4j
public class EventController {
    @Autowired
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventShortDto> getAllEvents(@PathVariable Long userId,
                                            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                            @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("private event controller getAllEvents userId {}, from {}, size {}", userId, from, size);
        return eventService.getAllEvents(userId, from, size);
    }

    @PostMapping
    public EventFullDto postEvent(@PathVariable Long userId, @RequestBody NewEventDto newEventDto) {
        log.info("private event controller postEvent userId {}, newEventDto {}", userId, newEventDto);
        return eventService.postEvent(userId, newEventDto);
    }

    @GetMapping(path="/{eventId}")
    public EventFullDto getEvent(@PathVariable Long userId,
                                 @PathVariable Long eventId) {
       log.info("private event controller getEvent userId {}, eventId {}", userId, eventId);
       return eventService.getEvent(userId, eventId);
    }

    @PatchMapping(path="/{eventId}")
    public EventFullDto patchEvent(@PathVariable Long userId,
                                   @PathVariable Long eventId,
                                   @RequestBody UpdateEventUserRequest updateEventUserRequest) {
        log.info("private event controller patchEvent userId {}, eventId {}, updateEventUserRequest {}",
                userId, eventId, updateEventUserRequest);
        return eventService.patchEvent(userId, eventId, updateEventUserRequest);
    }

    @GetMapping(path="/{eventId}/requests")
    public ParticipationRequestDto getRequest(@PathVariable Long userId,
                                              @PathVariable Long eventId) {
        log.info("private event controller getRequest userId {}, eventId {}", userId, eventId);
        return eventService.getRequest(userId, eventId);
    }

    @PatchMapping(path="/{eventId}/requests")
    public EventRequestStatusUpdateResult patchRequest(@PathVariable Long userId,
                                                       @PathVariable Long eventId,
                                                       @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.info("private event controller patchRequest userId {}, eventId {}, eventRequestStatusUpdateRequest {}",
                userId, eventId, eventRequestStatusUpdateRequest);
        return eventService.patchRequest(userId, eventId, eventRequestStatusUpdateRequest);
    }
}
