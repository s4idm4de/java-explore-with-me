package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.comment.Comment;
import ru.practicum.model.comment.LikeComment;
import ru.practicum.model.user.User;


public interface LikeCommentRepository extends JpaRepository<LikeComment, Long> {

    LikeComment findByUserAndComment(User user, Comment comment);
}
