package io.github.jluane.drinkslab.presentacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class ControlMain {
    private final VistaMain vista;

    @Autowired
    public ControlMain(VistaMain vista){
        this.vista = vista;
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

}
