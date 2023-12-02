package ru.practicum.privateCategory.comment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.exception.RequestException;
import ru.practicum.model.comment.dto.CommentDtoForUser;
import ru.practicum.model.comment.dto.CommentNewDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}")
@Slf4j
public class PrivateCommentController {

    @Autowired
    private final PrivateCommentService privateCommentService;

    public PrivateCommentController(PrivateCommentService privateCommentService) {
        this.privateCommentService = privateCommentService;
    }

    @GetMapping("/comments")
    public List<CommentDtoForUser> getComments(@PathVariable Long userId,
                                               @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                               @Positive @RequestParam(name = "size", defaultValue = "10") Integer size,
                                               @RequestParam(name = "sort", defaultValue = "LIKES") String sort) {
        log.info("PrivateCommentController getComments userId {} from {} size {}, sort {}", userId, from, size, sort);
        try {
            if (!sort.equals("LIKES") && !sort.equals("DATE")) throw new RequestException("wrong sort");
            return privateCommentService.getComments(userId, from, size, sort);
        } catch (RequestException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @GetMapping("/comments/{commentId}")
    public CommentDtoForUser getComment(@PathVariable Long userId,
                                        @PathVariable Long commentId) {
        log.info("PrivateCommentController getComment userId {} commentId {}", userId, commentId);
        return privateCommentService.getComment(userId, commentId);
    }

    @PostMapping("/events/{eventId}/comments")
    public ResponseEntity<CommentDtoForUser> postComment(@PathVariable Long userId,
                                                         @PathVariable Long eventId,
                                                         @RequestBody @Validated CommentNewDto commentNewDto) {
        log.info("PrivateCommentController postComment userId {}, eventId {}, commentNewDto {}", userId, eventId, commentNewDto);
        return new ResponseEntity<>(privateCommentService.postComment(userId, eventId, commentNewDto), HttpStatus.CREATED);
    }

    @PatchMapping("/comments/{commentId}")
    public CommentDtoForUser patchComment(@PathVariable Long userId,
                                          @PathVariable Long commentId,
                                          @RequestBody @Validated CommentNewDto commentNewDto) {
        log.info("PrivateCommentController patchComment userId {}, commentId {}, commentNewDto {}",
                userId, commentId, commentNewDto);
        return privateCommentService.patchComment(userId, commentId, commentNewDto);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long userId,
                                              @PathVariable Long commentId) {
        log.info("PrivateCommentController deleteComment userId {}, commentId {}", userId, commentId);
        privateCommentService.deleteComment(userId, commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/comments/{commentId}/like")
    public ResponseEntity<Void> addLike(@PathVariable Long userId,
                                        @PathVariable Long commentId) {
        log.info("PrivateCommentController addLike userId {}, commentId {}", userId, commentId);
        privateCommentService.addLike(userId, commentId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/comments/{commentId}/like")
    public ResponseEntity<Void> deleteLike(@PathVariable Long userId,
                                           @PathVariable Long commentId) {
        log.info("PrivateCommentController deleteLike userId {}, commentId {}", userId, commentId);
        privateCommentService.deleteLike(userId, commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
