package io.github.jluane.drinkslab.presentacion.CobroEfectivo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.jluane.drinkslab.negocio.ServicioVenta;
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

    /**
     * Método con el que se manda a iniciar al cobro
     * @param venta Es la venta de la que se hace el cobro
     * @param detalles Es la lista de detalles pertenecientes a la venta
     */
    public void inicia(Venta venta){
        this.venta = venta;
        vista.muestra(venta);
    }

    /**
     * Método encargado de procesar un pago en efectivo y decidir qué hacer si se loogró o no
     * En caso de que se haya logrado, se cierra la ventana iniciando una venta nueva. En caso de que no, se cierra la ventana dejando a la misma venta
     */
    public void procesarPago(){
        boolean procesado = servicioVenta.guardarNuevaVenta(venta, TipoPago.EFECTIVO);
        if(procesado){
            vista.cierra();
            System.out.println("Se cierra la vista");
            controlVenta.inicia();
            System.out.println("Se manda el regreso a control Venta");
        }else{
            System.out.println("Ocurrió un error al procesar el pago");
            vista.cierra();
            controlVenta.mostrarAlerta("No se pudieron guardar los datos de la venta. Se recomiendo reiniciar la venta (presionar F1)");
        }
        System.out.println(venta);
    }
}
