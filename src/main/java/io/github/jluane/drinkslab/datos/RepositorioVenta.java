package io.github.jluane.drinkslab.datos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import io.github.jluane.drinkslab.negocio.modelo.Venta;

public interface RepositorioVenta extends CrudRepository <Venta, Long>{
    
    List<Venta> findAll();
}
