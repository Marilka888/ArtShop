package ru.marilka888.jeweller.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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