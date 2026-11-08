package io.github.jluane.drinkslab.presentacion.GestionProducto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.jluane.drinkslab.negocio.ServicioProducto;
import io.github.jluane.drinkslab.negocio.ServicioProveedor;
import io.github.jluane.drinkslab.negocio.modelo.Producto;
import io.github.jluane.drinkslab.negocio.modelo.Proveedor;
import io.github.jluane.drinkslab.presentacion.LectorCodigoBarras;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import javafx.application.Platform;
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
    private final ServicioProveedor servicioProveedor;

    // Lector de códigos de barras
    private final LectorCodigoBarras lectorCodigoBarras;

    // Datos
    private Producto producto;
    private List<Proveedor> proveedores = new ArrayList<>();

    @Autowired
    public ControlGestionProducto(VistaGestionProducto vista, ServicioProducto servicioProducto, ServicioProveedor servicioProveedor, LectorCodigoBarras lectorCodigoBarras){
        this.vista = vista;
        this.servicioProducto = servicioProducto;
        this.servicioProveedor = servicioProveedor;
        this.lectorCodigoBarras = lectorCodigoBarras;
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

        lectorCodigoBarras.iniciar(codigoObtenido -> {
            Platform.runLater(() -> vista.ingresarCodigo(codigoObtenido));
        });

        vista.muestra(this.producto, false);
    }

    // -------------------- MODIFICACIÓN --------------------
    /**
     * Modifica a un procucto ya dado
     * @param producto Es el producto que modificaremos
     */
    public void inicia(Producto producto){
        this.producto = producto;

        lectorCodigoBarras.iniciar(codigoObtenido -> {
            Platform.runLater(() -> vista.ingresarCodigo(codigoObtenido));
        });

        vista.muestra(producto, true);
    }


    

    // GENERAL
    public void mostrarAlerta(String texto){
        vista.mostrarAlerta(texto);
    }
    public void guardarProducto(String nombre, String codigo, String precio, String cant, String cantPaquete, String precioPaquete, String cantPromo, String precioPromo, boolean aGranel){
        boolean logrado = servicioProducto.guardarProducto(this.producto, nombre, codigo, precio, cant, cantPaquete, precioPaquete, cantPromo, precioPromo, aGranel, proveedores);
        if(!logrado){
            mostrarAlerta("Estás en Creación.\nVerifica que no exista un producto con ese código.\nO busca el producto directamente con el código.");
        }else{
            // Mostrar aviso
            this.cierra();
        }
    }

    /**
     * Muestra una pantalla para pedir confirmación y eliminar a un producto
     */
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
            eliminarProducto(producto);
        } else {
            // Código si el usuario cancela o cierra la ventana
        }
    }
    /**
     * Manda a eliminar de repositorio al producto que le es dado
     * @param producto producto que se eliminará
     */
    private void eliminarProducto(Producto producto){
        if(servicioProducto.eliminarProducto(producto)){
        }
        this.cierra();
    }

    /**
     * Busca la lista de proveedores del producto actual y los proveedores generales
     */
    public void colocarProveedores(){
        List<Proveedor> todosProveedores = servicioProveedor.obtenerProveedores();
        this.proveedores = servicioProducto.obtenerProveedores(producto);
        mostrarProveedoresTenidos();

        if(!todosProveedores.isEmpty()){
            List<String> nombres = new ArrayList<>();
            for(Proveedor prov : todosProveedores)
                nombres.add(prov.getNombre());

            vista.mostrarProveedores(nombres);
        }
    }
    /**
     * Manda a vista a mostrar la lista de proveedores que se tiene actualmente
     */
    private void mostrarProveedoresTenidos(){
        vista.limpiarCajaProveedores();
        for(Proveedor prov : this.proveedores){
            vista.mostrarProveedoresSeleccionados(prov);
        }
    }
    /**
     * Recibe un nombre para proveedor, se obtiene un proveedor con ese nombre y se agrega a la lista
     * @param nombre Nombre para el proveedor
     */
    public void agregarProveedor(String nombre){
        Proveedor proveedor = servicioProveedor.obtenerPorNombre(nombre);
        if(proveedor == null) return;
        
        if(proveedores.contains(proveedor)) return ; // Si ya se encuentra en la lista, no se hace nada
        
        proveedores.add(proveedor);
        mostrarProveedoresTenidos();
    }

    /**
     * Elimina a un proveedor de su lista
     * @param proveedor Es el proveedor a eliminar
     */
    public void eliminarProveedor(Proveedor proveedor){
        proveedores.remove(proveedor);
        mostrarProveedoresTenidos();
    }

    /**
     * Cierra al código de barras
     */
    @PreDestroy
    public void cerrarLector(){
        lectorCodigoBarras.detener();
    }

    /**
     * Cierra al código de barras desde una llamada externa
     */
    public void detenerLector(){
        lectorCodigoBarras.detener();
    }

    /**
     * Lleva el proceso para cerrar correctamente la ventana
     */
    public void cierra(){
        detenerLector();
        vista.cierra();
    }
}
