package io.github.jluane.drinkslab.presentacion.Inventario;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.jluane.drinkslab.negocio.ServicioProducto;
import io.github.jluane.drinkslab.negocio.modelo.Producto;
import io.github.jluane.drinkslab.presentacion.GestionProducto.ControlGestionProducto;
import jakarta.annotation.PostConstruct;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

@Component
public class ControlInventario {
    // Vista que controla
    private final VistaInventario vista;
    // Controles a los que se comunica
    private final ControlGestionProducto controlGestionProducto;
    // Servicio a los que se conecta
    private final ServicioProducto servicioProducto;

    @Autowired
    public ControlInventario(VistaInventario vista, ServicioProducto servicioProducto, ControlGestionProducto controlGestionProducto){
        this.vista = vista;
        this.servicioProducto = servicioProducto;
        this.controlGestionProducto = controlGestionProducto;
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
        vista.inicia(servicioProducto.totalProductos(), 1);
        mostrarProductos(1);
    }

    /**
     * Mëtodo encargado de mandar a traer y mandar a mostrar a los productos en bloques de 8 para mostrar
     * @param i Es el número de bloque que estamos buscando y mostrando
     */
    public void mostrarProductos(int i){
        vista.limpiarCaja();
        List<Producto> productos = servicioProducto.obtenerBloqueDeRegistros(i);
        System.out.println("Lista de productos encontrados: "+productos);
        for(Producto producto : productos){
            vista.crearProducto(producto);
        }
    }

    /**
     * Método dado para cambiar la página a una nueva
     * @param pag Es la página nueva
     */
    public void cambioPagina(int pag){
        vista.inicia(servicioProducto.totalProductos(), pag);
        mostrarProductos(pag);
    }

    /**
     * Método que decide qué hacer de acuerdo al código que sea buscado
     * @param gtin Es el código del producto que buscamos
     */
    public void buscarProducto(String gtin){
        Producto producto = servicioProducto.recuperaProducto(gtin);
    
        vista.aplicarDifuminado();
        if(producto == null){
            System.out.println("Se creará al producto");
            controlGestionProducto.inicia(gtin);
        }else{
            System.out.println("Se modificará al producto");
            controlGestionProducto.inicia(producto);
        }
        inicia();
        vista.quitarDifuminado();
    }

    public void confirmacionEliminarProducto(Producto producto){
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
        inicia();
    }



    public List<String> obtenerSugerenciasNombres(String texto){
        return servicioProducto.obtenerSugerenciasNombres(texto);
    }
}
