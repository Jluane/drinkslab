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
    private List<VistaDetalleVenta> vistasDetalles;

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
     * Manda a la vista a traer el panel que se mostrará, en este caso de Venta
     * @return vista.cargaPanel() es el panel que regresa
     */
    public javafx.scene.Parent obtienePanel() {
        recargarObjetos();
        return vista.cargaPanel();
    }

    private void recargarObjetos(){
        venta = new Venta();
        detallesVenta = new ArrayList<>();
        vistasDetalles = new ArrayList<>();
        venta.setDetalles(detallesVenta);
    }

    /**
     * Inicia el programa mandando a llamar a la vista
     */
    public void inicia(){
        vista.inicia(servicioVenta.obtenerSigId());
    }

    public void agregarProducto(String gtin){
        // Se evalúa si existe un producto con ese código
        Producto producto = servicioProducto.recuperaProducto(gtin);
        if(producto == null){
            System.err.println("El producto no existe");
            // Mëtodo de aviso
            return;
        }
        System.out.println("El producto se encontró");
        
        // Verificamos si el producto ya existe en nuestra lista actual
        Integer indiceDetalle = servicioDetalleVenta.existeEnListaPorProducto(detallesVenta, producto);

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

    private void crearDetalleVenta(Producto producto){
        DetalleVenta detalle = servicioDetalleVenta.agregarProductoNuevo(detallesVenta, producto);
        if(detalle == null){
            System.err.println("Ocurrió un error al intentar guardar el producto");
            return;
        }
        System.out.println("Se logró guardar el producto en un nuevo detalle venta");
        System.out.println("Se intenta crear graficamente");
        VistaDetalleVenta tarjeta = vista.crearDetalleVenta(detalle);
        if(tarjeta == null){
            System.err.println("Ocurrió un error al intentar crear  al detalle venta gráficamente");
            return;
        }
        vistasDetalles.add(tarjeta);
    }
    private void actualizarDetalleVenta(int indice){
        DetalleVenta detalle =  detallesVenta.get(indice);
        VistaDetalleVenta vistaDet = vistasDetalles.get(indice);

        int cantidad = servicioDetalleVenta.incrementarProducto(detalle);
        if(cantidad == 0){
            System.err.println("Ocurrió un error al actualizar, se eliminará");
            eliminarDetalle(indice);
        }else{
            System.out.println("Se logró actualizar a detalle venta");
            System.out.println("Se intentará actualizar gráficamente");
            vistaDet.actualizarCantidad(cantidad, detalle.getCosto());
        }
    }

    public void actualizarVenta(){
        servicioVenta.actualizarVenta(venta, detallesVenta);
        vista.actualizarVenta(venta);
    }

    public void disminuirCantDetalle(DetalleVenta detalle){
        int idx = detallesVenta.indexOf(detalle);
        VistaDetalleVenta vistaDet = vistasDetalles.get(idx);
        int cant = servicioDetalleVenta.disminuirProducto(detalle);
        if(cant == 0){
            System.err.println("Se llegó a una cantidad inválida. Se eliminará");
            eliminarDetalle(idx);
        }else{
            System.out.println("Se actualizará la cantidad y el costo");
            vistaDet.actualizarCantidad(detalle.getCantidad(), detalle.getCosto());
            System.out.println("Fue disminuido. Lista ahora:\n"+detallesVenta);
        }
        actualizarVenta();
    }
    public void aumentarCantDetalle(DetalleVenta detalle){
        int idx = detallesVenta.indexOf(detalle);
        VistaDetalleVenta vistaDet = vistasDetalles.get(idx);
        int cant = servicioDetalleVenta.incrementarProducto(detalle);
        if(cant == 0){
            System.err.println("Ocurrió al actualizar, se eliminará el detalle");
            eliminarDetalle(idx);
        }else{
            System.out.println("Se actualizará la cantidad y el costo");
            vistaDet.actualizarCantidad(detalle.getCantidad(), detalle.getCosto());
            System.out.println("Fue aumentado. Lista ahora:\n"+detallesVenta);
        }
        actualizarVenta();
    }
    public void eliminarDetalle(DetalleVenta detalle){
        int indice = detallesVenta.indexOf(detalle);
        System.out.println("Se eliminará a "+detalle);
        eliminarDetalle(indice);
        actualizarVenta();
    }
    public void editarCantDetalle(DetalleVenta detalle, int cantidad){
        System.out.println("Se intenta cambiar el detalle a la cantidad "+cantidad);
        int idx = detallesVenta.indexOf(detalle);
        int cant = servicioDetalleVenta.editarCantidad(detalle, cantidad);
        if(cant == 0){
            System.err.println("Se llegó a una cantidad inválida. Se eliminará");
            eliminarDetalle(idx);
        }else{
            System.out.println("Fue modificado. Lista ahora:\n"+detallesVenta);
            vistasDetalles.get(idx).actualizarCantidad(cant, detalle.getCosto());
        }
        actualizarVenta();
    }

    private void eliminarDetalle(int indice){
        detallesVenta.remove(indice);
        vistasDetalles.get(indice).terminar();
        vistasDetalles.remove(indice);
        System.out.println("Fue eliminado. Lista ahora:\n"+detallesVenta);
    }
}
