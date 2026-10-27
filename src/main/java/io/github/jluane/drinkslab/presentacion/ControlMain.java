package io.github.jluane.drinkslab.presentacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.jluane.drinkslab.presentacion.Venta.ControlVenta;
import jakarta.annotation.PostConstruct;

@Component
public class ControlMain {
    // Vista que controla
    private final VistaMain vista;
    // Controles a los que se comunica
    private final ControlVenta controlVenta;
    // Servicios a los que se conecta
    // ...

    @Autowired
    public ControlMain(VistaMain vista, ControlVenta controlVenta){
        this.vista = vista;
        this.controlVenta = controlVenta;
    }

    /**
	 * Método que se ejecuta después de la construcción del bean
	 * y realiza la conexión bidireccional entre el control y la ventana
	 */
    @PostConstruct
    public void init() {
        vista.setControlMain(this);
    }

    /**
     * Inicia el programa mandando a llamar a la vista
     */
    public void inicia(){
        vista.muestra();
    }

    /**
     * Pasa el control a la pantalla de ventas para que se dibuje en el centro
     */
    public void irVenta(){
        // Obtiene el diseño de la venta
        javafx.scene.Parent panelVenta = controlVenta.obtienePanel();
        // Le dice a la ventana principal que lo ponga en el centro
        vista.cambiarVista(panelVenta);
    }

}
