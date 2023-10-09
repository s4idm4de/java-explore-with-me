package ru.practicum.model.event.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import ru.practicum.model.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder(toBuilder = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewEventDto {
    @NotNull
    @NotBlank
    @Length(min = 20, max = 2000)
    private String annotation;

    @NotNull
    private Long category;


    @NotNull
    @NotBlank
    @Length(min = 20, max = 7000)
    private String description;

    @NotNull
    private String eventDate;

    @NotNull
    private Location location;

    private Boolean paid = false;

    private Long participantLimit = 0L;

    private Boolean requestModeration = true;

    @NotNull
    @NotBlank
    @Length(min = 3, max = 120)
    private String title; //3-120
}
