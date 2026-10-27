package io.github.jluane.drinkslab.presentacion;

import java.io.IOException;

import org.springframework.stereotype.Component;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

@Component
public class VistaMain {
    
    private Stage stage;
    private ControlMain control;

    private boolean initialized = false;

    // Constructor necesario para la clase
    public VistaMain(){}

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
			stage.setTitle("Drinks Lab"); // El título que toma la pantalla
			stage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/img/logo.png"))); // El logo de la aplicación
			
			// Load FXML
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainLayout.fxml"));
			loader.setController(this);
			Scene scene = new Scene(loader.load(), 1024, 768);
			
			stage.setScene(scene);
			stage.setMaximized(true); // Se dibuja en pantalla maximizada
            stage.toFront(); // Manda a la pantalla al frente
			
			initialized = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
	}

    /**
	 * Establece el controlador asociado a esta ventana
	 * 
	 * @param control El controlador asociado
	 */
	public void setControlMain(ControlMain control) {
		this.control = control;
	}

    /**
	 * Muestra la pantalla principal
	 */
    public void muestra(){
        if (!Platform.isFxApplicationThread()) {
			Platform.runLater(() -> this.muestra());
			return;
		}
		initializeUI();
        // Espacio para posibles métodos
		stage.show();
    }

}
