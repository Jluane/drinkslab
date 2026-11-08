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
public class itemDetalleVenta {
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
    private GridPane contenedorDetalle; // Contenedor completo del detalle

    // Variables
    private DetalleVenta detalle; // Variable que guarda al detalle del que trata la pantalla

    // Constructor necesario para la clase
    public itemDetalleVenta(){}

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

    /**
     * Método encargado de configurar los datos con los que nace el item de acuerdo al detalleVenta que represente
     * @param detalle Es el detalle venta del que se saca la información
     */
    public void configurarDatos(DetalleVenta detalle){
        nombre.setText(detalle.getProducto().getNombre());
        codigo.setText("Código: "+detalle.getProducto().getGTIN());
        cantidad.setText(detalle.getCantidad().toString());
        pUnitario.setText("$"+detalle.getProducto().getPrecioUnitario());
        pTotal.setText("$"+detalle.getCosto());

        this.detalle = detalle;
    }

    /**
     * Actualiza los datos que dependen de la cantidad, según la nueva cantidad y el nuevo costo
     * @param cant
     * @param costo
     */
    public void actualizarCantidad(BigDecimal cant, BigDecimal costo){
        cantidad.setText(cant.toString());
        pTotal.setText("$"+costo);
    }

    // On Action's
    /**
     * En caso de presionar el botón disminuir de un detalle
     */
    @FXML
    public void onActionDisminuir(){
        control.disminuirCantDetalle(this.detalle);
    }
    /**
     * En caso de ingresar una cantidad específica para el detalle
     */
    @FXML
    public void onActionCantidad(){
        BigDecimal cant = new BigDecimal(cantidad.getText()); // Obtenemos la cantidad ingresdada

        try {

            if(detalle.getProducto().getAGranel()){
                if(cant.compareTo(BigDecimal.ZERO)<0){
                    throw new IllegalArgumentException("La cantidad es negativa");
                }
            }else{
                if(Integer.valueOf(cantidad.getText())<0){
                    throw new IllegalArgumentException("La cantidad es negativa");
                }
            }
        } catch (Exception e) {
            // Alerta
            System.err.println("ItemDetalleVenta: No es una cantidad válida. "+e);
            cantidad.setText("1");
            return;
        }
        
        control.editarCantDetalle(detalle, cant);
    }
    /**
     * En caso de presioanr el botón aumentar de un detalle
     */
    @FXML
    public void onActionAumentar(){
        control.aumentarCantDetalle(detalle);
    }
    /**
     * En caso de presionar el botón eliminar de un detalle
     */
    @FXML
    public void onActionEliminar(){
        control.eliminarDetalle(detalle);
    }

    /**
     * Regresa al contenedor en sí, necesario para elimina al detalle por completo una vez es eliminado (Se elimina desde la VistaVenta puesto que ella lo creó)
     * @return Regresa al contenedor principal de esta tarjeta
     */
    public GridPane getContenedorDetalle(){
        return this.contenedorDetalle;
    }

    /**
     * Pide el enfoque a la cantidad del producto
     */
    public void enfocarCantidad() {
        javafx.application.Platform.runLater(() -> {
            cantidad.requestFocus();
            cantidad.selectAll();
        });
    }
}