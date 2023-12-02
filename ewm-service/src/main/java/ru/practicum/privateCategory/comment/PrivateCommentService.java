package ru.practicum.privateCategory.comment;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidatedException;
import ru.practicum.model.comment.Comment;
import ru.practicum.model.comment.LikeComment;
import ru.practicum.model.comment.dto.CommentDtoForUser;
import ru.practicum.model.comment.dto.CommentMapper;
import ru.practicum.model.comment.dto.CommentNewDto;
import ru.practicum.model.event.Event;
import ru.practicum.model.event.dto.EventStatus;
import ru.practicum.model.user.User;
import ru.practicum.repository.CommentRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.LikeCommentRepository;
import ru.practicum.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PrivateCommentService {

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final EventRepository eventRepository;

    @Autowired
    private final CommentRepository commentRepository;

    @Autowired
    private final LikeCommentRepository likeCommentRepository;

    public List<CommentDtoForUser> getComments(Long userId, Integer from, Integer size, String sort) {
        log.info("PrivateCommentService getComments userId {}", userId);
        if (sort.equals("LIKES"))
            return CommentMapper.toCommentDtoForUser(commentRepository.findAllByAuthor_Id(userId,
                    PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "likes"))));
        return CommentMapper.toCommentDtoForUser(commentRepository.findAllByAuthor_Id(userId,
                PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "created"))));
    }

    public CommentDtoForUser getComment(Long userId,
                                        Long commentId) {
        log.info("PrivateCommentService getComment userId {} commentId {}", userId, commentId);
        try {
            Comment comment = commentRepository.findById(commentId).orElseThrow(()
                    -> new NotFoundException("no such comment"));
            if (!comment.getAuthor().getId().equals(userId))
                throw new ValidatedException("by id you can get only your own comments");
            return CommentMapper.toCommentDtoForUser(comment);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ValidatedException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

    }

    public CommentDtoForUser postComment(Long userId, Long eventId, CommentNewDto commentNewDto) {
        log.info("PrivateCommentService postComment userId {}, eventId {}, commentNewDto {}", userId, eventId, commentNewDto);
        try {
            User user = userRepository.findById(userId).orElseThrow(()
                    -> new NotFoundException("no such user"));
            Event event = eventRepository.findById(eventId).orElseThrow(()
                    -> new NotFoundException("no such event"));
            log.info("PrivateCommentService event {} user {}", event, user);
            if (!event.getState().equals(EventStatus.PUBLISHED))
                throw new ValidatedException("event was not published");
            if (!user.getAllowToComment()) throw new ValidatedException("user have not permission to comment");
            Comment comment = CommentMapper.toComment(commentNewDto);
            comment.setAuthor(user);
            comment.setEvent(event);
            return CommentMapper.toCommentDtoForUser(commentRepository.save(comment));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ValidatedException e) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, e.getMessage(), e);
        }
    }

    public CommentDtoForUser patchComment(Long userId,
                                          Long commentId,
                                          CommentNewDto commentNewDto) { //коммент может обновлять только автор и только не позднее чем через два часа
        log.info("PrivateCommentService patchComment userId {}, commentId {}, commentNewDto {}",
                userId, commentId, commentNewDto);
        try {
            Comment comment = commentRepository.findById(commentId).orElseThrow(()
                    -> new NotFoundException("no such comment"));
            if (!comment.getAuthor().getId().equals(userId))
                throw new ValidatedException("you can change only your comments");
            if (comment.getCreated().plusHours(2).isBefore(LocalDateTime.now()))
                throw new ValidatedException("you miss your time for change comment");
            comment.setCreated(LocalDateTime.now());
            comment.setText(commentNewDto.getText());
            return CommentMapper.toCommentDtoForUser(commentRepository.save(comment));
        } catch (NotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ValidatedException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

    }


    public void deleteComment(Long userId, Long commentId) {
        log.info("PrivateCommentService deleteComment userId {}, commentId {}", userId, commentId);
        try {
            Comment comment = commentRepository.findById(commentId).orElseThrow(()
                    -> new NotFoundException("no such comment"));
            if (!comment.getAuthor().getId().equals(userId))
                throw new ValidatedException("you can delete only your comments");
            commentRepository.delete(comment);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ValidatedException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    public void addLike(Long userId, Long commentId) {
        log.info("PrivateCommentService addLike userId {}, commentId {}", userId, commentId);
        try {
            User user = userRepository.findById(userId).orElseThrow(()
                    -> new NotFoundException("no such user"));
            Comment comment = commentRepository.findById(commentId).orElseThrow(()
                    -> new NotFoundException("no such comment"));
            LikeComment likeComment = LikeComment.builder()
                    .created(LocalDateTime.now())
                    .comment(comment)
                    .user(user)
                    .build();
            likeCommentRepository.save(likeComment);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    public void deleteLike(Long userId, Long commentId) {
        log.info("PrivateCommentService deleteLike userId {}, commentId {}", userId, commentId);
        try {
            User user = userRepository.findById(userId).orElseThrow(()
                    -> new NotFoundException("no such user"));
            Comment comment = commentRepository.findById(commentId).orElseThrow(()
                    -> new NotFoundException("no such comment"));
            LikeComment likeComment = likeCommentRepository.findByUserAndComment(user, comment);
            if (likeComment == null) throw new ValidatedException("no like on the comment from this user");
            likeCommentRepository.delete(likeComment);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        } catch (ValidatedException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}
