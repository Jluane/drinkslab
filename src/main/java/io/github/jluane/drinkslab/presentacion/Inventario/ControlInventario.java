package io.github.jluane.drinkslab.presentacion.Inventario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.jluane.drinkslab.negocio.ServicioProducto;
import jakarta.annotation.PostConstruct;

@Component
public class ControlInventario {
    // Vista que controla
    private final VistaInventario vista;
    // Controles a los que se comunica
    // ...
    // Servicio a los que se conecta
    private final ServicioProducto servicioProducto;

    @Autowired
    public ControlInventario(VistaInventario vista, ServicioProducto servicioProducto){
        this.vista = vista;
        this.servicioProducto = servicioProducto;
    }

    /**
	 * Método que se ejecuta después de la construcción del bean
	 * y realiza la conexión bidireccional entre el control y la ventana
	 */
    @PostConstruct
    public void init() {
        vista.setControlInventario(this);
    }

    /**
     * Manda a la vista a traer el panel que se mostrará, en este caso, de Venta
     * @return vista.cargaPanel() es el panel que regresa
     */
    public javafx.scene.Parent obtienePanel() {
        return vista.cargaPanel();
    }

    /**
     * Inicia o reinicia la HU reestableciendo los valores de todo como para una nueva venta
     */
    public void inicia(){
        System.out.println("Inicia Control Inventario");
        vista.inicia(servicioProducto.totalProductos());
    }
}
