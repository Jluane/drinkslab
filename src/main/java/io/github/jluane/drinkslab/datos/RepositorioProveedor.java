package io.github.jluane.drinkslab.datos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import io.github.jluane.drinkslab.negocio.modelo.Proveedor;

public interface RepositorioProveedor extends CrudRepository <Proveedor, Long> {
    
    /**
     * Regresa a todos los proveedores en repositorio
     */
    List<Proveedor> findAll();

    Proveedor findByNombre(String nombre);
}
