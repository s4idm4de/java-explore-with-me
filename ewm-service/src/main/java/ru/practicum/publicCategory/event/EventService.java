package ru.practicum.publicCategory.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.event.dto.EventFullDto;
import ru.practicum.model.event.dto.EventShortDto;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EventService {

    public List<EventShortDto> getEvents(String text,
                                         List<Long> categories,
                                         Boolean paid,
                                         String rangeStart,
                                         String rangeEnd,
                                         Boolean onlyAvailable,
                                         String sort, //проверить дополнительные требования
                                         Integer from,
                                         Integer size
    ) {
        log.info("public event service getEvents text {}, categories {}, paid {}, rangeStart {}, " +
                        "rangeEnd {}, onlyAvailable {}, sort {}, from {}, size {}", text, categories, paid,
                rangeStart, rangeEnd, onlyAvailable, sort, from, size);

    }


    public EventFullDto getEvent(Long eventId) {
        log.info("public event service getEvent eventId {}", eventId);

    }
}
