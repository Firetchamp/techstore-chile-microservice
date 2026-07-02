package cl.techstore.api.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Este es el más importante: Captura tus RuntimeException (Stock, Inactivo, etc.)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDetalle> manejarErroresDeNegocio(RuntimeException ex, WebRequest request) {
        
        ErrorDetalle error = new ErrorDetalle(
            LocalDateTime.now(),
            ex.getMessage(), // Esto traerá el "Errores de stock detectados..."
            "Operación no permitida: Revisa los datos de los productos.",
            "NEGOCIO_ERROR"
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST); // Usamos 400 (Bad Request)
    }

    // Captura errores de formato (como el del JSON anterior)
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorDetalle> manejarErrorJson(Exception ex, WebRequest request) {
        ErrorDetalle error = new ErrorDetalle(
            LocalDateTime.now(),
            "El formato del JSON enviado es inválido. Revisa llaves y comas.",
            request.getDescription(false),
            "FORMATO_INVALIDO"
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Solo si algo MUY grave pasa, cae aquí (Error 500)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetalle> manejarGlobal(Exception ex, WebRequest request) {
        ErrorDetalle error = new ErrorDetalle(
            LocalDateTime.now(),
            "Error interno: " + ex.getMessage(), // Le agregamos ex.getMessage() para saber qué falló realmente
            request.getDescription(false),
            "INTERNAL_SERVER_ERROR"
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}