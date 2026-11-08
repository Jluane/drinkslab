package io.github.jluane.drinkslab.presentacion.Cobro;

import java.io.IOException;

import io.github.jluane.drinkslab.negocio.modelo.Venta;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

public abstract class VistaCobro {
    protected Stage stage;
    protected boolean initialized = false;

    // Controlador al que todas se conectan
    protected ControlCobro control;

    // Elementos FXML que comparten
    @FXML protected Label totalAPagar;

    /**
     * Se da el control que controlará a las ventanas
     * @param control Es específicamente el control que tendrá
     */
    public void setControlCobro(ControlCobro control){
        this.control = control;
    }

    /**
	 * Initialize UI components on the JavaFX application thread
	 */
	protected void initializeUI(String tipo) {
		if (initialized) {
			return;
		}
		
		// Create UI only if we're on JavaFX thread
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(() -> this.initializeUI(tipo));
			return;
		}
		
		try {
			stage = new Stage();
			stage.setTitle("Cobro con "+tipo);
			stage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/img/logo.png")));
			
			stage.initModality(Modality.APPLICATION_MODAL);

			// Load FXML
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana-cobro-"+tipo+".fxml"));
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
	 * Cierra la ventana
	 */
	public void cierra(){
		stage.close();
	}

	// On Action's
	@FXML
	public void onActionCancelar(){
		stage.close();
	}
	@FXML
	public void onActionPagar(){
		control.procesarPago();
	}

    public abstract void muestra(Venta venta);
}
