package io.github.jluane.drinkslab.presentacion.Venta;

import java.io.IOException;

import org.springframework.stereotype.Component;

import io.github.jluane.drinkslab.negocio.modelo.DetalleVenta;
import io.github.jluane.drinkslab.negocio.modelo.Venta;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

@Component
public class VistaVenta {
    
    // Controlador de esta pantalla
    private ControlVenta control;


    // Componentes del FXML
    @FXML
    private Button btnBuscarEnter;
    @FXML
    private Button btnPagarF12;
    @FXML
    private Button btnTarjetaF10;
    @FXML
    private TextField txtBusqueda;
    @FXML
    private VBox cajaVentas; // Contenedor de las ventas
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
     * Carga el FXML y devuelve el panel visual listo
     */
    public Parent cargaPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana-venta.fxml")); // Ajusta el nombre de tu archivo .fxml
            loader.setController(this);
            Parent parent = loader.load();
            cajaVentas.getChildren().clear();
            return parent;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public VistaDetalleVenta crearDetalleVenta(DetalleVenta detalle){
        try{
            // Carga a la plantilla de la tarjeta de los pedidos
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/item-detalle-venta.fxml"));
            VistaDetalleVenta tarjeta = new VistaDetalleVenta(); // Se asigna el controlador hecho a la tarjeta
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

    public void inicia(int idxVenta){
        numVenta.setText("ID VENTA: #"+idxVenta);
        cantVenta.setText(0+" artículo "+"("+0+" unid.)");
        sumaVenta.setText("$0");
        totalVenta.setText("$0");
        totalPagar.setText("$0");
        btnPagarF12.setDisable(true);
        btnTarjetaF10.setDisable(true);
        pedirFocoBuscador();
    }

    public void actualizarVenta(Venta venta){
        int articulos = venta.getDetalles().size();
        int productos = 0;
        for(DetalleVenta det : venta.getDetalles()){
            productos = productos + det.getCantidad();
        }

        cantVenta.setText(articulos+" artículo "+"("+productos+" unid.)");
        sumaVenta.setText("$"+venta.getTotal());
        totalVenta.setText("$"+venta.getTotal());
        totalPagar.setText("$"+venta.getTotal());

        if(productos <= 0){
            btnPagarF12.setDisable(true);
            btnTarjetaF10.setDisable(true);
        }else{
            btnPagarF12.setDisable(false);
            btnTarjetaF10.setDisable(false);
        }
    }

    /**
     * Solicita el enfoque en el texto de búsqueda para asegurarse de que es esta pantalla a la que se está apuntando
     */
    private void pedirFocoBuscador(){
        txtBusqueda.requestFocus();
    }

    // Atajos
    @FXML
    public void onAtajoTeclado(KeyEvent event){
        // Detecta qué tecla se presionó en cualquier parte de esta pantalla
        if (event.getCode() == KeyCode.F12) {
            System.out.println("Se presinó tecla F12");
            btnPagarF12.fire(); // Presiona el botón automáticamente
        } 
        else if (event.getCode() == KeyCode.F2) {
            System.out.println("Se presionó tecla F2");
            txtBusqueda.requestFocus(); // Pone el cursor en el buscador
        }else if(event.getCode() == KeyCode.F10) {
            System.out.println("Se presionó tecla F10");
            btnTarjetaF10.fire();
        }
    }

    // OnAction's
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

    @FXML
    public void onActionPagar(){
        System.out.println("Se presionó botón pagar");
    }

    @FXML
    public void onActionTarjeta(){
        System.out.println("Se presionó botón pagar con tarjeta");
    }

}
