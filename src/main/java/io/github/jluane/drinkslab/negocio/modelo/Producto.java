package io.github.jluane.drinkslab.negocio.modelo;

import java.math.BigDecimal;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

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
    @ManyToMany(mappedBy = "productos")
    private List<Proveedor> proveedores;

    // Constructor
    public Producto(){}
    /**
     * Constructor para productos comunes
     */
    public Producto(String nombre, BigDecimal precioUnitario, String GTIN, boolean granel){
        this.nombre = nombre;
        this.precioUnitario = precioUnitario;
        this.cantInv = BigDecimal.ZERO;
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
    public void setProveedores(List<Proveedor> proveedores){
        this.proveedores = proveedores;
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
    public List<Proveedor> getProveedores(){
        return proveedores;
    }

    // toString
    @Override
    public String toString(){
        String cadena = "Producto: "+nombre+", $"+precioUnitario+". "+cantInv+" unidades";
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
