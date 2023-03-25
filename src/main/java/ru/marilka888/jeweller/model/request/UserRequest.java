package ru.marilka888.jeweller.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.marilka888.jeweller.model.Role;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
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