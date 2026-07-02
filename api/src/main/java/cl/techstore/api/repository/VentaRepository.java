package cl.techstore.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.techstore.api.model.Venta;

@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    
}