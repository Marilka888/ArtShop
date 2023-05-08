package ru.marilka888.jeweller.model.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import ru.marilka888.jeweller.model.Favour;
import ru.marilka888.jeweller.model.Order;
import ru.marilka888.jeweller.model.Stage;
import ru.marilka888.jeweller.model.User;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    @JsonSerialize()
    private Long id;
    @JsonSerialize()
    public Long userId;
    @JsonSerialize()
    public UserResponse user;
    @JsonSerialize()
    public Favour favour;
    @JsonSerialize()
    public Long favourId;
    @JsonSerialize()
    private Boolean success;
    @JsonSerialize()
    private List<Order> orders;
    @JsonSerialize()
    private Order order;
    @JsonSerialize()
    private Long qty;

    @JsonSerialize()
    private String description;
    @JsonSerialize()
    private Stage stage;
    @JsonSerialize()
    private Long sum;
    @JsonSerialize()
    private boolean status;
    @JsonSerialize()
    private LocalDateTime dateOfCreated;

}
