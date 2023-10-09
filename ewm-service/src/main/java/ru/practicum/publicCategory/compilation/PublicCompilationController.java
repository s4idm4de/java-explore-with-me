package ru.practicum.publicCategory.compilation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.compilation.dto.CompilationDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/compilations")
@Slf4j
public class PublicCompilationController {
    @Autowired
    private final PublicCompilationService publicCompilationService;

    public PublicCompilationController(PublicCompilationService publicCompilationService) {
        this.publicCompilationService = publicCompilationService;
    }

    @GetMapping
    public List<CompilationDto> getCompilations(@RequestParam(name = "pinned", required = false) Boolean pinned,
                                                @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("public CompilationController getCompilations pinned {}, from {}, size {}", pinned, from, size);
        return publicCompilationService.getCompilations(pinned, from, size);
    }

    @GetMapping(path = "/{compId}")
    public CompilationDto getCompilation(@PathVariable Long compId) {
        log.info("public CompilationController getCompilation compId {}", compId);
        return publicCompilationService.getCompilation(compId);
    }
}
