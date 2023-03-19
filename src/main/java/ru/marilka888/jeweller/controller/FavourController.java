package ru.marilka888.jeweller.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.marilka888.jeweller.model.Favour;
import ru.marilka888.jeweller.model.request.FavourRequest;
import ru.marilka888.jeweller.model.response.FavourResponse;
import ru.marilka888.jeweller.service.FavourService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favours")
public class FavourController {
    private final FavourService favourService;

    @GetMapping(value = "/all")
    @Transactional
    public ResponseEntity<Page<Favour>> getFavours(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(favourService.findAllFavours(pageable));
    }

    @GetMapping(value = "/{id}")
    @Transactional
    public ResponseEntity<Favour> getFavour(@PathVariable Long id) {
        return ResponseEntity.ok(favourService.getFavourById(id));
    }

    @PostMapping("/create")
    @Transactional
    public void createFavour(@RequestBody FavourRequest favour) {
        favourService.saveFavour(favour);
    }

    @PostMapping("/update")
    @Transactional
    public void updateFavour(@PathVariable FavourRequest favour) {
        favourService.saveFavour(favour);
    }

    @DeleteMapping("/delete/{id}")
    @Transactional
    public void deleteFavour(@PathVariable Long id) {
        favourService.deleteFavour(id);
    }

}