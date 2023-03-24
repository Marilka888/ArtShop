package ru.marilka888.jeweller.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import ru.marilka888.jeweller.model.Favour;
import ru.marilka888.jeweller.model.request.FavourRequest;
import ru.marilka888.jeweller.repository.FavourRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class FavourService {
    private final FavourRepository favourRepository;

    @Cacheable(value = "allFavours")
    public Page<Favour> findAllFavours(Pageable pageable) {
        return favourRepository.findAll(pageable);
    }

    @Cacheable(value = "favourById")
    public Favour getFavourById(Long id) {
        Favour favour = favourRepository.findById(id).orElse(null);
        if (ObjectUtils.isEmpty(favour)) {
            //todo log
        }
        return favour;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @CacheEvict(value = {"allFavours", "favourById"})
    public void saveFavour(FavourRequest favourRequest) {
        //todo valid
        try {
            Favour favour = Favour.builder()
                    .title(favourRequest.getTitle())
                    .description(favourRequest.getDescription())
                    .price(favourRequest.getPrice())
                    .build();
            favourRepository.save(favour);
            log.info("Saving new Order. Title: {}", favour.getTitle());//todo logs
        } catch (NullPointerException e) {
            throw new RuntimeException(); //todo НЕ ЗАПОЛНЕНЫ ПОЛЯ
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @CacheEvict(value = {"allFavours", "favourById"})
    public void deleteFavour(Long id) {
        try {
            favourRepository.deleteById(id);
            log.info("Order with id = {} was deleted", id);//todo logs
        } catch (Exception e) {
            //todo log
        }
    }

}
