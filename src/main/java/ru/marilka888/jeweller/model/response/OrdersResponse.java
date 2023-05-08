package ru.marilka888.jeweller.model.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import ru.marilka888.jeweller.model.Favour;
import ru.marilka888.jeweller.model.Order;
import ru.marilka888.jeweller.model.Stage;

import java.time.LocalDateTime;
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
