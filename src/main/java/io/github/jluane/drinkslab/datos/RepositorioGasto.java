package io.github.jluane.drinkslab.datos;

import org.springframework.data.repository.CrudRepository;

import io.github.jluane.drinkslab.negocio.modelo.Gasto;

public interface RepositorioGasto extends CrudRepository <Gasto, Long> {
    
}
