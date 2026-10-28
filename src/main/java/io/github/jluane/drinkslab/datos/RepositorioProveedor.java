package io.github.jluane.drinkslab.datos;

import org.springframework.data.repository.CrudRepository;

import io.github.jluane.drinkslab.negocio.modelo.Proveedor;

public interface RepositorioProveedor extends CrudRepository <Proveedor, Long> {
    
}
