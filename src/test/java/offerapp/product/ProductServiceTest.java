package offerapp.product;

import offerapp.product.dto.CreateProductRequest;
import offerapp.product.dto.ProductResponse;
import offerapp.product.dto.UpdateProductRequest;
import offerapp.product.enums.ProductType;
import offerapp.product.exception.ProductAlreadyExistsException;
import offerapp.product.exception.ProductNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void testCreateProductSuccess() {
        CreateProductRequest request = new CreateProductRequest();
        request.setDescription("Test Product Description");
        request.setPicture("test.jpg");
        request.setPrice(150);
        request.setType(ProductType.OTHER);

        Mockito.when(productRepository.existsByDescription(request.getDescription())).thenReturn(false);
        Mockito.when(productRepository.save(Mockito.any(Product.class)))
                .thenAnswer(invocation -> {
                    Product productArg = invocation.getArgument(0);
                    return productArg;
                });


        ProductResponse response = productService.createProduct(request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("Test Product Description", response.getDescription());
        Assertions.assertEquals(150, response.getPrice());
        Assertions.assertEquals(ProductType.OTHER, response.getType());

        Mockito.verify(productRepository).existsByDescription(request.getDescription());
        Mockito.verify(productRepository).save(Mockito.argThat(p ->
                "Test Product Description".equals(p.getDescription()) &&
                        "test.jpg".equals(p.getPicture()) &&
                        150 == p.getPrice() &&
                        ProductType.OTHER.equals(p.getType())
        ));
    }

    @Test
    void testCreateProductAlreadyExists() {
        CreateProductRequest request = new CreateProductRequest();
        request.setDescription("Existing Product");
        request.setPicture("existing.jpg");
        request.setPrice(100);
        request.setType(ProductType.FRIDGE);

        Mockito.when(productRepository.existsByDescription(request.getDescription())).thenReturn(true);

        Assertions.assertThrows(ProductAlreadyExistsException.class, () -> productService.createProduct(request));

        Mockito.verify(productRepository, Mockito.never()).save(Mockito.any(Product.class));
    }

    @Test
    void testGetProductByIdFound() {
        Product product = new Product("Laptop", "laptop.jpg", 1200, ProductType.OTHER);
        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponse foundProduct = productService.getProductById(1L);

        Assertions.assertNotNull(foundProduct);
        Assertions.assertEquals("Laptop", foundProduct.getDescription());
        Assertions.assertEquals(1200, foundProduct.getPrice());
    }

    @Test
    void testGetProductByIdNotFound() {
        Mockito.when(productRepository.findById(99L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.getProductById(99L));
    }

    @Test
    void testGetAllProducts() {
        Product p1 = new Product("Mouse", "mouse.jpg", 25, ProductType.FRIDGE);
        Product p2 = new Product("Keyboard", "keyboard.jpg", 75, ProductType.FREEZER);

        Mockito.when(productRepository.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<ProductResponse> products = productService.getAllProducts();

        Assertions.assertNotNull(products);
        Assertions.assertEquals(2, products.size());
        Assertions.assertEquals("Mouse", products.get(0).getDescription());
        Assertions.assertEquals("Keyboard", products.get(1).getDescription());
    }

    @Test
    void testGetAllProductsEmptyList() {
        Mockito.when(productRepository.findAll()).thenReturn(Arrays.asList());

        List<ProductResponse> products = productService.getAllProducts();

        Assertions.assertNotNull(products);
        Assertions.assertTrue(products.isEmpty());
    }

    @Test
    void testUpdateProductSuccess() {
        Product existingProduct = new Product("Old Description", "old.jpg", 100, ProductType.FRIDGE);

        UpdateProductRequest updateRequest = new UpdateProductRequest();
        updateRequest.setDescription("New Description");
        updateRequest.setPicture("new.jpg");
        updateRequest.setPrice(120);
        updateRequest.setType(ProductType.FREEZER);

        Mockito.when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        Mockito.when(productRepository.save(Mockito.any(Product.class))).thenReturn(existingProduct);

        ProductResponse updatedProduct = productService.updateProduct(1L, updateRequest);

        Assertions.assertNotNull(updatedProduct);
        Assertions.assertEquals("New Description", updatedProduct.getDescription());
        Assertions.assertEquals("new.jpg", updatedProduct.getPicture());
        Assertions.assertEquals(120, updatedProduct.getPrice());
        Assertions.assertEquals(ProductType.FREEZER, updatedProduct.getType());

        Mockito.verify(productRepository).save(Mockito.argThat(p ->
                        "New Description".equals(p.getDescription()) &&
                        "new.jpg".equals(p.getPicture()) &&
                        120 == p.getPrice() &&
                        ProductType.FREEZER.equals(p.getType())
        ));
    }

    @Test
    void testUpdateProductNotFound() {
        UpdateProductRequest updateRequest = new UpdateProductRequest();
        updateRequest.setDescription("Non-existent");
        updateRequest.setPicture("non.jpg");
        updateRequest.setPrice(10);
        updateRequest.setType(ProductType.OTHER);

        Mockito.when(productRepository.findById(99L)).thenReturn(Optional.empty());

        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.updateProduct(99L, updateRequest));

        Mockito.verify(productRepository, Mockito.never()).save(Mockito.any(Product.class));
    }

    @Test
    void testDeleteProductSuccess() {
        Mockito.when(productRepository.existsById(5L)).thenReturn(true);

        productService.deleteProduct(5L);

        Mockito.verify(productRepository).deleteById(5L);
    }

    @Test
    void testDeleteProductNotFound() {
        Mockito.when(productRepository.existsById(55L)).thenReturn(false);

        Assertions.assertThrows(ProductNotFoundException.class, () -> productService.deleteProduct(55L));

        Mockito.verify(productRepository, Mockito.never()).deleteById(Mockito.anyLong());
    }
}