package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.model.event.Event;
import ru.practicum.model.request.Request;
import ru.practicum.model.user.User;

import java.util.List;


public interface RequestRepository extends JpaRepository<Request, Long>, QuerydslPredicateExecutor<Request> {
    List<Request> findAllByEvent_Id(Long eventId);

    List<Request> findAllByRequester_Id(Long requesterId);

    Request findByRequesterAndEvent(User requester, Event event);

    @Query("SELECT r FROM Request r WHERE r.id in ?1 ORDER BY r.id")
    List<Request> findAllByIds(List<Long> requestIds);
}
