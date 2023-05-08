package ru.marilka888.jeweller.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FavourRequest {
    private Long id;
    private String title;
    private String description;
    private Integer price;
    private String category;
    private String imageUrl;
    private Boolean is_enabled;
    private Integer num;
    private String origin_price;
}