package cl.techstore.api.service;

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
    @Transactional // CRÍTICO: Si algo falla, se hace rollback de todo
    public Venta registrarVenta(Venta venta) {
        Double totalVenta = 0.0;

        for (DetalleVenta detalle : venta.getDetalles()) {
            // 1. Obtener el producto gestionado por la DB
            Producto producto = productoRepository.findById(detalle.getProducto().getId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado: " + detalle.getProducto().getId()));

            // 2. Validar Stock
            if (producto.getStock() < detalle.getCantidad()) {
                throw new RuntimeException("Stock insuficiente para: " + producto.getNombre());
            }

            // 3. Descontar Stock
            producto.setStock(producto.getStock() - detalle.getCantidad());
            productoRepository.save(producto);

            // 4. Calcular subtotales para el detalle
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setSubtotal(producto.getPrecio() * detalle.getCantidad());
            detalle.setVenta(venta);
            
            totalVenta += detalle.getSubtotal();
        }

        venta.setTotal(totalVenta);
        return ventaRepository.save(venta);
    }
}