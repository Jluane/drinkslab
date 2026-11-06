package io.github.jluane.drinkslab.datos;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import io.github.jluane.drinkslab.negocio.modelo.Producto;
import io.github.jluane.drinkslab.negocio.modelo.Proveedor;
import io.github.jluane.drinkslab.negocio.modelo.RelacionProductoProveedor;

public interface RepositorioRelacionProductoProveedor extends CrudRepository<RelacionProductoProveedor, Long> {
    List<RelacionProductoProveedor> findByProveedor(Proveedor proveedor);
    List<RelacionProductoProveedor> findByProducto(Producto producto);
}
