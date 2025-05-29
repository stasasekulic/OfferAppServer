package offerapp.user.exception;

public class UsernameAlreadyExistsException extends RuntimeException {
    public UsernameAlreadyExistsException(String email) {
        super("User with email " + email + " already exists.");
    }
}
