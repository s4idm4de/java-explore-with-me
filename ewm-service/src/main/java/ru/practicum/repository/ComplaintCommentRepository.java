package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.practicum.model.comment.ComplaintComment;


public interface ComplaintCommentRepository extends JpaRepository<ComplaintComment, Long>, QuerydslPredicateExecutor<ComplaintComment> {

    ComplaintComment findByComment_Id(Long commentId);
}
