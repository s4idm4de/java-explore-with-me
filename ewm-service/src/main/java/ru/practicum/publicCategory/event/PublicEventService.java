package ru.practicum.publicCategory.event;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.StatsClient;
import ru.practicum.StatsDto;
import ru.practicum.StatsDtoOut;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.EventParams;
import ru.practicum.model.event.QEvent;
import ru.practicum.model.event.dto.EventFullDto;
import ru.practicum.model.event.dto.EventMapper;
import ru.practicum.model.event.dto.EventShortDto;
import ru.practicum.model.event.dto.EventStatus;
import ru.practicum.repository.EventRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PublicEventService {

    @Autowired
    private final EventRepository eventRepository;

    @Autowired
    private final StatsClient statsClient;

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final String appName = "ewm";

    private final String start = "2000-01-01 00:00:01";

    public List<EventShortDto> getEvents(EventParams eventParams, HttpServletRequest request) {
        log.info("public event service getEvents eventParams {}", eventParams);
        statsClient.setStats(StatsDto.builder()
                .app(appName)
                .uri(request.getRequestURI())
                .ip(request.getRemoteAddr())
                .timestamp(LocalDateTime.now().format(formatter)).build()
        );
        if (eventParams.getRangeStart() == null) eventParams.setRangeStart(LocalDateTime.now());
        BooleanExpression basicConditions = QEvent.event.state.eq(EventStatus.PUBLISHED).and(
                QEvent.event.eventDate.after(eventParams.getRangeStart())
        );
        if (eventParams.getText() != null) {
            BooleanExpression textConstraints = QEvent.event.description
                    .toLowerCase().contains(eventParams.getText().toLowerCase()).or(
                            QEvent.event.annotation.toLowerCase().contains(eventParams.getText().toLowerCase()));
            basicConditions = basicConditions.and(textConstraints);
        }
        if (eventParams.getCategories() != null) {
            BooleanExpression categoriesConstraints = QEvent.event.category.id.in(eventParams.getCategories());
            basicConditions = basicConditions.and(categoriesConstraints);
        }
        if (eventParams.getPaid() != null) {
            BooleanExpression paidConstraint = QEvent.event.paid.eq(eventParams.getPaid());
            basicConditions = basicConditions.and(paidConstraint);
        }
        if (eventParams.getRangeEnd() != null) {
            BooleanExpression endConstraint = QEvent.event.eventDate.before(eventParams.getRangeEnd());
            basicConditions = basicConditions.and(endConstraint);
        }
        if (eventParams.getOnlyAvailable()) {
            BooleanExpression availableCondition = QEvent.event.participantLimit.gt(QEvent.event.confirmedRequests);
            basicConditions = basicConditions.and(availableCondition);
        }
        if (eventParams.getSort() == "EVENT_DATE") {
            Page<Event> events = eventRepository.findAll(basicConditions,
                    PageRequest.of(eventParams.getFrom() / eventParams.getSize(), eventParams.getSize(),
                            Sort.by(Sort.Direction.DESC, "eventDate")));
            events.forEach(event -> {
                event.setViews(event.getViews() + 1);
                eventRepository.save(event);
            });
            return EventMapper.toEventShortDto(events);
        } else if (eventParams.getSort() == "VIEWS") {
            Page<Event> events = eventRepository.findAll(basicConditions,
                    PageRequest.of(eventParams.getFrom() / eventParams.getSize(), eventParams.getSize(),
                            Sort.by(Sort.Direction.DESC, "views")));
            events.forEach(event -> {
                event.setViews(event.getViews() + 1);
                eventRepository.save(event);
            });
            return EventMapper.toEventShortDto(events);
        } else {
            Page<Event> events = eventRepository.findAll(basicConditions,
                    PageRequest.of(eventParams.getFrom() / eventParams.getSize(), eventParams.getSize()));
            events.forEach(event -> {
                event.setViews(event.getViews() + 1);
                eventRepository.save(event);
            });
            return EventMapper.toEventShortDto(events);
        }
    }


    public EventFullDto getEvent(Long eventId, HttpServletRequest request) {
        log.info("public event service getEvent eventId {}", eventId);
        try {
            statsClient.setStats(StatsDto.builder()
                    .app(appName)
                    .uri(request.getRequestURI())
                    .ip(request.getRemoteAddr())
                    .timestamp(LocalDateTime.now().format(formatter)).build()
            );
            log.info("statsClient setStats done");
            Event event = eventRepository.findById(eventId).orElseThrow(()
                    -> new NotFoundException("no such event " + eventId));
            if (event.getPublishedOn() == null) throw new NotFoundException("event was not published");
            List<String> uris = new ArrayList<>();
            uris.add(request.getRequestURI());
            ResponseEntity<Object> responseEntity = statsClient.getStats(start, LocalDateTime.now().plusDays(1).format(formatter), uris, true);
            log.info("responseEntity {}", responseEntity.getBody());
            List<StatsDtoOut> answer = (List<StatsDtoOut>) responseEntity.getBody();
            log.info("getEvent answer {}", answer);
            event.setViews((long) answer.size());
            eventRepository.save(event);
            return EventMapper.toEventFullDto(event);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}
