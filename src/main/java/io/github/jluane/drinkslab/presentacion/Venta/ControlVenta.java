package io.github.jluane.drinkslab.presentacion.Venta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class ControlVenta {
    // Vista que controla
    private final VistaVenta vista;
    // Controles con los que se comunica
    // ...
    // Servicios a los que se conecta
    // ...

    @Autowired
    public ControlVenta(VistaVenta vista){
        this.vista = vista;
    }

    /**
	 * Método que se ejecuta después de la construcción del bean
	 * y realiza la conexión bidireccional entre el control y la ventana
	 */
    @PostConstruct
    public void init() {
        vista.setControlVenta(this);
    }

    public javafx.scene.Parent obtienePanel() {
        return vista.cargaPanel();
    }

    /**
     * Inicia el programa mandando a llamar a la vista
     */
    public void inicia(){
        vista.muestra();
    }


}
