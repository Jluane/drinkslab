package io.github.jluane.drinkslab;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import io.github.jluane.drinkslab.datos.RepositorioProducto;
import io.github.jluane.drinkslab.datos.RepositorioVenta;
import io.github.jluane.drinkslab.negocio.modelo.Producto;
import io.github.jluane.drinkslab.negocio.modelo.Venta;
import io.github.jluane.drinkslab.presentacion.ControlMain;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

@SpringBootApplication
public class DrinkslabApplication {
    
    // Control que inicializa
    private final ControlMain controlMain;

    // Repositorios para la base de datos de prueba
    private final RepositorioProducto repositorioProducto;
    private final RepositorioVenta repositorioVenta;

    @Autowired
    public DrinkslabApplication(ControlMain controlMain, RepositorioProducto repositorioProducto, RepositorioVenta  repositorioVenta){
        this.controlMain = controlMain;
        this.repositorioProducto = repositorioProducto;
        this.repositorioVenta = repositorioVenta;
    }


    public static void main(String[] args) {
        // Le damos el control inicial a JavaFX
        Application.launch(JavaFXApplication.class, args);
    }

    public static class JavaFXApplication extends Application {
        
        private static ConfigurableApplicationContext applicationContext;
        
        @Override
        public void init() {
            // Arrancamos Spring Boot dentro de JavaFX
            SpringApplicationBuilder builder = new SpringApplicationBuilder(DrinkslabApplication.class);
            builder.headless(false); // Fundamental para permitir ventanas de escritorio
            applicationContext = builder.run(getParameters().getRaw().toArray(new String[0]));
        }
        
        @Override
        public void start(Stage primaryStage) {
            // Spring busca la instancia de esta clase y ejecuta inicia()
            Platform.runLater(() -> {
                applicationContext.getBean(DrinkslabApplication.class).inicia();
            });
        }
        
        @Override
        public void stop() {
            // Cierre seguro de ambos motores
            applicationContext.close();
            Platform.exit();
        }
    }

	/**
     * Método que arranca la lógica de la aplicación.
     * Aquí llamaremos al controlador de la primera ventana gráfica.
     */
    public void inicia() {

        // Inicializo Base de datos de prueba
        inicializaBD();

        Platform.runLater(() -> {
            controlMain.inicia();
            System.out.println("¡Spring Boot y JavaFX están conectados!");
        });
    }
    

    /**
     * Inicializa BD para pruebas
     */
    public void inicializaBD(){
        Producto p1 = new Producto();
        p1.setNombre("Producto1");
        p1.setPrecio(new BigDecimal("100"));
        p1.setGTIN("123");
        repositorioProducto.save(p1);

        Producto p2 = new Producto();
        p2.setNombre("Producto2");
        p2.setPrecio(new BigDecimal("50"));
        p2.setGTIN("321");
        repositorioProducto.save(p2);

        Venta v1 = new Venta();
        repositorioVenta.save(v1);
    }
}