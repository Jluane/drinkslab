package io.github.jluane.drinkslab.presentacion.CobroTarjeta;

import java.io.IOException;

import org.springframework.stereotype.Component;

import io.github.jluane.drinkslab.negocio.modelo.Venta;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

@Component
public class VistaCobroTarjeta {
    private Stage stage;
    private boolean initialized = false;

    // Controlador de esta vista
    private ControlCobroTarjeta control;

    // Componentes del FXML
    @FXML private Label totalAPagar;

    // Constructor
    public VistaCobroTarjeta(){}

    /**
     * Se da el control que controlará a este panel
     * @param control Es específicamente el control que tendrá
     */
    public void setControlCobroTarjeta(ControlCobroTarjeta control){
        this.control = control;
    }

    /**
     * Inicializa con la vista
     */
    @FXML
    public void initialize(){
    }

    /**
	 * Initialize UI components on the JavaFX application thread
	 */
	private void initializeUI() {
		if (initialized) {
			return;
		}
		
		// Create UI only if we're on JavaFX thread
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(this::initializeUI);
			return;
		}
		
		try {
			stage = new Stage();
			stage.setTitle("Cobro con tarjeta");
			stage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/img/logo.png")));
			
			stage.initModality(Modality.APPLICATION_MODAL);

			// Load FXML
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana-cobro-tarjeta.fxml"));
			loader.setController(this);
			Scene scene = new Scene(loader.load(), 580, 440);
			
			stage.setScene(scene);
            stage.toFront();

			initialized = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		initializeUI();

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

    /**
	 * Cierra la ventana
	 */
	public void cierra(){
		stage.close();
	}

    // On Action's
	@FXML
	public void onActionCancelar(){
		System.out.println("Se presionó el botón cancelar");
		System.out.println("Se cerrará la pantalla emergente");
		stage.close();
	}
	@FXML
	public void onActionPagar(){
		System.out.println("Se presionó el botón Paga");
		control.procesarPago();
	}
}
