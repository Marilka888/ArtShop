package ru.marilka888.jeweller.controller;

import io.micrometer.core.annotation.Counted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.marilka888.jeweller.common.exception.BadRequestException;
import ru.marilka888.jeweller.common.exception.InnerException;
import ru.marilka888.jeweller.common.exception.UserNotFoundException;
import ru.marilka888.jeweller.model.request.UserRequest;
import ru.marilka888.jeweller.service.UserService;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Slf4j
@CrossOrigin
public class UserController {

    private final UserService userService;

    @GetMapping(path = "/profile")
    @Transactional
    @Counted(value = "jeweller.shop.userController.getUser")
    public Object getUser(Principal principal) {
        try {
            return ResponseEntity.ok(userService.findProfile(principal));
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound();
        } catch (InnerException e) {
            return ResponseEntity.internalServerError();
        } catch (Exception e) {
            log.warn(e.getMessage());
            return ResponseEntity.internalServerError();
        }
    }

    @PostMapping(path = "/profile")
    @Transactional
    @Counted(value = "jeweller.shop.userController.updateUser")
    public Object updateUser(@RequestBody UserRequest user) {
        try {
            userService.updateUser(user);
            return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION, "/api/users/profile").build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest();
        } catch (InnerException e) {
            return ResponseEntity.internalServerError();
        }
    }


    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Transactional
    @Counted(value = "jeweller.shop.userController.getUserList")
    public Object getUserList() {
        try {
            return ResponseEntity.ok(userService.findAll());
        } catch (InnerException e) {
            return ResponseEntity.internalServerError();
        }
    }

    @GetMapping(path = "/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Transactional
    @Counted(value = "jeweller.shop.userController.getUser")
    public Object getUser(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(userService.findUser(id));
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound();
        } catch (InnerException e) {
            return ResponseEntity.internalServerError();
        }
    }
}
