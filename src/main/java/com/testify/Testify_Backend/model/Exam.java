package com.testify.Testify_Backend.model;
import com.testify.Testify_Backend.converter.QuestionSequenceConverter;
import com.testify.Testify_Backend.enums.OrderType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
@Entity
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor

public class Exam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne
    private User createdBy;

    @ManyToOne
    private Organization organization;

    @Lob //Specifies that the column should be capable of storing large objects, allowing TEXT types in the database.
    @Column(columnDefinition = "TEXT")
    private String description;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String instructions;
    private int duration;

    @Column(nullable = false)
    private LocalDateTime startDatetime;

    @Column(nullable = false)
    private LocalDateTime endDatetime;

    @Column(nullable = false)
    private boolean isPrivate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderType orderType;

    @OneToOne(mappedBy = "exam", cascade = CascadeType.ALL)
    private FixedOrder fixedOrder;

    @OneToOne(mappedBy = "exam", cascade = CascadeType.ALL)
    private RandomOrder randomOrder;

    @ManyToOne
    @JoinColumn(name = "moderator_id")
    private ExamSetter moderator;

    @ManyToMany
    @JoinTable(
            name = "exam_proctors",
            joinColumns = @JoinColumn(name = "exam_id"),
            inverseJoinColumns = @JoinColumn(name = "proctor_id")
    )
    private Set<ExamSetter> proctors;

    @ManyToMany
    @JoinTable(
            name = "exam_candidates",
            joinColumns = @JoinColumn(name = "exam_id"),
            inverseJoinColumns = @JoinColumn(name = "candidate_id")
    )
    private Set<Candidate> candidates;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL)
    private Set<Question> questions;

    @Convert(converter = QuestionSequenceConverter.class)
    private List<Long> questionSequence;

}
