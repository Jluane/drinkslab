package io.github.jluane.drinkslab.negocio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jluane.drinkslab.datos.RepositorioProducto;
import io.github.jluane.drinkslab.negocio.modelo.Producto;

@Service
public class ServicioProducto {
    // Repositorios a los que se conecta
    private final RepositorioProducto repositorioProducto;

    @Autowired
    public ServicioProducto(RepositorioProducto repositorioProducto){
        this.repositorioProducto = repositorioProducto;
    }

    /**
     * Va a repositorio para encontrar al producto que tenga al GTIN indicado
     * @param gtin Es la cadena que queremos que tenga en GTIN
     * @return Regresa al producto encontrado o null si no existen ningún objeto o si GTIN no tiene el formato esperado
     */
    @Transactional(readOnly = true)
    public Producto recuperaProducto(String gtin){
        if(gtin.matches("\\d+"))
            return repositorioProducto.findByGTIN(gtin);
        else
            return null;
    }

}
