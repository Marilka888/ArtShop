//package ru.marilka888.jeweller.model;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import jakarta.persistence.*;
//import lombok.*;
//import org.hibernate.annotations.CreationTimestamp;
//
//import java.time.LocalDateTime;
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Entity
//@Data
//@Builder
//@EqualsAndHashCode
//@Table(name = "_carts")
//public class Cart {
//    @Id
//    @Column(name = "id")
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    @EqualsAndHashCode.Exclude
//    private Long id;
//
//    @Column(name = "final_total")
//    private Integer final_total;
//
//    @ManyToOne
//    @JoinColumn(name = "favour_id")
//    private Favour favour;
//
//    @Column(name = "favour_id")
//    private Integer favour_id;
//
//    @Column(name = "qty")
//    private Integer qty;
//
//    @Column(name = "total")
//    private Integer total;
//
//}
//
//
