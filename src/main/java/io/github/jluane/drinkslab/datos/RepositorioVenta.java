package io.github.jluane.drinkslab.datos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import io.github.jluane.drinkslab.negocio.modelo.Venta;

public interface RepositorioVenta extends CrudRepository <Venta, Long>{
    
    // Método que regresa a todas las ventas guardadas en repositorio
    List<Venta> findAll();
}
