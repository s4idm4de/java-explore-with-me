package ru.practicum.admin.event;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.RequestException;
import ru.practicum.exception.ValidatedException;
import ru.practicum.model.request.ActionStatus;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.EventParams;
import ru.practicum.model.event.QEvent;
import ru.practicum.model.event.dto.EventFullDto;
import ru.practicum.model.event.dto.EventMapper;
import ru.practicum.model.event.dto.EventStatus;
import ru.practicum.model.event.dto.UpdateEventAdminRequest;
import ru.practicum.repository.EventRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AdminEventService {

    @Autowired
    private final EventRepository eventRepository;

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public List<EventFullDto> getEvents(EventParams eventParams) {
        log.info("admin event service getEvents eventParams {}", eventParams);
        BooleanExpression basicExpression = QEvent.event.id.gt(0L);
        if (eventParams.getUsers() != null) {
            BooleanExpression usersConstraint = QEvent.event.initiator.id.in(eventParams.getUsers());
            basicExpression = basicExpression.and(usersConstraint);
        }
        if (eventParams.getStates() != null) {
            BooleanExpression statesConstraint = QEvent.event.state.in(eventParams.getStates().stream()
                    .map(state -> EventStatus.valueOf(state)).collect(Collectors.toList()));
            basicExpression = basicExpression.and(statesConstraint);
        }
        if (eventParams.getCategories() != null) {
            BooleanExpression categoriesConstraint = QEvent.event.category.id.in(eventParams.getCategories());
            basicExpression = basicExpression.and(categoriesConstraint);
        }
        if (eventParams.getRangeStart() != null) {
            BooleanExpression startConstraint = QEvent.event.eventDate.after(eventParams.getRangeStart());
            basicExpression = basicExpression.and(startConstraint);
        }
        if (eventParams.getRangeEnd() != null) {
            BooleanExpression endConstraint = QEvent.event.eventDate.before(eventParams.getRangeEnd());
            basicExpression = basicExpression.and(endConstraint);
        }
        Page<Event> events = eventRepository.findAll(basicExpression,
                PageRequest.of(eventParams.getFrom() / eventParams.getSize(), eventParams.getSize(),
                        Sort.by(Sort.Direction.ASC, "id")
                ));
        events.forEach(event -> {
            event.setViews(event.getViews() + 1);
            eventRepository.save(event);
        });
        return EventMapper.toEventFullDto(events);
    }


    public EventFullDto patchEvent(Long eventId,
                                   UpdateEventAdminRequest updateEventAdminRequest, LocalDateTime currently) {
        log.info("admin event service patchEvent eventId {}, updateEventAdminRequest {}",
                eventId, updateEventAdminRequest);
        try {
            Event event = eventRepository.findById(eventId).orElseThrow(()
                    -> new NotFoundException("no such event"));
            if (updateEventAdminRequest.getStateAction() != null
                    && updateEventAdminRequest.getStateAction().equals(ActionStatus.PUBLISH_EVENT)
                    && !event.getState().equals(EventStatus.PENDING))
                throw new RequestException("not allowed to publish");
            if (updateEventAdminRequest.getStateAction() != null
                    && updateEventAdminRequest.getStateAction().equals(ActionStatus.REJECT_EVENT)
                    && event.getState().equals(EventStatus.PUBLISHED))
                throw new RequestException("not allowed to cancel");
            if (currently.plusHours(1).isAfter(event.getEventDate()) || (updateEventAdminRequest.getEventDate() != null &&
                    currently.plusHours(1).isAfter(LocalDateTime.parse(updateEventAdminRequest.getEventDate(), formatter))))
                throw new ValidatedException("event too close to time, can't change");
            Event newEvent = EventMapper.toEvent(updateEventAdminRequest, event);
            if (updateEventAdminRequest.getStateAction() != null && updateEventAdminRequest.getStateAction().equals(ActionStatus.PUBLISH_EVENT)) {
                newEvent.setPublishedOn(currently);
                newEvent.setState(EventStatus.PUBLISHED);
            } else if (updateEventAdminRequest.getStateAction() != null) {
                newEvent.setState(EventStatus.CANCELED);
            }
            return EventMapper.toEventFullDto(eventRepository.save(newEvent));
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
}
