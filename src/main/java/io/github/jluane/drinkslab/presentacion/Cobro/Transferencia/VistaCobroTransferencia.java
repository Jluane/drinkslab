package io.github.jluane.drinkslab.presentacion.Cobro.Transferencia;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.github.jluane.drinkslab.negocio.modelo.Venta;
import io.github.jluane.drinkslab.presentacion.Cobro.VistaCobro;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

@Component
public class VistaCobroTransferencia extends VistaCobro {
    // Elementos FXML
    @FXML
    private Label clabe;
    
    @Value("${tienda.cobro.transferencia.clabe:CLABE pendiente de configurar con application-local.yaml}")
    private String txtClabe; // Texto que deberá estar en el clabe

    // Constructor
    public VistaCobroTransferencia(){}

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
		initializeUI("transferencia");

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
        clabe.setText(txtClabe);
	}
}
