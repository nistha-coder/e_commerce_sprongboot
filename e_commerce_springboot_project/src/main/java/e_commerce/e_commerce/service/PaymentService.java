//
//package e_commerce.e_commerce.service;
//
//import e_commerce.e_commerce.dto.PaymentWebhookRequest;
//import e_commerce.e_commerce.model.Order;
//import e_commerce.e_commerce.model.Payment;
//import e_commerce.e_commerce.repository.OrderRepository;
//import e_commerce.e_commerce.repository.PaymentRepository;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//import java.time.Instant;
//import java.util.UUID;
//
//@Service
//public class PaymentService {
//
//    private final PaymentRepository paymentRepository;
//    private final OrderRepository orderRepository;
//    private final RestTemplate restTemplate = new RestTemplate();
//
//    public PaymentService(PaymentRepository paymentRepository,
//                          OrderRepository orderRepository) {
//        this.paymentRepository = paymentRepository;
//        this.orderRepository = orderRepository;
//    }
//
//    // CREATE PAYMENT
//    public Payment create(String orderId, double amount) {
//
//        Order order = orderRepository.findById(orderId)
//                .orElseThrow(() -> new RuntimeException("Order not found"));
//
//        if (!"CREATED".equals(order.getStatus())) {
//            throw new RuntimeException("Order not eligible for payment");
//        }
//
//        Payment payment = new Payment();
//        payment.setOrderId(orderId);
//        payment.setAmount(amount);
//        payment.setStatus("PENDING");
//        payment.setPaymentId("pay_mock_" + UUID.randomUUID());
//        payment.setCreatedAt(Instant.now());
//        paymentRepository.save(payment);
//
//        // MOCK WEBHOOK (after 3 seconds)
//        new Thread(() -> {
//            try {
//                Thread.sleep(3000);
//                restTemplate.postForObject(
//                        "http://localhost:8080/api/webhooks/payment",
//                        new PaymentWebhookRequest(orderId, "SUCCESS"),
//                        String.class
//                );
//            } catch (Exception ignored) {}
//        }).start();
//
//        return payment;
//    }
//
//    // HANDLE WEBHOOK
//    public void handleWebhook(String orderId, String status) {
//
//        Payment payment = paymentRepository.findByOrderId(orderId);
//        payment.setStatus(status);
//        paymentRepository.save(payment);
//
//        Order order = orderRepository.findById(orderId).orElseThrow();
//        order.setStatus("SUCCESS".equals(status) ? "PAID" : "FAILED");
//        orderRepository.save(order);
//    }
//}







package e_commerce.e_commerce.service;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import e_commerce.e_commerce.model.Payment;
import e_commerce.e_commerce.repository.OrderRepository;
import e_commerce.e_commerce.repository.PaymentRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    @Value("${razorpay.key_id}")
    private String keyId;

    @Value("${razorpay.key_secret}")
    private String keySecret;

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    public PaymentService(PaymentRepository paymentRepository,
                          OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
    }

    public Map<String, Object> createRazorpayPayment(String orderId, double amount) {
        try {
            RazorpayClient client = new RazorpayClient(keyId, keySecret);

            JSONObject options = new JSONObject();
            options.put("amount", amount * 100); // paise
            options.put("currency", "INR");
            options.put("receipt", orderId);

            Order razorpayOrder = client.orders.create(options);

            Payment payment = new Payment();
            payment.setOrderId(orderId);
            payment.setAmount(amount);
            payment.setStatus("PENDING");
            payment.setPaymentId(razorpayOrder.get("id"));
            payment.setCreatedAt(Instant.now());
            paymentRepository.save(payment);

            Map<String, Object> response = new HashMap<>();
            response.put("razorpayOrderId", razorpayOrder.get("id"));
            response.put("amount", amount);
            response.put("currency", "INR");
            response.put("status", "PENDING");

            return response;

        } catch (Exception e) {
            throw new RuntimeException("Razorpay payment failed");
        }
    }

    public void handlePaymentSuccess(String razorpayOrderId) {
        Payment payment = paymentRepository.findByPaymentId(razorpayOrderId);

        if (payment == null) {
            throw new RuntimeException("Payment not found for Razorpay Order ID");
        }

        payment.setStatus("SUCCESS");
        paymentRepository.save(payment);

        var order = orderRepository.findById(payment.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus("PAID");
        orderRepository.save(order);
    }
}
