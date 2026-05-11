package cl.techstore.api.model;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ventas")
@Data
@NoArgsConstructor
@AllArgsConstructor
// Blindaje contra proxies de Hibernate
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @OneToMany(mappedBy = "venta", cascade = CascadeType.ALL)
    // Evitamos que al serializar los detalles, estos intenten serializar la venta de vuelta
    @JsonIgnoreProperties("venta") 
    private List<DetalleVenta> detalles;

    @Column(name = "fecha_venta", nullable = false)
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss") // Formato profesional
    private LocalDateTime fechaVenta;

    @Column(nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.NUMBER_FLOAT, pattern = "#,###") // Formato moneda
    private Double total;

    @PrePersist
    protected void onCreate() {
        this.fechaVenta = LocalDateTime.now();
    }
}