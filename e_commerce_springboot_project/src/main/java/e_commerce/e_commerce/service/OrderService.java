package e_commerce.e_commerce.service;

import e_commerce.e_commerce.model.CartItem;
import e_commerce.e_commerce.model.Order;
import e_commerce.e_commerce.model.OrderItem;
import e_commerce.e_commerce.model.Product;
import e_commerce.e_commerce.repository.CartRepository;
import e_commerce.e_commerce.repository.OrderItemRepository;
import e_commerce.e_commerce.repository.OrderRepository;
import e_commerce.e_commerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class OrderService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public OrderService(CartRepository cartRepository,
                        ProductRepository productRepository,
                        OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    // CREATE ORDER FROM CART
    public Order createOrder(String userId) {

        List<CartItem> cartItems = cartRepository.findByUserId(userId);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setStatus("CREATED");
        order.setCreatedAt(Instant.now());
        order = orderRepository.save(order);

        double totalAmount = 0;

        for (CartItem cartItem : cartItems) {
            Product product = productRepository.findById(cartItem.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));

            if (product.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException("Insufficient stock");
            }

            // Reduce stock
            product.setStock(product.getStock() - cartItem.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem();
            orderItem.setOrderId(order.getId());
            orderItem.setProductId(product.getId());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(product.getPrice());
            orderItemRepository.save(orderItem);

            totalAmount += product.getPrice() * cartItem.getQuantity();
        }

        order.setTotalAmount(totalAmount);
        orderRepository.save(order);

        // Clear cart
        cartRepository.deleteByUserId(userId);

        return order;
    }

    public Order getOrderById(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    // BONUS: Order history
    public List<Order> getOrdersByUser(String userId) {
        return orderRepository.findByUserId(userId);
    }

    // BONUS: Cancel order
    public void cancelOrder(String orderId) {
        Order order = getOrderById(orderId);

        if ("PAID".equals(order.getStatus())) {
            throw new RuntimeException("Paid order cannot be cancelled");
        }

        order.setStatus("CANCELLED");
        orderRepository.save(order);
    }
}
