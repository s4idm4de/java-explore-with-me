package ru.practicum.admin.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.event.dto.EventFullDto;
import ru.practicum.model.event.dto.UpdateEventAdminRequest;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
@Slf4j
public class EventController {

    @Autowired
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventFullDto> getEvents(@RequestParam(name="users", required = false) List<Long> users,
                                       @RequestParam(name="states", required = false) List<String> states,
                                       @RequestParam(name="categories", required = false) List<Long> categories,
                                       @RequestParam(name="rangeStart", required = false) String rangeStart,
                                       @RequestParam(name="rangeEnd", required = false) String rangeEnd,
                                       @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                       @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("admin event controller getEvents users {}, states {}, categories {}, rangeStart {}, rangeEnd {}, from {}, size {}",
                users, states, categories, rangeStart, rangeEnd, from, size);
        return eventService.getEvents(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping(path="/{eventId}")
    public EventFullDto patchEvent(@PathVariable Long eventId,
                                   @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("admin event controller patchEvent eventId {}, updateEventAdminRequest",
                eventId, updateEventAdminRequest);
        return eventService.patchEvent(eventId, updateEventAdminRequest);
    }
}
