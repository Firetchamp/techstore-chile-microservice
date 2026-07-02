package cl.techstore.api.exception;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorDetalle {
    private LocalDateTime timestamp;
    private String mensaje;
    private String detalles;
    private String codigoError;
}