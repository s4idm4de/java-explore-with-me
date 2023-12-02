package ru.practicum.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.comment.Comment;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByEvent_Id(Long eventId, PageRequest pageRequest);

    List<Comment> findAllByAuthor_Id(Long authorId, PageRequest pageRequest);

    List<Comment> findAllByAuthor_Id(Long authorId);

    void deleteAllByAuthor_Id(Long authorId);
}
