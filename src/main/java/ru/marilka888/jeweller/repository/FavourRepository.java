package ru.marilka888.jeweller.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.marilka888.jeweller.model.Favour;

@Repository
public interface FavourRepository extends CrudRepository<Favour, Long> {
    Page<Favour> findAll(Pageable pageable);
}
