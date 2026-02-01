package e_commerce.e_commerce.repository;

import e_commerce.e_commerce.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
}
