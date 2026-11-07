package io.github.jluane.drinkslab.presentacion.Cobro.Efectivo;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import io.github.jluane.drinkslab.negocio.modelo.Venta;
import io.github.jluane.drinkslab.presentacion.Cobro.VistaCobro;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

@Component 
public class VistaCobroEfectivo extends VistaCobro {
	private BigDecimal totalVenta = BigDecimal.ZERO; // Inicializada para realizar calculos

    // Componentes del FXML
	@FXML private Label cambioAEntregar;
	@FXML private TextField dineroEntregado;
	@FXML private Label txtAviso;
	@FXML private Button btnConfirmarCobro;

    // Constructor obligatorio
    public VistaCobroEfectivo(){}

	/**
     * Inicialización, puesta principalmente para el listener del input
     */
    @FXML
    public void initialize() {
        // Ata la propiedad "managed" del aviso a la propiedad "visible". Cuando se inicia o se oculta
        txtAviso.managedProperty().bind(txtAviso.visibleProperty());

		// Le añadimos un escuchador a la propiedad de texto del TextField
        dineroEntregado.textProperty().addListener((observable, valorViejo, valorNuevo) -> {
            calcularCambio(valorNuevo);
        });
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
		initializeUI("efectivo");

		colocarDatos(venta);
		dineroEntregado.requestFocus();

		stage.showAndWait();
    }

	/**
	 * Método encargadoo de colocar los datos de la venta que se cobrará
	 * @param venta
	 */
	private void colocarDatos(Venta venta){
		this.totalVenta = venta.getTotal();
		totalAPagar.setText("$"+totalVenta);
		dineroEntregado.setText("");
		cambioAEntregar.setText("$0");
	}

	/**
	 * Método activado cada que sucede un cambio en la cantidad ingresada. Se calcula el cambio y/o se alerta en caso de que se hayan ingresado caracteres inválidos
	 * @param textoIngresado Es el texto que le es ingresado al textinput
	 */
	private void calcularCambio(String textoIngresado){
		BigDecimal entregado = BigDecimal.ZERO;

		try{
			if(textoIngresado != null && !textoIngresado.trim().isEmpty()){
				entregado = new BigDecimal(textoIngresado);
			}
			mostrarError(false);
		}catch(Exception e){
			mostrarError(true);
			System.out.println("Entrada inválida en el teclado: " + textoIngresado);
			mostrarError(true);
		}

		BigDecimal restante = entregado.subtract(totalVenta);

		if (restante.compareTo(BigDecimal.ZERO) < 0) {
            cambioAEntregar.setText("$0");
            btnConfirmarCobro.setDisable(true);
        } else {
            cambioAEntregar.setText("$" + restante);
			btnConfirmarCobro.setDisable(false);
        }
	}

	/**
	 * Método encargado de mostrar un mensaje de error u ocultarlo
	 * @param valor Especifica si se quiere mostrar u ocultar
	 */
	public void mostrarError(boolean valor){
        txtAviso.setVisible(valor); 
        txtAviso.setDisable(!valor);

		if(valor)
			System.out.println("Mostrando error");
	}
}
