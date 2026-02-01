package e_commerce.e_commerce.controller;

import e_commerce.e_commerce.model.CartItem;
import e_commerce.e_commerce.service.CartService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // POST /api/cart/add
    @PostMapping("/add")
    public CartItem addToCart(@RequestBody Map<String, Object> request) {
        String userId = (String) request.get("userId");
        String productId = (String) request.get("productId");
        int quantity = (int) request.get("quantity");

        return cartService.add(userId, productId, quantity);
    }

    // GET /api/cart/{userId}
    @GetMapping("/{userId}")
    public List<CartItem> getUserCart(@PathVariable String userId) {
        return cartService.getCart(userId);
    }

    // DELETE /api/cart/{userId}/clear
    @DeleteMapping("/{userId}/clear")
    public Map<String, String> clearCart(@PathVariable String userId) {
        cartService.clear(userId);
        return Map.of("message", "Cart cleared successfully");
    }
}
