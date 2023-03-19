package ru.marilka888.jeweller.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.AUTO;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "_favours")
public class Favour {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = AUTO)
    @JsonIgnore
    private Integer id;

    private String title;

    @Column(columnDefinition = "text")
    private String description;

    private String price;

    private Long userId;
}




