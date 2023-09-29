package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.category.Category;
import ru.practicum.model.user.User;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
