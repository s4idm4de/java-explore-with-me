package ru.practicum.model.category.dto;

import ru.practicum.model.category.Category;
import ru.practicum.model.user.User;
import ru.practicum.model.user.dto.UserDto;

import java.util.ArrayList;
import java.util.List;

public class CategoryMapper {

    public static Category toCategory(NewCategoryDto newCategoryDto) {
        return Category.builder().name(newCategoryDto.getName()).build();
    }

    public static Category toCategory(CategoryDto categoryDto) {
        return Category.builder().name(categoryDto.getName()).build();
    }

    public static CategoryDto toCategoryDto(Category category) {
        return CategoryDto.builder().name(category.getName()).id(category.getId()).build();
    }

    public static List<CategoryDto> toCategoryDto(Iterable<Category> categories) {
        List<CategoryDto> result = new ArrayList<>();

        for (Category category : categories) {
            result.add(toCategoryDto(category));
        }

        return result;
    }
}
