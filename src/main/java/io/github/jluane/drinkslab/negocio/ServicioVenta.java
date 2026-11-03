package io.github.jluane.drinkslab.negocio;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jluane.drinkslab.datos.RepositorioProducto;
import io.github.jluane.drinkslab.datos.RepositorioVenta;
import io.github.jluane.drinkslab.negocio.modelo.DetalleVenta;
import io.github.jluane.drinkslab.negocio.modelo.Producto;
import io.github.jluane.drinkslab.negocio.modelo.Venta;

@Service
public class ServicioVenta {
    // Repositorios a los que se conecta
    private final RepositorioVenta repositorioVenta;
    private final RepositorioProducto repositorioProducto;

    @Autowired
    public ServicioVenta(RepositorioVenta repositorioVenta, RepositorioProducto repositorioProducto){
        this.repositorioVenta = repositorioVenta;
        this.repositorioProducto = repositorioProducto;
    }

    /**
     * Obtiene al que será el id de la siguiente venta que se creará en repositorio
     * @return
     */
    @Transactional(readOnly = true)
    public int obtenerSigId(){
        return repositorioVenta.findAll().size()+1;
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

    /**
     * Función encargada de guardar una venta en la base de datos. La venta ya debe tener a sus detalles
     * @param venta Es la venta que se guardará en la base de datos
     * @param tipo Es el tipo de pago que se aplica en la venta
     * @return Regresa true si se logró realizar el guardado, false si ocurrió algún error
     */
    @Transactional
    public boolean guardarNuevaVenta(Venta venta){
        venta.setDeuda(null);
        venta.setFecha(LocalDateTime.now());

        try{
            // Se afecta al inventario
            for(DetalleVenta det : venta.getDetalles()){
                Producto prod = det.getProducto();
                BigDecimal cantComprada = det.getCantidad();
                BigDecimal cantProducto = prod.getCantInv();
                BigDecimal diferencia = cantProducto.subtract(cantComprada);

                if(diferencia.compareTo(BigDecimal.ZERO)<0)
                    prod.setCantInv(BigDecimal.ONE.negate());
                else
                    prod.setCantInv(diferencia);
                repositorioProducto.save(prod);
            }

            repositorioVenta.save(venta); // Gracias a la configuación en las entidades, se guardan los detalles y la venta automáticamente
            System.out.println("Se guadó la venta satisfactoriamente");
            return true;
        }catch(Exception e){
            System.err.println("Ocurrió un error al guardar la venta");
            e.printStackTrace();
            return false;
        }
    }
}
