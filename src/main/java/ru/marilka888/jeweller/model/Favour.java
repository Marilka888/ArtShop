package ru.marilka888.jeweller.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "_favours")
@Builder
@EqualsAndHashCode
public class Favour {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @EqualsAndHashCode.Exclude
    private Long id;
    private String title;
    @Column(columnDefinition = "text")
    private String description;
    private String category;
    private String imageUrl;
    private Boolean is_enabled;
    private Integer num;
    private String origin_price;
    private Integer price;
}




