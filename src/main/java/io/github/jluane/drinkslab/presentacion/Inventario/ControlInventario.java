package io.github.jluane.drinkslab.presentacion.Inventario;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import io.github.jluane.drinkslab.negocio.ServicioProducto;
import io.github.jluane.drinkslab.negocio.modelo.Producto;
import io.github.jluane.drinkslab.presentacion.LectorCodigoBarras;
import io.github.jluane.drinkslab.presentacion.GestionProducto.ControlGestionProducto;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import javafx.application.Platform;
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

    // Variable para saber la página en la que se encuentra
    private int pagina = 1;

    // Lector de código de barras
    private final LectorCodigoBarras lectorCodigoBarras;
    private boolean lectorActivo = false;

    @Autowired
    public ControlInventario(VistaInventario vista, ServicioProducto servicioProducto, @Lazy ControlGestionProducto controlGestionProducto, LectorCodigoBarras lectorCodigoBarras){
        this.vista = vista;
        this.servicioProducto = servicioProducto;
        this.controlGestionProducto = controlGestionProducto;
        this.lectorCodigoBarras = lectorCodigoBarras;
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
        inicia(1);
        lectorActivo = false;
    }

    private void inicia(int pagina){
        vista.inicia(servicioProducto.totalProductos(), pagina);
        mostrarProductos(pagina);
        this.pagina = pagina;

        iniciarLector();
    }

    /**
     * Variable para conocer el estado del lector sin pasar por toda su función de inicio
     */
    private void iniciarLector(){
        if(lectorActivo) return;

        lectorCodigoBarras.iniciar(codigo ->
            Platform.runLater(() -> buscarProducto(codigo))
        );
        lectorActivo = true;
    }

    /**
     * Mëtodo encargado de mandar a traer y mandar a mostrar a los productos en bloques de 8 para mostrar
     * @param i Es el número de bloque que estamos buscando y mostrando
     */
    public void mostrarProductos(int i){
        vista.limpiarCaja();
        List<Producto> productos = servicioProducto.obtenerBloqueDeRegistros(i);
        for(Producto producto : productos){
            vista.crearProducto(producto);
        }
    }

    /**
     * Método dado para cambiar la página a una nueva
     * @param pag Es la página nueva
     */
    public void cambioPagina(int pag){
        inicia(pag);
    }

    /**
     * Método que decide qué hacer de acuerdo al código que sea buscado
     * @param gtin Es el código del producto que buscamos
     */
    public void buscarProducto(String gtin){
        detenerLector();
        Producto producto = servicioProducto.recuperaProducto(gtin);
        
        vista.aplicarDifuminado();
        if(producto == null){
            controlGestionProducto.inicia(gtin);
        }else{
            controlGestionProducto.inicia(producto);
        }
        inicia(this.pagina);
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
            eliminarProducto(producto);
        } else {
            // Código si el usuario cancela o cierra la ventana
        }
    }
    private void eliminarProducto(Producto producto){
        if(servicioProducto.eliminarProducto(producto)){
        }
        inicia();
    }



    public List<String> obtenerSugerenciasNombres(String texto){
        return servicioProducto.obtenerSugerenciasNombres(texto);
    }

    /**
     * Cierra al código de barras
     */
    @PreDestroy
    public void cerrarLector(){
        lectorCodigoBarras.detener();
        lectorActivo = false;
    }

    /**
     * Cierra al código de barras desde una llamada externa
     */
    public void detenerLector(){
        lectorCodigoBarras.detener();
        lectorActivo = false;
    }
}
