package ru.practicum.model.comment.dto;

import lombok.*;
import ru.practicum.model.user.dto.UserShortDto;

@Data
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDtoForEvent {

    private Long id;

    UserShortDto author;

    private String text;

    private String created;

    private Long likes;
}
