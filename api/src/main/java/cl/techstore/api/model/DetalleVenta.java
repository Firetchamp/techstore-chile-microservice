package cl.techstore.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "detalle_ventas")
@Data
@NoArgsConstructor
@AllArgsConstructor
// Blindaje contra proxies de Hibernate
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) 
public class DetalleVenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false)
    private Double precioUnitario;

    @Column(nullable = false)
    private Double subtotal;

    @ManyToOne
    @JoinColumn(name = "venta_id", nullable = false)
    @JsonIgnore // Crucial para evitar recursividad infinita
    private Venta venta;

    @ManyToOne
    @JoinColumn(name = "producto_id", nullable = false)
    // Toque profesional: Solo mostramos lo relevante del producto en el detalle
    @JsonIgnoreProperties({"descripcion", "stock", "activo", "categoria", "hibernateLazyInitializer", "handler"})
    private Producto producto;
}