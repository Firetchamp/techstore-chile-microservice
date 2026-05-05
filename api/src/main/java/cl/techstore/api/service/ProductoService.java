package cl.techstore.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.techstore.api.model.Producto;
import cl.techstore.api.repository.ProductoRepository;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto> listarTodos() {
        return productoRepository.findAll();
    }

    public List<Producto> listarActivos() {
        return productoRepository.findByActivoTrue();
    }

    public Producto guardar(Producto producto) {
        return productoRepository.save(producto);
    }

    public Producto buscarPorId(Long id) {
        return productoRepository.findById(id).orElse(null);
    }

    // IE5: Implementación de eliminación lógica
    public void eliminarLogico(Long id) {
        Producto producto = buscarPorId(id);
        if (producto != null) {
            producto.setActivo(false); // Cambiamos el estado en lugar de borrar el registro
            productoRepository.save(producto);
        }
    }
}