package io.github.jluane.drinkslab.datos;

import org.springframework.data.repository.CrudRepository;

import io.github.jluane.drinkslab.negocio.modelo.Cliente;

public interface RepositorioCliente extends CrudRepository <Cliente, Long> {
    
}
