package io.github.jluane.drinkslab.datos;

import org.springframework.data.repository.CrudRepository;

import io.github.jluane.drinkslab.negocio.modelo.Producto;

public interface RepositorioProducto extends CrudRepository <Producto, Long> {
    
}
