package io.github.jluane.drinkslab.presentacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.jluane.drinkslab.presentacion.Corte.ControlCorte;
import io.github.jluane.drinkslab.presentacion.Inventario.ControlInventario;
import io.github.jluane.drinkslab.presentacion.Venta.ControlVenta;
import jakarta.annotation.PostConstruct;

@Component
public class ControlMain {
    // Vista que controla
    private final VistaMain vista;
    // Controles a los que se comunica
    private final ControlVenta controlVenta;
    private final ControlInventario controlInventario;
    private final ControlCorte controlCorte;
    // Servicios a los que se conecta
    // ...

    @Autowired
    public ControlMain(VistaMain vista, ControlVenta controlVenta, ControlInventario controlInventario, ControlCorte controlCorte){
        this.vista = vista;
        this.controlVenta = controlVenta;
        this.controlInventario = controlInventario;
        this.controlCorte = controlCorte;
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
        // Cerramos los lectores de los demás
        controlInventario.detenerLector();

        // Obtiene el diseño de la venta
        javafx.scene.Parent panelVenta = controlVenta.obtienePanel();
        // Le dice a la ventana principal que lo ponga en el centro
        vista.cambiarVista(panelVenta);
        controlVenta.inicia();
    }
    /**
     * Pasa el control a la pantalla de inventario para que se dibuje en el centro
     */
    public void irInventario(){
        // Cerramos al lector que apunta a ControlVenta
        controlVenta.detenerLector();

        // Obtiene el diseño de la venta
        javafx.scene.Parent panelInventario = controlInventario.obtienePanel();
        // Le dice a la ventana principal que lo ponga en el centro
        vista.cambiarVista(panelInventario);
        controlInventario.inicia();
    }
    /**
     * Pasa el control a la pantalla de corte para que se dibuje en el centro
     */
    public void irCorte(){
        // Cerramos al lector que apunta a ControlVenta
        controlVenta.detenerLector();
        controlInventario.detenerLector();

        // Obtiene el diseño de la venta
        javafx.scene.Parent panelCorte = controlCorte.obtienePanel();
        // Le dice a la ventana principal que lo ponga en el centro
        vista.cambiarVista(panelCorte);
        controlCorte.inicia();
    }
}
