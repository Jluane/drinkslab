package io.github.jluane.drinkslab.presentacion.Venta;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Component;

import io.github.jluane.drinkslab.negocio.modelo.DetalleVenta;
import io.github.jluane.drinkslab.negocio.modelo.Venta;
import javafx.animation.PauseTransition;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.util.Duration;

@Component
public class VistaVenta {
    
    // Controlador de esta pantalla
    private ControlVenta control;

    private PauseTransition temporizadorAviso; // Variable para temporizador del aviso
    // Variables para las sugerencias del buscador
    private Popup popupSugerencias;
    private ListView<String> listaSugerencias;
    private final PauseTransition esperaBusqueda = new PauseTransition(Duration.millis(250));
    private Task<List<String>> tareaSugerencias;
    private final ExecutorService hiloSugerencias = Executors.newSingleThreadExecutor(r -> {
        Thread hilo = new Thread(r, "sugerencias");
        hilo.setDaemon(true); // No impide que la aplicación cierre
        return hilo;
    });

    // Componentes del FXML
    // Botones
    @FXML
    private Button btnBuscarEnter;
    @FXML
    private Button btnPagarF12;
    @FXML
    private Button btnTarjetaF10;
    @FXML
    private Button btnTransferenciaF8;
    @FXML
    private TextField txtBusqueda;
    // Contenedores
    @FXML
    private VBox cajaVentas; // Detalles Ventas
    // Textos
    @FXML
    private Label txtAviso;
    // Partes de Venta
    @FXML
    private Label numVenta;
    @FXML
    private Label cantVenta;
    @FXML
    private Label sumaVenta;
    @FXML
    private Label totalVenta;
    @FXML
    private Label totalPagar;

    // Constructor necesario para la clase
    public VistaVenta(){}

    /**
     * Se da el control que controlará a este panel
     * @param control Es específicamente el control que tendrá
     */
    public void setControlVenta(ControlVenta control){
        this.control = control;
    }

    /**
     * Inicialización, puesta principalmente para el temporizador de la notificación de alerta
     */
    @FXML
    public void initialize() {
        // Ata la propiedad "managed" a la propiedad "visible" del aviso
        txtAviso.managedProperty().bind(txtAviso.visibleProperty());

        // Configuración del temporizador
        temporizadorAviso = new PauseTransition(Duration.seconds(5));
        temporizadorAviso.setOnFinished(evento -> {
            txtAviso.setVisible(false); // Al ocultarse, el bind automáticamente quita el espacio
            txtAviso.setDisable(true);
        });

        // Configuración para el buscador y sus sugerencias
        popupSugerencias = new Popup(); // Inicializamos el popup
        listaSugerencias = new ListView<>(); // Inicializamos a la lista de vista
        popupSugerencias.getContent().add(listaSugerencias); // Agregamos la lista al popup
        popupSugerencias.setAutoHide(true); // Se ocultará automáticamente al dar click en otro lado
        // Cada que se actualice el valor de la busqueda se decide si se muestran las sugerencias o se esconde el popup
        txtBusqueda.textProperty().addListener((observable, viejoValor, nuevoValor) -> {
            esperaBusqueda.stop();
            String texto = (nuevoValor == null) ? "" : nuevoValor.trim();

            // Muy corto, o solo números (es un código de barras): sin sugerencias
            if (texto.length() < 2 || texto.matches("\\d+")) {
                popupSugerencias.hide();
                return;
            }
            esperaBusqueda.setOnFinished(e -> mostrarSugerencias(texto));
            esperaBusqueda.playFromStart();
        });
        // Cuando se da click a una sugerencia, se coloca realiza la busqueda
        listaSugerencias.setOnMouseClicked(event -> {
            String seleccionado = listaSugerencias.getSelectionModel().getSelectedItem();
            if (seleccionado != null) {
                txtBusqueda.setText(seleccionado); // Pone el nombre en el buscador
                popupSugerencias.hide();
                onActionBuscar(); // Dispara la búsqueda automáticamente
            }
        });
        // Cuando se presiona Enter sobre uno, realiza la misma acción que la búsqueda
        listaSugerencias.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String seleccionado = listaSugerencias.getSelectionModel().getSelectedItem();
                if (seleccionado != null) {
                    txtBusqueda.setText(seleccionado); // Pone el nombre en el buscador
                    popupSugerencias.hide();
                    onActionBuscar(); // Dispara la búsqueda automáticamente
                }
                event.consume(); // Previene que el Enter dispare otras cosas
            }
        });
    }

    /**
     * Carga el FXML y devuelve el panel visual listo
     */
    public Parent cargaPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana-venta.fxml"));
            loader.setController(this);
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Muestra las sugerencias del popup
     * @param texto Es el texto ingresado para saber qué sugerencias se deben mostrar
     */
    private void mostrarSugerencias(String texto) {
        if (tareaSugerencias != null) tareaSugerencias.cancel();

        Task<List<String>> tarea = new Task<>() {
            @Override
            protected List<String> call() {
                return control.obtenerSugerenciasNombres(texto); // Fuera del hilo de JavaFX
            }
        };
        tarea.setOnSucceeded(e -> {
            // Si el usuario ya escribió otra cosa, se descarta este resultado
            if (texto.equals(txtBusqueda.getText().trim())) {
                mostrarPopup(tarea.getValue());
            }
        });
        tarea.setOnFailed(e -> popupSugerencias.hide());

        tareaSugerencias = tarea;
        hiloSugerencias.submit(tarea);
    }
    private void mostrarPopup(List<String> resultados) {
        if (resultados.isEmpty()) {
            popupSugerencias.hide();
            return;
        }
        listaSugerencias.getItems().setAll(resultados);
        listaSugerencias.setPrefWidth(txtBusqueda.getWidth());
        listaSugerencias.setPrefHeight(resultados.size() * 24 + 5);

        Bounds bounds = txtBusqueda.localToScreen(txtBusqueda.getBoundsInLocal());
        if (bounds != null) {
            popupSugerencias.show(txtBusqueda, bounds.getMinX(), bounds.getMaxY());
        }
    }

    /**
     * Manda a crear un ítem de detalle venta con el objeto del que el ítem sacará la información
     * @param detalle Es el detalle con la información necesaria para el ítem
     * @return Regresa al objeto del item
     */
    public itemDetalleVenta crearDetalleVenta(DetalleVenta detalle){
        try{
            // Carga a la plantilla de la tarjeta de los pedidos
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/item-detalle-venta.fxml"));
            itemDetalleVenta tarjeta = new itemDetalleVenta(); // Se asigna el controlador hecho a la tarjeta
            tarjeta.setControlVenta(control); // tarjeta.setControl(controlRegistroPedido); // Se da que este controlador tiene el control sobre las tarjetas, para apoyar la comunicación con los servicios
            loader.setController(tarjeta);
            Node tarjetaVisual = loader.load(); // Se inicializa la tarjeta
            tarjeta.configurarDatos(detalle); // Mediante el controlador se llama al método para ingresar la información
            cajaVentas.getChildren().add(tarjetaVisual); // Se agrega la tarjeta al contenedor
            return tarjeta;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Elimina al ítem que le llega, eliminandolo del contenedor en el que fue puesto
     * @param nodoVisual // Es el visual del ítem que se eliminará
     */
    public void eliminarDetalleVenta(GridPane nodoVisual){
        cajaVentas.getChildren().remove(nodoVisual);
    }

    /**
     * Inicializa todos los datos de manera en que tenga aspecto de una venta nueva
     * @param idxVenta Es el id de la venta sobre la que se está
     */
    public void inicia(Long idxVenta){
        cajaVentas.getChildren().clear();
        numVenta.setText("ID VENTA: #"+idxVenta);
        cantVenta.setText(0+" artículo "+"("+0+" unid.)");
        sumaVenta.setText("$0");
        totalVenta.setText("$0");
        totalPagar.setText("$0");
        habilitarBotones(false);
        pedirFocoBuscador();
    }

    /**
     * Actualiza los datos que son de la venta
     * @param venta
     */
    public void actualizarVenta(Venta venta){
        // Obtiene los datos que se pondrán
        int articulos = venta.getDetalles().size();
        int productos = 0;

        // Suma de los productos totales
        for(DetalleVenta det : venta.getDetalles()){
            // Si el artículo es a granel entonces sólo se sumará 1 como producto
            try{
                productos = productos + det.getCantidad().intValueExact();
            } catch(Exception e){
                productos++;
            }
        }

        // Coloca los datos en los Label correspondientes
        cantVenta.setText(articulos+" artículo "+"("+productos+" unid.)");
        sumaVenta.setText("$"+venta.getTotal());
        totalVenta.setText("$"+venta.getTotal());
        totalPagar.setText("$"+venta.getTotal());

        // Dependiendo de si es una venta válida o no, se habilita o deshabilitan los botones
        if(productos <= 0){
            habilitarBotones(false);
        }else{
            habilitarBotones(true);
        }
    }

    /**
     * Solicita el enfoque en el texto de búsqueda para asegurarse de que es esta pantalla a la que se está apuntando
     */
    public void pedirFocoBuscador(){
        txtBusqueda.requestFocus();
    }
    
    /**
     * Mëtodo para decidir cómo se configuran los botones en la aplicación
     * @param hab Variable que determina si se muestran o no
     */
    private void habilitarBotones(boolean hab){
        btnPagarF12.setDisable(!hab);
        btnTarjetaF10.setDisable(!hab);
        btnTransferenciaF8.setDisable(!hab);
    }

    /**
     * Muestra un aviso de error
     * @param motivo Es el mensaje con el que se presentará el aviso de error
     */
    public void mostrarErrorTemporal(String motivo) {
        txtAviso.setText(motivo);
        
        // Al ejecutar esto, el bind avisa al VBox en tiempo real y hace el redibujado instantáneo
        txtAviso.setVisible(true); 
        txtAviso.setDisable(false);

        temporizadorAviso.playFromStart();
    }

    public void aplicarDifuminado(){ColorAdjust oscurecer = new ColorAdjust();
        oscurecer.setBrightness(-0.5); // Reduce el brillo un 50% (rango de -1.0 a 0.0)

        txtBusqueda.getScene().getRoot().setEffect(oscurecer);
    }
    public void quitarDifuminado(){
        txtBusqueda.getScene().getRoot().setEffect(null);
    }

    // Atajos
    /**
     * Es el método que detecta las presiones de teclas 
     * @param event Es la tecla que fue presionada
     */
    @FXML
    public void onAtajoTeclado(KeyEvent event){
        // El Enter está definido como Default Button dentro de FXML
        if (event.getCode() == KeyCode.F12) {
            btnPagarF12.fire(); // Presiona el botón automáticamente
        } 
        else if (event.getCode() == KeyCode.F2) {
            txtBusqueda.requestFocus(); // Pone el cursor en el buscador
        }
        else if(event.getCode() == KeyCode.F10) {
            btnTarjetaF10.fire();
        }
        else if(event.getCode() == KeyCode.F8){
            btnTransferenciaF8.fire();
        }
        else if (event.getCode().isLetterKey() || event.getCode().isDigitKey()) {
            // Si presionaron una letra o número, pero el buscador NO tenía el foco
            if (!txtBusqueda.isFocused()) {
                txtBusqueda.clear();
                txtBusqueda.requestFocus();
            }
        }
    }

    // OnAction's
    /**
     * Método dado al presionar el botón Busca o presionar Enter
     */
    @FXML
    public void onActionBuscar(){
        String texto = txtBusqueda.getText();
        if (!texto.isEmpty()) {

            control.agregarProducto(texto, false);
            // Aquí llamas a tu controlador: control.buscarProducto(texto);
            txtBusqueda.clear(); // Limpias el campo después de buscar
        }else{
        }
    }
    /**
     * Método dado al presionar el botón pagar
     */
    @FXML
    public void onActionPagar(){
        control.cobroEfectivo();
    }
    /**
     * Método dado al presiona el botón pagar con tarjeta
     */
    @FXML
    public void onActionTarjeta(){
        control.cobroTarjeta();
    }
    /**
     * Método dado al presionar el botón para transferencias
     */
    @FXML
    public void onActionTransferencia(){
        control.cobroTransferencia();
    }

}
