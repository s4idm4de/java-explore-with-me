package ru.practicum.model.user.dto;

import lombok.*;

@Data
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserShortDto {
    private Long id;

    private String name;
}
