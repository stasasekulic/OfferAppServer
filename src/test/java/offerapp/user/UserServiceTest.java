package offerapp.user;

import offerapp.offer.Offer;
import offerapp.product.Product;
import offerapp.product.enums.ProductType;
import offerapp.user.dto.UpdateUserRequest;
import offerapp.user.dto.UserResponse;
import offerapp.user.enums.UserRole;
import offerapp.user.exception.UserNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testFindUserByIdFound() {
        User user = new User("Marko", "Markovic", "marko@example.com", "12345678790", "06000112233", UserRole.USER);
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserResponse found = userService.getUserById(1L);

        Assertions.assertEquals("Marko", found.getName());
        Assertions.assertEquals("Markovic", found.getSurname());
        Assertions.assertEquals("marko@example.com", found.getEmail());
    }

    @Test
    void testFindUserByIdNotFound() {
        Mockito.when(userRepository.findById(999L)).thenReturn(Optional.empty());
        Assertions.assertThrows(RuntimeException.class, () -> userService.getUserById(999L));
    }

    @Test
    void testFindUserWithOffersAndProducts() {
        Product p1 = new Product("Opis 1", "slika1.jpg", 100, ProductType.FRIDGE);
        Product p2 = new Product("Opis 2", "slika2.jpg", 200, ProductType.FREEZER);

        Offer offer = new Offer();
        offer.setTitle("Specijalna Ponuda");
        offer.setProducts(Arrays.asList(p1, p2));

        User user = new User("Milica", "Milic", "milica@example.com", "98765432100", "0612345678", UserRole.ADMIN);
        List<Offer> offers = new ArrayList<>();
        offers.add(offer);
        offer.setUser(user);
        user.setOffers(offers);

        Mockito.when(userRepository.findById(5L)).thenReturn(Optional.of(user));
        Mockito.when(userRepository.findById(5L)).thenReturn(Optional.of(user));

        UserResponse found = userService.getUserById(5L);

        Assertions.assertEquals("Milica", found.getName());
        Assertions.assertNotNull(found.getOffers());
        Assertions.assertEquals(1, found.getOffers().size());
        Assertions.assertEquals("Specijalna Ponuda", found.getOffers().get(0).getTitle());
        Assertions.assertEquals(2, found.getOffers().get(0).getProducts().size());
    }

    @Test
    void testGetByEmailSuccess() {
        User user = new User("Jovana", "Jovic", "jovana@example.com", "11111111111", "0654321987", UserRole.USER);
        Mockito.when(userRepository.existsByEmail("jovana@example.com")).thenReturn(true);
        Mockito.when(userRepository.findByEmail("jovana@example.com")).thenReturn(user);

        UserResponse response = userService.getByEmail("jovana@example.com");

        Assertions.assertEquals("Jovana", response.getName());
        Assertions.assertEquals("jovana@example.com", response.getEmail());
    }

    @Test
    void testGetByEmailNotFound() {
        Mockito.when(userRepository.existsByEmail("nonexistent@example.com")).thenReturn(false);

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.getByEmail("nonexistent@example.com"));
    }

    @Test
    void testUpdateUserSuccess() {
        User existingUser = new User("Milan", "Milic", "milan@example.com", "22222222222", "0651234567", UserRole.USER);
        UpdateUserRequest updateRequest = new UpdateUserRequest("MilanUpdated", "MilicUpdated", "0657654321", UserRole.ADMIN);

        Mockito.when(userRepository.findById(10L)).thenReturn(Optional.of(existingUser));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponse updatedUser = userService.updateUser(10L, updateRequest);

        Assertions.assertEquals("MilanUpdated", updatedUser.getName());
        Assertions.assertEquals("MilicUpdated", updatedUser.getSurname());
        Assertions.assertEquals("0657654321", updatedUser.getPhoneNumber());
        Assertions.assertEquals(UserRole.ADMIN, updatedUser.getRole());
    }

    @Test
    void testUpdateUserNotFound() {
        Mockito.when(userRepository.findById(404L)).thenReturn(Optional.empty());

        UpdateUserRequest request = new UpdateUserRequest("Ime", "Prezime", "0600000000", UserRole.USER);

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.updateUser(404L, request));
    }

    @Test
    void testDeleteUserSuccess() {
        Mockito.when(userRepository.existsById(7L)).thenReturn(true);

        userService.deleteUser(7L);

        Mockito.verify(userRepository).deleteById(7L);
    }

    @Test
    void testDeleteUserNotFound() {
        Mockito.when(userRepository.existsById(77L)).thenReturn(false);

        Assertions.assertThrows(UserNotFoundException.class, () -> userService.deleteUser(77L));
    }

    @Test
    void testFindUserByIdWithNoOffers() {
        User user = new User("Petar", "Petrovic", "petar@example.com", "12345678900", "0601122334", UserRole.USER);
        user.setOffers(new ArrayList<>());

        Mockito.when(userRepository.findById(2L)).thenReturn(Optional.of(user));

        UserResponse found = userService.getUserById(2L);

        Assertions.assertEquals("Petar", found.getName());
        Assertions.assertNotNull(found.getOffers());
        Assertions.assertTrue(found.getOffers().isEmpty());
    }

    @Test
    void testFindUserWithOffersButNoProducts() {
        Offer offer = new Offer();
        offer.setTitle("Ponuda Bez Proizvoda");
        offer.setProducts(new ArrayList<>());

        User user = new User("Ana", "Anic", "ana@example.com", "11223344550", "0698765432", UserRole.USER);
        List<Offer> offers = new ArrayList<>();
        offers.add(offer);
        offer.setUser(user);
        user.setOffers(offers);

        Mockito.when(userRepository.findById(6L)).thenReturn(Optional.of(user));

        UserResponse found = userService.getUserById(6L);

        Assertions.assertEquals("Ana", found.getName());
        Assertions.assertNotNull(found.getOffers());
        Assertions.assertEquals(1, found.getOffers().size());
        Assertions.assertEquals("Ponuda Bez Proizvoda", found.getOffers().get(0).getTitle());
        Assertions.assertNotNull(found.getOffers().get(0).getProducts());
        Assertions.assertTrue(found.getOffers().get(0).getProducts().isEmpty());
    }

    @Test
    void testFindUserWithMultipleOffers() {
        Product p1 = new Product("Laptop", "laptop.jpg", 1200, ProductType.FRIDGE);
        Product p2 = new Product("Telefon", "phone.jpg", 800, ProductType.FREEZER);
        Product p3 = new Product("Frižider", "fridge.jpg", 500, ProductType.FRIDGE);

        Offer offer1 = new Offer();
        offer1.setTitle("Tehnika Akcija");
        offer1.setProducts(Arrays.asList(p1, p2));

        Offer offer2 = new Offer();
        offer2.setTitle("Bela Tehnika Sniženje");
        offer2.setProducts(List.of(p3));

        User user = new User("Dragan", "Draganic", "dragan@example.com", "00112233445", "0641234567", UserRole.ADMIN);
        List<Offer> offers = new ArrayList<>(Arrays.asList(offer1, offer2));
        offer1.setUser(user);
        offer2.setUser(user);
        user.setOffers(offers);

        Mockito.when(userRepository.findById(7L)).thenReturn(Optional.of(user));

        UserResponse found = userService.getUserById(7L);

        Assertions.assertEquals("Dragan", found.getName());
        Assertions.assertNotNull(found.getOffers());
        Assertions.assertEquals(2, found.getOffers().size());
        Assertions.assertEquals("Tehnika Akcija", found.getOffers().get(0).getTitle());
        Assertions.assertEquals(2, found.getOffers().get(0).getProducts().size());
        Assertions.assertEquals("Bela Tehnika Sniženje", found.getOffers().get(1).getTitle());
        Assertions.assertEquals(1, found.getOffers().get(1).getProducts().size());
    }

    @Test
    void testUpdateUserVerifiesSaveCall() {
        User existingUser = new User("Stari", "Korisnik", "stari@example.com", "12312312312", "060123123", UserRole.USER);
        UpdateUserRequest updateRequest = new UpdateUserRequest("Novi", "Korisnik", "060321321", UserRole.ADMIN);

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(existingUser);

        userService.updateUser(1L, updateRequest);

        Mockito.verify(userRepository).save(Mockito.argThat(user ->
                "Novi".equals(user.getName()) &&
                        "Korisnik".equals(user.getSurname()) &&
                        "060321321".equals(user.getPhoneNumber()) &&
                        UserRole.ADMIN.equals(user.getRole())
        ));
    }
}
