package e_commerce.e_commerce.webhook;

import e_commerce.e_commerce.service.PaymentService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/webhooks/payment")
public class PaymentWebhookController {

    private final PaymentService paymentService;

    public PaymentWebhookController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public void handleWebhook(@RequestBody Map<String, Object> payload) {

        Map<String, Object> payloadMap = (Map<String, Object>) payload.get("payload");
        Map<String, Object> payment = (Map<String, Object>) payloadMap.get("payment");
        Map<String, Object> entity = (Map<String, Object>) payment.get("entity");

        String razorpayOrderId = entity.get("id").toString();
        String status = entity.get("status").toString();

        if ("captured".equals(status)) {
            paymentService.handlePaymentSuccess(razorpayOrderId);
        }
    }
}
