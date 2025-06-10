package offerapp.product;

import offerapp.offer.Offer; // Dodato
import offerapp.offer.OfferRepository; // Dodato
import offerapp.offer.enums.OfferStatus; // Dodato
import offerapp.product.dto.CreateProductRequest;
import offerapp.product.dto.UpdateProductRequest;
import offerapp.product.dto.ProductResponse;
import offerapp.product.exception.ProductAlreadyExistsException;
import offerapp.product.exception.ProductInUseException;
import offerapp.product.exception.ProductNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final OfferRepository offerRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, OfferRepository offerRepository) {
        this.productRepository = productRepository;
        this.offerRepository = offerRepository;
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

    @Transactional
    public void deleteProduct(Long id) {
        Product productToDelete = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));

        List<Offer> associatedOffers = offerRepository.findByProducts_Id(id);

        boolean hasActiveOffers = false;
        for (Offer offer : associatedOffers) {
            OfferStatus status = offer.getStatus();
            if (status != OfferStatus.CANCELED && status != OfferStatus.FINISHED) {
                hasActiveOffers = true;
                break;
            }
        }

        if (hasActiveOffers) {
            throw new ProductInUseException("Product cannot be deleted because it is part of one or more active offers.");
        } else {
            for (Offer offer : associatedOffers) {
                offer.removeProduct(productToDelete);
                offerRepository.save(offer);
            }
            productRepository.deleteById(id);
        }
    }
}