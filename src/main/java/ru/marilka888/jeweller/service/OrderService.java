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
import ru.marilka888.jeweller.common.exception.*;
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
    @Counted(value = "jeweller.shop.orderService.ERROR.saveOrder", recordFailuresOnly = true)
    public void saveOrder(OrderRequest request, Principal principal) {
        try {
            User user = userRepository.findByEmail(principal.getName()).orElseThrow(UserNotFoundException::new);

            Order order = Order.builder()
                    .title(request.getTitle())
                    .description(request.getDescription())
                    .price(request.getPrice())
                    .status(false)
                    .user(user)
                    .build();

            orderRepository.save(order);
        } catch (UserNotFoundException e) {
            log.warn("В БД не найден пользователь с email: {}", principal.getName());
            throw new UserNotFoundException();
        } catch (NullPointerException e) {
            log.warn("В запросе на создание заказа не заполнены обязательные поля");
            throw new BadRequestException();
        } catch (Exception e) {
            log.warn("Произошла внутренняя ошибка");
            throw new InnerException();
        }
    }

    @Cacheable(value = "userOrders")
    @Counted(value = "jeweller.shop.orderService.ERROR.getUserOrders", recordFailuresOnly = true)
    public List<OrderResponse> getUserOrders(Principal principal, Pageable pageable) {
        try {
            User user = userRepository.findByEmail(principal.getName()).orElseThrow(UserNotFoundException::new);
            List<Order> orders = orderRepository.findAllByUser(user, pageable);

            if (orders.isEmpty()) {
                throw new OrderNotFoundException("Заказы не были найдены");
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
            log.warn("В БД не найден пользователь с email: {}", principal.getName());
            throw new UserNotFoundException();
        } catch (NullPointerException e) {
            log.warn("В запросе на получение заказов не заполнены обязательные поля");
            throw new BadRequestException();
        } catch (OrderNotFoundException e) {
            log.warn("В БД не найдены заказы пользователя с email: {}", principal.getName());
            throw new OrderNotFoundException();
        } catch (Exception e) {
            log.warn("Произошла внутренняя ошибка");
            throw new InnerException();
        }
    }

    @Cacheable(value = "userOrder")
    @Counted(value = "jeweller.shop.orderService.ERROR.getUserOrder", recordFailuresOnly = true)
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
                throw new UserHaveNotOrder();
            }
            return response;

        } catch (UserNotFoundException e) {
            log.warn("В БД не найден пользователь с email: {}", principal.getName());
            throw new UserNotFoundException();
        } catch (NullPointerException e) {
            log.warn("В запросе на получение заказа не заполнены обязательные поля");
            throw new BadRequestException();
        } catch (OrderNotFoundException e) {
            log.warn("В БД не найдены заказы пользователя с email: {}", principal.getName());
            throw new OrderNotFoundException();
        } catch (UserHaveNotOrder e) {
            log.warn("Заказ с id = {} не принадлежит пользователю с email: {}", id, principal.getName());
            throw new UserHaveNotOrder();
        } catch (Exception e) {
            log.warn("Произошла внутренняя ошибка");
            throw new InnerException();
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @Cacheable("allOrders")
    @Counted(value = "jeweller.shop.orderService.ERROR.findAllOrders", recordFailuresOnly = true)
    public Page<Order> findAllOrders(Pageable pageable) {
        try {
            return orderRepository.findAll(pageable);
        } catch (Exception e) {
            log.warn("Произошла внутренняя ошибка");
            throw new InnerException();
        }
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @CacheEvict(value = {"userOrders", "userOrder", "allOrders"})
    @Counted(value = "jeweller.shop.orderService.ERROR.updateOrder", recordFailuresOnly = true)
    public void updateOrder(OrderRequest request, String id) {
        try {
            orderRepository.findById(Long.valueOf(id)).orElseThrow(OrderNotFoundException::new);

            User user = userRepository.findById(toIntExact(request.userId)).orElseThrow(UserNotFoundException::new);

            Order order = Order.builder()
                    .title(request.getTitle())
                    .description(request.getDescription())
                    .price(request.getPrice())
                    .status(request.isStatus())
                    .user(user)
                    .build();

            orderRepository.save(order);
            log.info("Order with id = {} was update", order.getId());

        } catch (UserNotFoundException e) {
            log.warn("В БД не найден пользователь для заказа с id: {}", id);
            throw new UserNotFoundException();
        } catch (NullPointerException e) {
            log.warn("В запросе на обновление заказа не заполнены обязательные поля");
            throw new BadRequestException();
        } catch (OrderNotFoundException e) {
            log.warn("В БД не найден заказ с id: {}", id);
            throw new OrderNotFoundException();
        } catch (Exception e) {
            log.warn("Произошла внутренняя ошибка");
            throw new InnerException();
        }
    }
}