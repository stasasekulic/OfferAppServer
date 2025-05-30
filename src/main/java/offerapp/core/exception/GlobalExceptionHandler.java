package offerapp.core.exception;

import offerapp.user.exception.UsernameAlreadyExistsException;
import offerapp.product.exception.ProductInUseException;
import offerapp.product.exception.ProductNotFoundException;
import offerapp.product.exception.ProductAlreadyExistsException;
import offerapp.offer.exception.OfferNotFoundException;
import offerapp.user.exception.UserNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException ex, WebRequest request) {
        ApiErrorResponse errorDetails = new ApiErrorResponse(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ProductAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleProductAlreadyExistsException(ProductAlreadyExistsException ex, WebRequest request) {
        ApiErrorResponse errorDetails = new ApiErrorResponse(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ProductInUseException.class)
    public ResponseEntity<ApiErrorResponse> handleProductInUseException(ProductInUseException ex, WebRequest request) {
        ApiErrorResponse errorDetails = new ApiErrorResponse(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.CONFLICT); // 409 Conflict
    }

    @ExceptionHandler({ProductNotFoundException.class, OfferNotFoundException.class, UserNotFoundException.class})
    public ResponseEntity<ApiErrorResponse> handleNotFoundExceptions(RuntimeException ex, WebRequest request) {
        ApiErrorResponse errorDetails = new ApiErrorResponse(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_FOUND); // 404 Not Found
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        ApiErrorResponse errorDetails = new ApiErrorResponse(
                LocalDateTime.now(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST); // 400 Bad Request
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        ApiErrorResponse errorDetails = new ApiErrorResponse(
                LocalDateTime.now(),
                "An unexpected error occurred: " + ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}