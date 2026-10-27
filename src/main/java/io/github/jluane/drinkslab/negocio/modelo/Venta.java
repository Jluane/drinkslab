package io.github.jluane.drinkslab.negocio.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.GenerationType;

@Entity
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idVenta;

    // Enum
    public enum TipoPago{
        EFECTIVO,
        TARJETA,
        TRANSFERENCIA
    }

    // Atributos
    private BigDecimal total;
    private LocalDateTime fecha;
    @Enumerated(EnumType.STRING)
    private TipoPago tipoPago;

    // Relaciones
    // DetallesVenta
    @OneToMany(mappedBy = "venta", targetEntity = DetalleVenta.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    List<DetalleVenta> detalles = new ArrayList<>();
    // Deuda
    @OneToOne(mappedBy = "venta", targetEntity = Deuda.class, cascade = CascadeType.ALL, orphanRemoval = true)
    private Deuda deuda;

    // Constructores
    public Venta(){}
    public Venta(BigDecimal total, LocalDateTime fecha, TipoPago tipo){
        this.total = total;
        this.fecha = fecha;
        this.tipoPago = tipo;
    }

    // Setters
    public void setIdVenta(Long id){
        this.idVenta = id;
    }
    public void setTotal(BigDecimal total){
        this.total = total;
    }
    public void setFecha(LocalDateTime fecha){
        this.fecha = fecha;
    }
    public void setTipoPago(TipoPago tipo){
        this.tipoPago = tipo;
    }
    public void setDetalles(List<DetalleVenta> detalles){
        this.detalles = detalles;
    }
    public void setDeuda(Deuda deuda){
        this.deuda = deuda;
    }

    // Getters
    public Long getIdVenta(){
        return idVenta;
    }
    public BigDecimal getTotal(){
        return total;
    }
    public LocalDateTime getFecha(){
        return fecha;
    }
    public TipoPago getTipoPago(){
        return tipoPago;
    }
    public List<DetalleVenta> getDetalles(){
        return detalles;
    }
    public Deuda getDeuda(){
        return deuda;
    }

    // toString
    @Override
    public String toString(){
        String cadena = "Venta #"+idVenta+" - "+fecha+" - $"+total+" pagada en "+tipoPago;
        return cadena;
    }

}
