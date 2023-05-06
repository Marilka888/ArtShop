package ru.marilka888.jeweller.model.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import ru.marilka888.jeweller.model.Favour;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FavourResponse {
    @JsonSerialize()
    private Boolean success;
    @JsonSerialize()
    private List<Favour> favours;

    @JsonSerialize()
    private Favour favour;
}