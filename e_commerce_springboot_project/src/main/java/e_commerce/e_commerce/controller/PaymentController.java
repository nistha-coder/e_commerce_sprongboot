package e_commerce.e_commerce.controller;

import e_commerce.e_commerce.model.Payment;
import e_commerce.e_commerce.service.PaymentService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create")
    public Map<String, Object> createPayment(@RequestBody Map<String, Object> req) {
        String orderId = req.get("orderId").toString();
        double amount = Double.parseDouble(req.get("amount").toString());

        return paymentService.createRazorpayPayment(orderId, amount);
    }
}


