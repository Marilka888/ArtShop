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
    public Long userId;
    @JsonSerialize()
    public Favour favour;
    @JsonSerialize()
    private String description;
    @JsonSerialize()
    private Stage stage;
    @JsonSerialize()
    private Integer sum;
    @JsonSerialize()
    private boolean status;
    @JsonSerialize()
    private LocalDateTime dateOfCreated;

}
