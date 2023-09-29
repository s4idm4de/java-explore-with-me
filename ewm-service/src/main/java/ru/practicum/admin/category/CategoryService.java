package ru.practicum.admin.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.category.Category;
import ru.practicum.model.category.dto.CategoryDto;
import ru.practicum.model.category.dto.CategoryMapper;
import ru.practicum.model.category.dto.NewCategoryDto;
import ru.practicum.repository.CategoryRepository;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    @Autowired
    private final CategoryRepository categoryRepository;

    public CategoryDto postCategory(NewCategoryDto newCategoryDto) {
        log.info("admin category service postCategory {}", newCategoryDto);
        Category category = CategoryMapper.toCategory(newCategoryDto);
        CategoryDto categoryDto = CategoryMapper.toCategoryDto(categoryRepository.save(category));
        return categoryDto;
    }

    public void deleteCategory(Long catId) {
        log.info("admin category Service deleteCategory {}", catId);
        try {
            Category category = categoryRepository.findById(catId).orElseThrow(()
                    -> new NotFoundException("no such category"));
            categoryRepository.delete(category);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    public CategoryDto patchCategory(Long catId, CategoryDto categoryDto) {
        log.info("admin category Service patchCategory {}", catId);
        try {
            Category category = categoryRepository.findById(catId).orElseThrow(()
                    -> new NotFoundException("no such category"));
            category.setName(categoryDto.getName()); //в кактегории только имя и айдишник, уникальность прописана в Category
            return CategoryMapper.toCategoryDto(categoryRepository.save(category));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}
