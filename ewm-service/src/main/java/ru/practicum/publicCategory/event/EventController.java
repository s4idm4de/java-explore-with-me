package ru.practicum.publicCategory.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.event.dto.EventFullDto;
import ru.practicum.model.event.dto.EventShortDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/events")
@Slf4j
public class EventController {
    @Autowired
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping
    public List<EventShortDto> getEvents(@RequestParam(name="text", required = false) String text,
                                         @RequestParam(name="categories", required = false) List<Long> categories,
                                         @RequestParam(name="paid", defaultValue = "false") Boolean paid,
                                         @RequestParam(name="rangeStart", required = false) String rangeStart,
                                         @RequestParam(name="rangeEnd", required = false) String rangeEnd,
                                         @RequestParam(name="onlyAvailable", defaultValue = "false") Boolean onlyAvailable,
                                         @RequestParam(name="sort") String sort, //проверить дополнительные требования
                                         @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                         @Positive @RequestParam(name = "size", defaultValue = "10") Integer size
    ) {
        log.info("public event controller getEvents text {}, categories {}, paid {}, rangeStart {}, " +
                "rangeEnd {}, onlyAvailable {}, sort {}, from {}, size {}", text, categories, paid,
                rangeStart, rangeEnd, onlyAvailable, sort, from, size);
        return eventService.getEvents(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size);
    }

    @GetMapping(path="/{eventId}")
    public EventFullDto getEvent(@PathVariable Long eventId) {
        log.info("public event controller getEvent eventId {}", eventId);
        return eventService.getEvent(eventId);
    }
}
