package ru.marilka888.jeweller.service;

import io.micrometer.core.annotation.Counted;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import ru.marilka888.jeweller.common.exception.*;
import ru.marilka888.jeweller.model.*;
import ru.marilka888.jeweller.model.request.OrderRequest;
import ru.marilka888.jeweller.model.response.OrderResponse;
import ru.marilka888.jeweller.model.response.UserResponse;
import ru.marilka888.jeweller.repository.FavourRepository;
import ru.marilka888.jeweller.repository.OrderRepository;
import ru.marilka888.jeweller.repository.UserRepository;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

import static ru.marilka888.jeweller.model.Stage.CREATED;
import static ru.marilka888.jeweller.model.Stage.PAID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final FavourRepository favourRepository;

    @CacheEvict(value = {"userOrders", "userOrder", "allOrders"})
    @Counted(value = "jeweller.shop.orderService.ERROR.createOrder", recordFailuresOnly = true)
    public Long createOrder(Principal principal, OrderRequest request) {
        try {
            var accessories = 0;
            var sketch = 100;
            var description = new StringBuffer("Заказ: ");

            User user = userRepository.findByEmail(principal.getName()).orElseThrow(UserNotFoundException::new);
            Favour favour = favourRepository.findById(request.favourId).orElseThrow(NullPointerException::new);

            if (request.accessories) {
                accessories = 250;
                description.append("с дополнительными аксессуарами, ");
            }
            if (request.sketch) {
                sketch = 250;
                description.append("с эскизом под заказ, ");
            }
            description.append("с размером: " + request.size + ". ");
            var sum = request.getQty() * request.size * (favour.getPrice() + accessories + sketch);

            if (request.getDescription().isPresent()) {
                description.append("Комментарий заказчика: " + request.getDescription());
            }

            Order order = Order.builder()
                    .user(user)
                    .favour(favour)
                    .description(String.valueOf(description))
                    .stage(CREATED)
                    .sum(sum)
                    .qty(request.getQty())
                    .status(false)
                    .build();

            var now = LocalDateTime.now();
            orderRepository.save(order);

            Order orderResponse = orderRepository.findByDateOfCreatedAfterAndAndUser(now, user)
                    .orElseThrow(OrderNotFoundException::new);

            return orderResponse.getId();
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

    @CacheEvict(value = {"userOrders", "userOrder", "allOrders"})
    @Counted(value = "jeweller.shop.orderService.ERROR.payOrder", recordFailuresOnly = true)
    public void payOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(OrderNotFoundException::new);
        updateOrder(order, PAID, false);
    }

    @Cacheable(value = "userOrders")
    @Counted(value = "jeweller.shop.orderService.ERROR.getUserOrders", recordFailuresOnly = true)
    public OrderResponse getUserOrders(Principal principal) {
        try {
            User user = userRepository.findByEmail(principal.getName()).orElseThrow(UserNotFoundException::new);
            List<Order> orders = orderRepository.findAllByUser(user);

            if (orders.isEmpty()) {
                throw new OrderNotFoundException("Заказы не были найдены");
            }
            return OrderResponse.builder()
                    .success(true)
                    .orders(orders)
                    .build();

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

            var userResponse = UserResponse.builder()
                    .firstname(user.getFirstname())
                    .lastname(user.getLastname())
                    .phone(user.getPhone())
                    .email(user.getEmail())
                    .build();

            OrderResponse response = OrderResponse.builder()
                    .id(order.getId())
                    .user(userResponse)
                    .favour(order.getFavour())
                    .description(order.getDescription())
                    .stage(order.getStage())
                    .qty(order.getQty())
                    .sum(order.getSum())
                    .status(order.isStatus())
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

    @Cacheable("allOrders")
    @Counted(value = "jeweller.shop.orderService.ERROR.findAllOrders", recordFailuresOnly = true)
    public OrderResponse findAllOrders() {
        try {
            List<Order> orders = orderRepository.findAll();

            if (orders.isEmpty()) {
                throw new OrderNotFoundException("Заказы не были найдены");
            }

            return OrderResponse.builder()
                    .success(true)
                    .orders(orders)
                    .build();

        } catch (Exception e) {
            log.warn("Произошла внутренняя ошибка");
            throw new InnerException();
        }
    }

    @CacheEvict(value = {"userOrders", "userOrder", "allOrders"})
    @Counted(value = "jeweller.shop.orderService.ERROR.updateOrder", recordFailuresOnly = true)
    public void updateOrder(OrderRequest request, String id) {
        try {
            Order order = orderRepository.findById(Long.valueOf(id)).orElseThrow(OrderNotFoundException::new);
            updateOrder(order, CREATED, false);
            log.info("Order with id = {} was update", order.getId());

        } catch (OrderNotFoundException e) {
            log.warn("В БД не найден заказ с id: {}", id);
            throw new OrderNotFoundException();
        } catch (Exception e) {
            log.warn("Произошла внутренняя ошибка");
            throw new InnerException();
        }
    }

    private void updateOrder(Order order, Stage stage, Boolean status) {
        try {
            Order updatedOrder = Order.builder()
                    .id(order.getId())
                    .user(order.getUser())
                    .favour(order.getFavour())
                    .description(order.getDescription())
                    .stage(stage)
                    .sum(order.getSum())
                    .status(status)
                    .build();

            orderRepository.save(updatedOrder);
        } catch (NullPointerException e) {
            log.warn("В запросе на создание заказа не заполнены обязательные поля");
            throw new BadRequestException();
        } catch (Exception e) {
            log.warn("Произошла внутренняя ошибка");
            throw new InnerException();
        }
    }
}