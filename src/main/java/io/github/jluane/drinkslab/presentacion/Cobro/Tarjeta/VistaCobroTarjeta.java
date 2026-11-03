package io.github.jluane.drinkslab.presentacion.Cobro.Tarjeta;

import org.springframework.stereotype.Component;

import io.github.jluane.drinkslab.negocio.modelo.Venta;
import io.github.jluane.drinkslab.presentacion.Cobro.VistaCobro;
import javafx.application.Platform;
import javafx.fxml.FXML;

@Component
public class VistaCobroTarjeta extends VistaCobro{
    // Constructor
    public VistaCobroTarjeta(){}

    /**
     * Inicializa con la vista
     */
    @FXML
    public void initialize(){
    }

    /**
	 * Método en el que se muestra toda la pantalla, así mismo se colocan los datos necesarios
	 * @param venta
	 */
    public void muestra(Venta venta){
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(() -> this.muestra(venta));
			return;
		}
		initializeUI("tarjeta");

		colocarDatos(venta);

		stage.showAndWait();
    }

    /**
	 * Método encargadoo de colocar los datos de la venta que se cobrará
	 * @param venta
	 */
	private void colocarDatos(Venta venta){
		String totalVenta = venta.getTotal().toString();
		totalAPagar.setText("$"+totalVenta);
	}
}
