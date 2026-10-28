package io.github.jluane.drinkslab.datos;

import org.springframework.data.repository.CrudRepository;

import io.github.jluane.drinkslab.negocio.modelo.DetalleGasto;

public interface RepositorioDetalleGasto extends CrudRepository <DetalleGasto, Long> {
    
}
