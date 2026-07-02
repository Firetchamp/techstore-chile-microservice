package cl.techstore.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
    private IProductoService productoService;

    // 1. Listar solo productos activos (Tienda)
    @GetMapping
    public List<Producto> listarActivos() {
        return productoService.obtenerActivos();
    }

    // 2. NUEVO: Listar productos inactivos (Papelera/Admin)
    @GetMapping("/inactivos")
    public List<Producto> listarInactivos() {
        return productoService.obtenerInactivos();
    }

    // 3. Crear producto
    @PostMapping
    public ResponseEntity<Producto> guardar(@RequestBody Producto producto) {
        return ResponseEntity.ok(productoService.guardarProducto(producto));
    }

    // 4. Actualizar producto (Mantenemos la lógica de integridad de ID)
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizar(@PathVariable Long id, @RequestBody Producto producto) {
        return ResponseEntity.ok(productoService.actualizarProducto(id, producto));
    }

    // 5. Borrado Lógico (Desactivar)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        productoService.eliminarLogico(id);
        return ResponseEntity.ok().body("Producto desactivado con éxito.");
    }

    // 6. NUEVO: Reactivar producto (PATCH)
    @PatchMapping("/{id}/activar")
    public ResponseEntity<?> reactivar(@PathVariable Long id) {
        productoService.reactivarProducto(id);
        return ResponseEntity.ok().body("Producto reactivado con éxito.");
    }

    // 7. Buscar por categoría
    @GetMapping("/categoria/{nombre}")
    public List<Producto> buscarPorCategoria(@PathVariable String nombre) {
        return productoService.filtrarCategoria(nombre);
    }
}