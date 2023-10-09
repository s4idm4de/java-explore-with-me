package ru.practicum.model.user.dto;

import lombok.*;

import javax.validation.constraints.Email;

@Data
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @Email
    private String email;

    private Long id;

    private String name;
}
