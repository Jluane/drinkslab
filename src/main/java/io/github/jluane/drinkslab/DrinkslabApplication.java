package io.github.jluane.drinkslab;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

@SpringBootApplication
public class DrinkslabApplication {

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
        Platform.runLater(() -> {
            // TODO: Iniciar el controlador principal (ej. controlVentanaPrincipal.inicia())
            System.out.println("¡Spring Boot y JavaFX están conectados!");
        });
    }
}