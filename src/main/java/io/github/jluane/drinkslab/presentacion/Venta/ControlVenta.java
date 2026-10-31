package io.github.jluane.drinkslab.presentacion.Venta;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.jluane.drinkslab.negocio.ServicioDetalleVenta;
import io.github.jluane.drinkslab.negocio.ServicioProducto;
import io.github.jluane.drinkslab.negocio.ServicioVenta;
import io.github.jluane.drinkslab.negocio.modelo.DetalleVenta;
import io.github.jluane.drinkslab.negocio.modelo.Producto;
import io.github.jluane.drinkslab.negocio.modelo.Venta;
import jakarta.annotation.PostConstruct;
import javafx.scene.layout.GridPane;

@Component
public class ControlVenta {
    // Vista que controla
    private final VistaVenta vista;
    // Controles con los que se comunica
    // ...
    // Servicios a los que se conecta
    private final ServicioProducto servicioProducto;
    private final ServicioDetalleVenta servicioDetalleVenta;
    private final ServicioVenta servicioVenta;

    // Variables en la clase dadas para la creación de los objetos
    private Venta venta;
    private List<DetalleVenta> detallesVenta;
    private List<itemDetalleVenta> vistasDetalles;

    @Autowired
    public ControlVenta(VistaVenta vista, ServicioProducto servicioProducto, ServicioDetalleVenta servicioDetalleVenta, ServicioVenta servicioVenta){
        this.vista = vista;
        this.servicioProducto = servicioProducto;
        this.servicioDetalleVenta = servicioDetalleVenta;
        this.servicioVenta = servicioVenta;
    }

    /**
	 * Método que se ejecuta después de la construcción del bean
	 * y realiza la conexión bidireccional entre el control y la ventana
	 */
    @PostConstruct
    public void init() {
        vista.setControlVenta(this);
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
        recargarObjetos();
        vista.inicia(servicioVenta.obtenerSigId());
    }

    /**
     * Método encargado de los pasos a seguir al momento en el que es ingresado un nuevo producto mediante el código GTIN
     * @param gtin Es el valor del GTIN del producto que se buscará
     */
    public void agregarProducto(String gtin){
        // Se evalúa si existe un producto con ese código
        Producto producto = servicioProducto.recuperaProducto(gtin);
        if(producto == null){
            System.err.println("El producto no existe");
            mostrarAlerta("No se encontró producto con ese código.");
            return;
        }
        System.out.println("El producto se encontró");
        
        // Verificamos si el producto ya existe en nuestra lista actual
        Integer indiceDetalle = servicioDetalleVenta.existeEnListaPorProducto(detallesVenta, producto);

        // Dependiendo de si se encuentra o no en la lista de la venta actual, decidimos qué hacer
        if(indiceDetalle == null){ // Significa que ese producto es nuevo en la lista
            System.out.println("El producto no existe en la lista, se creará");
            crearDetalleVenta(producto);
        }else{ // Significa que ya existe un producto así en esa lista
            System.out.println("El producto ya existe en la lista, se actualizará");
            actualizarDetalleVenta(indiceDetalle);
        }
        System.out.println("La lista: \n"+detallesVenta);
        actualizarVenta();
    }

    /* ----------------- UTILERÍA ----------------- */
    /**
     * Se actualizan los datos internos del control para que se recargue y sea una nueva venta
     */
    private void recargarObjetos(){
        venta = new Venta();
        detallesVenta = new ArrayList<>();
        vistasDetalles = new ArrayList<>();
        venta.setDetalles(detallesVenta);
    }
    /**
     * Manda a la vista a mostrar un aviso de error o de lo que sea
     * @param motivo
     */
    private void mostrarAlerta(String motivo){
        String cad = "Ocurrió un error: "+motivo;
        vista.mostrarErrorTemporal(cad);
    }
    

    /* ----------------- MODIFICACIONES A LA LISTA Y VENTA ACTUAL ----------------- */
    /**
     * Método destinado a agregar un nuevo producto a la lista que tenemos
     * @param producto Es el producto que se agregará dentro de su detalle correspondiente
     */
    private void crearDetalleVenta(Producto producto){
        // Actualizamos las listas con el producto
        DetalleVenta detalle = servicioDetalleVenta.agregarProductoNuevo(detallesVenta, producto);
        if(detalle == null){
            System.err.println("Ocurrió un error al intentar guardar el producto");
            mostrarAlerta("No se pudo crear al detalle de venta para el producto "+producto.getNombre()+".");
            return;
        }
        System.out.println("Se logró guardar el producto en un nuevo detalle venta");
        
        // Agregamos gráficamente al detalle
        System.out.println("Se intenta crear graficamente");
        itemDetalleVenta tarjeta = vista.crearDetalleVenta(detalle);
        if(tarjeta == null){
            System.err.println("Ocurrió un error al intentar crear  al detalle venta gráficamente");
            mostrarAlerta("El producto se guardó pero no se logró mostrar.");
            return;
        }
        vistasDetalles.add(tarjeta); // Guardamos también la tarjeta en nuestra lista de tarjetas visuales
    }
    /**
     * Método destinado a agregar un producto ya existente a la lista que tenemos
     * @param indice Es el índice en el que se encuentra el DetalleVenta que contiene al producto que agregaremos
     */
    private void actualizarDetalleVenta(int indice){
        // Obtenemos los objetos en esos índices
        DetalleVenta detalle =  detallesVenta.get(indice);
        itemDetalleVenta vistaDet = vistasDetalles.get(indice);

        // Aumentamos la cantidad en 1
        int cantidad = servicioDetalleVenta.aumentarCantidadProducto(detalle);
        if(cantidad == 0){ // En caso de que haya sucedido un error
            System.err.println("Ocurrió un error al actualizar, se eliminará");
            mostrarAlerta("No se pudo aumentar la cantidad del producto. Se eliminará.");
            eliminarDetalle(indice);
        }else{
            System.out.println("Se logró actualizar a detalle venta");
            System.out.println("Se intentará actualizar gráficamente");
            vistaDet.actualizarCantidad(cantidad, detalle.getCosto());
        }
    }
    /**
     * Método encargado de actualizar a la Venta guardada y de actualizar gráficamente los detalles de la venta
     */
    private void actualizarVenta(){
        servicioVenta.actualizarVenta(venta, detallesVenta);
        vista.actualizarVenta(venta);
    }
    /**
     * Método que realiza la acción de eliminar un detalle en la lista y gráficamente
     * @param indice
     */
    private void eliminarDetalle(int indice){
        GridPane itemGrafico = vistasDetalles.get(indice).getContenedorDetalle();
        
        detallesVenta.remove(indice);
        vistasDetalles.remove(indice);
        vista.eliminarDetalleVenta(itemGrafico);
        System.out.println("Fue eliminado. Lista ahora:\n"+detallesVenta);
    }

    /* ----------------- CONTROLES DE CADA DETALLE VENTA ----------------- */
    /**
     * Método encargado de disminuir en 1 la cantidad de productos en el DetalleVenta dado
     * @param detalle DetalleVenta que recibirá el decremento
     */
    public void disminuirCantDetalle(DetalleVenta detalle){
        int idx = detallesVenta.indexOf(detalle);
        itemDetalleVenta vistaDet = vistasDetalles.get(idx);
        int cant = servicioDetalleVenta.disminuirCantidadProducto(detalle);
        if(cant == 0){
            System.err.println("Se llegó a una cantidad inválida. Se eliminará");
            mostrarAlerta("El producto llegó a 0, se eliminó.");
            eliminarDetalle(idx);
        }else{
            // Si la cantidad nueva es aceptable, se actualiza como corresponde
            System.out.println("Se actualizará la cantidad y el costo");
            vistaDet.actualizarCantidad(detalle.getCantidad(), detalle.getCosto());
            System.out.println("Fue disminuido. Lista ahora:\n"+detallesVenta);
        }
        actualizarVenta(); // Se actualiza la Venta tras el cambio
    }
    /**
     * Método encargado de aumentar en 1 la cantidad de productos en el DetalleVenta dado
     * @param detalle DetalleVenta que recibirá el incremento
     */
    public void aumentarCantDetalle(DetalleVenta detalle){
        int idx = detallesVenta.indexOf(detalle);
        itemDetalleVenta vistaDet = vistasDetalles.get(idx);
        int cant = servicioDetalleVenta.aumentarCantidadProducto(detalle);
        if(cant == 0){
            System.err.println("Ocurrió al actualizar, se eliminará el detalle");
            mostrarAlerta("No se pudo aumentar la cantidad del producto. Se eliminó.");
            eliminarDetalle(idx);
        }else{
            System.out.println("Se actualizará la cantidad y el costo");
            vistaDet.actualizarCantidad(detalle.getCantidad(), detalle.getCosto());
            System.out.println("Fue aumentado. Lista ahora:\n"+detallesVenta);
        }
        actualizarVenta(); // Se actualiza la Venta tras el cambio
    }
    /**
     * Método encargado de eliminar al detalle dado
     * @param detalle
     */
    public void eliminarDetalle(DetalleVenta detalle){
        int indice = detallesVenta.indexOf(detalle);
        System.out.println("Se eliminará a "+detalle);
        eliminarDetalle(indice);
        actualizarVenta(); // Se actualiza la venta
    }
    /**
     * Método encargado de actualizar la cantidad de un detalle a una cantidad específica
     * @param detalle Es el detalle cuya cantidad será modificada
     * @param cantidad Es la nueva cantidad que tendrá el detalleVenta
     */
    public void editarCantDetalle(DetalleVenta detalle, int cantidad){
        System.out.println("Se intenta cambiar el detalle a la cantidad "+cantidad);
        int idx = detallesVenta.indexOf(detalle);
        int cant = servicioDetalleVenta.editarCantidad(detalle, cantidad);
        if(cant == 0){
            System.err.println("Se llegó a una cantidad inválida. Se eliminará");
            mostrarAlerta("La cantidad es inválida, se eliminó.");
            eliminarDetalle(idx);
        }else{
            System.out.println("Fue modificado. Lista ahora:\n"+detallesVenta);
            vistasDetalles.get(idx).actualizarCantidad(cant, detalle.getCosto());
        }
        actualizarVenta(); // Se actualiza la venta
    }
}
