package ru.marilka888.jeweller.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.marilka888.jeweller.model.Stage;

import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderRequest {
    public Long userId;
    public Long favourId;
    private Optional<String> description;
    private Stage stage;
    private Integer num;
    private boolean status;

}