package ru.marilka888.jeweller.controller;

import io.micrometer.core.annotation.Counted;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.marilka888.jeweller.common.exception.*;
import ru.marilka888.jeweller.model.request.OrderRequest;
import ru.marilka888.jeweller.service.OrderService;

import java.security.Principal;


@Controller
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping(value = "/create")
    @Transactional
    @Counted(value = "jeweller.shop.orderController.createOrder")
    public Object createOrder(Principal principal, @RequestBody OrderRequest order) {
        try {
            orderService.saveOrder(order, principal);
            return ResponseEntity.ok();
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound();
        } catch (InnerException e) {
            return ResponseEntity.internalServerError();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest();
        }
    }

    @GetMapping(value = "/all")
    @Transactional
    @Counted(value = "jeweller.shop.orderController.getUserOrders")
    public Object getUserOrders(Principal principal, Pageable pageable) {
        try {
            return ResponseEntity.ok(orderService.getUserOrders(principal, pageable));
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound();
        } catch (InnerException e) {
            return ResponseEntity.internalServerError();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest();
        } catch (OrderNotFoundException e) {
            return ResponseEntity.noContent();
        }
    }

    @GetMapping(value = "/{id}")
    @Transactional
    @Counted(value = "jeweller.shop.orderController.getUserOrder")
    public Object getUserOrder(Principal principal, @PathVariable Long id) {
        try {
            return ResponseEntity.ok(orderService.getUserOrder(principal, Long.valueOf(id)));
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound();
        } catch (InnerException e) {
            return ResponseEntity.internalServerError();
        } catch (BadRequestException | UserHaveNotOrder e) {
            return ResponseEntity.badRequest();
        } catch (OrderNotFoundException e) {
            return ResponseEntity.noContent();
        }
    }

    @GetMapping("/admin/all")
    @Transactional
    @Counted(value = "jeweller.shop.orderController.getAllOrders")
    public Object getAllOrders(@PageableDefault Pageable pageable) {
        try {
            return ResponseEntity.ok(orderService.findAllOrders(pageable));
        } catch (InnerException e) {
            return ResponseEntity.internalServerError();
        }
    }

    @PostMapping(value = "/{id}")
    @Transactional
    @Counted(value = "jeweller.shop.orderController.updateUserOrder")
    public Object updateUserOrder(@RequestBody OrderRequest order, @PathVariable String id) {
        try {
            orderService.updateOrder(order, id);
            return ResponseEntity.ok();
        } catch (UserNotFoundException | InnerException e) {
            return ResponseEntity.internalServerError();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest();
        } catch (OrderNotFoundException e) {
            return ResponseEntity.noContent();
        }
    }

}

