package e_commerce.e_commerce.repository;

import e_commerce.e_commerce.model.Payment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PaymentRepository extends MongoRepository<Payment, String> {

    Payment findByOrderId(String orderId);

    // ✅ REQUIRED for Razorpay
    Payment findByPaymentId(String paymentId);
}
