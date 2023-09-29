package ru.practicum.model.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.model.Location;
import ru.practicum.model.category.Category;
import ru.practicum.model.category.dto.CategoryDto;
import ru.practicum.model.event.dto.EventStatus;
import ru.practicum.model.user.User;
import ru.practicum.model.user.dto.UserShortDto;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@Entity
@Table(name = "events", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    @Column
    private String annotation;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column
    private Long confirmedRequests; //Количество одобренных заявок на участие в данном событии

    @Column
    private LocalDateTime createdOn; //создание события

    @Column
    private String description;

    @Column
    private LocalDateTime eventDate; //дата события

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User initiator;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;

    @Column
    private Boolean paid;

    @Column
    private Long participantLimit;

    @Column
    private LocalDateTime publishedOn; //публикация события

    @Column
    private Boolean requestModeration;

    @Enumerated(EnumType.STRING)
    private EventStatus state;

    @Column
    private String title;

    @Column
    private Long views;
}
