package cl.techstore.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.techstore.api.model.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    
    // Listar solo los disponibles (Para la tienda)
    List<Producto> findByActivoTrue();
    
    // Listar solo los "borrados" (Para administración/reactivación)
    List<Producto> findByActivoFalse();
    
    // Búsqueda por categoría manteniendo el filtro de activos
    List<Producto> findByCategoriaAndActivoTrue(String categoria);
}