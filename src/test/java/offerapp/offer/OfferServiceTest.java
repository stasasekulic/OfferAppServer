package offerapp.offer;

import offerapp.offer.dto.CreateOfferRequest;
import offerapp.offer.dto.OfferResponse;
import offerapp.offer.exception.OfferNotFoundException;
import offerapp.product.Product;
import offerapp.product.ProductRepository;
import offerapp.product.enums.ProductType;
import offerapp.user.User;
import offerapp.user.UserRepository;
import offerapp.user.enums.UserRole;
import org.junit.jupiter.api.Assertions;
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
public class OfferServiceTest {

    @Mock
    private OfferRepository offerRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OfferService offerService;

    @Test
    void testCreateOfferSuccess() {
        CreateOfferRequest request = new CreateOfferRequest();
        request.setTitle("Test Offer");
        request.setUserId(1L);
        request.setProductIds(Arrays.asList(10L, 20L));

        User mockUser = new User("John", "Doe", "test@example.com", "1234567890123", "0601234567", UserRole.USER);

        Product mockProduct1 = new Product("Laptop", "laptop.jpg", 1200, ProductType.OTHER);
        Product mockProduct2 = new Product("Mouse", "mouse.jpg", 25, ProductType.FREEZER);
        List<Product> mockProducts = Arrays.asList(mockProduct1, mockProduct2);

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        Mockito.when(productRepository.findAllById(Arrays.asList(10L, 20L))).thenReturn(mockProducts);
        Mockito.when(offerRepository.save(Mockito.any(Offer.class)))
                .thenAnswer(invocation -> {
                    Offer offerArg = invocation.getArgument(0);
                    Offer savedOffer = new Offer(offerArg.getTitle(), offerArg.getUser(), new ArrayList<>(offerArg.getProducts()));

                    return savedOffer;
                });

        OfferResponse response = offerService.createOffer(request);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("Test Offer", response.getTitle());
        Assertions.assertEquals("test@example.com", response.getUserEmail());
        Assertions.assertEquals(2, response.getProducts().size());
        Assertions.assertEquals("Laptop", response.getProducts().get(0).getDescription());
        Assertions.assertEquals("Mouse", response.getProducts().get(1).getDescription());

        Mockito.verify(userRepository).findById(1L);
        Mockito.verify(productRepository).findAllById(Arrays.asList(10L, 20L));
        Mockito.verify(offerRepository).save(Mockito.argThat(offer ->
                "Test Offer".equals(offer.getTitle()) &&
                        offer.getProducts().size() == 2
        ));
    }

    @Test
    void testCreateOfferWithEmptyProductIds() {
        CreateOfferRequest request = new CreateOfferRequest();
        request.setTitle("Offer with no products");
        request.setUserId(1L);
        request.setProductIds(Collections.emptyList());

        Assertions.assertThrows(IllegalArgumentException.class, () -> offerService.createOffer(request));

        Mockito.verify(offerRepository, Mockito.never()).save(Mockito.any(Offer.class));
    }

    @Test
    void testCreateOfferUserNotFound() {
        CreateOfferRequest request = new CreateOfferRequest();
        request.setTitle("Test Offer");
        request.setUserId(99L);
        request.setProductIds(Arrays.asList(10L));

        Mockito.when(userRepository.findById(99L)).thenReturn(Optional.empty());

        Assertions.assertThrows(IllegalArgumentException.class, () -> offerService.createOffer(request));

        Mockito.verify(offerRepository, Mockito.never()).save(Mockito.any(Offer.class));
    }

    @Test
    void testCreateOfferSomeProductsNotFound() {
        CreateOfferRequest request = new CreateOfferRequest();
        request.setTitle("Test Offer");
        request.setUserId(1L);
        request.setProductIds(Arrays.asList(10L, 20L, 30L)); // Tražimo 3 proizvoda

        User mockUser = new User("John", "Doe", "test@example.com", "1234567890123", "0601234567", UserRole.USER);
        Product mockProduct1 = new Product("Laptop", "laptop.jpg", 1200, ProductType.FRIDGE);
        Product mockProduct2 = new Product("Mouse", "mouse.jpg", 25, ProductType.FRIDGE);
        List<Product> foundProducts = Arrays.asList(mockProduct1, mockProduct2);

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        Mockito.when(productRepository.findAllById(Arrays.asList(10L, 20L, 30L))).thenReturn(foundProducts);

        Assertions.assertThrows(IllegalArgumentException.class, () -> offerService.createOffer(request));

        Mockito.verify(offerRepository, Mockito.never()).save(Mockito.any(Offer.class));
    }

    @Test
    void testGetAllOffers() {
        User user1 = new User("User", "One", "user1@example.com", "1112223334445", "0601112233", UserRole.USER);
        user1.setId(1L);

        Product prod1 = new Product("P1", "p1.jpg", 10, ProductType.OTHER);

        Offer offer1 = Mockito.spy(new Offer("Offer A", user1, Collections.singletonList(prod1)));
        Mockito.doReturn(1L).when(offer1).getId();

        Offer offer2 = Mockito.spy(new Offer("Offer B", user1, Collections.emptyList()));
        Mockito.doReturn(2L).when(offer2).getId();


        Mockito.when(offerRepository.findAll()).thenReturn(Arrays.asList(offer1, offer2));

        List<OfferResponse> offers = offerService.getAllOffers();

        Assertions.assertNotNull(offers);
        Assertions.assertEquals(2, offers.size());
        Assertions.assertEquals("Offer A", offers.get(0).getTitle());
        Assertions.assertEquals(1L, offers.get(0).getId());
        Assertions.assertEquals(1L, offers.get(0).getUserId());
        Assertions.assertEquals("Offer B", offers.get(1).getTitle());
        Assertions.assertEquals(2L, offers.get(1).getId());
    }

    @Test
    void testGetAllOffersEmptyList() {
        Mockito.when(offerRepository.findAll()).thenReturn(Collections.emptyList());

        List<OfferResponse> offers = offerService.getAllOffers();

        Assertions.assertNotNull(offers);
        Assertions.assertTrue(offers.isEmpty());
    }

    @Test
    void testGetOfferByIdFound() {
        User user = new User("Test", "User", "test@example.com", "9998887776665", "0619876543", UserRole.USER);
        user.setId(1L);

        Product product = new Product("Test Product", "test.jpg", 100, ProductType.OTHER);

        Offer offer = Mockito.spy(new Offer("Found Offer", user, Collections.singletonList(product)));
        Mockito.doReturn(1L).when(offer).getId();

        Mockito.when(offerRepository.findById(1L)).thenReturn(Optional.of(offer));

        OfferResponse foundOffer = offerService.getOfferById(1L);

        Assertions.assertNotNull(foundOffer);
        Assertions.assertEquals(1L, foundOffer.getId());
        Assertions.assertEquals("Found Offer", foundOffer.getTitle());
        Assertions.assertEquals(1L, foundOffer.getUserId());
        Assertions.assertEquals(1, foundOffer.getProducts().size());
        Assertions.assertEquals("Test Product", foundOffer.getProducts().get(0).getDescription());
    }

    @Test
    void testGetOfferByIdNotFound() {
        Mockito.when(offerRepository.findById(99L)).thenReturn(Optional.empty());

        Assertions.assertThrows(OfferNotFoundException.class, () -> offerService.getOfferById(99L));
    }

    @Test
    void testDeleteOfferSuccess() {
        Mockito.when(offerRepository.existsById(5L)).thenReturn(true);

        offerService.deleteOffer(5L);

        Mockito.verify(offerRepository).deleteById(5L);
    }

    @Test
    void testDeleteOfferNotFound() {
        Mockito.when(offerRepository.existsById(55L)).thenReturn(false);

        Assertions.assertThrows(OfferNotFoundException.class, () -> offerService.deleteOffer(55L));

        Mockito.verify(offerRepository, Mockito.never()).deleteById(Mockito.anyLong());
    }
}