package io.github.jluane.drinkslab.negocio.modelo;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class DetalleGasto {
    // Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalleGasto;
    
    // Atributos
    private BigDecimal costo;
    private int cantidad;

    // Relaciones
    // Gasto
    @ManyToOne
    @JoinColumn(name = "idGasto")
    private Gasto gasto;
    // Producto
    @ManyToOne
    @JoinColumn(name = "idProducto")
    private Producto producto;

    // Constructores
    public DetalleGasto(){}
    public DetalleGasto(BigDecimal costo, int cantidad){
        this.costo = costo;
        this.cantidad = cantidad;
    }

    // Setters
    public void setCosto(BigDecimal costo){
        this.costo = costo;
    }
    public void setCantidad(int cant){
        this.cantidad = cant;
    }

    // Getters
    public BigDecimal getCosto(){
        return costo;
    }
    public int getCantidad(){
        return cantidad;
    }
    public Long getIdDetalleGasto(){
        return idDetalleGasto;
    }

    @Override
    public boolean equals(Object object){
        if(object == null) return false;
        if(this == object) return true;
        if(object instanceof DetalleGasto detalle)
            if(this.idDetalleGasto != null & detalle.getIdDetalleGasto() != null)
                if(this.idDetalleGasto == detalle.getIdDetalleGasto())
                    return true;
        return false;
    }
}
