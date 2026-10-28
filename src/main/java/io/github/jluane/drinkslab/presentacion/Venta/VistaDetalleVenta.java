package io.github.jluane.drinkslab.presentacion.Venta;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import io.github.jluane.drinkslab.negocio.modelo.DetalleVenta;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * VistaDetalleVenta
 */
@Component
public class VistaDetalleVenta {
    // Controlador
    ControlVenta control;

    // Elementos del FXML
    @FXML
    private Label nombre;
    @FXML
    private Label codigo;
    @FXML 
    private TextField cantidad;
    @FXML
    private Label pUnitario;
    @FXML
    private Label pTotal;
    @FXML
    private GridPane contenedorDetalle;

    // Constructor necesario para la clase
    public VistaDetalleVenta(){}

    /**
     * Inicialización de la tarjeta
     */
    @FXML
    public void initialize() {
    }

    /**
     * Se da el control que controlará a esta tarjeta
     * @param control Es específicamente el control que tendrá
     */
    public void setControlVenta(ControlVenta control){
        this.control = control;
    }

    public void configurarDatos(DetalleVenta detalle){
        nombre.setText(detalle.getProducto().getNombre());
        codigo.setText("Código: "+detalle.getProducto().getGTIN());
        cantidad.setText(Integer.toString(detalle.getCantidad()));
        pUnitario.setText("$"+detalle.getProducto().getPrecio());
        pTotal.setText("$"+detalle.getCosto());

        contenedorDetalle.setUserData(detalle);
    }

    public void actualizarCantidad(int cant, BigDecimal costo){
        cantidad.setText(Integer.toString(cant));
        pTotal.setText("$"+costo);
    }

    public void terminar(){
        contenedorDetalle.setManaged(false);
        contenedorDetalle.setVisible(false);
        contenedorDetalle.setDisable(true);
    }

    // On Action's
    @FXML
    public void onActionDisminuir(){
        System.out.println("Se presionó el botón para disminuir");
        if(contenedorDetalle.getUserData() instanceof DetalleVenta detalle)
            control.disminuirCantDetalle(detalle);
        else
            System.err.println("No es posible modificar al detalle");
    }
    @FXML
    public void onActionCantidad(){
        System.out.println("Se presionó el botón para cambiar cantidad");
        try {
            if(Integer.valueOf(cantidad.getText())<0){
                // Alerta
                System.err.println("No es una cantidad válida");
                cantidad.setText("1");
                return;
            }    
        } catch (Exception e) {
            // Alerta
            System.err.println("No es una cantidad válida. "+e);
            cantidad.setText("1");
            return;
        }
        
        if(contenedorDetalle.getUserData() instanceof DetalleVenta detalle)
            control.editarCantDetalle(detalle, Integer.valueOf(cantidad.getText()));
        else
            System.err.println("No es posible modificar al detalle");
    }
    @FXML
    public void onActionAumentar(){
        System.out.println("Se presionó el botón para aumentar");
        if(contenedorDetalle.getUserData() instanceof DetalleVenta detalle)
            control.aumentarCantDetalle(detalle);
        else
            System.err.println("No es posible modificar al detalle");
    }
    @FXML
    public void onActionEliminar(){
        System.out.println("Se presionó el botón para eliminar");
        if(contenedorDetalle.getUserData() instanceof DetalleVenta detalle)
            control.eliminarDetalle(detalle);
        else
            System.err.println("No es posible eliminar al detalle");
    }
}