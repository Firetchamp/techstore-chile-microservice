package cl.techstore.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.techstore.api.model.DetalleVenta;
import cl.techstore.api.model.Producto;
import cl.techstore.api.model.Venta;
import cl.techstore.api.repository.ProductoRepository;
import cl.techstore.api.repository.VentaRepository;

@Service
public class VentaServiceImpl implements IVentaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ProductoRepository productoRepository;

   @Override
@Transactional
public Venta registrarVenta(Venta venta) {
    if (venta.getDetalles() == null || venta.getDetalles().isEmpty()) {
        throw new RuntimeException("La venta debe tener al menos un producto.");
    }

    StringBuilder errores = new StringBuilder();
    Double totalAcumulado = 0.0;

    // PRIMER PASO: Validar todo sin modificar nada (Check de stock)
    for (DetalleVenta detalle : venta.getDetalles()) {
        Producto producto = productoRepository.findById(detalle.getProducto().getId())
                .orElseThrow(() -> new RuntimeException("Producto ID " + detalle.getProducto().getId() + " no existe."));

        if (!producto.getActivo()) {
            errores.append("- ").append(producto.getNombre()).append(" no está activo.\n");
        }

        if (producto.getStock() < detalle.getCantidad()) {
            errores.append("- ").append(producto.getNombre())
                   .append(": solicitado ").append(detalle.getCantidad())
                   .append(", disponible ").append(producto.getStock()).append(".\n");
        }
    }

    // Si hubo errores, lanzamos la excepción con la lista completa
    if (errores.length() > 0) {
        throw new RuntimeException("Errores de stock detectados:\n" + errores.toString());
    }

    // SEGUNDO PASO: Si llegamos aquí, todo está bien. Procedemos a procesar.
    for (DetalleVenta detalle : venta.getDetalles()) {
        Producto producto = productoRepository.findById(detalle.getProducto().getId()).get();

        // Descontar stock
        producto.setStock(producto.getStock() - detalle.getCantidad());
        productoRepository.save(producto);

        // Sincronizar detalle con datos reales
        detalle.setPrecioUnitario(producto.getPrecio());
        detalle.setSubtotal(producto.getPrecio() * detalle.getCantidad());
        detalle.setVenta(venta);
        
        // ¡TRUCO CLAVE! Seteamos el producto completo al detalle 
        // para que el JSON de respuesta NO salga en null
        detalle.setProducto(producto); 
        
        totalAcumulado += detalle.getSubtotal();
    }

    venta.setTotal(totalAcumulado);
    return ventaRepository.save(venta);
}

    @Override
    @Transactional(readOnly = true)
    public List<Venta> listarTodas() {
        return ventaRepository.findAll();
    }
}