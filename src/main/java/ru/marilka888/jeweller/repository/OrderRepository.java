package ru.marilka888.jeweller.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.marilka888.jeweller.model.Order;
import ru.marilka888.jeweller.model.User;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findAllByUser(User user, Pageable pageable);

    Page<Order> findAll(Pageable pageable);

    void deleteById(Long id);
}
