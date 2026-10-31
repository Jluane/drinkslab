package io.github.jluane.drinkslab.presentacion.Venta;

import java.io.IOException;

import org.springframework.stereotype.Component;

import io.github.jluane.drinkslab.negocio.modelo.DetalleVenta;
import io.github.jluane.drinkslab.negocio.modelo.Venta;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

@Component
public class VistaVenta {
    
    // Controlador de esta pantalla
    private ControlVenta control;

    private PauseTransition temporizadorAviso;

    // Componentes del FXML
    // Botones
    @FXML
    private Button btnBuscarEnter;
    @FXML
    private Button btnPagarF12;
    @FXML
    private Button btnTarjetaF10;
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
        // EL TRUCO PROFESIONAL: Ata la propiedad "managed" a la propiedad "visible"
        txtAviso.managedProperty().bind(txtAviso.visibleProperty());

        // Configuración del temporizador
        temporizadorAviso = new PauseTransition(Duration.seconds(3));
        temporizadorAviso.setOnFinished(evento -> {
            txtAviso.setVisible(false); // Al ocultarse, el bind automáticamente quita el espacio
            txtAviso.setDisable(true);
        });
    }

    /**
     * Carga el FXML y devuelve el panel visual listo
     */
    public Parent cargaPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana-venta.fxml")); // Ajusta el nombre de tu archivo .fxml
            loader.setController(this);
            Parent parent = loader.load();
            return parent;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
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
        System.out.println("Se mató gráficamente al detalle");
    }

    /**
     * Inicializa todos los datos de manera en que tenga aspecto de una venta nueva
     * @param idxVenta Es el id de la venta sobre la que se está
     */
    public void inicia(int idxVenta){
        cajaVentas.getChildren().clear();
        numVenta.setText("ID VENTA: #"+idxVenta);
        cantVenta.setText(0+" artículo "+"("+0+" unid.)");
        sumaVenta.setText("$0");
        totalVenta.setText("$0");
        totalPagar.setText("$0");
        btnPagarF12.setDisable(true);
        btnTarjetaF10.setDisable(true);
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
        for(DetalleVenta det : venta.getDetalles()){
            productos = productos + det.getCantidad();
        }

        // Coloca los datos en los Label correspondientes
        cantVenta.setText(articulos+" artículo "+"("+productos+" unid.)");
        sumaVenta.setText("$"+venta.getTotal());
        totalVenta.setText("$"+venta.getTotal());
        totalPagar.setText("$"+venta.getTotal());

        // Dependiendo de si es una venta válida o no, se habilita o deshabilitan los botones
        if(productos <= 0){
            btnPagarF12.setDisable(true);
            btnTarjetaF10.setDisable(true);
        }else{
            btnPagarF12.setDisable(false);
            btnTarjetaF10.setDisable(false);
        }
        pedirFocoBuscador(); // Solicita de vuelta el foco para el buscador para evitar que se quede el foco en el ítem modificado
    }

    /**
     * Solicita el enfoque en el texto de búsqueda para asegurarse de que es esta pantalla a la que se está apuntando
     */
    private void pedirFocoBuscador(){
        txtBusqueda.requestFocus();
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

        System.out.println("Mostrando error con mensaje:\n" +'"'+motivo+'"');
        temporizadorAviso.playFromStart();
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
            System.out.println("Se presinó tecla F12");
            btnPagarF12.fire(); // Presiona el botón automáticamente
        } 
        else if (event.getCode() == KeyCode.F2) {
            System.out.println("Se presionó tecla F2");
            txtBusqueda.requestFocus(); // Pone el cursor en el buscador
        }
        else if(event.getCode() == KeyCode.F10) {
            System.out.println("Se presionó tecla F10");
            btnTarjetaF10.fire();
        }
        else if (event.getCode().isLetterKey() || event.getCode().isDigitKey()) {
            // Si presionaron una letra o número, pero el buscador NO tenía el foco
            if (!txtBusqueda.isFocused()) {
                System.out.println("Tecla perdida detectada");
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
            System.out.println("Buscando: " + texto);

            control.agregarProducto(texto);
            // Aquí llamas a tu controlador: control.buscarProducto(texto);
            txtBusqueda.clear(); // Limpias el campo después de buscar
        }else{
            System.out.println("Se buscó cadena vacía");
        }
    }
    /**
     * Método dado al presionar el botón pagar
     */
    @FXML
    public void onActionPagar(){
        System.out.println("Se presionó botón pagar");
    }
    /**
     * Método dado al presiona el botón pagar con tarjeta
     */
    @FXML
    public void onActionTarjeta(){
        System.out.println("Se presionó botón pagar con tarjeta");
    }

}
