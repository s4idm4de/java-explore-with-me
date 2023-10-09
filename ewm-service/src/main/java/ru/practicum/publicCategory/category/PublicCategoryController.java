package ru.practicum.publicCategory.category;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.category.dto.CategoryDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/categories")
@Slf4j
public class PublicCategoryController {

    @Autowired
    private final PublicCategoryService publicCategoryService;

    public PublicCategoryController(PublicCategoryService publicCategoryService) {
        this.publicCategoryService = publicCategoryService;
    }

    @GetMapping
    public List<CategoryDto> getAllCategories(@PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                              @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("public Controller getAllCategories from {}, size {}", from, size);
        return publicCategoryService.getAllCategories(from, size);
    }

    @GetMapping(path = "/{catId}")
    public CategoryDto getCategory(@PathVariable Long catId) {
        log.info("public Controller getCategory catId {}", catId);
        return publicCategoryService.getCategory(catId);
    }
}
