package io.github.jluane.drinkslab.negocio.modelo;

import java.math.BigDecimal;

import jakarta.persistence.Column;
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
    private BigDecimal precioUnitario;
    private BigDecimal cantInv;
    @Column(unique = true, nullable = false)
    private String GTIN;
    
    // Tipos de productos
    private boolean aGranel;
    private BigDecimal cantPromocion;
    private BigDecimal precioPromocion;
    private BigDecimal cantPaquete;
    private BigDecimal precioPaquete;

    // Relaciones
    @ManyToOne
    @JoinColumn(name = "idProveedor")
    private Proveedor proveedor;

    // Constructor
    public Producto(){}
    /**
     * Constructor para productos comunes
     */
    public Producto(String nombre, BigDecimal precioUnitario, Proveedor proveedor, String GTIN, boolean granel){
        this.nombre = nombre;
        this.precioUnitario = precioUnitario;
        this.cantInv = BigDecimal.ZERO;
        this.proveedor = proveedor;
        this.GTIN = GTIN;
        this.aGranel = granel;
        cantPromocion = null;
        precioPromocion = null;
        cantPaquete = null;
        precioPaquete = null;
    }

    // Setters
    public void setIdProducto(Long id){
        this.idProducto = id;
    }
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    public void setPrecioUnitario(BigDecimal precio){
        this.precioUnitario = precio;
    }
    public void setCantInv(BigDecimal cantInv){
        this.cantInv = cantInv;
    }
    public void setProveedor(Proveedor proveedor){
        this.proveedor = proveedor;
    }
    public void setGTIN(String GTIN){
        this.GTIN = GTIN;
    }
    public void setAGranel(boolean granel){
        this.aGranel = granel;
    }
    public void setCantPromocion(BigDecimal cant){
        this.cantPromocion = cant;
    }
    public void setPrecioPromocion(BigDecimal precio){
        this.precioPromocion = precio;
    }
    public void setCantPaquete(BigDecimal cant){
        this.cantPaquete = cant;
    }
    public void setPrecioPaquete(BigDecimal precio){
        this.precioPaquete = precio;
    }

    // Getters
    public Long getIdProducto(){
        return idProducto;
    }
    public String getNombre(){
        return nombre;
    }
    public BigDecimal getPrecioUnitario(){
        return precioUnitario;
    }
    public BigDecimal getCantInv(){
        return cantInv;
    }
    public Proveedor getProveedor(){
        return proveedor;
    }
    public String getGTIN(){
        return GTIN;
    }
    public boolean getAGranel(){
        return aGranel;
    }
    public BigDecimal getCantPromocion(){
        return cantPromocion;
    }
    public BigDecimal getPrecioPromocion(){
        return precioPromocion;
    }
    public BigDecimal getCantPaquete(){
        return cantPaquete;
    }
    public BigDecimal getPrecioPaquete(){
        return precioPaquete;
    }

    // toString
    @Override
    public String toString(){
        String cadena = "Producto: "+nombre+" de "+proveedor+" $"+precioUnitario+". "+cantInv+" unidades";
        return cadena;
    }

    // equals
    @Override
    public boolean equals(Object objeto){
        if(this == objeto)
            return true;
        if(!(objeto instanceof Producto producto))
            return false;
        else
            return producto.getGTIN().equals(this.GTIN);
    }
}
