package io.github.jluane.drinkslab.datos;

import org.springframework.data.repository.CrudRepository;

import io.github.jluane.drinkslab.negocio.modelo.Deuda;

public interface RepositorioDeuda extends CrudRepository <Deuda, Long> {
    
}
