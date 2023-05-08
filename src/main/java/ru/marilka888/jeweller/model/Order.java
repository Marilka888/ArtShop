package ru.marilka888.jeweller.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Builder
@EqualsAndHashCode
@Table(name = "_orders")
public class Order {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @EqualsAndHashCode.Exclude
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    public User user;
    @ManyToOne
    @JoinColumn(name = "favour_id")
    public Favour favour;
    @Column(name = "description", columnDefinition = "text")
    private String description;
    @Column(name = "stage")
    private Stage stage;
    @Column(name = "sum")
    private Long sum;
    @Column(name = "qty")
    private Long qty;
    @Column(name = "status")
    private boolean status;
    @Column(name = "date_of_created")
    @CreationTimestamp
    private LocalDateTime dateOfCreated;

}


