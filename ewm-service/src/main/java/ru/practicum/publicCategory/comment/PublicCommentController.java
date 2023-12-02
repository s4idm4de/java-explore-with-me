package ru.practicum.publicCategory.comment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.model.comment.dto.CommentDtoForEvent;
import ru.practicum.model.comment.dto.ComplaintCommentDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/events/{eventId}/comments")
@Slf4j
public class PublicCommentController {

    @Autowired
    private final PublicCommentService publicCommentService;

    public PublicCommentController(PublicCommentService publicCommentService) {
        this.publicCommentService = publicCommentService;
    }

    @GetMapping
    public List<CommentDtoForEvent> getComments(@PathVariable Long eventId,
                                                @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("PublicCommentController getComments eventId {}, from {}, size {}", eventId, from, size);
        return publicCommentService.getComments(eventId, from, size);
    }

    @PostMapping("/{commentId}/complaint")
    public ResponseEntity<ComplaintCommentDto> postComplaint(@PathVariable Long eventId,
                                                             @PathVariable Long commentId) {
        log.info("PublicCommentController postComplaint eventId {} commentId {} ", eventId, commentId);
        return new ResponseEntity<>(publicCommentService.postComplaint(eventId, commentId), HttpStatus.CREATED);
    }
}
