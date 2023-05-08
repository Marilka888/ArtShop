package ru.marilka888.jeweller.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderRequest {
    public Long userId;
    public Long favourId;
    public Long size;
    public Long qty;
    public Boolean accessories;
    public Boolean sketch;
    private Optional<String> description;
    private boolean status;

}