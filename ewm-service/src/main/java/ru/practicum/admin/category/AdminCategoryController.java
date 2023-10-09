package ru.practicum.admin.category;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.exception.ValidatedException;
import ru.practicum.model.category.dto.CategoryDto;
import ru.practicum.model.category.dto.NewCategoryDto;

@RestController
@RequestMapping(path = "/admin/categories")
@Slf4j
public class AdminCategoryController {

    @Autowired
    private final AdminCategoryService adminCategoryService;

    public AdminCategoryController(AdminCategoryService adminCategoryService) {
        this.adminCategoryService = adminCategoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryDto> postCategory(@RequestBody @Validated NewCategoryDto newCategoryDto) {
        log.info("admin category controller postCategory {}", newCategoryDto);
        return new ResponseEntity<>(adminCategoryService.postCategory(newCategoryDto), HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{catId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long catId) {
        log.info("admin category controller deleteCategory {}", catId);
        adminCategoryService.deleteCategory(catId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping(path = "/{catId}")
    public CategoryDto patchCategory(@PathVariable Long catId, @RequestBody CategoryDto categoryDto) {
        try {
            if (categoryDto.getName() != null && (categoryDto.getName().length() < 1
                    || categoryDto.getName().length() > 50 || categoryDto.getName().isBlank()))
                throw new ValidatedException("wrong name");
            return adminCategoryService.patchCategory(catId, categoryDto);
        } catch (ValidatedException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}
