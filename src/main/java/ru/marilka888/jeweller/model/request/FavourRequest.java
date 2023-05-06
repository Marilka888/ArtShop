package ru.marilka888.jeweller.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FavourRequest {
    private String title;

    private String description;

    private Integer price;
}