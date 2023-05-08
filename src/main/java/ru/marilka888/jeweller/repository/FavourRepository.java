package ru.marilka888.jeweller.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.marilka888.jeweller.model.Favour;

import java.util.List;

@Repository
public interface FavourRepository extends CrudRepository<Favour, Long> {
    List<Favour> findAll();
}
