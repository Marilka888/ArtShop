//package ru.marilka888.jeweller.model.response;
//
//import com.fasterxml.jackson.databind.annotation.JsonSerialize;
//import jakarta.persistence.Column;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
//import jakarta.persistence.Id;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.EqualsAndHashCode;
//import lombok.NoArgsConstructor;
//import ru.marilka888.jeweller.model.Cart;
//import ru.marilka888.jeweller.model.Favour;
//
//import java.util.List;
//
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//public class CartResponse {
//
//    @JsonSerialize()
//    private Boolean success;
//    @JsonSerialize()
//    private Long id;
//
//    @JsonSerialize()
//    private Integer final_total;
//
//    @JsonSerialize()
//    private Favour favour;
//
//    @JsonSerialize()
//    private Long favour_id;
//
//    @JsonSerialize()
//    private Integer qty;
//
//    @JsonSerialize()
//    private List<Cart> carts;
//
//}
