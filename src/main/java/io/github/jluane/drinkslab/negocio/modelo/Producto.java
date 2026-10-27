package io.github.jluane.drinkslab.negocio.modelo;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity 
public class Producto {
    // Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProducto;

    // Atributos
    private String nombre;
    private BigDecimal precio;
    private int cantInv;
    private String GTIN;

    // Relaciones
    @ManyToOne
    @JoinColumn(name = "idProveedor")
    private Proveedor proveedor;

    // Constructor
    public Producto(){}
    public Producto(String nombre,  BigDecimal precio, int cantInv, Proveedor proveedor, String GTIN){
        this.nombre = nombre;
        this.precio = precio;
        this.cantInv = cantInv;
        this.proveedor = proveedor;
        this.GTIN = GTIN;
    }
    public Producto(String nombre, BigDecimal precio, Proveedor proveedor, String GTIN){
        this.nombre = nombre;
        this.precio = precio;
        this.cantInv = 0;
        this.proveedor = proveedor;
        this.GTIN = GTIN;
    }

    // Setters
    public void setIdProducto(Long id){
        this.idProducto = id;
    }
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    public void setPrecio(BigDecimal precio){
        this.precio = precio;
    }
    public void setCantInv(int cantInv){
        this.cantInv = cantInv;
    }
    public void setProveedor(Proveedor proveedor){
        this.proveedor = proveedor;
    }
    public void setGTIN(String GTIN){
        this.GTIN = GTIN;
    }

    // Getters
    public Long getIdProducto(){
        return idProducto;
    }
    public String getNombre(){
        return nombre;
    }
    public BigDecimal getPrecio(){
        return precio;
    }
    public int getCantInv(){
        return cantInv;
    }
    public Proveedor getProveedor(){
        return proveedor;
    }
    public String getGTIN(){
        return GTIN;
    }

    // toString
    @Override
    public String toString(){
        String cadena = "Producto: "+nombre+" de "+proveedor+" $"+precio+". "+cantInv+" unidades";
        return cadena;
    }
}
