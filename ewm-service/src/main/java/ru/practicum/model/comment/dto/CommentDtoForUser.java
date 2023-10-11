package ru.practicum.model.comment.dto;

import lombok.*;
import ru.practicum.model.event.dto.EventShortDto;

@Data
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentDtoForUser {

    private Long id;

    private EventShortDto event;

    private String text;

    private String created;

    private Long likes;
}
