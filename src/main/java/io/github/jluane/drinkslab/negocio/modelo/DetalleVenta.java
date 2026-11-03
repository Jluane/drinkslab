package io.github.jluane.drinkslab.negocio.modelo;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity 
public class DetalleVenta {
    // Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDetalleVenta;

    // Atributos
    private BigDecimal cantidad;
    private BigDecimal costo;

    // Relaciones
    // Venta
    @ManyToOne
    @JoinColumn(name = "idVenta")
    private Venta venta;
    // Producto
    @ManyToOne
    @JoinColumn(name = "idProducto")
    private Producto producto;

    // Constructores
    public DetalleVenta(){}
    public DetalleVenta(BigDecimal cantidad, BigDecimal costo, Venta venta, Producto producto){
        this.cantidad = cantidad;
        this.costo = costo;
        this.venta = venta;
        this.producto = producto;
    }
    public DetalleVenta(Producto producto){
        this.cantidad = BigDecimal.ONE;
        this.costo = producto.getPrecioUnitario();
        this.producto = producto;
    }

    // Setters
    public void setCantidad(BigDecimal cantidad){
        this.cantidad = cantidad;
    }
    public void setCosto(BigDecimal costo){
        this.costo = costo;
    }
    public void setVenta(Venta venta){
        this.venta = venta;
    }
    public void setProducto(Producto producto){
        this.producto = producto;
    }

    // Getters
    public BigDecimal getCantidad(){
        return cantidad;
    }
    public BigDecimal getCosto(){
        return costo;
    }
    public Venta getVenta(){
        return venta;
    }
    public Producto getProducto(){
        return producto;
    }
    public Long getIdDetalleVenta(){
        return idDetalleVenta;
    }

    // toString
    @Override
    public String toString(){
        String cadena = producto.getNombre() + " x "+cantidad+" = "+costo+"\n";
        return cadena;
    }

    // equals
    @Override
    public boolean equals(Object object){
        if(object==null) return false;
        if(!(object instanceof DetalleVenta detalle))
            return false;
        else{
            if(this.idDetalleVenta != null && detalle.getIdDetalleVenta() != null)
                if(this.idDetalleVenta == detalle.getIdDetalleVenta())
                    return true;
            if(!(this.producto.equals(detalle.getProducto())))
                return false;
            if(!(this.cantidad == detalle.getCantidad()))
                return false;
        }
        return true;
    }
}
