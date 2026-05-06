package cl.techstore.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "productos")
// Esta anotación evita el error de ByteBuddyInterceptor que vimos en tu consola
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Data 
@NoArgsConstructor  // Recomendado para JPA
@AllArgsConstructor // Útil para pruebas
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 255)
    private String descripcion;

    @Column(nullable = false)
    private Double precio;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false, length = 50)
    private String categoria;

    // Aseguramos que por defecto sea true, pero permitimos que Jackson lo mapee
    @Column(nullable = false)
    private Boolean activo = true;
}