package ru.practicum.admin.comment;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.comment.Comment;
import ru.practicum.model.comment.ComplaintComment;
import ru.practicum.model.comment.ComplaintParams;
import ru.practicum.model.comment.QComplaintComment;
import ru.practicum.model.comment.dto.CommentFullDto;
import ru.practicum.model.comment.dto.CommentMapper;
import ru.practicum.model.comment.dto.ComplaintCommentDto;
import ru.practicum.model.user.User;
import ru.practicum.repository.CommentRepository;
import ru.practicum.repository.ComplaintCommentRepository;
import ru.practicum.repository.UserRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AdminCommentService {

    @Autowired
    private final CommentRepository commentRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final ComplaintCommentRepository complaintCommentRepository;

    public CommentFullDto getComment(Long commentId) {
        log.info("AdminCommentService getComment commentId {}", commentId);
        try {
            Comment comment = commentRepository.findById(commentId).orElseThrow(()
                    -> new NotFoundException("no such comment"));
            return CommentMapper.toCommentFullDto(comment);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    public List<CommentFullDto> getCommentsOfUser(Long userId, Integer from, Integer size, String sort) {
        log.info("AdminCommentService getCommentsOfUser userId {}", userId);
        if (sort.equals("LIKES"))
            return CommentMapper.toCommentFullDto(commentRepository.findAllByAuthor_Id(userId,
                    PageRequest.of(from / size, size, Sort.by(Sort.Direction.ASC, "likes"))));
        return CommentMapper.toCommentFullDto(commentRepository.findAllByAuthor_Id(userId,
                PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "created"))));

    }

    public void deleteComment(Long commentId) {
        log.info("AdminCommentService deleteComment commentId {}", commentId);
        try {
            Comment comment = commentRepository.findById(commentId).orElseThrow(()
                    -> new NotFoundException("no such comment"));
            User author = comment.getAuthor();
            author.setAllowToComment(false);
            userRepository.save(author);
            commentRepository.delete(comment);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }

    public void deleteCommentsOfUser(Long userId) {
        log.info("AdminCommentService deleteCommentsOfUser userId {}", userId);
        try {
            User user = userRepository.findById(userId).orElseThrow(()
                    -> new NotFoundException("no such user"));
            user.setAllowToComment(false);
            userRepository.save(user);
            commentRepository.deleteAllByAuthor_Id(userId);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }


    public List<ComplaintCommentDto> getComplaints(ComplaintParams complaintParams) {
        log.info("AdminCommentService getComplaints complaintParams {}", complaintParams);
        BooleanExpression basicConditions = QComplaintComment.complaintComment.id.gt(0L);
        if (!complaintParams.getIncludeConsidered()) {
            BooleanExpression notInclude = QComplaintComment.complaintComment.isConsidered.isFalse();
            basicConditions = basicConditions.and(notInclude);
        }
        if (complaintParams.getRangeStart() != null) {
            BooleanExpression startCondition = QComplaintComment.complaintComment.lastCreated.after(complaintParams.getRangeStart());
            basicConditions = basicConditions.and(startCondition);
        }
        if (complaintParams.getRangeEnd() != null) {
            BooleanExpression endCondition = QComplaintComment.complaintComment.lastCreated.before(complaintParams.getRangeEnd());
            basicConditions = basicConditions.and(endCondition);
        }
        if (complaintParams.getSort().equals("DATE")) {
            Page<ComplaintComment> complaintComments = complaintCommentRepository.findAll(basicConditions,
                    PageRequest.of(complaintParams.getFrom() / complaintParams.getSize(), complaintParams.getSize(),
                            Sort.by(Sort.Direction.DESC, "lastCreated")));
            complaintComments.forEach(complaintComment -> {
                complaintComment.setIsConsidered(true);
                complaintCommentRepository.save(complaintComment);
            });
            return CommentMapper.toComplaintCommentDto(complaintComments);
        }
        Page<ComplaintComment> complaintComments = complaintCommentRepository.findAll(basicConditions,
                PageRequest.of(complaintParams.getFrom() / complaintParams.getSize(), complaintParams.getSize(),
                        Sort.by(Sort.Direction.DESC, "numberOfComplaints")));
        complaintComments.forEach(complaintComment -> {
            complaintComment.setIsConsidered(true);
            complaintCommentRepository.save(complaintComment);
        });
        return CommentMapper.toComplaintCommentDto(complaintComments);
    }
}
