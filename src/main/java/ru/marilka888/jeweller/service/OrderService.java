package ru.marilka888.jeweller.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.marilka888.jeweller.model.Order;
import ru.marilka888.jeweller.repository.OrderRepository;
import ru.marilka888.jeweller.repository.UserRepository;

import java.security.Principal;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public void saveOrder(Order order, Principal principal) {
        order.setUser(userRepository.findByEmail(principal.getName()).get());
        orderRepository.save(order);
    }

    @Cacheable
    public List<Order> getUserOrders(Principal principal) {
        return orderRepository.findAllByUser(
                userRepository.findByEmail(principal.getName()).get());
    }

    @Cacheable
    public Order getUserOrder(Long id) {
        try {
            return orderRepository.findById(id).get();
        } catch (NullPointerException e) {
            throw new RuntimeException(); //todo exception
        }

    }

    @Cacheable
    public Page<Order> findAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    public void updateOrder(Order order) {
        orderRepository.saveAndFlush(order);
        log.info("Order with id = {} was update", order.getId());
    }

    public void deleteOrder(Long id) {
        orderRepository.deleteById(id);
        log.info("Order with id = {} was deleted", id);
    }
}