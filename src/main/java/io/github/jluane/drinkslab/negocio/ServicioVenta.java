package io.github.jluane.drinkslab.negocio;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.jluane.drinkslab.datos.RepositorioVenta;
import io.github.jluane.drinkslab.negocio.modelo.DetalleVenta;
import io.github.jluane.drinkslab.negocio.modelo.Venta;

@Service
public class ServicioVenta {
    // Repositorios a los que se conecta
    private final RepositorioVenta repositorioVenta;


    @Autowired
    public ServicioVenta(RepositorioVenta repositorioVenta){
        this.repositorioVenta = repositorioVenta;
    }

    public int obtenerSigId(){
        return repositorioVenta.findAll().size();
    }

    public void actualizarVenta(Venta venta, List<DetalleVenta> detalles){
        BigDecimal total = BigDecimal.ZERO;
        for(DetalleVenta detalle : detalles){
            total = total.add(detalle.getCosto());
            detalle.setVenta(venta);
        }
        venta.setDetalles(detalles);
        venta.setTotal(total);
    }
}
