package ru.marilka888.jeweller.service;

import io.micrometer.core.annotation.Counted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import ru.marilka888.jeweller.common.exception.BadRequestException;
import ru.marilka888.jeweller.common.exception.InnerException;
import ru.marilka888.jeweller.model.Favour;
import ru.marilka888.jeweller.model.request.FavourRequest;
import ru.marilka888.jeweller.repository.FavourRepository;

@Service
@Slf4j
@RequiredArgsConstructor
public class FavourService {
    private final FavourRepository favourRepository;

    @Cacheable(value = "allFavours")
    @Counted(value = "jeweller.shop.favourService.ERROR.findAllFavours", recordFailuresOnly = true)
    public Page<Favour> findAllFavours(Pageable pageable) {
        try {
            return favourRepository.findAll(pageable);
        } catch (Exception e) {
            log.warn("Произошла внутренняя ошибка");
            throw new InnerException();
        }
    }

    @Cacheable(value = "favourById")
    @Counted(value = "jeweller.shop.favourService.ERROR.getFavourById", recordFailuresOnly = true)
    public Favour getFavourById(Long id) {
        try {
            Favour favour = favourRepository.findById(id).orElse(null);
            if (ObjectUtils.isEmpty(favour)) {
                log.info("Услуги пока пустые");
            }
            return favour;
        } catch (Exception e) {
            log.warn("Произошла внутренняя ошибка");
            throw new InnerException();
        }
    }

    @CacheEvict(value = {"allFavours", "favourById"})
    @Counted(value = "jeweller.shop.favourService.ERROR.saveFavour", recordFailuresOnly = true)
    public void saveFavour(FavourRequest request) {
        try {
            Favour favour = Favour.builder()
                    .title(request.getTitle())
                    .description(request.getDescription())
                    .price(request.getPrice())
                    .build();
            favourRepository.save(favour);
            log.info("Была сохранена новая услуга с названием: {}", favour.getTitle());
        } catch (NullPointerException e) {
            log.warn("В запросе на создание/обновление новой услуги не заполнены обязательные поля");
            throw new BadRequestException();
        } catch (Exception e) {
            log.warn("Произошла внутренняя ошибка");
            throw new InnerException();
        }
    }

    @CacheEvict(value = {"allFavours", "favourById"})
    @Counted(value = "jeweller.shop.favourService.ERROR.deleteFavour", recordFailuresOnly = true)
    public void deleteFavour(Long id) {
        try {
            favourRepository.deleteById(id);
            log.info("Была удалена услуга с id: {}", id);
        } catch (Exception e) {
            log.warn("Произошла внутренняя ошибка");
            throw new InnerException();
        }
    }

}
