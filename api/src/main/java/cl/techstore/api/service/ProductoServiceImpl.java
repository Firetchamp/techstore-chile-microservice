package cl.techstore.api.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cl.techstore.api.model.Producto;
import cl.techstore.api.repository.ProductoRepository;

@Service
public class ProductoServiceImpl implements IProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public List<Producto> obtenerActivos() {
        return productoRepository.findByActivoTrue();
    }

    @Override
    public List<Producto> filtrarCategoria(String categoria) {
        return productoRepository.findByCategoriaAndActivoTrue(categoria);
    }

    @Override
    public Producto guardarProducto(Producto producto) {
        if (producto.getActivo() == null) {
            producto.setActivo(true);
        }
        return productoRepository.save(producto);
    }

    @Override
    public void eliminarLogico(Long id) {
        productoRepository.findById(id).ifPresent(p -> {
            p.setActivo(false);
            productoRepository.save(p);
        });
    }
}