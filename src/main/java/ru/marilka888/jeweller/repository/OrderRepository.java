package ru.marilka888.jeweller.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.marilka888.jeweller.model.Order;
import ru.marilka888.jeweller.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByUser(User user);

    Optional<Order> findByDateOfCreatedAfterAndAndUser(LocalDateTime dateOfCreated, User user);

    List<Order> findAll();

    void deleteById(Long id);

    @Override
    <S extends Order> S saveAndFlush(S entity);
}
