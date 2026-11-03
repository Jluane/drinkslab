package io.github.jluane.drinkslab.presentacion.Cobro.Transferencia;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.jluane.drinkslab.negocio.ServicioVenta;
import io.github.jluane.drinkslab.presentacion.Cobro.ControlCobro;
import io.github.jluane.drinkslab.presentacion.Venta.ControlVenta;

@Component 
public class ControlCobroTransferencia extends ControlCobro{
    @Autowired
    public ControlCobroTransferencia(VistaCobroTransferencia vista, ControlVenta controlVenta, ServicioVenta servicioVenta){
        super(vista, controlVenta, servicioVenta);
    }
}
