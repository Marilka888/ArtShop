package ru.marilka888.jeweller.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderResponse {
    private String title;

    private String description;

    private String price;

    private boolean status;

    public Long userId;

    private LocalDateTime dateOfCreated;
}
