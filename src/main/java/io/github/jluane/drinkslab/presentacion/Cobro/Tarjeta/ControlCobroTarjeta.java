package io.github.jluane.drinkslab.presentacion.Cobro.Tarjeta;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.jluane.drinkslab.negocio.ServicioVenta;
import io.github.jluane.drinkslab.presentacion.Cobro.ControlCobro;
import io.github.jluane.drinkslab.presentacion.Venta.ControlVenta;

@Component 
public class ControlCobroTarjeta extends ControlCobro {
    @Autowired
    public ControlCobroTarjeta(VistaCobroTarjeta vista, ControlVenta controlVenta, ServicioVenta servicioVenta){
        super(vista, controlVenta, servicioVenta); // Llama al constructor del padre
    }
}
