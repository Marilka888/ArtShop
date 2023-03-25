package ru.marilka888.jeweller.controller;

import io.micrometer.core.annotation.Counted;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("api/users")
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
        }
    }

    @PostMapping(path = "/profile")
    @Transactional
    @Counted(value = "jeweller.shop.userController.updateUser")
    public Object updateUser(@RequestBody UserRequest user) {
        try {
            userService.updateUser(user);
            return ResponseEntity.ok();
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest();
        } catch (InnerException e) {
            return ResponseEntity.internalServerError();
        }
    }


    @GetMapping("/users/all")
    @Transactional
    @Counted(value = "jeweller.shop.userController.getUserList")
    public Object getUserList(@PageableDefault Pageable pageable) {
        try {
            return ResponseEntity.ok(userService.findAll(pageable));
        } catch (InnerException e) {
            return ResponseEntity.internalServerError();
        }
    }

    @GetMapping(path = "/users/{id}")
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
