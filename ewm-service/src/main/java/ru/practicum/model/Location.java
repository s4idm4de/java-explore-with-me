package ru.practicum.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Data
@Builder(toBuilder = true)
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class Location {

    private Double lat;

    private Double lon;
}
