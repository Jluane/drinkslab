package io.github.jluane.drinkslab.presentacion.GestionProducto;

import org.springframework.stereotype.Component;

import io.github.jluane.drinkslab.negocio.modelo.Proveedor;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

@Component
public class ItemProveedor {
    // Controlador
    ControlGestionProducto control;

    // Variables
    private Proveedor proveedor = new Proveedor();

    // Elementos FXML
    @FXML private Label nombre;
    
    // Constructor de la tarjeta
    public ItemProveedor(){}

    /**
     * Inicialización
     */
    @FXML
    public void initialize(){
    }

    /**
     * Se da el control que controlará a esta tarjeta
     * @param control Es específicamente el control que tendrá
     */
    public void setControlGestionProducto(ControlGestionProducto control){
        this.control = control;
    }

    /**
     * Configura los datos del producto en el ítem
     * @param prov Es el proveedor del que se sacan los datos
     */
    public void configurarDatos(Proveedor prov){
        this.proveedor = prov;
        nombre.setText(proveedor.getNombre());
    }

    // On Action's
    @FXML
    public void onActionEliminar(){
        control.eliminarProveedor(proveedor);
    }
}
