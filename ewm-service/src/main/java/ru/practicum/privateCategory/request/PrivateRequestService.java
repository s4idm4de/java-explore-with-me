package ru.practicum.privateCategory.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidatedException;
import ru.practicum.model.event.Event;
import ru.practicum.model.request.EventRequestStatus;
import ru.practicum.model.request.Request;
import ru.practicum.model.request.dto.ParticipationRequestDto;
import ru.practicum.model.request.dto.RequestMapper;
import ru.practicum.model.user.User;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PrivateRequestService {

    @Autowired
    private final RequestRepository requestRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final EventRepository eventRepository;


    public List<ParticipationRequestDto> getRequestsOfUser(Long userId) {
        log.info("private RequestService getRequestsOfUser userId {}", userId);
        try {
            userRepository.findById(userId).orElseThrow(()
                    -> new NotFoundException("no such user"));
            return RequestMapper.toParticipationRequestDto(requestRepository.findAllByRequester_Id(userId));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }


    public ParticipationRequestDto postRequest(Long userId, Long eventId) {
        log.info("private RequestService postRequest userId {}, eventId {}", userId, eventId);
        try {
            User user = userRepository.findById(userId).orElseThrow(()
                    -> new NotFoundException("no such user"));
            Event event = eventRepository.findById(eventId).orElseThrow(()
                    -> new NotFoundException("no such event"));
            if (event.getInitiator().getId().equals(userId))
                throw new ValidatedException("cant request for yourself event");
            if (event.getPublishedOn() == null) throw new ValidatedException("cant request for unpublished event");
            if (requestRepository.findByRequesterAndEvent(user, event) != null)
                throw new ValidatedException("cant request twice");
            if (event.getParticipantLimit() != 0 && event.getParticipantLimit() <= event.getConfirmedRequests())
                throw new ValidatedException("no free places");
            if (event.getRequestModeration() && event.getParticipantLimit() > 0) {
                Request request = Request.builder()
                        .requester(user)
                        .created(LocalDateTime.now())
                        .status(EventRequestStatus.PENDING)
                        .event(event)
                        .build();
                return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
            }
            Request request = Request.builder()
                    .requester(user)
                    .created(LocalDateTime.now())
                    .status(EventRequestStatus.CONFIRMED)
                    .event(event)
                    .build();
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
            return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ValidatedException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }


    public ParticipationRequestDto patchRequest(Long userId, Long requestId) {
        log.info("private RequestService patchRequest userId {}, requestId {}", userId, requestId);
        try {
            User user = userRepository.findById(userId).orElseThrow(()
                    -> new NotFoundException("no such user"));
            Request request = requestRepository.findById(requestId).orElseThrow(()
                    -> new NotFoundException("no such request"));
            if (!request.getRequester().equals(user)) throw new ValidatedException("not request owner");
            if (request.getStatus().equals(EventRequestStatus.CONFIRMED)) {
                Event event = request.getEvent();
                event.setConfirmedRequests(event.getConfirmedRequests() - 1);
                eventRepository.save(event);
            }
            request.setStatus(EventRequestStatus.CANCELED);
            return RequestMapper.toParticipationRequestDto(requestRepository.save(request));
        } catch (NotFoundException | ValidatedException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}
