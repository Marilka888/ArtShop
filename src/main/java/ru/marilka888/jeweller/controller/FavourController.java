package ru.marilka888.jeweller.controller;

import io.micrometer.core.annotation.Counted;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.marilka888.jeweller.common.exception.BadRequestException;
import ru.marilka888.jeweller.common.exception.InnerException;
import ru.marilka888.jeweller.model.request.FavourRequest;
import ru.marilka888.jeweller.service.FavourService;

import java.io.IOException;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favours")
@Slf4j
public class FavourController {
    private final FavourService favourService;

    @GetMapping(value = "/all")
    @Transactional
    @Counted(value = "jeweller.shop.favourController.getFavours")
    public Object getFavours(@PageableDefault Pageable pageable) {
        try {
            return ResponseEntity.ok(favourService.findAllFavours(pageable));
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
            return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION, "/api/favours/all").build();
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
    public Object updateFavour(@PathVariable FavourRequest favour) {
        try {
            favourService.saveFavour(favour);
            return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION, "/api/favours/all").build();
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
            return ResponseEntity.status(HttpStatus.MOVED_PERMANENTLY).header(HttpHeaders.LOCATION, "/api/favours/all").build();
        } catch (InnerException e) {
            return ResponseEntity.internalServerError();
        }
    }

}