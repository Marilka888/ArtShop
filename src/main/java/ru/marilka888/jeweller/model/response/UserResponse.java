package ru.marilka888.jeweller.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import ru.marilka888.jeweller.model.Order;
import ru.marilka888.jeweller.model.Role;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    private String phone;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private Integer age;
    private LocalDateTime dateOfCreated;
    private Role role;
    private List<Order> orders;
}