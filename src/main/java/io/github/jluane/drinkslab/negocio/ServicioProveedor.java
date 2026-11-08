package io.github.jluane.drinkslab.negocio;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jluane.drinkslab.datos.RepositorioProveedor;
import io.github.jluane.drinkslab.negocio.modelo.Proveedor;

@Service
public class ServicioProveedor {
    // Repositorio al que se conecta
    private final RepositorioProveedor repositorioProveedor;

    @Autowired
    public ServicioProveedor(RepositorioProveedor repositorioProveedor){
        this.repositorioProveedor = repositorioProveedor;
    }

    /**
     * Regresa a todos los proveedores en repositorio
     * @return Regresa un List de proveedor
     */
    @Transactional(readOnly = true)
    public List<Proveedor> obtenerProveedores(){
        return repositorioProveedor.findAll();
    }

    /**
     * Regresa a un proveedor con el nombre dado, si existe en repositorio lo saca de ahí, si no, crea uno nuevo sin guardarlo en repositorio
     * @param nombre Es el nombre del proveedor buscado
     * @return Regresa al proveedor con el nombre dado
     */
    @Transactional(readOnly = true)
    public Proveedor obtenerPorNombre(String nombre){
        // Sólo regresa null si el nombre es inválido
        if(nombre == null || nombre.isBlank()) return null;

        // Arreglamos al nombre
        nombre = nombre.trim(); // Quitamos espacios de más
        nombre = nombre.substring(0,1).toUpperCase() + nombre.substring(1).toLowerCase(); // Damos a la primera mayúscula
        // Verificamos si existe o no un proveedor con el nombre
        Proveedor proveedor = repositorioProveedor.findByNombre(nombre);
        if(proveedor == null){
            proveedor = new Proveedor(nombre);
        }
        return proveedor;
    }
}
