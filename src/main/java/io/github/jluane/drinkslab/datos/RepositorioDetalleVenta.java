package io.github.jluane.drinkslab.datos;

import org.springframework.data.repository.CrudRepository;

import io.github.jluane.drinkslab.negocio.modelo.DetalleVenta;

public interface RepositorioDetalleVenta extends CrudRepository <DetalleVenta, Long> {
    
}
