package io.github.jluane.drinkslab.negocio;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    /**
     * Obtiene al que será el id de la siguiente venta que se creará en repositorio
     * @return
     */
    @Transactional(readOnly = true)
    public int obtenerSigId(){
        return repositorioVenta.findAll().size();
    }

    /**
     * Actualiza los valores de venta según la lista de detalles que se le asignará y le da todos los detalles la venta que se está actualizando
     * @param venta Es la venta que se actualizará y la que se le dará a los detalles
     * @param detalles Es la lista de detalles que ayudarán a formar a venta y que serán asignados a la venta
     */
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
