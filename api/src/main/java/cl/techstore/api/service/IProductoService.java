package cl.techstore.api.service;

import java.util.List;

import cl.techstore.api.model.Producto;

public interface IProductoService {
    // Estos nombres DEBEN ser iguales a los que usa el controlador
    List<Producto> obtenerActivos(); 
    List<Producto> filtrarCategoria(String categoria);
    Producto guardarProducto(Producto producto);
    void eliminarLogico(Long id);
}