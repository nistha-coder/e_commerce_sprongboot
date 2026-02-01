package e_commerce.e_commerce.controller;

import e_commerce.e_commerce.model.Order;
import e_commerce.e_commerce.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // POST /api/orders
    @PostMapping
    public Order createOrder(@RequestBody Map<String, String> request) {
        String userId = request.get("userId");
        return orderService.createOrder(userId);
    }

    // GET /api/orders/{orderId}
    @GetMapping("/{orderId}")
    public Order getOrder(@PathVariable String orderId) {
        return orderService.getOrderById(orderId);
    }

    // BONUS: GET /api/orders/user/{userId}
    @GetMapping("/user/{userId}")
    public List<Order> getUserOrders(@PathVariable String userId) {
        return orderService.getOrdersByUser(userId);
    }

    // BONUS: POST /api/orders/{orderId}/cancel
    @PostMapping("/{orderId}/cancel")
    public Map<String, String> cancelOrder(@PathVariable String orderId) {
        orderService.cancelOrder(orderId);
        return Map.of("message", "Order cancelled successfully");
    }
}
