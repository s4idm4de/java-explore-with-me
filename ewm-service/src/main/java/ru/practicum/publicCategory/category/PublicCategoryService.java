package ru.practicum.publicCategory.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.category.Category;
import ru.practicum.model.category.dto.CategoryDto;
import ru.practicum.model.category.dto.CategoryMapper;
import ru.practicum.repository.CategoryRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PublicCategoryService {

    @Autowired
    private final CategoryRepository categoryRepository;

    public List<CategoryDto> getAllCategories(Integer from,
                                              Integer size) {
        log.info("public Service getAllCategories from {}, size {}", from, size);
        Page<Category> categories = categoryRepository.findAll(PageRequest.of(from / size, size));
        return CategoryMapper.toCategoryDto(categories);
    }


    public CategoryDto getCategory(Long catId) {
        log.info("public Service getCategory catId {}", catId);
        try {
            Category category = categoryRepository.findById(catId).orElseThrow(()
                    -> new NotFoundException("no such category"));
            return CategoryMapper.toCategoryDto(category);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}
