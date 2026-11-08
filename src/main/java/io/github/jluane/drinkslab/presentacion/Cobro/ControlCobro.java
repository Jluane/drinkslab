package io.github.jluane.drinkslab.presentacion.Cobro;

import io.github.jluane.drinkslab.negocio.ServicioVenta;
import io.github.jluane.drinkslab.negocio.modelo.Venta;
import io.github.jluane.drinkslab.negocio.modelo.Venta.TipoPago;
import io.github.jluane.drinkslab.presentacion.Venta.ControlVenta;
import jakarta.annotation.PostConstruct;

public abstract class ControlCobro {
    // Vista que controla
    protected VistaCobro vista;
    // Controles a los que se comunica
    protected ControlVenta controlVenta;
    // Servicios con los que se comunica
    protected ServicioVenta servicioVenta;
    
    // Datos
    protected Venta venta;

    /**
     * Nos ahora el uso del init para ahora más líneas de código. Se deben tener los mismo elementos que guardar
     */
    public ControlCobro(VistaCobro vista, ControlVenta controlVenta, ServicioVenta servicioVenta){
        this.vista = vista;
        this.controlVenta = controlVenta;
        this.servicioVenta = servicioVenta;

        this.vista.setControlCobro(this);
    }

    /**
     * Método con el que se manda a iniciar al cobro según el tipo dado
     * @param venta Es la venta de la que se hace el cobro
     * @param detalles Es la lista de detalles pertenecientes a la venta
     */
    public void inicia(Venta venta, TipoPago tipo){
        this.venta = venta;
        this.venta.setTipoPago(tipo);

        vista.muestra(venta);
    }

    /**
     * Método encargado de procesar un pago en efectivo y decidir qué hacer si se loogró o no
     * En caso de que se haya logrado, se cierra la ventana iniciando una venta nueva. En caso de que no, se cierra la ventana dejando a la misma venta
     */
    public void procesarPago(){
        boolean procesado = servicioVenta.guardarNuevaVenta(venta);
        if(procesado){
            vista.cierra();
            controlVenta.inicia();
        }else{
            vista.cierra();
            controlVenta.mostrarAlerta("No se pudieron guardar los datos de la venta. Se recomiendo reiniciar la venta (presionar F1)");
        }
    }

    /**
	 * Método que se ejecuta después de la construcción del bean
	 * y realiza la conexión bidireccional entre el control y la ventana
	 */
    @PostConstruct
    public void init() {
        vista.setControlCobro(this);
    }
}
