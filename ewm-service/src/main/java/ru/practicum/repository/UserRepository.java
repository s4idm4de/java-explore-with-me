package ru.practicum.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.model.user.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>, QuerydslPredicateExecutor<User> {

    @Query("SELECT u FROM User u WHERE u.id IN ?1")
    List<User> findAllUsersByIds(List<Long> userIds, PageRequest pageRequest);

    List<User> findAllByName(String name);
}
