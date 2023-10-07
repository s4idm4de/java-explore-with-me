package ru.practicum.model.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.model.Location;
import ru.practicum.model.category.Category;
import ru.practicum.model.event.dto.EventStatus;
import ru.practicum.model.user.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@Entity
@Table(name = "events", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    @Length(min = 20, max = 2000)
    @Column(length = 2000)
    private String annotation;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "confirmed_requests")
    private Long confirmedRequests = 0L; //Количество одобренных заявок на участие в данном событии

    @Column(name = "created_on")
    private LocalDateTime createdOn; //создание события

    @Length(min = 20, max = 7000)
    @Column(length = 7000)
    private String description;

    @Column(name = "event_date")
    private LocalDateTime eventDate; //дата события

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "initiator_id", nullable = false)
    private User initiator;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "lat", column = @Column(name = "lat")),
            @AttributeOverride(name = "lon", column = @Column(name = "lon"))
    })
    private Location location;

    @Column
    private Boolean paid;

    @Column(name = "participant_limit")
    private Long participantLimit;

    @Column(name = "published_on")
    private LocalDateTime publishedOn; //публикация события

    @Column(name = "request_moderation")
    private Boolean requestModeration;

    @Enumerated(EnumType.STRING)
    private EventStatus state;

    @Length(min = 3, max = 120)
    @Column
    private String title;

    @Column
    private Long views = 0L;

}
