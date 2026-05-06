package cl.techstore.api.service;

import java.util.List;

import cl.techstore.api.model.Venta;

public interface IVentaService {
    Venta registrarVenta(Venta venta);
    List<Venta> listarTodas();
}