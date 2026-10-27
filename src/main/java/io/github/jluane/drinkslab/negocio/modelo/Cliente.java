package io.github.jluane.drinkslab.negocio.modelo;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.GenerationType;

@Entity
public class Cliente {
    // Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCliente;

    // Atributos
    private String nombre;

    // Relaciones
    @OneToMany(mappedBy = "cliente", targetEntity = Deuda.class, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Deuda> deudas;

    // Constructores
    public Cliente(){}
    public Cliente(String nombre){
        this.nombre = nombre;
    }

    // Setters
    public void setIdCliente(Long id){
        this.idCliente = id;
    }
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    public void setDeudas(List<Deuda> deudas){
        this.deudas = deudas;
    }

    // Getters
    public Long getIdCliente(){
        return idCliente;
    }
    public String getNombre(){
        return nombre;
    }
    public List<Deuda> getDeudas(){
        return deudas;
    }

    @Override
    public String toString(){
        return nombre;
    }
}
