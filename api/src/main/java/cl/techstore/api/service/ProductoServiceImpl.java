package cl.techstore.api.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.techstore.api.dto.AuditMessage;
import cl.techstore.api.model.Producto;
import cl.techstore.api.repository.ProductoRepository;

@Service
public class ProductoServiceImpl implements IProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private SqsProducerService sqsProducerService;

    @Override
    @Transactional(readOnly = true)
    public List<Producto> obtenerActivos() {
        return productoRepository.findByActivoTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> obtenerInactivos() {
        return productoRepository.findByActivoFalse();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> filtrarCategoria(String categoria) {
        return productoRepository.findByCategoriaAndActivoTrue(categoria);
    }

    @Override
    @Transactional
    public Producto guardarProducto(Producto producto) {
        // Aseguramos que el producto nazca activo si no se especifica
        if (producto.getActivo() == null) {
            producto.setActivo(true);
        }
        
        // 1. Guardamos el producto en la base de datos PostgreSQL local
        Producto productoGuardado = productoRepository.save(producto);
        
        // 2. Construimos de manera limpia el DTO con las variables correspondientes
        AuditMessage auditMessage = new AuditMessage(
            "PRODUCT_CREATED",
            productoGuardado.getId(),
            productoGuardado.getNombre(),
            productoGuardado.getPrecio(),
            LocalDateTime.now().toString()
        );
        
        // 3. Enviamos el mensaje a la cola de AWS SQS de fondo sin retrasar la respuesta del cliente
        sqsProducerService.sendAuditMessage(auditMessage);
        
        return productoGuardado;
    }

    @Override
    @Transactional
    public Producto actualizarProducto(Long id, Producto productoDetalles) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
        
        producto.setNombre(productoDetalles.getNombre());
        producto.setDescripcion(productoDetalles.getDescripcion());
        producto.setPrecio(productoDetalles.getPrecio());
        producto.setStock(productoDetalles.getStock());
        producto.setCategoria(productoDetalles.getCategoria());
        // No tocamos el estado 'activo' aquí para evitar reactivaciones accidentales vía PUT
        
        return productoRepository.save(producto);
    }

    @Override
    @Transactional
    public void eliminarLogico(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede eliminar: Producto no encontrado con ID: " + id));
        
        producto.setActivo(false);
        productoRepository.save(producto);
    }

    @Override
    @Transactional
    public void reactivarProducto(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se puede reactivar: Producto no encontrado con ID: " + id));
        
        producto.setActivo(true);
        productoRepository.save(producto);
    }
}