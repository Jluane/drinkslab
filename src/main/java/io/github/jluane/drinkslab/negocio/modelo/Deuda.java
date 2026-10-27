package io.github.jluane.drinkslab.negocio.modelo;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Deuda {
    // Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idDeuda;

    // Atributos
    private BigDecimal cantPagada;
    private BigDecimal cantFaltante;

    // Relaciones
    // Venta
    @OneToOne
    @JoinColumn(name = "idVenta")
    private Venta venta;
    // Cliente
    @ManyToOne
    @JoinColumn(name = "idCliente")
    private Cliente cliente;

    // Constructores
    public Deuda(){}
    public Deuda(BigDecimal cantPagada, BigDecimal cantFaltante, Venta venta, Cliente cliente){
        this.cantPagada = cantPagada;
        this.cantFaltante = cantFaltante;
        this.venta = venta;
        this.cliente = cliente;
    }

    // Setters
    public void setCantPagada(BigDecimal cant){
        this.cantPagada = cant;
    }
    public void setCantFaltante(BigDecimal cant){
        this.cantFaltante = cant;
    }
    public void setVenta(Venta venta){
        this.venta = venta;
    }
    public void setCliente(Cliente cliente){
        this.cliente = cliente;
    }

    // Getters
    public BigDecimal getCantPagada(){
        return cantPagada;
    }
    public BigDecimal getCantFaltante(){
        return cantFaltante;
    }
    public Venta getVenta(){
        return venta;
    }
    public Cliente getCliente(){
        return cliente;
    }

}
