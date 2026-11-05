package io.github.jluane.drinkslab.presentacion.GestionProducto;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.jluane.drinkslab.negocio.ServicioProducto;
import io.github.jluane.drinkslab.negocio.modelo.Producto;
import jakarta.annotation.PostConstruct;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

@Component
public class ControlGestionProducto {
    // Vista que controla
    private final VistaGestionProducto vista;
    // Controladores a los que se comunica
    // ...
    // Servicios a los que se comunica
    private final ServicioProducto servicioProducto;

    // Datos
    private Producto producto;

    @Autowired
    public ControlGestionProducto(VistaGestionProducto vista, ServicioProducto servicioProducto){
        this.vista = vista;
        this.servicioProducto = servicioProducto;
    }

    /**
	 * Método que se ejecuta después de la construcción del bean
	 * y realiza la conexión bidireccional entre el control y la ventana
	 */
    @PostConstruct
    public void init(){
        vista.setControlGestionProducto(this);
    }
    
    // -------------------- CREACIÓN --------------------
    /**
     * Inicia para crear un nuevo producto
     * @param codigo Es el código con el que se intenta crear el nuevo producto
     */
    public void inicia(String codigo){
        this.producto = new Producto();
        producto.setGTIN(codigo);
        vista.muestra(this.producto, false);
    }

    // -------------------- MODIFICACIÓN --------------------
    /**
     * Modifica a un procucto ya dado
     * @param producto Es el producto que modificaremos
     */
    public void inicia(Producto producto){
        this.producto = producto;
        vista.muestra(producto, true);
    }


    

    // GENERAL
    public void mostrarAlerta(String texto){
        vista.mostrarAlerta(texto);
    }
    public void guardarProducto(String nombre, String codigo, String precio, String cant, String cantPaquete, String precioPaquete, String cantPromo, String precioPromo, boolean aGranel){
        boolean logrado = servicioProducto.guardarProducto(this.producto, nombre, codigo, precio, cant, cantPaquete, precioPaquete, cantPromo, precioPromo, aGranel);
        if(!logrado){
            mostrarAlerta("Estás en Creación.\nVerifica que no exista un producto con ese código.\nO busca el producto directamente con el código.");
        }else{
            // Mostrar aviso
            vista.cierra();
        }
    }

    public void confirmacionEliminarProducto(){
        // Creamos una alerta de confirmación
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("ELIMINACIÓN DE PRODUCTO");
        alert.setHeaderText("¿Estás seguro de que deseas eliminar un producto?");
        alert.setContentText("Producto:\n"+producto+"\nEsta acción no se puede deshacer.");

        // Mostrar la ventana y esperar respuesta
        Optional<ButtonType> resultado = alert.showAndWait();

        // Evaluar el botón presionado
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            System.out.println("Se eliminará al producto");
            eliminarProducto(producto);
        } else {
            // Código si el usuario cancela o cierra la ventana
            System.out.println("Acción cancelada");
        }
    }
    private void eliminarProducto(Producto producto){
        if(servicioProducto.eliminarProducto(producto)){
            System.out.println("No se pudo borrar al producto");
        }
        vista.cierra();
    }


}
