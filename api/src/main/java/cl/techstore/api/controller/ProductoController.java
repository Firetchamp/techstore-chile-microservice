package cl.techstore.api.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.techstore.api.model.Producto;
import cl.techstore.api.repository.ProductoRepository;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    // Listar solo productos activos (Cumple con el estándar de filtrado)
    @GetMapping
    public List<Producto> listarTodos() {
        return productoRepository.findAll()
                .stream()
                .filter(p -> p.getActivo() != null && p.getActivo())
                .collect(Collectors.toList());
    }

    // Crear producto asegurando que entre como activo
    @PostMapping
    public Producto guardar(@RequestBody Producto producto) {
        producto.setActivo(true); // Usamos tu variable 'activo'
        return productoRepository.save(producto);
    }

    // Borrado Lógico (Soft Delete) cambiando estado de 'activo' a false
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        return productoRepository.findById(id).map(producto -> {
            producto.setActivo(false); // No borramos, desactivamos
            productoRepository.save(producto);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}