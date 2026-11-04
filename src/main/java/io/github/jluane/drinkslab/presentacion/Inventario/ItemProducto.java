package io.github.jluane.drinkslab.presentacion.Inventario;

import org.springframework.stereotype.Component;

import io.github.jluane.drinkslab.negocio.modelo.Producto;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

@Component
public class ItemProducto {
    // Controlador
    ControlInventario control;

    // Elementos FXML
    @FXML private Label txtCodigo;
    @FXML private Label txtNombre;
    @FXML private Label txtPrecio;
    @FXML private Label txtCantidad;
    
    // Constructor de la tarjeta
    public ItemProducto(){}

    /**
     * Inicialización de la tarjeta
     */
    @FXML
    public void initialize(){
    }

    /**
     * Se da el control que controlará a esta tarjeta
     * @param control Es específicamente el control que tendrá
     */
    public void setControlInventario(ControlInventario control){
        this.control = control;
    }

    public void configurarDatos(Producto producto){
        txtCodigo.setText(producto.getGTIN());
        txtNombre.setText(producto.getNombre());
        txtPrecio.setText(producto.getPrecioUnitario().toString());

        if(producto.getAGranel())
            txtCantidad.setText(producto.getCantInv()+" kg");
        else
            txtCantidad.setText(producto.getCantInv()+" pzas");
    }
}
