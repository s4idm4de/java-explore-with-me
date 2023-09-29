package ru.practicum.admin.compilations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.dto.CompilationDto;
import ru.practicum.model.dto.NewCompilationDto;
import ru.practicum.model.dto.UpdateCompilationRequest;

@RestController
@RequestMapping(path = "/admin/compilations")
@Slf4j
public class CompilationController {
    @Autowired
    private final CompilationService compilationService;

    public CompilationController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @PostMapping
    public CompilationDto postCompilation(@RequestBody NewCompilationDto newCompilationDto) {
        log.info("admin CompilationController postCompilation newCompilationDto {}", newCompilationDto);
        return compilationService.postCompilation(newCompilationDto);
    }

    @DeleteMapping(path="/{compId}")
    public void deleteCompilation(@PathVariable Long compId) {
        log.info("admin CompilationController deleteCompilation compId {}", compId);
        compilationService.deleteCompilation(compId);
    }

    @PatchMapping(path="/{compId}")
    public CompilationDto patchCompilation(@PathVariable Long compId,
                                           @RequestBody UpdateCompilationRequest updateCompilationRequest) {
        log.info("admin CompilationController patchCompilation compId {}, updateCompilationRequest",
                compId, updateCompilationRequest);
        return compilationService.patchCompilation(compId, updateCompilationRequest);

    }
}
