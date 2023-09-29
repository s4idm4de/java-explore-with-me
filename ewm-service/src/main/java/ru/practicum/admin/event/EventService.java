package ru.practicum.admin.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.model.event.dto.EventFullDto;
import ru.practicum.model.event.dto.UpdateEventAdminRequest;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EventService {

    public List<EventFullDto> getEvents(List<Long> users,
                                        List<String> states,
                                        List<Long> categories,
                                        String rangeStart,
                                        String rangeEnd,
                                        Integer from,
                                        Integer size) {
        log.info("admin event service getEvents users {}, states {}, categories {}, rangeStart {}, rangeEnd {}, from {}, size {}",
                users, states, categories, rangeStart, rangeEnd, from, size);

    }


    public EventFullDto patchEvent(Long eventId,
                                   UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("admin event service patchEvent eventId {}, updateEventAdminRequest",
                eventId, updateEventAdminRequest);

    }
}
