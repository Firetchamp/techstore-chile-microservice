package cl.techstore.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.techstore.api.model.Producto;
import cl.techstore.api.service.IProductoService;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private IProductoService productoService; // Cambiamos Repository por Service

    // Listar solo productos activos
    @GetMapping
    public List<Producto> listarTodos() {
        // La lógica del stream/filter ahora vive dentro del Service
        return productoService.obtenerActivos();
    }

    // Crear producto asegurando que entre como activo
    @PostMapping
    public Producto guardar(@RequestBody Producto producto) {
        return productoService.guardarProducto(producto);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id, @RequestBody Producto producto) {
        // Aseguramos que el producto que guardamos tenga el ID que viene en la URL
        producto.setId(id);
        return ResponseEntity.ok(productoService.guardarProducto(producto));
    }

    // Borrado Lógico (Soft Delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        productoService.eliminarLogico(id);
        return ResponseEntity.ok().build();
    }

    // Buscar por categoría específica
    @GetMapping("/categoria/{nombre}")
    public List<Producto> buscarPorCategoria(@PathVariable String nombre) {
        return productoService.filtrarCategoria(nombre);
    }
}