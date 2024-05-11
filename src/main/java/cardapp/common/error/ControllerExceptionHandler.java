package cardapp.common.error;

import cardapp.card.error.CardAppException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<Object> handleException(CardAppException e) {
        return switch (e.getErrorEnum()) {
            case NOT_FOUND -> ResponseEntity.notFound().build();
        };
    }

    @ExceptionHandler({SQLIntegrityConstraintViolationException.class,
            IllegalArgumentException.class, ConstraintViolationException.class})
    public ResponseEntity<Object> handleException(Exception e) {
        return ResponseEntity.badRequest().build();
    }

}
