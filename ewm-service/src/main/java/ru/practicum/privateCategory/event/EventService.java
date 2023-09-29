package ru.practicum.privateCategory.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.dto.*;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.dto.*;
import ru.practicum.model.user.User;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EventService {

    @Autowired
    private final EventRepository eventRepository;

    @Autowired
    private final UserRepository userRepository;

    public List<EventShortDto> getAllEvents(Long userId,
                                            Integer from,
                                            Integer size) {
        log.info("private event service getAllEvents userId {}, from {}, size {}", userId, from, size);

    }

    public EventFullDto postEvent(Long userId, NewEventDto newEventDto) {
        log.info("private event service postEvent userId {}, newEventDto {}", userId, newEventDto);
        try {
            User user = userRepository.findById(userId).orElseThrow(()
                    -> new NotFoundException("no such user"));
            Event event = EventMapper.toEvent(newEventDto);
            event.setInitiator(user);
            event.setCreatedOn(LocalDateTime.now());
            return EventMapper.toEventFullDto(eventRepository.save(event));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    public EventFullDto getEvent(Long userId,
                                 Long eventId) {
        log.info("private event service getEvent userId {}, eventId {}", userId, eventId);

    }

    public EventFullDto patchEvent(Long userId,
                                   Long eventId,
                                   UpdateEventUserRequest updateEventUserRequest) {
        log.info("private event service patchEvent userId {}, eventId {}, updateEventUserRequest {}",
                userId, eventId, updateEventUserRequest);

    }


    public ParticipationRequestDto getRequest(Long userId,
                                              Long eventId) {
        log.info("private event service getRequest userId {}, eventId {}", userId, eventId);

    }

    public EventRequestStatusUpdateResult patchRequest(Long userId,
                                                       Long eventId,
                                                       EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.info("private event service patchRequest userId {}, eventId {}, eventRequestStatusUpdateRequest {}",
                userId, eventId, eventRequestStatusUpdateRequest);

    }
}
