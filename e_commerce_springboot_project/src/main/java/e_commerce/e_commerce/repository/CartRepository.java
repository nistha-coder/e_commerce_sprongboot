package e_commerce.e_commerce.repository;

import e_commerce.e_commerce.model.CartItem;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CartRepository extends MongoRepository<CartItem, String> {

    List<CartItem> findByUserId(String userId);

    void deleteByUserId(String userId);
}
