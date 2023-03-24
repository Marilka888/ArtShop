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
import ru.marilka888.jeweller.common.exception.OrderNotFoundException;
import ru.marilka888.jeweller.common.exception.UserNotFoundException;
import ru.marilka888.jeweller.model.Order;
import ru.marilka888.jeweller.model.Role;
import ru.marilka888.jeweller.model.User;
import ru.marilka888.jeweller.model.request.OrderRequest;
import ru.marilka888.jeweller.model.response.OrderResponse;
import ru.marilka888.jeweller.repository.OrderRepository;
import ru.marilka888.jeweller.repository.UserRepository;

import java.security.Principal;
import java.util.List;

import static java.lang.Math.toIntExact;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @CacheEvict(value = {"userOrders", "userOrder", "allOrders"})
    public void saveOrder(OrderRequest orderRequest, Principal principal) {
        try {
            User user = userRepository.findByEmail(principal.getName()).orElseThrow(UserNotFoundException::new);

            Order order = Order.builder()
                    .title(orderRequest.getTitle())
                    .description(orderRequest.getDescription())
                    .price(orderRequest.getPrice())
                    .status(false)
                    .user(user)
                    .build();

            orderRepository.save(order);
        } catch (UserNotFoundException e) {
            // todo log

        } catch (NullPointerException e) {
            throw new RuntimeException(); //todo НЕ ЗАПОЛНЕНЫ ПОЛЯ
        }
    }

    @Cacheable(value = "userOrders")
    public List<OrderResponse> getUserOrders(Principal principal, Pageable pageable) {
        try {
            User user = userRepository.findByEmail(principal.getName()).orElseThrow(UserNotFoundException::new);
            List<Order> orders = orderRepository.findAllByUser(user, pageable);

            if (orders.isEmpty()) {
                throw new OrderNotFoundException("Договора не были найдены");
            }

            List<OrderResponse> response = orders.stream().map(order -> OrderResponse.builder()
                            .title(order.getTitle())
                            .description(order.getDescription())
                            .price(order.getPrice())
                            .status(order.isStatus())
                            .dateOfCreated(order.getDateOfCreated())
                            .userId(order.getUser().getId())
                            .build())
                    .toList();

            return response;
        } catch (UserNotFoundException e) {
            // todo log

        } catch (OrderNotFoundException e) {
            // todo log
        } catch (Exception e) {

        }
        return null;
    }

    @Cacheable(value = "userOrder")
    public OrderResponse getUserOrder(Principal principal, Long id) {
        try {
            User user = userRepository.findByEmail(principal.getName()).orElseThrow(UserNotFoundException::new);
            Order order = orderRepository.findById(id).orElseThrow(OrderNotFoundException::new);

            OrderResponse response = OrderResponse.builder()
                    .title(order.getTitle())
                    .description(order.getDescription())
                    .price(order.getPrice())
                    .status(order.isStatus())
                    .dateOfCreated(order.getDateOfCreated())
                    .userId(order.getUser().getId())
                    .build();

            response = user.getRole().equals(Role.ADMIN) || order.getUser().equals(user) ? response : null;

            if (ObjectUtils.isEmpty(response)) {
                throw new UserNotFoundException("Данный заказ не принадлежит пользователю");//todo ex
            }
            return response;

        } catch (UserNotFoundException e) {
            // todo log

        } catch (OrderNotFoundException e) {
            // todo log
        } catch (Exception e) {

        }
        return null;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Cacheable("allOrders")
    public Page<Order> findAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @CacheEvict(value = {"userOrders", "userOrder", "allOrders"})
    public void updateOrder(OrderRequest orderRequest, String id) {
        try {
            orderRepository.findById(Long.valueOf(id)).orElseThrow(OrderNotFoundException::new);

            User user = userRepository.findById(toIntExact(orderRequest.userId)).orElseThrow(UserNotFoundException::new);

            Order order = Order.builder()
                    .title(orderRequest.getTitle())
                    .description(orderRequest.getDescription())
                    .price(orderRequest.getPrice())
                    .status(orderRequest.isStatus())
                    .user(user)
                    .build();

            orderRepository.save(order);
            log.info("Order with id = {} was update", order.getId());

        } catch (UserNotFoundException e) {
            // todo log

        } catch (OrderNotFoundException e) {
            //todo
        } catch (NullPointerException e) {
            throw new RuntimeException(); //todo НЕ ЗАПОЛНЕНЫ ПОЛЯ
        }
    }
}