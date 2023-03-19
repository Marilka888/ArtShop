package ru.marilka888.jeweller.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FavourResponse {
    private String title;

    private String description;

    private String price;
}