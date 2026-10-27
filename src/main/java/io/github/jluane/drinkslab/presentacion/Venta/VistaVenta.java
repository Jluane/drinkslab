package io.github.jluane.drinkslab.presentacion.Venta;

import java.io.IOException;

import org.springframework.stereotype.Component;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

@Component
public class VistaVenta {
    
    // Controlador de esta pantalla
    public ControlVenta control;


    // Componentes del FXML

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
            return loader.load();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void muestra(){
        
    }
}
