package ru.practicum.publicCategory.compilation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.dto.CompilationDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/compilations")
@Slf4j
public class CompilationController {
    @Autowired
    private final CompilationService compilationService;

    public CompilationController(CompilationService compilationService) {
        this.compilationService = compilationService;
    }

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(name = "pinned", defaultValue = "false") Boolean pinned,
                                                @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("public CompilationController getCompilations pinned {}, from {}, size {}", pinned, from, size);
        return compilationService.getCompilations(pinned, from, size);
    }

    @GetMapping(path="/{compId}")
    public CompilationDto getCompilation(@PathVariable Long compId) {
        log.info("public CompilationController getCompilation compId {}", compId);
        return compilationService.getCompilation(compId);
    }
}
