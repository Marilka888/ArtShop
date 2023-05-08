package ru.marilka888.jeweller.model.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import ru.marilka888.jeweller.model.Favour;
import ru.marilka888.jeweller.model.Stage;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    @JsonSerialize()
    private Long id;
    @JsonSerialize()
    public UserResponse user;
    @JsonSerialize()
    public Long userId;
    @JsonSerialize()
    public Favour favour;
    @JsonSerialize()
    private Stage stage;
    @JsonSerialize()
    private Boolean success;
    @JsonSerialize()
    private Long qty;
    @JsonSerialize()
    private String description;
    @JsonSerialize()
    private Long sum;
    @JsonSerialize()
    private boolean status;
    @JsonSerialize()
    private LocalDateTime dateOfCreated;

}
