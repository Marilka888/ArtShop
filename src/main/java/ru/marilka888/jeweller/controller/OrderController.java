package ru.marilka888.jeweller.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.marilka888.jeweller.model.Order;
import ru.marilka888.jeweller.model.request.OrderRequest;
import ru.marilka888.jeweller.model.response.OrderResponse;
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
    public void createOrder(Principal principal, @RequestBody OrderRequest order) {
        //todo valid
        orderService.saveOrder(order, principal);
    }

    @GetMapping(value = "/all")
    @Transactional
    public ResponseEntity<List<OrderResponse>> getUserOrders(Principal principal, Pageable pageable) {
        return ResponseEntity.ok(orderService.getUserOrders(principal, pageable));
    }

    @GetMapping(value = "/{id}")
    @Transactional
    public ResponseEntity<OrderResponse> getUserOrder(Principal principal, @PathVariable Long id) {
        //todo valid
        return ResponseEntity.ok(orderService.getUserOrder(principal, Long.valueOf(id)));
    }

    @GetMapping("/admin/all")
    @Transactional
    public ResponseEntity<Page<Order>> getAllOrders(@PageableDefault Pageable pageable) {
        return ResponseEntity.ok(orderService.findAllOrders(pageable));
    }

    @PostMapping(value = "/{id}")
    @Transactional
    public void updateUserOrder(@RequestBody OrderRequest order, @PathVariable String id) {
        orderService.updateOrder(order, id);
    }

}

