package ru.practicum.admin.category;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.category.dto.CategoryDto;
import ru.practicum.model.category.dto.NewCategoryDto;

@RestController
@RequestMapping(path = "/admin/categories")
@Slf4j
public class CategoryController {

    @Autowired private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public CategoryDto postCategory(@RequestBody NewCategoryDto newCategoryDto) {
        log.info("admin category controller postCategory {}", newCategoryDto);
        return categoryService.postCategory(newCategoryDto);
    }

    @DeleteMapping(path = "/{catId}")
    public void deleteCategory(@PathVariable Long catId) {
        log.info("admin category controller deleteCategory {}", catId);
        categoryService.deleteCategory(catId);
    }

    @PatchMapping(path = "/{catId}")
    public CategoryDto patchCategory(@PathVariable Long catId, @RequestBody CategoryDto categoryDto) {
        return categoryService.patchCategory(catId, categoryDto);
    }
}
