package io.github.jluane.drinkslab.presentacion.GestionProducto;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import io.github.jluane.drinkslab.negocio.modelo.Producto;
import io.github.jluane.drinkslab.negocio.modelo.Proveedor;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

@Component
public class VistaGestionProducto {
    private Stage stage;
    private boolean initialized = false;

	private PauseTransition temporizadorAviso; // Variable para el temporizador de la alerta

    // Controlador al que pertenece
    private ControlGestionProducto control;

    // Elementos FXML
	@FXML private Label lblTitulo;
	@FXML private Button btnEliminar;
	@FXML private Label lblAviso;
	@FXML private ScrollPane scroll;
	@FXML private HBox boxPaquete;
	@FXML private HBox boxPromocion;
	@FXML private CheckBox aGranel, paquete, promocion;
	@FXML private VBox cajaProveedores;
	@FXML private ComboBox<String> comboProveedores;
	// Fields para escribir
	@FXML private TextField nombre, codigo, precio, cant, pzsPaquete, precioPaquete, cantPromo, precioPromo;

    // Constructor
    public VistaGestionProducto(){}

    @FXML
    public void initialize(){
		// Configuración del temporizador
        temporizadorAviso = new PauseTransition(Duration.seconds(5));
        temporizadorAviso.setOnFinished(evento -> {
            habilitarMostrar(lblAviso, false);
        });

		// En caso de que se cierre la pantalla
		stage.setOnCloseRequest(event -> {
			control.cierra();
		});
		stage.setOnHidden(event -> {
			control.cierra();
		});
    }

    /**
     * Se da el control que controlará a las ventanas
     * @param control Es específicamente el control que tendrá
     */
    public void setControlGestionProducto(ControlGestionProducto control){
        this.control = control;
    }

    /**
	 * Initialize UI components on the JavaFX application thread
	 */
	protected void initializeUI(String accion) {
		if (initialized) {
			return;
		}
		
		// Create UI only if we're on JavaFX thread
		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(() -> this.initializeUI(accion));
			return;
		}
		
		try {
			stage = new Stage();
			stage.setTitle(accion+" producto");
			stage.getIcons().add(new javafx.scene.image.Image(getClass().getResourceAsStream("/img/logo.png")));
			
			stage.initModality(Modality.APPLICATION_MODAL);

			// Load FXML
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana-gestion-producto.fxml"));
			loader.setController(this);
			Scene scene = new Scene(loader.load(), 600, 768);
			
			stage.setScene(scene);
            stage.toFront();

			initialized = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Método con el que inicializa todo. Todos los valores se dejan desactivados
	 * @param producto
	 * @param existe
	 */
	public void muestra(Producto producto, boolean existe){
		// Determinamos si estamos creando o modificando
		String accion = "";
		if(existe) accion = "Modificar";
		else accion = "Crear";

		if (!Platform.isFxApplicationThread()) {
			Platform.runLater(() -> this.muestra(producto, existe));
			return;
		}
		initializeUI(accion);
		stage.toFront();
		stage.setTitle(accion+" producto");
		
		lblTitulo.setText(accion+" producto");
		habilitarMostrar(btnEliminar, existe);
		habilitarMostrar(lblAviso, false);
		scroll.setVvalue(0.0);
		scroll.setHvalue(0.0);
		habilitarMostrar(boxPaquete, false);
		habilitarMostrar(boxPromocion, false);
		aGranel.setSelected(false);
		paquete.setSelected(false);
		promocion.setSelected(false);
		nombre.requestFocus();

		escribirDatos(producto);

		stage.showAndWait();
	}

	/**
	 * Mëtodo encargado de pintar en los fields los datos del producto que se haya dado
	 * @param producto Es el producto del que se sacarán los datos
	 */
	private void escribirDatos(Producto producto){
		nombre.setText(producto.getNombre());
		codigo.setText(producto.getGTIN());
		precio.setText(producto.getPrecioUnitario().toString());
		cant.setText(producto.getCantInv().toString());
		pzsPaquete.setText(producto.getCantPaquete().toString());
		precioPaquete.setText(producto.getPrecioPaquete().toString());
		cantPromo.setText(producto.getCantPromocion().toString());
		precioPromo.setText(producto.getPrecioPromocion().toString());
		comboProveedores.setValue(null);
		
		if(producto.getCantPaquete() != null && producto.getCantPaquete().compareTo(BigDecimal.ZERO) != 0){
			paquete.setSelected(true);
			habilitarMostrar(boxPaquete, true);
		}
		if(producto.getCantPromocion() != null && producto.getCantPromocion().compareTo(BigDecimal.ZERO) != 0){
			promocion.setSelected(true);
			habilitarMostrar(boxPromocion, true);
		}
		if(producto.getAGranel())
			aGranel.setSelected(true);
		else{
			aGranel.setSelected(false);
		}

		// Colocar a los proveedores
		limpiarCajaProveedores();
		control.colocarProveedores();
	}

	/**
	 * Limpia la caja de proveedores
	 */
	public void limpiarCajaProveedores(){
		cajaProveedores.getChildren().clear();
	}
	/**
	 * Manda a crear al item con la información deseada
	 * @param prov Es el proveedor del que se saca la información
	 */
	public void mostrarProveedoresSeleccionados(Proveedor prov){
		try{
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/item-proveedor.fxml"));
			ItemProveedor tarjeta = new ItemProveedor();
			tarjeta.setControlGestionProducto(control);
			loader.setController(tarjeta);
			Node tarjetaVisual = loader.load();
			tarjeta.configurarDatos(prov);
			cajaProveedores.getChildren().add(tarjetaVisual);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	/**
	 * Enseña a todos los proveedores en el Combo que los contiene
	 * @param nombres Es la lista de nombres de los proveedores
	 */
	public void mostrarProveedores(List<String> nombres){
		comboProveedores.setItems(FXCollections.observableArrayList(nombres));
	}

	// --------------------- HERRAMIENTAS ---------------------
    /**
	 * Cierra la ventana
	 */
	public void cierra(){
		stage.close();
	}
	/**
	 * Habilita o deshabilita a un elemento FXML
	 * @param objeto Es el elemento a habilitar o deshabilitar
	 * @param habilitar Decide si se habilita o se deshabilita
	 */
	private void habilitarMostrar(Node objeto, boolean habilitar){
		objeto.setVisible(habilitar);
        objeto.setDisable(!habilitar);
        objeto.setManaged(habilitar);
	}
	/**
	 * Mëtodo que revisa si todos los input cumplen con lo requerido para ser guardados
	 * @return Regresa true si todos cumplen, false si alguno no lo hace
	 */
	private boolean validarDatos(){
		boolean valido = true;
		boolean aGranel = this.aGranel.isSelected();

		if(nombre.getText() == null || nombre.getText().isBlank() || nombre.getText().matches("\\d+")){
			nombre.setText("");
			valido = false;
		}

		if(!validarEntero(codigo.getText())){
			codigo.setText("");
			valido = false;
		}

		if(!validarBigDecimal(precio.getText())){
			precio.setText("0");
			valido = false;
		}

		try{
			BigDecimal cantidadInv = new BigDecimal(cant.getText());
			// Si es -1, se marcará como inventario desconocido, pero es válido
			if(cantidadInv.compareTo(BigDecimal.ZERO) < 0){
				cant.setText("-1");
			}
			if(!aGranel){
				cantidadInv.intValueExact();
			}
		}catch(Exception e){
			cant.setText("");
			valido = false;
		}
		
		if(paquete.isSelected()){
			if(!validarCantidades(pzsPaquete.getText(), aGranel)){
				pzsPaquete.setText("");
				valido = false;
			}
			if(!validarBigDecimal(precioPaquete.getText())){
				precioPaquete.setText("");
				valido = false;
			}
		}else{
			pzsPaquete.setText("0");
			precioPaquete.setText("0");
		}

		if(promocion.isSelected()){
			if(!validarCantidades(cantPromo.getText(), aGranel)){
				cantPromo.setText("");
				valido = false;
			}
			if(!validarBigDecimal(precioPromo.getText())){
				precioPromo.setText("");
				valido = false;
			}
		}else{
			cantPromo.setText("0");
			precioPromo.setText("0");
		}

		return valido;
	}
	/**
	 * Revisa si un texto puede ser un bigdecimal positivo
	 * @param texto Es el texto a evaluar
	 * @return Regresa true si el texto puso ser convertido a BigDecimal positivo, de lo contrario, false
	 */
	private boolean validarBigDecimal(String texto){
		try {
			BigDecimal bd = new BigDecimal(texto); // Verificamos que sea posible construirlo
			if(bd.compareTo(BigDecimal.ZERO) < 0){
				// Si es negativo, regresamos falso
				return false;
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	/**
	 * Revisa si un texto puede ser un entero positivo
	 * @param texto Es el texto a evaluar
	 * @return Regresa true si el texto pudo ser converitdo a entero positivo, false si no
	 */
	private boolean validarEntero(String texto){
		if(texto.matches("0*(\\.0*)?")) return false;
		if(texto.matches("\\d+(\\.0*)?")){
			return true;
		}
		return false;
	}
	/**
	 * Revisa si un texto es válido de acuerdo a si se admiten puntos decimales o no
	 * @param texto Es el texto a evaluar
	 * @param aGranel Determina si se admiten puntos decimales o no
	 * @return Regresa true si se la cadena cumple el valor, false si no lo cumple
	 */
	private boolean validarCantidades(String texto, boolean aGranel){
		if(aGranel){
			if(!validarBigDecimal(texto)){
				return false;
			}
		}else{
			if(!validarEntero(texto)){
				return false;
			}
		}
		return true;
	}
	/**
	 * Método encargado de mostrar la alerta de que no se escribieron bien algunos datos
	 */
	public void mostrarAlerta(String texto){
		habilitarMostrar(lblAviso, true);
		lblAviso.setText(texto);
		scroll.setVvalue(0.0);
		scroll.setHvalue(0.0);

        System.out.println("Mostrando error: "+texto);
        temporizadorAviso.playFromStart();
	}
	/**
	 * Ingresa el texto dado en el field de código
	 * @param codigo
	 */
	public void ingresarCodigo(String codigo){
		this.codigo.setText(codigo);
	}

	// ------------------ ON ACTION'S ------------------
	@FXML
	public void onActionPaquete(ActionEvent event){
		CheckBox botonPresionado = (CheckBox) event.getSource();

		// Evaluamos si fue seleccionado o deseleccionado
		boolean seleccionado = botonPresionado.isSelected();
		habilitarMostrar(boxPaquete, seleccionado);
	}
	@FXML
	public void onActionPromocion(ActionEvent event){
		CheckBox botonPresionado = (CheckBox) event.getSource();

		// Evaluamos si fue seleccionado o deseleccionado
		boolean seleccionado = botonPresionado.isSelected();
		habilitarMostrar(boxPromocion, seleccionado);
	}
	@FXML
	public void onActionGuardar(){
		System.out.println("Se presionó Guardar");
		if(!validarDatos()){
			control.mostrarAlerta("Asegúrate de ingresar los datos correctamente");
		}else{
			control.guardarProducto(nombre.getText(), codigo.getText(), precio.getText(), cant.getText(), pzsPaquete.getText(), precioPaquete.getText(), cantPromo.getText(), precioPromo.getText(), aGranel.isSelected());
		}
	}
	@FXML
	public void onActionCancelar(){
		System.out.println("Se presionó Cancelar");
		control.cierra();
	}
	@FXML
	public void onActionEliminar(){
		System.out.println("Se presionó eliminar");
		control.confirmacionEliminarProducto();
	}
	@FXML
	public void onActionAgregarProveedor(Event event){
		System.out.println("Se presionó el agregar proveedor");

		control.agregarProveedor(comboProveedores.getValue());
		
		comboProveedores.setValue(null);
		event.consume();
	}
}
