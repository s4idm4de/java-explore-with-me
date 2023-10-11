package ru.practicum.model.comment.dto;

import lombok.*;

@Data
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ComplaintCommentDto {

    private CommentFullDto comment;

    private String lastCreated;

    private Long numberOfComplaints;

    private Boolean isConsidered;
}
