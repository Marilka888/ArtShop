package ru.marilka888.jeweller.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.marilka888.jeweller.model.Order;
import ru.marilka888.jeweller.model.User;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllById(Iterable<Long> longs);

    List<Order> findAllByUser(User user);

    void deleteById(Long id);
}
