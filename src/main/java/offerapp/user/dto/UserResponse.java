package offerapp.user.dto;

import offerapp.user.User;
import offerapp.user.enums.UserRole;

public class UserResponse {

    public UserResponse(User user) {
        Long id = user.getId();
        String name = user.getName();
        String surname = user.getSurname();
        String email = user.getEmail();
        String phoneNumber = user.getPhoneNumber();
        UserRole role = user.getRole();
    }
}
