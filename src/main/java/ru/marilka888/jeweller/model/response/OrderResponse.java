package ru.marilka888.jeweller.model.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {

    @JsonSerialize()
    private String title;

    @JsonSerialize()
    private String description;

    @JsonSerialize()
    private String price;

    @JsonSerialize()
    private boolean status;

    @JsonSerialize()
    public Long userId;

    @JsonSerialize()
    private LocalDateTime dateOfCreated;
}
