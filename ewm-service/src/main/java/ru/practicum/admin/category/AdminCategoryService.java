package ru.practicum.admin.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidatedException;
import ru.practicum.model.category.Category;
import ru.practicum.model.category.dto.CategoryDto;
import ru.practicum.model.category.dto.CategoryMapper;
import ru.practicum.model.category.dto.NewCategoryDto;
import ru.practicum.model.event.Event;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AdminCategoryService {

    @Autowired
    private final CategoryRepository categoryRepository;

    @Autowired
    private final EventRepository eventRepository;

    public CategoryDto postCategory(NewCategoryDto newCategoryDto) {
        log.info("admin category service postCategory {}", newCategoryDto);
        try {
            if (!categoryRepository.findAllByName(newCategoryDto.getName()).isEmpty())
                throw new ValidatedException("name is not unique");
            Category category = CategoryMapper.toCategory(newCategoryDto);
            CategoryDto categoryDto = CategoryMapper.toCategoryDto(categoryRepository.save(category));
            return categoryDto;
        } catch (ValidatedException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    public void deleteCategory(Long catId) {
        log.info("admin category Service deleteCategory {}", catId);
        try {
            Category category = categoryRepository.findById(catId).orElseThrow(()
                    -> new NotFoundException("no such category"));
            List<Event> events = eventRepository.findAllByCategory_Id(catId);
            log.info("admin category Service deleteCategory events {}", events);
            if (events != null && !events.isEmpty()) throw new ValidatedException("there are events of this category");
            categoryRepository.delete(category);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ValidatedException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    public CategoryDto patchCategory(Long catId, CategoryDto categoryDto) {
        log.info("admin category Service patchCategory {}, categoryDto {}", catId, categoryDto);
        try {
            Category category = categoryRepository.findById(catId).orElseThrow(()
                    -> new NotFoundException("no such category"));
            if (categoryDto.getName() != null
                    && !category.getName().equals(categoryDto.getName())
                    && !categoryRepository.findAllByName(categoryDto.getName()).isEmpty()) {
                throw new ValidatedException("name repeats");
            }
            category.setName(categoryDto.getName()); //в кактегории только имя и айдишник, уникальность прописана в Category
            return CategoryMapper.toCategoryDto(categoryRepository.save(category));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ValidatedException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }
}
