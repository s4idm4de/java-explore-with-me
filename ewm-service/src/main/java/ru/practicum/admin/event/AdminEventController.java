package ru.practicum.admin.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.exception.ValidatedException;
import ru.practicum.model.event.EventParams;
import ru.practicum.model.event.dto.EventFullDto;
import ru.practicum.model.event.dto.UpdateEventAdminRequest;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/events")
@Slf4j
public class AdminEventController {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private final AdminEventService adminEventService;

    public AdminEventController(AdminEventService adminEventService) {
        this.adminEventService = adminEventService;
    }

    @GetMapping
    public List<EventFullDto> getEvents(@RequestParam(name = "users", required = false) List<Long> users,
                                        @RequestParam(name = "states", required = false) List<String> states,
                                        @RequestParam(name = "categories", required = false) List<Long> categories,
                                        @RequestParam(name = "rangeStart", required = false) String rangeStart,
                                        @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
                                        @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                        @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("admin event controller getEvents users {}, states {}, categories {}, rangeStart {}, rangeEnd {}, from {}, size {}",
                users, states, categories, rangeStart, rangeEnd, from, size);
        EventParams eventParams = EventParams.builder()
                .users(users)
                .states(states)
                .categories(categories)
                .rangeStart(rangeStart == null ? null : LocalDateTime.parse(rangeStart, formatter))
                .rangeEnd(rangeEnd == null ? null : LocalDateTime.parse(rangeEnd, formatter))
                .from(from)
                .size(size)
                .build();
        return adminEventService.getEvents(eventParams);
    }

    @PatchMapping(path = "/{eventId}")
    public EventFullDto patchEvent(@PathVariable Long eventId,
                                   @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("admin event controller patchEvent eventId {}, updateEventAdminRequest {}",
                eventId, updateEventAdminRequest);
        try {
            if (updateEventAdminRequest.getDescription() != null && (updateEventAdminRequest.getDescription().length() < 20
                    || updateEventAdminRequest.getDescription().length() > 7000))
                throw new ValidatedException("wrong description");
            if (updateEventAdminRequest.getAnnotation() != null && (updateEventAdminRequest.getAnnotation().length() < 20
                    || updateEventAdminRequest.getAnnotation().length() > 2000))
                throw new ValidatedException("wrong description");
            if (updateEventAdminRequest.getTitle() != null && (updateEventAdminRequest.getTitle().length() < 3
                    || updateEventAdminRequest.getTitle().length() > 120))
                throw new ValidatedException("wrong description");
            return adminEventService.patchEvent(eventId, updateEventAdminRequest, LocalDateTime.now());
        } catch (ValidatedException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}
