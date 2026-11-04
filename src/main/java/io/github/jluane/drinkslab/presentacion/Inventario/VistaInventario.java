package io.github.jluane.drinkslab.presentacion.Inventario;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Component;

import io.github.jluane.drinkslab.negocio.modelo.Producto;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;

@Component
public class VistaInventario {
    // Controlador de esta pantalla
    private ControlInventario control;

    // Variables
    private int cantidadPaginas;
    private ToggleGroup botonesPagina = new ToggleGroup();
    // Variables para las sugerencias del buscador
    private Popup popupSugerencias;
    private ListView<String> listaSugerencias;

    // Componentes FXML
    @FXML private TextField txtBusqueda;
    @FXML private VBox cajaProductos;
    @FXML private Label txtProductosRegistrados;
    @FXML private Label txtProductosMostrados;
    @FXML private HBox contenedorBotonesPagina;

    // Constructor
    public VistaInventario(){}

    /**
     * Se da el control que controlará a este panel
     * @param control Es específicamente el control que tendrá
     */
    public void setControlInventario(ControlInventario control){
        this.control = control;
    }

    /**
     * Inicialización
     */
    @FXML
    public void initialize(){
        // Configuración para el buscador y sus sugerencias
        popupSugerencias = new Popup(); // Inicializamos el popup
        listaSugerencias = new ListView<>(); // Inicializamos a la lista de vista
        popupSugerencias.getContent().add(listaSugerencias); // Agregamos la lista al popup
        popupSugerencias.setAutoHide(true); // Se ocultará automáticamente al dar click en otro lado
        // Cada que se actualice el valor de la busqueda se decide si se muestran las sugerencias o se esconde el popup
        txtBusqueda.textProperty().addListener((observable, viejoValor, nuevoValor) -> {
            if (nuevoValor == null || nuevoValor.trim().isEmpty()) {
                popupSugerencias.hide();
            } else {
                mostrarSugerencias(nuevoValor);
            }
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana-inventario.fxml"));
            loader.setController(this);
            Parent parent = loader.load();
            return parent;
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
        // Pedimos las sugerencias al controlador
        List<String> resultados = control.obtenerSugerenciasNombres(texto);

        if (resultados.isEmpty()) {
            popupSugerencias.hide();
        } else {
            // Llenamos la lista visual con los resultados
            listaSugerencias.getItems().setAll(resultados);
            
            // Ajustamos el ancho para que sea idéntico al del TextField
            listaSugerencias.setPrefWidth(txtBusqueda.getWidth());
            
            // Calculamos una altura dinámica (por ejemplo, 24px por cada ítem)
            listaSugerencias.setPrefHeight(resultados.size() * 24 + 5);

            // Obtenemos las coordenadas exactas del txtBusqueda en el monitor
            Bounds bounds = txtBusqueda.localToScreen(txtBusqueda.getBoundsInLocal());
            
            if (bounds != null) {
                // Mostramos el popup justo debajo del TextField
                popupSugerencias.show(txtBusqueda, bounds.getMinX(), bounds.getMaxY());
            }
        }
    }

    /**
     * Inicia la pantalla de inventario
     * @param cantProductos Es la cantidad de productos que se encuentran en repositorio
     * @param paginaActual Es la página en la que nos encontramos
     */
    public void inicia(int cantProductos, int paginaActual){
        txtProductosRegistrados.setText(Integer.toString(cantProductos));
        txtProductosMostrados.setText("Mostrando máximo 8 de "+cantProductos+" productos en memoria");
        calcularPaginas(cantProductos);
        seleccionarBotonPagina(paginaActual);
        txtBusqueda.requestFocus();
    }

    /**
     * Método que calcula la cantidad de páginas que se necesitan para mostrar a todos los productos
     * @param cantProductos Cantidad de productos que se tienen
     */
    private void calcularPaginas(int cantProductos){
        System.out.println("Entra a calcular paginas");
        // Calculamos la cantidad de grupos que queremos
        int bloques = cantProductos/8;
        if(cantProductos%8 != 0){ // Si no es justamente un bloque, agregamos uno más para el residuo
            bloques++;
        }
        this.cantidadPaginas = bloques;
        System.out.println("Cantidad de páginas: "+cantidadPaginas);
    }

    /**
     * Pinta todos los botones de acuerdo a la página en la que se está y la cantidad total de páginas
     * @param pagina Es la página en la que nos encontramos
     */
    private void seleccionarBotonPagina(int pagina){
        System.out.println("Entra a seleccionar botón pagina");
        contenedorBotonesPagina.getChildren().clear();
        botonesPagina.getToggles().clear();

        // Si hay 5 páginas o menos, se pintan todos los botones
        if(cantidadPaginas < 5){
            System.out.println("Son menos de 5 páginas");
            // Vamos de uno en uno para pintar los botones
            for(int i=1; i<=cantidadPaginas; i++){
                if(i == pagina) // Si estamos en la página seleccionada, al crear el botón lo ponemos como seleccionado
                    fabricarBotonPagina(i, true);
                else
                    fabricarBotonPagina(i, false);
            }
        }
        else{
            System.out.println("Son más de 5 páginas");
            boolean puntosPintados = false;

            for(int i=1; i<=cantidadPaginas; i++){
                // Evalúa si se deben pintar
                if(i == 1 || i == cantidadPaginas || i == pagina+1 || i == pagina-1 || i == pagina)
                    if(i==pagina){
                        fabricarBotonPagina(i, true);
                        puntosPintados = false;
                    }else{
                        fabricarBotonPagina(i, false);
                    }
                else if(!puntosPintados){
                    fabricarPuntosEntreBotones();
                    puntosPintados = true;
                }
            }
        }
    }

    /**
     * Fabrica a los toggle button de las páginas
     * @param i Es el número de la página que se está pintando
     * @param seleccionado Especifica si el toggle creado debe ser marcado como seleccionado
     */
    private void fabricarBotonPagina(int i, boolean seleccionado){
        ToggleButton btn = new ToggleButton((Integer.toString(i)));
        btn.setToggleGroup(botonesPagina); // Asignamos el grupo al botón
        btn.setUserData(i); // Damos el valor que tendrá
        btn.getStyleClass().add("boton-pagina"); // Damos el estilo que debe tener
        btn.setSelected(seleccionado);
        btn.setOnAction(this::onActionCambioPagina);
        btn.setFocusTraversable(false);

        contenedorBotonesPagina.getChildren().add(btn);
    }
    /**
     * Fabrica un label con puntos suspensivos para 
     */
    private void fabricarPuntosEntreBotones(){
        Label lbl = new Label("...");
        lbl.getStyleClass().add("puntos-entre-paginas");

        contenedorBotonesPagina.getChildren().add(lbl);
    }

    /**
     * Manda a crear un ítem de producto con el objeto del que el ítem sacará la información
     * @param detalle Es el producto con la información necesaria para el ítem
     * @return Regresa al objeto del item
     */
    public ItemProducto crearProducto(Producto producto){
        try{
            // Carga a la plantilla de la tarjeta de los pedidos
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/item-producto.fxml"));
            ItemProducto tarjeta = new ItemProducto(); // Se asigna el controlador hecho a la tarjeta
            tarjeta.setControlInventario(control); // tarjeta.setControl(controlRegistroPedido); // Se da que este controlador tiene el control sobre las tarjetas, para apoyar la comunicación con los servicios
            loader.setController(tarjeta);
            Node tarjetaVisual = loader.load(); // Se inicializa la tarjeta
            tarjeta.configurarDatos(producto); // Mediante el controlador se llama al método para ingresar la información
            cajaProductos.getChildren().add(tarjetaVisual); // Se agrega la tarjeta al contenedor
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
    public void eliminarProducto(GridPane nodoVisual){
        cajaProductos.getChildren().remove(nodoVisual);
        System.out.println("Se mató gráficamente al producto");
    }
    
    /**
     * Es el método que detecta las presiones de teclas 
     * @param event Es la tecla que fue presionada
     */
    @FXML
    public void onAtajoTeclado(KeyEvent event){
        // El Enter está definido como Default Button dentro de FXML
        if (event.getCode() == KeyCode.F2) {
            System.out.println("Se presionó tecla F2");
            txtBusqueda.requestFocus(); // Pone el cursor en el buscador
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

    /**
     * Limpia la caja de productos para quitar todos los productos que contiene
     */
    public void limpiarCaja(){
        cajaProductos.getChildren().clear();
    }

    // OnAction's
    @FXML
    public void onActionBuscar(){
        System.out.println("Se presionó buscar");
        String texto = txtBusqueda.getText();
        if (!texto.isEmpty()) {
            System.out.println("Buscando: " + texto);

            control.buscarProducto(texto);
            // Aquí llamas a tu controlador: control.buscarProducto(texto);
            txtBusqueda.clear(); // Limpias el campo después de buscar
        }else{
            System.out.println("Se buscó cadena vacía");
            control.buscarProducto("");
        }
    }
    @FXML
    public void onActionCambioPagina(ActionEvent event){
        ToggleButton botonPresionado = (ToggleButton) event.getSource();
        botonPresionado.setSelected(true);

        if(botonPresionado.getUserData() instanceof Integer pag){
            control.cambioPagina(pag);
        }else
            System.err.println("No se pudo cambiar la página");
    }
}
