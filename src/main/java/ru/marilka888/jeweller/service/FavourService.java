package ru.marilka888.jeweller.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.marilka888.jeweller.model.Favour;
import ru.marilka888.jeweller.repository.FavourRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class FavourService {
    private final FavourRepository favourRepository;

    @Cacheable
    public Page<Favour> findAllFavours(Pageable pageable) {
        return favourRepository.findAll(pageable);
    }

    @Cacheable
    public Favour getFavourById(Long id) {
        return favourRepository.findById(id).orElse(null);
    }

    public void saveFavour(Favour favour) {
        favourRepository.save(favour); //todo посмотреть как работает флэш
        log.info("Saving new Order. Title: {}", favour.getTitle());
    }

    public void deleteFavour(Long id) {
        favourRepository.deleteById(id);
        log.info("Order with id = {} was deleted", id);
    }

}
