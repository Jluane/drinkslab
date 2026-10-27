package io.github.jluane.drinkslab.negocio.modelo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.GenerationType;

@Entity 
public class Gasto {
    // Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idGasto;

    // Atributos
    private BigDecimal costo;
    private String motivo;
    
    // Relaciones
    @ManyToOne
    @JoinColumn(name = "idProveedor")
    private Proveedor proveedor;
    @OneToMany(mappedBy = "gasto", targetEntity = DetalleGasto.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleGasto> detalles = new ArrayList<>();

    // Constructor
    public Gasto(){}
    public Gasto(BigDecimal costo, String motivo){
        this.costo = costo;
        this.motivo = motivo;
    }

    // Setters
    public void setIdGasto(Long id){
        this.idGasto = id;
    }
    public void setCosto(BigDecimal costo){
        this.costo = costo;
    }
    public void setMotivo(String motivo){
        this.motivo = motivo;
    }

    // Getters
    public Long getIdGasto(){
        return idGasto;
    }
    public BigDecimal getCosto(){
        return costo;
    }
    public String getMotivo(){
        return motivo;
    }

    // toString
    @Override
    public String toString(){
        String cadena = "Gasto de $"+costo+" por: "+motivo;
        return cadena;
    }
}
