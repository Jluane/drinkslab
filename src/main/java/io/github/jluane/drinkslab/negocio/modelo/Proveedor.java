package io.github.jluane.drinkslab.negocio.modelo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity 
public class Proveedor {
    // id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProveedor;

    // Atributos
    private String nombre;

    // Constructores
    public Proveedor(){}
    public Proveedor(String nombre){
        this.nombre = nombre;
    }

    // Setters
    public void setIdProveedor(Long id){
        this.idProveedor = id;
    }
    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    // Getters
    public Long getIdProveedor(){
        return idProveedor;
    }
    public String getNombre(){
        return nombre;
    }

    @Override
    public String toString(){
        return nombre;
    }
}
