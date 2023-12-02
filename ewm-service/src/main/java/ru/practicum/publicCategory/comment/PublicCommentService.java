package ru.practicum.publicCategory.comment;

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
import ru.practicum.model.comment.Comment;
import ru.practicum.model.comment.ComplaintComment;
import ru.practicum.model.comment.dto.CommentDtoForEvent;
import ru.practicum.model.comment.dto.CommentMapper;
import ru.practicum.model.comment.dto.ComplaintCommentDto;
import ru.practicum.repository.CommentRepository;
import ru.practicum.repository.ComplaintCommentRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PublicCommentService {

    @Autowired
    private final CommentRepository commentRepository;

    @Autowired
    private final ComplaintCommentRepository complaintCommentRepository;

    public List<CommentDtoForEvent> getComments(Long eventId, Integer from, Integer size) {
        log.info("PublicCommentService getComments eventId {}", eventId);
        return CommentMapper.toCommentDtoForEvent(commentRepository.findAllByEvent_Id(eventId,
                PageRequest.of(from / size, size, Sort.by(Sort.Direction.DESC, "likes"))));
    }


    public ComplaintCommentDto postComplaint(Long eventId,
                                             Long commentId) {
        log.info("PublicCommentService postComplaint eventId {} commentId {}", eventId, commentId);

        ComplaintComment oldComplaintComment = complaintCommentRepository.findByComment_Id(commentId);
        if (oldComplaintComment != null) {
            oldComplaintComment.setNumberOfComplaints(oldComplaintComment.getNumberOfComplaints() + 1);
            oldComplaintComment.setLastCreated(LocalDateTime.now());
            return CommentMapper.toComplaintCommentDto(complaintCommentRepository.save(oldComplaintComment));
        }
        try {
            Comment comment = commentRepository.findById(commentId).orElseThrow(()
                    -> new NotFoundException("no such comment"));
            ComplaintComment complaintComment = ComplaintComment.builder()
                    .comment(comment)
                    .lastCreated(LocalDateTime.now())
                    .build();
            return CommentMapper.toComplaintCommentDto(complaintComment);
        } catch (NotFoundException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage(), e);
        }
    }
}
