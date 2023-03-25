package ru.marilka888.jeweller.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.marilka888.jeweller.common.exception.BadRequestException;
import ru.marilka888.jeweller.common.exception.InnerException;
import ru.marilka888.jeweller.model.request.FavourRequest;
import ru.marilka888.jeweller.service.FavourService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favours")
public class FavourController {
    private final FavourService favourService;

    @GetMapping(value = "/all")
    @Transactional
    public Object getFavours(@PageableDefault Pageable pageable) {
        try {
            return ResponseEntity.ok(favourService.findAllFavours(pageable));
        } catch (InnerException e) {
            return ResponseEntity.internalServerError();
        }
    }

    @GetMapping(value = "/{id}")
    @Transactional
    public Object getFavour(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(favourService.getFavourById(id));
        } catch (InnerException e) {
            return ResponseEntity.internalServerError();
        }
    }

    @PostMapping("/create")
    @Transactional
    public Object createFavour(@RequestBody FavourRequest favour) {
        try {
            favourService.saveFavour(favour);
            return ResponseEntity.ok();
        } catch (InnerException e) {
            return ResponseEntity.internalServerError();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest();
        }
    }

    @PostMapping("/update")
    @Transactional
    public Object updateFavour(@PathVariable FavourRequest favour) {
        try {
            favourService.saveFavour(favour);
            return ResponseEntity.ok();
        } catch (InnerException e) {
            return ResponseEntity.internalServerError();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest();
        }
    }

    @DeleteMapping("/delete/{id}")
    @Transactional
    public Object deleteFavour(@PathVariable Long id) {
        try {
            favourService.deleteFavour(id);
            return ResponseEntity.ok();
        } catch (InnerException e) {
            return ResponseEntity.internalServerError();
        }
    }

}