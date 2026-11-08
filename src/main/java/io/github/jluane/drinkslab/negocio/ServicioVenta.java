package io.github.jluane.drinkslab.negocio;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jluane.drinkslab.datos.RepositorioProducto;
import io.github.jluane.drinkslab.datos.RepositorioVenta;
import io.github.jluane.drinkslab.negocio.modelo.DetalleVenta;
import io.github.jluane.drinkslab.negocio.modelo.Producto;
import io.github.jluane.drinkslab.negocio.modelo.Venta;
import io.github.jluane.drinkslab.negocio.modelo.Venta.TipoPago;

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
    public Long obtenerSigId(){
        return repositorioVenta.count()+1;
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

            // Calculamos la comisión de la terminal
            if(venta.getTipoPago() == TipoPago.TARJETA){
                BigDecimal porcentaje = new BigDecimal("0.0299");
                BigDecimal IVA = new BigDecimal("0.16");
                BigDecimal comisionCompleta = ((venta.getTotal().multiply(porcentaje)).add(BigDecimal.ONE)).setScale(2, RoundingMode.UP); // Redondeamos a dos cifras
                BigDecimal IVAComision = (comisionCompleta.multiply(IVA)).setScale(2, RoundingMode.UP);
                BigDecimal descuento = comisionCompleta.add(IVAComision);
                BigDecimal nuevoTotal = venta.getTotal().subtract(descuento);
                venta.setTotal(nuevoTotal);
            }


            repositorioVenta.save(venta); // Gracias a la configuación en las entidades, se guardan los detalles y la venta automáticamente
            return true;
        }catch(Exception e){
            System.err.println("Ocurrió un error al guardar la venta");
            e.printStackTrace();
            return false;
        }
    }

    public List<Venta> obtenerPorFecha(LocalDate fecha){
        LocalDateTime inicioDia = fecha.atStartOfDay();
        LocalDateTime finalDia = fecha.atTime(LocalTime.MAX);

        return repositorioVenta.findByFechaBetween(inicioDia, finalDia);
    }
}
