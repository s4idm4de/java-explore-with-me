package ru.practicum.admin.compilations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.dto.CompilationDto;
import ru.practicum.model.dto.NewCompilationDto;
import ru.practicum.model.dto.UpdateCompilationRequest;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CompilationService {

    public CompilationDto postCompilation(NewCompilationDto newCompilationDto) {
        log.info("admin CompilationService postCompilation newCompilationDto {}", newCompilationDto);

    }


    public void deleteCompilation(Long compId) {
        log.info("admin CompilationService deleteCompilation compId {}", compId);

    }


    public CompilationDto patchCompilation(Long compId,
                                           UpdateCompilationRequest updateCompilationRequest) {
        log.info("admin CompilationService patchCompilation compId {}, updateCompilationRequest",
                compId, updateCompilationRequest);
    }
}
