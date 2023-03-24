package ru.marilka888.jeweller.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.marilka888.jeweller.model.User;
import ru.marilka888.jeweller.model.request.UserRequest;
import ru.marilka888.jeweller.model.response.UserResponse;
import ru.marilka888.jeweller.service.UserService;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;

    @GetMapping(path = "/profile")
    @Transactional
    public ResponseEntity<UserResponse> getUser(Principal principal) {
        return ResponseEntity.ok(userService.findProfile(principal));
    }

    @PostMapping(path = "/profile")
    @Transactional
    public void updateUser(@RequestBody UserRequest user) {
        userService.updateUser(user);
    }


    @GetMapping("/users/all")
    @Transactional
    public ResponseEntity<Page<User>> getUserList(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(userService.findAll(pageable));
    }

    @GetMapping(path = "/users/{id}")
    @Transactional
    public ResponseEntity<User> getUser(@PathVariable Integer id) {
        return ResponseEntity.ok(userService.findUser(id));
    }
}
