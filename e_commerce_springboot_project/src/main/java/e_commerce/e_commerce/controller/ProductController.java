package e_commerce.e_commerce.controller;

import e_commerce.e_commerce.model.Product;
import e_commerce.e_commerce.service.ProductService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // POST /api/products
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.create(product);
    }

    // GET /api/products
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAll();
    }

    // BONUS: GET /api/products/search?q=laptop
    @GetMapping("/search")
    public List<Product> searchProducts(@RequestParam String q) {
        return productService.search(q);
    }
}
