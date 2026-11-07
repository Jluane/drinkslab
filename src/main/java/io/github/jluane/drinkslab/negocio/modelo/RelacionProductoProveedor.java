package io.github.jluane.drinkslab.negocio.modelo;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class RelacionProductoProveedor {
    // Id obligatorio
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRelacion;

    @ManyToOne
    @JoinColumn(name="idProducto")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Producto producto;

    @ManyToOne
    @JoinColumn(name="idProveedor")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Proveedor proveedor;

    public RelacionProductoProveedor(){}

    public RelacionProductoProveedor(Producto producto, Proveedor proveedor){
        this.producto = producto;
        this.proveedor = proveedor;
    }

    public void setProducto(Producto producto){
        this.producto = producto;
    }
    public void setProveedor(Proveedor proveedor){
        this.proveedor = proveedor;
    }

    public Producto getProducto(){
        return producto;
    }
    public Proveedor getProveedor(){
        return proveedor;
    }

    @Override
    public boolean equals(Object object){
        if(object == null) return false;
        if(object instanceof RelacionProductoProveedor relacion){
            if(this.producto.equals(relacion.getProducto()) && this.proveedor.equals(relacion.getProveedor())){
                return true;
            }
        }
        return false;
    }
}
