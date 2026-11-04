package io.github.jluane.drinkslab.presentacion.Inventario;

import java.io.IOException;

import org.springframework.stereotype.Component;

import io.github.jluane.drinkslab.negocio.modelo.Producto;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

@Component
public class VistaInventario {
    // Controlador de esta pantalla
    private ControlInventario control;

    // Componentes FXML
    @FXML private VBox cajaProductos;
    @FXML private Label txtProductosRegistrados;
    @FXML private Label txtProductosMostrados;

    // Constructor
    public VistaInventario(){}

    /**
     * Se da el control que controlará a este panel
     * @param control Es específicamente el control que tendrá
     */
    public void setControlInventario(ControlInventario control){
        this.control = control;
    }

    /**
     * Inicialización
     */
    @FXML
    public void initialize(){
    }

    /**
     * Carga el FXML y devuelve el panel visual listo
     */
    public Parent cargaPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana-inventario.fxml"));
            loader.setController(this);
            Parent parent = loader.load();
            return parent;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Manda a crear un ítem de producto con el objeto del que el ítem sacará la información
     * @param detalle Es el producto con la información necesaria para el ítem
     * @return Regresa al objeto del item
     */
    public ItemProducto crearProducto(Producto producto){
        try{
            // Carga a la plantilla de la tarjeta de los pedidos
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/item-producto.fxml"));
            ItemProducto tarjeta = new ItemProducto(); // Se asigna el controlador hecho a la tarjeta
            tarjeta.setControlInventario(control); // tarjeta.setControl(controlRegistroPedido); // Se da que este controlador tiene el control sobre las tarjetas, para apoyar la comunicación con los servicios
            loader.setController(tarjeta);
            Node tarjetaVisual = loader.load(); // Se inicializa la tarjeta
            tarjeta.configurarDatos(producto); // Mediante el controlador se llama al método para ingresar la información
            cajaProductos.getChildren().add(tarjetaVisual); // Se agrega la tarjeta al contenedor
            return tarjeta;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Elimina al ítem que le llega, eliminandolo del contenedor en el que fue puesto
     * @param nodoVisual // Es el visual del ítem que se eliminará
     */
    public void eliminarProducto(GridPane nodoVisual){
        cajaProductos.getChildren().remove(nodoVisual);
        System.out.println("Se mató gráficamente al producto");
    }
    
    /**
     * Inicia la pantalla de inventario
     * @param cantProductos
     */
    public void inicia(int cantProductos){
        txtProductosRegistrados.setText(Integer.toString(cantProductos));
        txtProductosMostrados.setText("Mostrando 8 de "+cantProductos+" productos en memoria");
    }



    // OnAction's
    @FXML
    public void onActionBuscar(){
        System.out.println("Se presionó buscar");
    }
}
