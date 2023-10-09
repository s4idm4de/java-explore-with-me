package ru.practicum.model.user.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequest {
    @NotNull
    @Email
    @Length(min = 6, max = 254)
    private String email;
    @NotNull
    @NotBlank
    @Length(min = 2, max = 250)
    private String name;
}
