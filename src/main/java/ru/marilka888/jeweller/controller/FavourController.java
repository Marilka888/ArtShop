package ru.marilka888.jeweller.controller;

import io.micrometer.core.annotation.Counted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.marilka888.jeweller.common.exception.BadRequestException;
import ru.marilka888.jeweller.common.exception.InnerException;
import ru.marilka888.jeweller.model.request.FavourRequest;
import ru.marilka888.jeweller.service.FavourService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favours")
@Slf4j
@CrossOrigin
public class FavourController {
    private final FavourService favourService;

    @GetMapping(value = "/all")
    @Transactional
    @Counted(value = "jeweller.shop.favourController.getFavours")
    public Object getFavours() {
        try {
            return ResponseEntity.ok(favourService.findAllFavours());
        } catch (InnerException e) {
            return ResponseEntity.internalServerError();
        }
    }

    @GetMapping(value = "/{id}")
    @Transactional
    @Counted(value = "jeweller.shop.favourController.getFavour")
    public Object getFavour(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(favourService.getFavourById(id));
        } catch (InnerException e) {
            return ResponseEntity.internalServerError();
        }
    }

    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Transactional
    @Counted(value = "jeweller.shop.favourController.createFavour")
    public Object createFavour(@RequestBody FavourRequest favour) {
        try {
            favourService.saveFavour(favour);
            return ResponseEntity.ok(true);
        } catch (InnerException e) {
            return ResponseEntity.internalServerError();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest();
        }
    }

    @PostMapping("/update")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Transactional
    @Counted(value = "jeweller.shop.favourController.updateFavour")
    public Object updateFavour(@RequestBody FavourRequest favour) {
        try {
            favourService.updateFavour(favour);
            return ResponseEntity.ok(true);
        } catch (InnerException e) {
            return ResponseEntity.internalServerError();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest();
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Transactional
    @Counted(value = "jeweller.shop.favourController.deleteFavour")
    public Object deleteFavour(@PathVariable Long id) {
        try {
            favourService.deleteFavour(id);
            return ResponseEntity.ok(true);
        } catch (InnerException e) {
            return ResponseEntity.internalServerError();
        }
    }

}