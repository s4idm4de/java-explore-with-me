package ru.practicum.publicCategory.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.compilation.Compilation;
import ru.practicum.model.compilation.dto.CompilationDto;
import ru.practicum.model.compilation.dto.CompilationMapper;
import ru.practicum.repository.CompilationRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PublicCompilationService {

    @Autowired
    private final CompilationRepository compilationRepository;

    public List<CompilationDto> getCompilations(Boolean pinned,
                                                Integer from,
                                                Integer size) {
        log.info("public CompilationService getCompilations pinned {}, from {}, size {}", pinned, from, size);
        if (pinned == null) {
            return CompilationMapper.toCompilationDto(compilationRepository.findAll(PageRequest.of(from / size, size)));
        }
        return CompilationMapper.toCompilationDto(compilationRepository.findAllByPinned(pinned, PageRequest.of(from / size, size)));
    }

    public CompilationDto getCompilation(Long compId) {
        log.info("public CompilationService getCompilation compId {}", compId);
        try {
            Compilation compilation = compilationRepository.findById(compId).orElseThrow(()
                    -> new NotFoundException("no such compilation"));
            return CompilationMapper.toCompilationDto(compilation);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}
