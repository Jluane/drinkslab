package io.github.jluane.drinkslab.presentacion.Cobro.ControlesCobro;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.jluane.drinkslab.negocio.ServicioVenta;
import io.github.jluane.drinkslab.presentacion.Cobro.ControlCobro;
import io.github.jluane.drinkslab.presentacion.Cobro.VistasCobro.VistaCobroEfectivo;
import io.github.jluane.drinkslab.presentacion.Venta.ControlVenta;

@Component 
public class ControlCobroEfectivo extends ControlCobro{

    @Autowired
    public ControlCobroEfectivo(VistaCobroEfectivo vista, ControlVenta controlVenta, ServicioVenta servicioVenta){
        super(vista, controlVenta, servicioVenta); // Llama al constructor del padre
    }
}