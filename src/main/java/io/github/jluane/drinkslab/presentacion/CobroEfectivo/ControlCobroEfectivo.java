package io.github.jluane.drinkslab.presentacion.CobroEfectivo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.jluane.drinkslab.negocio.ServicioVenta;
import io.github.jluane.drinkslab.negocio.modelo.DetalleVenta;
import io.github.jluane.drinkslab.negocio.modelo.Venta;
import io.github.jluane.drinkslab.negocio.modelo.Venta.TipoPago;
import io.github.jluane.drinkslab.presentacion.Venta.ControlVenta;
import jakarta.annotation.PostConstruct;

@Component 
public class ControlCobroEfectivo {
    // Vista que controla
    private final VistaCobroEfectivo vista;
    // Controles a los que se conecta
    private final ControlVenta controlVenta;
    // Servicios a los que se comunica
    private final ServicioVenta servicioVenta;

    // Datos
    private Venta venta;
    private List<DetalleVenta> detalles;

    @Autowired
    public ControlCobroEfectivo(VistaCobroEfectivo vista, ControlVenta controlVenta, ServicioVenta servicioVenta){
        this.vista = vista;
        this.controlVenta = controlVenta;
        this.servicioVenta = servicioVenta;
    }

    /**
	 * Método que se ejecuta después de la construcción del bean
	 * y realiza la conexión bidireccional entre el control y la ventana
	 */
    @PostConstruct
    public void init() {
        vista.setControlCobroEfectivo(this);
    }

    public void inicia(Venta venta, List<DetalleVenta> detalles){
        this.venta = venta;
        this.detalles = detalles;
        vista.muestra(venta);
    }

    public void procesarPago(){
        boolean procesado = servicioVenta.guardarVenta(venta, detalles, TipoPago.EFECTIVO);
        if(procesado){
            vista.cierra();
            System.out.println("Se cierra la vista");
            controlVenta.inicia();
            System.out.println("Se manda el regreso a control Venta");
        }else{
            System.out.println("Ocurrió un error al procesar el pago");
        }
        System.out.println(venta);
    }
}
