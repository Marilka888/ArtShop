package ru.marilka888.jeweller.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderRequest {
    private String title;

    private String description;

    private String price;

    private boolean status;

    public Long userId;
}