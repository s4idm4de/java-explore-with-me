package ru.practicum.privateCategory.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.RequestException;
import ru.practicum.exception.ValidatedException;
import ru.practicum.model.category.Category;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.dto.*;
import ru.practicum.model.request.EventRequestStatus;
import ru.practicum.model.request.Request;
import ru.practicum.model.request.dto.ParticipationRequestDto;
import ru.practicum.model.request.dto.RequestMapper;
import ru.practicum.model.user.User;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.repository.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PrivateEventService {

    @Autowired
    private final EventRepository eventRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final RequestRepository requestRepository;

    @Autowired
    private final CategoryRepository categoryRepository;

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public List<EventShortDto> getAllEvents(Long userId,
                                            Integer from,
                                            Integer size) {
        log.info("private event service getAllEvents userId {}, from {}, size {}", userId, from, size);
        List<Event> events = eventRepository.findAllByInitiator_Id(userId, PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "id")));
        return EventMapper.toEventShortDto(events);
    }

    public EventFullDto postEvent(Long userId, NewEventDto newEventDto) {
        log.info("private event service postEvent userId {}, newEventDto {}", userId, newEventDto);
        try {
            User user = userRepository.findById(userId).orElseThrow(()
                    -> new NotFoundException("no such user"));
            Event event = EventMapper.toEvent(newEventDto);
            event.setInitiator(user);
            if (LocalDateTime.now().plusHours(2).isAfter(event.getEventDate()))
                throw new ValidatedException("incorrect event");
            event.setCreatedOn(LocalDateTime.now());
            Category category = categoryRepository.findById(newEventDto.getCategory()).orElseThrow(()
                    -> new NotFoundException("no such category"));
            event.setCategory(category);
            event.setState(EventStatus.PENDING); //в задание ничего не сказано
            Event eventSaved = eventRepository.save(event);
            log.info("eventSaved {}", eventSaved);
            return EventMapper.toEventFullDto(eventSaved);
        } catch (NotFoundException | ValidatedException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    public EventFullDto getEvent(Long userId,
                                 Long eventId) {
        log.info("private event service getEvent userId {}, eventId {}", userId, eventId);
        try {
            Event event = eventRepository.findById(eventId).orElseThrow(()
                    -> new NotFoundException("no such event " + eventId));
            if (!event.getInitiator().getId().equals(userId)) throw new ValidatedException("wrong user");
            eventRepository.save(event);
            return EventMapper.toEventFullDto(event);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ValidatedException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    public EventFullDto patchEvent(Long userId,
                                   Long eventId,
                                   UpdateEventUserRequest updateEventUserRequest) {
        log.info("private event service patchEvent userId {}, eventId {}, updateEventUserRequest {}",
                userId, eventId, updateEventUserRequest);
        try {
            Event event = eventRepository.findById(eventId).orElseThrow(()
                    -> new NotFoundException("no such event"));
            if (!event.getInitiator().getId().equals(userId)) throw new RequestException("wrong userId");
            if (event.getState().equals(EventStatus.PUBLISHED))
                throw new RequestException("cant change published event");
            if (event.getEventDate().isBefore(LocalDateTime.now().plusHours(2))
                    || (updateEventUserRequest.getEventDate() != null &&
                    LocalDateTime.parse(updateEventUserRequest.getEventDate(), formatter).isBefore(LocalDateTime.now().plusHours(2))))
                throw new ValidatedException("you havent time to change event");
            Event newEvent = EventMapper.toEvent(updateEventUserRequest, event);
            return EventMapper.toEventFullDto(newEvent);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ValidatedException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (RequestException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }


    public List<ParticipationRequestDto> getRequests(Long userId,
                                                     Long eventId) {
        log.info("private event service getRequest userId {}, eventId {}", userId, eventId);
        try {
            Event event = eventRepository.findById(eventId).orElseThrow(()
                    -> new NotFoundException("no such event"));
            if (!event.getInitiator().getId().equals(userId)) throw new ValidatedException("wrong userId");
            List<ParticipationRequestDto> participationRequestDtos = RequestMapper.toParticipationRequestDto(
                    requestRepository.findAllByEvent_Id(eventId));
            return participationRequestDtos;
        } catch (NotFoundException | ValidatedException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    public EventRequestStatusUpdateResult patchRequest(Long userId,
                                                       Long eventId,
                                                       EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        log.info("private event service patchRequest userId {}, eventId {}, eventRequestStatusUpdateRequest {}",
                userId, eventId, eventRequestStatusUpdateRequest);
        try {
            Event event = eventRepository.findById(eventId).orElseThrow(()
                    -> new NotFoundException("no such event"));
            if (!event.getState().equals(EventStatus.PUBLISHED)) throw new NotFoundException("event is not published");
            List<Request> requests = requestRepository.findAllByIds(eventRequestStatusUpdateRequest.getRequestIds());
            if (!event.getRequestModeration() || event.getParticipantLimit().equals(0L)) { //если событие не ограничено по численности или отсутсвует модерация, всё одобряем?!
                requests.forEach(request -> {
                    request.setStatus(EventRequestStatus.CONFIRMED);
                    requestRepository.save(request);
                });
                event.setConfirmedRequests(event.getConfirmedRequests() + requests.size());
                eventRepository.save(event);
                return EventRequestStatusUpdateResult.builder()
                        .confirmedRequests(RequestMapper.toParticipationRequestDto(requests))
                        .rejectedRequests(new ArrayList<>())
                        .build();
            }
            if (event.getParticipantLimit() <= event.getConfirmedRequests()) throw new ValidatedException("no place");
            EventRequestStatusUpdateResult eventRequestStatusUpdateResult = new EventRequestStatusUpdateResult();
            for (Request request : requests) {
                if (!request.getStatus().equals(EventRequestStatus.PENDING))
                    throw new ValidatedException("only requests with PENDING status");
                if (event.getConfirmedRequests() < event.getParticipantLimit()
                        && eventRequestStatusUpdateRequest.getStatus().equals(EventRequestStatus.CONFIRMED)) {
                    request.setStatus(EventRequestStatus.CONFIRMED);
                    event.setConfirmedRequests(event.getConfirmedRequests() + 1);
                    eventRequestStatusUpdateResult.getConfirmedRequests().add(RequestMapper.toParticipationRequestDto(request));
                } else {
                    request.setStatus(EventRequestStatus.REJECTED);
                    eventRequestStatusUpdateResult.getRejectedRequests().add(RequestMapper.toParticipationRequestDto(request));
                }
                requestRepository.save(request);
            }
            eventRepository.save(event);
            return eventRequestStatusUpdateResult;
        } catch (NotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ValidatedException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }
}
