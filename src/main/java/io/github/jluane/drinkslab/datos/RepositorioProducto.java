package io.github.jluane.drinkslab.datos;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import io.github.jluane.drinkslab.negocio.modelo.Producto;

public interface RepositorioProducto extends CrudRepository <Producto, Long> {
    
    /**
     * Método que busca en la base de datos al producto que tenga el nombre dado
    */
    Producto findByGTIN(String GTIN);

    /**
     * Método que busca en la base de datos al producto que tenga el nombre dado
     */
    Producto findByNombre(String nombre);

    /**
     * Mëtodo para encontrar una página de los datos en el repositorios
     */
    Page<Producto> findAll(Pageable pageable);

    /**
     * Método para encontrar a máximo 5 productos cuyo nombre inicie con el dado
     * @param nombreParcial Es la parte del nombre que queremos que coincida
     * @return Regresa la lista con 5 coincidencias
     */
    List<Producto> findTop5ByNombreContainingIgnoreCase(String nombreParcial);
}
