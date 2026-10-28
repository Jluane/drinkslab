package io.github.jluane.drinkslab.negocio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public Producto recuperaProducto(String gtin){
        if(gtin.matches("\\d+"))
            return repositorioProducto.findByGTIN(gtin);
        else
            return null;
    }

}
