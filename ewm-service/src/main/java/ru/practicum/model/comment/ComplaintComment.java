package ru.practicum.model.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
@Entity
@Table(name = "complaints_on_comments", schema = "public")
@AllArgsConstructor
@NoArgsConstructor
public class ComplaintComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "comment_id", nullable = false, unique = true)
    private Comment comment;

    @Column(name = "last_created")
    private LocalDateTime lastCreated;

    @Column(name = "number_of_complaints")
    private Long numberOfComplaints;

    @Column(name = "is_considered")
    private Boolean isConsidered = false;
}
