package ru.practicum.model.comment.dto;

import ru.practicum.model.comment.Comment;
import ru.practicum.model.comment.ComplaintComment;
import ru.practicum.model.event.dto.EventMapper;
import ru.practicum.model.user.dto.UserMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class CommentMapper {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Comment toComment(CommentNewDto commentNewDto) {
        return Comment.builder()
                .text(commentNewDto.getText())
                .created(LocalDateTime.now())
                .build();
    }

    public static CommentFullDto toCommentFullDto(Comment comment) {
        return CommentFullDto.builder()
                .id(comment.getId())
                .author(UserMapper.toUserShortDto(comment.getAuthor()))
                .event(EventMapper.toEventShortDto(comment.getEvent()))
                .text(comment.getText())
                .created(comment.getCreated().format(formatter))
                .likes(comment.getLikes())
                .build();
    }

    public static CommentDtoForUser toCommentDtoForUser(Comment comment) {
        return CommentDtoForUser.builder()
                .id(comment.getId())
                .event(EventMapper.toEventShortDto(comment.getEvent()))
                .text(comment.getText())
                .created(comment.getCreated().format(formatter))
                .likes(comment.getLikes())
                .build();
    }

    public static CommentDtoForEvent toCommentDtoForEvent(Comment comment) {
        return CommentDtoForEvent.builder()
                .id(comment.getId())
                .author(UserMapper.toUserShortDto(comment.getAuthor()))
                .text(comment.getText())
                .created(comment.getCreated().format(formatter))
                .likes(comment.getLikes())
                .build();
    }

    public static List<CommentDtoForEvent> toCommentDtoForEvent(Iterable<Comment> comments) {
        List<CommentDtoForEvent> result = new ArrayList<>();

        for (Comment comment : comments) {
            result.add(toCommentDtoForEvent(comment));
        }

        return result;
    }

    public static List<CommentDtoForUser> toCommentDtoForUser(Iterable<Comment> comments) {
        List<CommentDtoForUser> result = new ArrayList<>();

        for (Comment comment : comments) {
            result.add(toCommentDtoForUser(comment));
        }

        return result;
    }

    public static List<CommentFullDto> toCommentFullDto(Iterable<Comment> comments) {
        List<CommentFullDto> result = new ArrayList<>();

        for (Comment comment : comments) {
            result.add(toCommentFullDto(comment));
        }

        return result;
    }

    public static ComplaintCommentDto toComplaintCommentDto(ComplaintComment complaintComment) {
        return ComplaintCommentDto.builder()
                .lastCreated(complaintComment.getLastCreated().format(formatter))
                .numberOfComplaints(complaintComment.getNumberOfComplaints())
                .isConsidered(complaintComment.getIsConsidered())
                .comment(CommentMapper.toCommentFullDto(complaintComment.getComment()))
                .build();
    }

    public static List<ComplaintCommentDto> toComplaintCommentDto(Iterable<ComplaintComment> complaints) {
        List<ComplaintCommentDto> result = new ArrayList<>();

        for (ComplaintComment complaint : complaints) {
            result.add(toComplaintCommentDto(complaint));
        }

        return result;
    }
}
