package ru.marilka888.jeweller.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.marilka888.jeweller.model.Favour;
import ru.marilka888.jeweller.service.FavourService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/favours")
public class FavourController {
    private final FavourService favourService;

    @GetMapping
    @Transactional
    public Page<Favour> getFavours(@PageableDefault Pageable pageable) {
        return favourService.findAllFavours(pageable);
    }

    @GetMapping(value = "/{id}")
    @Transactional
    public Favour getFavour(@PathVariable Long id) {
        return favourService.getFavourById(id);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/create")
    @Transactional
    public void createFavour(@RequestBody Favour favour) {
        favourService.saveFavour(favour);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/update")
    @Transactional
    public void updateFavour(@PathVariable Favour favour) {
        favourService.saveFavour(favour);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    @Transactional
    public void deleteFavour(@PathVariable Long id) {
        favourService.deleteFavour(id);
    }
}