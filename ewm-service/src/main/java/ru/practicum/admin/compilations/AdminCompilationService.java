package ru.practicum.admin.compilations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidatedException;
import ru.practicum.model.compilation.Compilation;
import ru.practicum.model.compilation.dto.CompilationDto;
import ru.practicum.model.compilation.dto.CompilationMapper;
import ru.practicum.model.compilation.dto.NewCompilationDto;
import ru.practicum.model.compilation.dto.UpdateCompilationRequest;
import ru.practicum.model.event.Event;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventRepository;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AdminCompilationService {

    @Autowired
    private final CompilationRepository compilationRepository;

    @Autowired
    private final EventRepository eventRepository;

    public CompilationDto postCompilation(NewCompilationDto newCompilationDto) {
        log.info("admin CompilationService postCompilation newCompilationDto {}", newCompilationDto);
        try {
            if (newCompilationDto.getEvents() == null) newCompilationDto.setEvents(new ArrayList<>());
            Compilation compilation = CompilationMapper.toCompilation(newCompilationDto);
            List<Event> events = eventRepository.findAllByIds(newCompilationDto.getEvents());
            log.info("admin Compilation Service events {}", events);
            if (newCompilationDto.getEvents() != null && events.size() != newCompilationDto.getEvents().size())
                throw new ValidatedException("wrong event ids");
            Set<Event> eventsSet = new HashSet<>();
            for (Event event : events) {
                eventsSet.add(event);
            }
            compilation.setEvents(eventsSet);
            compilationRepository.save(compilation);
            return CompilationMapper.toCompilationDto(compilation);
        } catch (ValidatedException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }


    public void deleteCompilation(Long compId) {
        log.info("admin CompilationService deleteCompilation compId {}", compId);
        try {
            Compilation compilation = compilationRepository.findById(compId).orElseThrow(()
                    -> new NotFoundException("no such compilation"));
            compilationRepository.delete(compilation);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }

    }


    public CompilationDto patchCompilation(Long compId,
                                           UpdateCompilationRequest updateCompilationRequest) {
        log.info("admin CompilationService patchCompilation compId {}, updateCompilationRequest {}",
                compId, updateCompilationRequest);
        try {
            Compilation compilation = compilationRepository.findById(compId).orElseThrow(()
                    -> new NotFoundException("no such compilation"));
            Compilation newCompilation = CompilationMapper.toCompilation(updateCompilationRequest, compilation);
            if (updateCompilationRequest.getEvents() != null) {
                List<Event> events = eventRepository.findAllByIds(updateCompilationRequest.getEvents());
                if (events.size() != updateCompilationRequest.getEvents().size())
                    throw new ValidatedException("wrong event ids");
                Set<Event> eventsSet = new HashSet<>();
                for (Event event : events) {
                    eventsSet.add(event);
                }
                newCompilation.setEvents(eventsSet);
            }
            compilationRepository.save(newCompilation);
            return CompilationMapper.toCompilationDto(newCompilation);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ValidatedException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }
}
