package offerapp.user;

import offerapp.user.dto.UserResponse;
import offerapp.user.enums.UserRole;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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
    }
}
