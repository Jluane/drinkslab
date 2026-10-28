package io.github.jluane.drinkslab.negocio;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.jluane.drinkslab.negocio.modelo.DetalleVenta;
import io.github.jluane.drinkslab.negocio.modelo.Producto;

@Service
public class ServicioDetalleVenta {
    
    @Autowired
    public ServicioDetalleVenta(){

    }

    /**
     * Crea un DetalleVenta con un Producto para agregarlo a una lista y regresa al detalle ya creado
     * @param detalles Es la lista a la que se agregará el nuevo detalle
     * @param producto Es el producto con el que se creará el detalle
     * @return Regresa al detalle nuevo o null si no se logró crear
     */
    public DetalleVenta agregarProductoNuevo(List<DetalleVenta> detalles, Producto producto){
        if(producto  == null || detalles == null) return null;
        try {
            DetalleVenta detalle = new DetalleVenta(producto);
            detalles.add(detalle);
            return detalle;
        } catch (Exception e) {
            System.err.println("Error en servicio: "+e);
            return null;
        }
    }

    /**
     * Incrementa el atributo cantidad en DetalleVenta en 1 y regresa la nueva cantidad de detalle
     * @param detalle Es el detalle a actualizar
     * @return Regresa el nuevo valor de cantidad del DetalleVenta
     */
    public int incrementarProducto(DetalleVenta detalle){
        if(detalle == null) return 0;
        if(!cambiarCantidad(detalle, 1)) return 0;
        return detalle.getCantidad();
    }

    /**
     * Disminuye el atributo cantidad en DetalleVenta en 1 y regresa la nueva cantidad de detalle
     * @param detalle Es el detalle a actualizar
     * @return Regresa el nuevo valor de cantidad del DetalleVenta
     */
    public int disminuirProducto(DetalleVenta detalle){
        if(detalle == null) return 0;
        if(!cambiarCantidad(detalle, -1)) return 0;
        return detalle.getCantidad();
    }

    /**
     * Aumenta al atributo cantidad de un DetalleVenta en la N
     * @param detalle Es el detalle al que se actualizará
     * @param N Es la cantidad a la que se sumará la cantidad actual de detalle
     * @return Regresa true si se logró modificar a detalle, false si no se logró
     */
    private boolean cambiarCantidad(DetalleVenta detalle, int N){
        int cantActual = detalle.getCantidad();
        BigDecimal costoIndividual = detalle.getProducto().getPrecio();

        int cantNueva = cantActual + N;
        
        if(cantNueva <= 0)
            return false;
        
        BigDecimal costoTotal = costoIndividual.multiply(BigDecimal.valueOf(cantNueva));
    
        detalle.setCantidad(cantNueva);
        detalle.setCosto(costoTotal);
        return true;
    }
    public int editarCantidad(DetalleVenta detalle, int N){
        if(N <= 0) return 0;
        BigDecimal costoIndividual = detalle.getProducto().getPrecio();
        BigDecimal costoTotal = costoIndividual.multiply(BigDecimal.valueOf(N));

        detalle.setCantidad(N);
        detalle.setCosto(costoTotal);
        return N;
    }

    /**
     * Revisa una lista de DetalleVenta en busca de un DetalleVenta que contenga a producto
     * @param detalles Es la lista de detalles sobre la que se buscará al detalle indicado
     * @param producto Es el producto que debe contener el detalle que buscamos
     * @return Regresa al índice del detalle que contenga al producto, o null si no existe
     */
    public Integer existeEnListaPorProducto(List<DetalleVenta> detalles, Producto producto){
        if(producto == null) return null;
        if(detalles.size() <= 0) return null;
        int i = 0;
        for(DetalleVenta detalle : detalles){
            if(detalle.getProducto().equals(producto))
                return i;
            i++;
        }
        return null;
    }

}
