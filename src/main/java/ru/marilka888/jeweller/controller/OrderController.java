package ru.marilka888.jeweller.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.marilka888.jeweller.model.Order;
import ru.marilka888.jeweller.service.OrderService;

import java.security.Principal;
import java.util.List;


@Controller
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping(value = "/create")
    @Transactional
    public void createOrder(Principal principal, @RequestBody Order order) {
        //todo valid
        orderService.saveOrder(order, principal);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping()
    @Transactional
    public List<Order> getUserOrders(Principal principal) {
        return orderService.getUserOrders(principal);
    }

    @GetMapping(value = "/{id}")
    @Transactional
    public Order getUserOrder(@PathVariable Long id) {
        //todo valid
        return orderService.getUserOrder(Long.valueOf(id));
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping()
    @Transactional
    public Page<Order> getAllOrders(@PageableDefault Pageable pageable) {
        return orderService.findAllOrders(pageable);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping(value = "/{id}")
    @Transactional
    public void updateUserOrder(@RequestBody Order order) {
        orderService.updateOrder(order);
    }
}

