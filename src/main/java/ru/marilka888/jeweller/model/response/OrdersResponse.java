package ru.marilka888.jeweller.model.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrdersResponse {
    @JsonSerialize()
    private Boolean success;
    @JsonSerialize()
    private List<OrderResponse> orders;

}
