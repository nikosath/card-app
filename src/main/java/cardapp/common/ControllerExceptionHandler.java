package cardapp.common;

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
//
//    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
//    public ResponseEntity<Object> handleException(SQLIntegrityConstraintViolationException e) {
//        return ResponseEntity.badRequest().build();
//    }
//
//    @ExceptionHandler(IllegalArgumentException.class)
//    public ResponseEntity<Object> handleException(IllegalArgumentException e) {
//        return ResponseEntity.badRequest().build();
//    }
//
//    @ExceptionHandler(ConstraintViolationException.class)
//    public ResponseEntity<Object> handleException(ConstraintViolationException e) {
//        return ResponseEntity.badRequest().build();
//    }

    @ExceptionHandler({SQLIntegrityConstraintViolationException.class,
            IllegalArgumentException.class, ConstraintViolationException.class})
    public ResponseEntity<Object> handleException(Exception e) {
        return ResponseEntity.badRequest().build();
    }

}
