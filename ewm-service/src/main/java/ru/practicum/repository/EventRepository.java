package ru.practicum.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.model.event.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {
    List<Event> findAllByInitiator_Id(Long initiatorId, PageRequest pageRequest);

    List<Event> findAllByCategory_Id(Long catId);

    @Query("SELECT e FROM Event e WHERE e.id in ?1")
    List<Event> findAllByIds(List<Long> eventIds);
}
