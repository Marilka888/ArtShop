package ru.marilka888.jeweller.model.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import ru.marilka888.jeweller.model.Role;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    @JsonSerialize()
    private String phone;
    @JsonSerialize()
    private String firstname;
    @JsonSerialize()
    private String lastname;
    @JsonSerialize()
    private String email;
    @JsonSerialize()
    private Integer age;
    @JsonSerialize()
    private LocalDateTime dateOfCreated;
    @JsonSerialize()
    private Role role;
    @JsonSerialize()
    private List<OrderResponse> orders;
}