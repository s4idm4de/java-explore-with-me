package ru.practicum.admin.compilations;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.exception.ValidatedException;
import ru.practicum.model.compilation.dto.CompilationDto;
import ru.practicum.model.compilation.dto.NewCompilationDto;
import ru.practicum.model.compilation.dto.UpdateCompilationRequest;

@RestController
@RequestMapping(path = "/admin/compilations")
@Slf4j
public class AdminCompilationController {
    @Autowired
    private final AdminCompilationService adminCompilationService;

    public AdminCompilationController(AdminCompilationService adminCompilationService) {
        this.adminCompilationService = adminCompilationService;
    }

    @PostMapping
    public ResponseEntity<CompilationDto> postCompilation(@RequestBody @Validated NewCompilationDto newCompilationDto) {
        log.info("admin CompilationController postCompilation newCompilationDto {}", newCompilationDto);
        return new ResponseEntity<>(adminCompilationService.postCompilation(newCompilationDto), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{compId}")
    public ResponseEntity<Void> deleteCompilation(@PathVariable Long compId) {
        log.info("admin CompilationController deleteCompilation compId {}", compId);
        adminCompilationService.deleteCompilation(compId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(path = "/{compId}")
    public CompilationDto patchCompilation(@PathVariable Long compId,
                                           @RequestBody @Validated UpdateCompilationRequest updateCompilationRequest) {
        log.info("admin CompilationController patchCompilation compId {}, updateCompilationRequest {}",
                compId, updateCompilationRequest);
        try {
            if (updateCompilationRequest.getTitle() != null && (updateCompilationRequest.getTitle().length() < 1
                    || updateCompilationRequest.getTitle().length() > 50))
                throw new ValidatedException("wrong parameters");
            return adminCompilationService.patchCompilation(compId, updateCompilationRequest);
        } catch (ValidatedException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}
