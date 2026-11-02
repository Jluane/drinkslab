package io.github.jluane.drinkslab.presentacion.CobroEfectivo;

import java.io.IOException;
import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import io.github.jluane.drinkslab.negocio.modelo.Venta;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

@Component 
public class VistaCobroEfectivo {
    private Stage stage;
    private boolean initialized = false;

	private BigDecimal totalVenta = BigDecimal.ZERO; // Inicializada para realizar calculos

    // Controlador de esta vista
    private ControlCobroEfectivo control;

    // Componentes del FXML
    @FXML private Label totalAPagar;
	@FXML private Label cambioAEntregar;
	@FXML private TextField dineroEntregado;
	@FXML private Label txtAviso;
	@FXML private Button btnConfirmarCobro;

    // Constructor obligatorio
    public VistaCobroEfectivo(){}

    /**
     * Se da el control que controlará a este panel
     * @param control Es específicamente el control que tendrá
     */
    public void setControlCobroEfectivo(ControlCobroEfectivo control){
        this.control = control;
    }

	/**
     * Inicialización, puesta principalmente para el temporizador de la notificación de alerta
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
			stage.setTitle("Cobro en efectivo");
			stage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/img/logo.png")));
			
			stage.initModality(Modality.APPLICATION_MODAL);

			// Load FXML
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana-cobro-efectivo.fxml"));
			loader.setController(this);
			Scene scene = new Scene(loader.load(), 580, 440);
			
			stage.setScene(scene);
            stage.toFront();

			initialized = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    public void muestra(Venta venta){
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(() -> this.muestra(venta));
			return;
		}
		initializeUI();

		colocarDatos(venta);
		dineroEntregado.requestFocus();

		stage.showAndWait();
    }

	private void colocarDatos(Venta venta){
		this.totalVenta = venta.getTotal();
		totalAPagar.setText("$"+totalVenta);
		dineroEntregado.setText("");
		cambioAEntregar.setText("$0");
	}

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

	public void mostrarError(boolean valor){
        txtAviso.setVisible(valor); 
        txtAviso.setDisable(!valor);

		if(valor)
			System.out.println("Mostrando error");
	}

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
