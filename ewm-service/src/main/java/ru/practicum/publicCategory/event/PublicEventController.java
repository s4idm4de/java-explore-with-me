package ru.practicum.publicCategory.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.exception.ValidatedException;
import ru.practicum.model.event.EventParams;
import ru.practicum.model.event.dto.EventFullDto;
import ru.practicum.model.event.dto.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping(path = "/events")
@Slf4j
public class PublicEventController {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private final PublicEventService publicEventService;

    public PublicEventController(PublicEventService publicEventService) {
        this.publicEventService = publicEventService;
    }

    @GetMapping
    public List<EventShortDto> getEvents(@RequestParam(name = "text", required = false) String text,
                                         @RequestParam(name = "categories", required = false) List<Long> categories,
                                         @RequestParam(name = "paid", required = false) Boolean paid,
                                         @RequestParam(name = "rangeStart", required = false) String rangeStart,
                                         @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
                                         @RequestParam(name = "onlyAvailable", defaultValue = "false") Boolean onlyAvailable,
                                         @RequestParam(name = "sort", required = false) String sort, //проверить дополнительные требования
                                         @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                         @Positive @RequestParam(name = "size", defaultValue = "10") Integer size,
                                         HttpServletRequest request
    ) {
        log.info("public event controller getEvents text {}, categories {}, paid {}, rangeStart {}, " +
                        "rangeEnd {}, onlyAvailable {}, sort {}, from {}, size {}", text, categories, paid,
                rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        try {
            if (sort != null && !sort.equals("EVENT_DATE") && !sort.equals("VIEWS"))
                throw new ValidatedException("wrong sort parameter");
            if (rangeStart != null && rangeEnd != null &&
                    LocalDateTime.parse(rangeStart, formatter).isAfter(LocalDateTime.parse(rangeEnd, formatter)))
                throw new ValidatedException("rangeStart should be before rangeEnd");
            EventParams eventParams = EventParams.builder()
                    .text(text)
                    .categories(categories)
                    .paid(paid)
                    .rangeStart(rangeStart == null ? null : LocalDateTime.parse(rangeStart, formatter))
                    .rangeEnd(rangeEnd == null ? null : LocalDateTime.parse(rangeEnd, formatter))
                    .onlyAvailable(onlyAvailable)
                    .sort(sort)
                    .from(from)
                    .size(size)
                    .build();
            return publicEventService.getEvents(eventParams, request);
        } catch (ValidatedException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @GetMapping(path = "/{eventId}")
    public EventFullDto getEvent(@PathVariable Long eventId, HttpServletRequest request) {
        log.info("public event controller getEvent eventId {}", eventId);
        return publicEventService.getEvent(eventId, request);
    }
}
