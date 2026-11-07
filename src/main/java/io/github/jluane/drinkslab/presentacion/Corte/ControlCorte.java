package io.github.jluane.drinkslab.presentacion.Corte;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.jluane.drinkslab.negocio.ServicioVenta;
import io.github.jluane.drinkslab.negocio.modelo.Venta;
import jakarta.annotation.PostConstruct;

@Component
public class ControlCorte {
    // Vista que controla
    private final VistaCorte vista;
    // Controles a los que se comunica
    // ...
    // Servicios a los que se conecta
    private final ServicioVenta servicioVenta;

    // Variables
    LocalDate fechaActual;

    @Autowired
    public ControlCorte(VistaCorte vista, ServicioVenta servicioVenta){
        this.vista = vista;
        this.servicioVenta = servicioVenta;
    }

    /**
	 * Método que se ejecuta después de la construcción del bean
	 * y realiza la conexión bidireccional entre el control y la ventana
	 */
    @PostConstruct
    public void init() {
        vista.setControlCorte(this);
    }

    /**
     * Manda a la vista a traer el panel que se mostrará, en este caso, de Venta
     * @return vista.cargaPanel() es el panel que regresa
     */
    public javafx.scene.Parent obtienePanel() {
        return vista.cargaPanel();
    }

    public void inicia(){
        fechaActual = LocalDate.now();
        mostrar();
    }

    private void mostrar(){
        List<Venta> ventas = servicioVenta.obtenerPorFecha(fechaActual);

        vista.inicia(fechaActual, ventas);
    }

    public void cambiarDia(int i){
        this.fechaActual = fechaActual.plusDays(i);
        mostrar();
    }
}
