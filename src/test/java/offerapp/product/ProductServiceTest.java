package offerapp.product;

import offerapp.offer.Offer;
import offerapp.offer.OfferRepository;
import offerapp.offer.enums.OfferStatus; // Dodato
import offerapp.product.dto.CreateProductRequest;
import offerapp.product.dto.ProductResponse;
import offerapp.product.dto.UpdateProductRequest;
import offerapp.product.enums.ProductType;
import offerapp.product.exception.ProductAlreadyExistsException;
import offerapp.product.exception.ProductInUseException;
import offerapp.product.exception.ProductNotFoundException;
import offerapp.user.User;
import offerapp.user.enums.UserRole; // Dodato za UserRole
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach; // Dodato
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OfferRepository offerRepository;

    @InjectMocks
    private ProductService productService;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User("John", "Doe", "john.doe@example.com", "1234567890123", "0601234567", UserRole.USER);
    }

    @Test
    void testCreateProductSuccess() {
        CreateProductRequest request = new CreateProductRequest();
        request.setDescription("New Product");
        request.setPicture("new.jpg");
        request.setPrice(100);
        request.setType(ProductType.FRIDGE);

        Mockito.when(productRepository.existsByDescription("New Product")).thenReturn(false);
        Product savedProduct = new Product("New Product", "new.jpg", 100, ProductType.OTHER);
        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(savedProduct);

        ProductResponse response = productService.createProduct(request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("New Product", response.getDescription());
        Mockito.verify(productRepository).save(Mockito.any(Product.class));
    }

    @Test
    void testCreateProductAlreadyExists() {
        CreateProductRequest request = new CreateProductRequest();
        request.setDescription("Existing Product");

        Mockito.when(productRepository.existsByDescription("Existing Product")).thenReturn(true);

        Assertions.assertThrows(ProductAlreadyExistsException.class, () -> productService.createProduct(request));
        Mockito.verify(productRepository, Mockito.never()).save(Mockito.any(Product.class));
    }

    @Test
    void testGetProductByIdFound() {
        Product product = new Product("Test Product", "test.jpg", 50, ProductType.FRIDGE);
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponse response = productService.getProductById(1L);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("Test Product", response.getDescription());
    }

    @Test
    void testGetProductByIdNotFound() {
        Mockito.when(productRepository.findById(99L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.getProductById(99L));
    }

    @Test
    void testGetAllProducts() {
        Product product1 = new Product("P1", "p1.jpg", 10, ProductType.OTHER);
        Product product2 = new Product("P2", "p2.jpg", 20, ProductType.FREEZER);
        Mockito.when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        List<ProductResponse> products = productService.getAllProducts();

        Assertions.assertNotNull(products);
        Assertions.assertEquals(2, products.size());
        Assertions.assertEquals("P1", products.get(0).getDescription());
        Assertions.assertEquals("P2", products.get(1).getDescription());
    }

    @Test
    void testUpdateProductSuccess() {
        UpdateProductRequest request = new UpdateProductRequest();
        request.setDescription("Updated Product");
        request.setPicture("updated.jpg");
        request.setPrice(150);
        request.setType(ProductType.OTHER);

        Product existingProduct = new Product("Old Product", "old.jpg", 100, ProductType.FRIDGE);
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(existingProduct);

        ProductResponse response = productService.updateProduct(1L, request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("Updated Product", response.getDescription());
        Assertions.assertEquals("updated.jpg", response.getPicture());
        Assertions.assertEquals(150, response.getPrice());
        Assertions.assertEquals(ProductType.OTHER, response.getType());

        Mockito.verify(productRepository).save(Mockito.any(Product.class));
    }

    @Test
    void testUpdateProductNotFound() {
        UpdateProductRequest request = new UpdateProductRequest();
        // ...
        Mockito.when(productRepository.findById(99L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(99L, request));
        Mockito.verify(productRepository, Mockito.never()).save(Mockito.any(Product.class));
    }

    @Test
    void testDeleteProductSuccessNoOffers() {
        Mockito.when(productRepository.findById(5L)).thenReturn(Optional.of(new Product("Test", "", 0, ProductType.OTHER)));
        Mockito.when(offerRepository.findByProducts_Id(5L)).thenReturn(Collections.emptyList()); // Nema povezanih ponuda

        productService.deleteProduct(5L);

        Mockito.verify(productRepository).deleteById(5L);
        Mockito.verify(offerRepository, Mockito.never()).save(Mockito.any(Offer.class)); // Nema ponuda za save
    }

    @Test
    void testDeleteProductNotFound() {
        Mockito.when(productRepository.findById(99L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(99L));

        Mockito.verify(productRepository, Mockito.never()).deleteById(Mockito.anyLong());
    }

    @Test
    void testDeleteProductInActiveOffer() {
        Long productId = 5L;
        Product productToDelete = new Product("Product in Active Offer", "img.jpg", 100, ProductType.OTHER);

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(productToDelete));

        Offer activeOffer = Mockito.spy(new Offer("Active Offer", mockUser, new ArrayList<>(Arrays.asList(productToDelete)), OfferStatus.ACTIVE));

        Mockito.when(offerRepository.findByProducts_Id(productId)).thenReturn(Arrays.asList(activeOffer));

        Assertions.assertThrows(ProductInUseException.class, () -> productService.deleteProduct(productId));

        Mockito.verify(productRepository, Mockito.never()).deleteById(Mockito.anyLong());
        Mockito.verify(offerRepository, Mockito.never()).save(Mockito.any(Offer.class));
    }

    @Test
    void testDeleteProductInFinishedOffer() {
        Long productId = 5L;
        Product productToDelete = new Product("Product in Finished Offer", "img.jpg", 100, ProductType.OTHER);

        Mockito.when(productRepository.findById(productId)).thenReturn(Optional.of(productToDelete));

        Offer finishedOffer = Mockito.spy(new Offer("Finished Offer", mockUser, new ArrayList<>(Arrays.asList(productToDelete)), OfferStatus.FINISHED));

        Mockito.when(offerRepository.findByProducts_Id(productId)).thenReturn(Arrays.asList(finishedOffer));

        Mockito.when(offerRepository.save(Mockito.any(Offer.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        productService.deleteProduct(productId);

        Mockito.verify(finishedOffer).removeProduct(productToDelete);
        Mockito.verify(offerRepository).save(finishedOffer);
        Mockito.verify(productRepository).deleteById(productId);
    }
}