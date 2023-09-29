package ru.practicum.publicCategory.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import ru.practicum.model.dto.CompilationDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CompilationService {

    public List<CompilationDto> getCompilations(Boolean pinned,
                                                Integer from,
                                                Integer size) {
        log.info("public CompilationService getCompilations pinned {}, from {}, size {}", pinned, from, size);

    }

    public CompilationDto getCompilation(Long compId) {
        log.info("public CompilationService getCompilation compId {}", compId);

    }
}
