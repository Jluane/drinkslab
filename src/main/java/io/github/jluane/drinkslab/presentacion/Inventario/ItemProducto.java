package io.github.jluane.drinkslab.presentacion.Inventario;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import io.github.jluane.drinkslab.negocio.modelo.Producto;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

@Component
public class ItemProducto {
    // Controlador
    ControlInventario control;

    // Variables
    private Producto producto = new Producto();

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

    /**
     * Configura los datos del producto en el ítem
     * @param producto Es el producto del que se saca la información
     */
    public void configurarDatos(Producto producto){
        this.producto = producto;
        txtCodigo.setText(producto.getGTIN());
        txtNombre.setText(producto.getNombre());
        txtPrecio.setText("$"+producto.getPrecioUnitario());

        if(producto.getCantInv().compareTo(BigDecimal.ZERO) < 0){
            txtCantidad.setText("Sin info");
            txtCantidad.setStyle("-fx-background-color: #E6DC57");
        }else if(producto.getAGranel())
            txtCantidad.setText(producto.getCantInv().stripTrailingZeros().toPlainString()+" kg");
        else
            txtCantidad.setText(producto.getCantInv().intValue()+" pzas");
    }

    // On Action's
    @FXML
    public void onActionEditar(){
        System.out.println("Se presionó al botón editar");
        control.buscarProducto(producto.getGTIN());
    }
    @FXML
    public void onActionEliminar(){
        System.out.println("Se presionó el botón eliminar");
        control.confirmacionEliminarProducto(producto);
    }
}
