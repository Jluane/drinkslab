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
    private int cantidad;
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
    public DetalleVenta(int cantidad, BigDecimal costo, Venta venta, Producto producto){
        this.cantidad = cantidad;
        this.costo = costo;
        this.venta = venta;
        this.producto = producto;
    }

    // Setters
    public void setCantidad(int cantidad){
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
    public int getCantidad(){
        return cantidad;
    }
    public BigDecimal getCosto(){
        return costo;
    }
    public Venta getVenta(){
        return venta;
    }
    public Producto geProducto(){
        return producto;
    }
}
