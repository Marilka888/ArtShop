package ru.marilka888.jeweller.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.marilka888.jeweller.model.User;
import ru.marilka888.jeweller.service.UserService;

import java.security.Principal;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping()
    @Transactional
    public Page<User> getUserList(@PageableDefault Pageable pageable) {
        return userService.findAll(pageable);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(path = "/{id}")
    @Transactional
    public User getUser(@PathVariable Long id) {
        return userService.findUser(id);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping(path = "/profile")
    @Transactional
    public User getUser(Principal principal) {
        return userService.findProfile(principal);
    }

    @PreAuthorize("hasAuthority('USER')")
    @PostMapping(path = "/profile")
    @Transactional
    public void updateUser(@RequestBody User user) {
        userService.updateUser(user);
    }

}
