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
import ru.marilka888.jeweller.repository.FavourRepository;
import ru.marilka888.jeweller.repository.OrderRepository;
import ru.marilka888.jeweller.repository.UserRepository;

import java.security.Principal;
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
    public void createOrder(OrderRequest request, Principal principal) {
        try {
            User user = userRepository.findByEmail(principal.getName()).orElseThrow(UserNotFoundException::new);
            Favour favour = favourRepository.findById(request.favourId).orElseThrow(NullPointerException::new);
            var sum = request.getNum() * favour.getPrice();

            Order order = Order.builder()
                    .user(user)
                    .favour(favour)
                    .description(request.getDescription().orElse(null))
                    .stage(CREATED)
                    .sum(sum)
                    .status(false)
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

    @CacheEvict(value = {"userOrders", "userOrder", "allOrders"})
    @Counted(value = "jeweller.shop.orderService.ERROR.payOrder", recordFailuresOnly = true)
    public void payOrder(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(OrderNotFoundException::new);
        updateOrder(order, PAID, false);
    }

    @Cacheable(value = "userOrders")
    @Counted(value = "jeweller.shop.orderService.ERROR.getUserOrders", recordFailuresOnly = true)
    public List<OrderResponse> getUserOrders(Principal principal) {
        try {
            User user = userRepository.findByEmail(principal.getName()).orElseThrow(UserNotFoundException::new);
            List<Order> orders = orderRepository.findAllByUser(user);

            if (orders.isEmpty()) {
                throw new OrderNotFoundException("Заказы не были найдены");
            }
            return orders.stream().map(order -> OrderResponse.builder()
                            .userId(order.getUser().getId())
                            .favour(order.favour)
                            .description(order.getDescription())
                            .stage(order.getStage())
                            .sum(order.getSum())
                            .status(order.isStatus())
                            .dateOfCreated(order.getDateOfCreated())
                            .build())
                    .toList();
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
                    .userId(order.getUser().getId())
                    .favour(order.favour)
                    .description(order.getDescription())
                    .stage(order.getStage())
                    .sum(order.getSum())
                    .status(order.isStatus())
                    .dateOfCreated(order.getDateOfCreated())
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
    public List<OrderResponse> findAllOrders() {
        try {
            List<Order> orders = orderRepository.findAll();

            if (orders.isEmpty()) {
                throw new OrderNotFoundException("Заказы не были найдены");
            }

            return orders.stream().map(order -> OrderResponse.builder()
                            .userId(order.getUser().getId())
                            .favour(order.favour)
                            .description(order.getDescription())
                            .stage(order.getStage())
                            .sum(order.getSum())
                            .status(order.isStatus())
                            .dateOfCreated(order.getDateOfCreated())
                            .build())
                    .toList();
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