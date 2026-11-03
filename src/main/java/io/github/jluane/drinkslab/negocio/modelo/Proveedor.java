package io.github.jluane.drinkslab.negocio.modelo;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

@Entity 
public class Proveedor {
    // id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProveedor;

    // Atributos
    private String nombre;

    // Relaciones
    @ManyToMany
    @JoinTable(
        name = "proveedor_producto", // Nombre de la tabla intermedia en la BD
        joinColumns = @JoinColumn(name = "id_proveedor"), // Llave que apunta a esta clase (Proveedor)
        inverseJoinColumns = @JoinColumn(name = "id_producto") // Llave que apunta a la otra clase (Producto)
    )
    private List<Producto> productos;

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
