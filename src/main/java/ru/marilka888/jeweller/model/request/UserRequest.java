package ru.marilka888.jeweller.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.marilka888.jeweller.model.Order;
import ru.marilka888.jeweller.model.Role;
import ru.marilka888.jeweller.model.token.Token;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserRequest {
    private String phone;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private Integer age;
    private LocalDateTime dateOfCreated;
    private Role role;
}