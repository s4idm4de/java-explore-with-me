package ru.practicum.model.comment.dto;

import lombok.*;
import ru.practicum.model.event.dto.EventShortDto;
import ru.practicum.model.user.dto.UserShortDto;

@Data
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentFullDto {

    private Long id;

    private UserShortDto author;

    private EventShortDto event;

    private String text;

    private String created;

    private Long likes;
}
