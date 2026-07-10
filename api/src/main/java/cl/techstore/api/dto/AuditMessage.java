package cl.techstore.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuditMessage {
    private String operation;   // Ejemplo: "PRODUCT_CREATED"
    private Long productId;     // ID asignado al producto
    private String name;        // Nombre del producto
    private Double price;       // Precio del producto
    private String timestamp;   // Fecha y hora del evento en formato texto
}