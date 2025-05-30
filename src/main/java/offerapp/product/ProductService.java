package offerapp.product;

import offerapp.product.dto.CreateProductRequest;
import offerapp.product.dto.UpdateProductRequest;
import offerapp.product.dto.ProductResponse;
import offerapp.product.exception.ProductAlreadyExistsException;
import offerapp.product.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public ProductResponse createProduct(CreateProductRequest request) {
        if (productRepository.existsByDescription(request.getDescription())) {
            throw new ProductAlreadyExistsException(
                    "Product already exists with description: " + request.getDescription());
        }

        Product product = new Product(
                request.getDescription(),
                request.getPicture(),
                request.getPrice(),
                request.getType()
        );
        Product saved = productRepository.save(product);
        return new ProductResponse(saved);
    }

    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
        return new ProductResponse(product);
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(ProductResponse::new)
                .collect(Collectors.toList());
    }

    public ProductResponse updateProduct(Long id, UpdateProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

        product.setDescription(request.getDescription());
        product.setPicture(request.getPicture());
        product.setPrice(request.getPrice());
        product.setType(request.getType());

        Product updated = productRepository.save(product);
        return new ProductResponse(updated);
    }

    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }
}
