package ru.practicum.admin.comment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.exception.RequestException;
import ru.practicum.exception.ValidatedException;
import ru.practicum.model.comment.ComplaintParams;
import ru.practicum.model.comment.dto.CommentFullDto;
import ru.practicum.model.comment.dto.ComplaintCommentDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping(path = "/admin")
@Slf4j
public class AdminCommentController {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public final AdminCommentService adminCommentService;

    public AdminCommentController(AdminCommentService adminCommentService) {
        this.adminCommentService = adminCommentService;
    }

    @GetMapping(path = "/comments/{commentId}")
    public CommentFullDto getComment(@PathVariable Long commentId) {
        log.info("AdminCommentController getComment commentId {}", commentId);
        return adminCommentService.getComment(commentId);
    }

    @GetMapping(path = "/users/{userId}/comments")
    public List<CommentFullDto> getCommentsOfUser(@PathVariable Long userId,
                                                  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                  @Positive @RequestParam(name = "size", defaultValue = "10") Integer size,
                                                  @RequestParam(name = "sort", defaultValue = "DATE") String sort) {
        log.info("AdminCommentController getCommentsOfUser userId {}, from {}, size {}, sort {}", userId, from, size, sort);
        try {
            if (!sort.equals("DATE") && !sort.equals("LIKES")) throw new RequestException("wrong sort");

            return adminCommentService.getCommentsOfUser(userId, from, size, sort);
        } catch (RequestException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

    }

    @DeleteMapping(path = "/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        log.info("AdminCommentController deleteComment commentId {}", commentId);
        adminCommentService.deleteComment(commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path = "/users/{userId}/comments")
    public ResponseEntity<Void> deleteCommentsOfUser(@PathVariable Long userId) {
        log.info("AdminCommentController deleteCommentsOfUser userId {}", userId);
        adminCommentService.deleteCommentsOfUser(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(path = "/comments/complaints")
    public List<ComplaintCommentDto> getComplaints(@RequestParam(name = "sort", defaultValue = "DATE") String sort,
                                                   @RequestParam(name = "includeConsidered", defaultValue = "false") Boolean includeConsidered,
                                                   @RequestParam(name = "rangeStart", required = false) String rangeStart,
                                                   @RequestParam(name = "rangeEnd", required = false) String rangeEnd,
                                                   @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                   @Positive @RequestParam(name = "size", defaultValue = "10") Integer size) {
        log.info("AdminCommentController getComplaints sort {}, includeConsidered {}, rangeStart {}, rangeEnd {}, from {}, size {}",
                sort, includeConsidered, rangeStart, rangeEnd, from, size);
        try {
            if (!sort.equals("DATE") && !sort.equals("COMPLAINTS")) throw new ValidatedException("wrong sort");
            ComplaintParams complaintParams = ComplaintParams.builder()
                    .sort(sort)
                    .includeConsidered(includeConsidered)
                    .rangeStart(rangeStart == null ? null : LocalDateTime.parse(rangeStart, formatter))
                    .rangeEnd(rangeEnd == null ? null : LocalDateTime.parse(rangeEnd, formatter))
                    .from(from)
                    .size(size)
                    .build();
            return adminCommentService.getComplaints(complaintParams);
        } catch (ValidatedException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }
}
