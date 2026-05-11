package cl.techstore.api.service;

import java.util.List;

import cl.techstore.api.model.Producto;

public interface IProductoService {
    // Listados de estado
    List<Producto> obtenerActivos();
    List<Producto> obtenerInactivos(); // Para administración
    
    // Persistencia y actualización
    Producto guardarProducto(Producto producto);
    Producto actualizarProducto(Long id, Producto producto);
    
    // Gestión de estado (Borrado lógico y reactivación)
    void eliminarLogico(Long id);
    void reactivarProducto(Long id); // <--- Nuevo método para volver a activar
    
    // Búsqueda
    List<Producto> filtrarCategoria(String nombre);
}