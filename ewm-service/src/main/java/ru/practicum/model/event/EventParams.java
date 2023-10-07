package ru.practicum.model.event;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EventParams {
    private String text;
    private List<Long> users;
    private List<String> states;
    private List<Long> categories;
    private Boolean paid;
    private LocalDateTime rangeStart;
    private LocalDateTime rangeEnd;
    private Boolean onlyAvailable;
    private String sort; //проверить дополнительные требования
    private Integer from;
    private Integer size;
}
