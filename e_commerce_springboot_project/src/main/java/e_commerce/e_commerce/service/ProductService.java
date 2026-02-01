package e_commerce.e_commerce.service;

import e_commerce.e_commerce.model.Product;
import e_commerce.e_commerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product create(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    // BONUS: product search
    public List<Product> search(String query) {
        return productRepository.findByNameContainingIgnoreCase(query);
    }

    public Product getById(String id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }
}
