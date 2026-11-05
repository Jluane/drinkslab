package io.github.jluane.drinkslab;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import io.github.jluane.drinkslab.datos.RepositorioProducto;
import io.github.jluane.drinkslab.datos.RepositorioVenta;
import io.github.jluane.drinkslab.negocio.modelo.DetalleVenta;
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
    public DrinkslabApplication(ControlMain controlMain, RepositorioProducto repositorioProducto, RepositorioVenta repositorioVenta){
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
        p1.setPrecioUnitario(new BigDecimal("100"));
        p1.setGTIN("123");
        p1.setPrecioPaquete(BigDecimal.valueOf(400));
        p1.setCantPaquete(BigDecimal.valueOf(5));
        p1.setPrecioPromocion(BigDecimal.valueOf(100));
        p1.setCantPromocion(BigDecimal.valueOf(2));
        p1.setCantInv(BigDecimal.valueOf(10));
        repositorioProducto.save(p1);

        Producto p2 = new Producto();
        p2.setNombre("Producto2");
        p2.setPrecioUnitario(new BigDecimal("50"));
        p2.setGTIN("321");
        p2.setAGranel(true);
        p2.setCantInv(new BigDecimal("1.1"));
        repositorioProducto.save(p2);

        Producto p3 = new Producto();
        p3.setNombre("Vasos desechables");
        p3.setPrecioUnitario(new BigDecimal("1"));
        p3.setGTIN("7501219112605");
        p3.setPrecioPaquete(BigDecimal.valueOf(25));
        p3.setCantPaquete(BigDecimal.valueOf(50));
        p3.setPrecioPromocion(BigDecimal.valueOf(4));
        p3.setCantPromocion(BigDecimal.valueOf(5));
        repositorioProducto.save(p3);

        Producto p4 = new Producto();
        p4.setNombre("Agua natural 600ml");
        p4.setPrecioUnitario(new BigDecimal("15"));
        p4.setGTIN("456");
        p4.setCantInv(BigDecimal.valueOf(30));
        repositorioProducto.save(p4);

        Producto p5 = new Producto();
        p5.setNombre("Refresco 600ml");
        p5.setPrecioUnitario(new BigDecimal("18"));
        p5.setGTIN("654");
        p5.setPrecioPaquete(BigDecimal.valueOf(200));
        p5.setCantPaquete(BigDecimal.valueOf(12));
        p5.setCantInv(BigDecimal.valueOf(24));
        repositorioProducto.save(p5);

        Producto p6 = new Producto();
        p6.setNombre("Sabritas");
        p6.setPrecioUnitario(new BigDecimal("17"));
        p6.setGTIN("1234");
        p6.setPrecioPromocion(BigDecimal.valueOf(30));
        p6.setCantPromocion(BigDecimal.valueOf(2));
        p6.setCantInv(BigDecimal.valueOf(20));
        repositorioProducto.save(p6);

        Producto p7 = new Producto();
        p7.setNombre("Galletas");
        p7.setPrecioUnitario(new BigDecimal("12"));
        p7.setGTIN("4321");
        p7.setCantInv(BigDecimal.valueOf(15));
        repositorioProducto.save(p7);

        Producto p8 = new Producto();
        p8.setNombre("Leche 1L");
        p8.setPrecioUnitario(new BigDecimal("28"));
        p8.setGTIN("789");
        p8.setPrecioPaquete(BigDecimal.valueOf(320));
        p8.setCantPaquete(BigDecimal.valueOf(12));
        p8.setCantInv(BigDecimal.valueOf(18));
        repositorioProducto.save(p8);

        Producto p9 = new Producto();
        p9.setNombre("Chocolate");
        p9.setPrecioUnitario(new BigDecimal("10"));
        p9.setGTIN("987");
        p9.setAGranel(false);
        p9.setCantInv(BigDecimal.valueOf(25));
        repositorioProducto.save(p9);

        Producto p10 = new Producto();
        p10.setNombre("Dulces a granel");
        p10.setPrecioUnitario(new BigDecimal("0.50"));
        p10.setGTIN("7913");
        p10.setAGranel(true);
        p10.setCantInv(BigDecimal.valueOf(100));
        repositorioProducto.save(p10);

        Producto p11 = new Producto();
        p11.setNombre("Arroz 1kg");
        p11.setPrecioUnitario(new BigDecimal("32"));
        p11.setGTIN("2468");
        p11.setPrecioPromocion(BigDecimal.valueOf(55));
        p11.setCantPromocion(BigDecimal.valueOf(2));
        p11.setCantInv(BigDecimal.valueOf(20));
        repositorioProducto.save(p11);

        Producto p12 = new Producto();
        p12.setNombre("Huevo por pieza");
        p12.setPrecioUnitario(new BigDecimal("4"));
        p12.setGTIN("8642");
        p12.setAGranel(true);
        p12.setCantInv(BigDecimal.valueOf(60));
        repositorioProducto.save(p12);

        Producto p13 = new Producto();
        p13.setNombre("Aceite 1L");
        p13.setPrecioUnitario(new BigDecimal("45"));
        p13.setGTIN("147258");
        p13.setPrecioPaquete(BigDecimal.valueOf(510));
        p13.setCantPaquete(BigDecimal.valueOf(12));
        p13.setCantInv(BigDecimal.valueOf(12));
        repositorioProducto.save(p13);

        Producto p14 = new Producto();
p14.setNombre("Coca Cola 600ml");
p14.setPrecioUnitario(new BigDecimal("18"));
p14.setGTIN("111");
p14.setCantInv(BigDecimal.valueOf(20));
repositorioProducto.save(p14);

Producto p15 = new Producto();
p15.setNombre("Pepsi 600ml");
p15.setPrecioUnitario(new BigDecimal("16"));
p15.setGTIN("222");
p15.setCantInv(BigDecimal.valueOf(15));
repositorioProducto.save(p15);

Producto p16 = new Producto();
p16.setNombre("Jugo de naranja");
p16.setPrecioUnitario(new BigDecimal("14"));
p16.setGTIN("333");
p16.setPrecioPaquete(BigDecimal.valueOf(150));
p16.setCantPaquete(BigDecimal.valueOf(12));
repositorioProducto.save(p16);

Producto p17 = new Producto();
p17.setNombre("Agua mineral");
p17.setPrecioUnitario(new BigDecimal("17"));
p17.setGTIN("444");
p17.setCantInv(BigDecimal.valueOf(18));
repositorioProducto.save(p17);

Producto p18 = new Producto();
p18.setNombre("Boing mango");
p18.setPrecioUnitario(new BigDecimal("12"));
p18.setGTIN("555");
p18.setCantInv(BigDecimal.valueOf(25));
repositorioProducto.save(p18);

Producto p19 = new Producto();
p19.setNombre("Gansito");
p19.setPrecioUnitario(new BigDecimal("18"));
p19.setGTIN("666");
p19.setPrecioPromocion(BigDecimal.valueOf(32));
p19.setCantPromocion(BigDecimal.valueOf(2));
repositorioProducto.save(p19);

Producto p20 = new Producto();
p20.setNombre("Pingüinos");
p20.setPrecioUnitario(new BigDecimal("20"));
p20.setGTIN("777");
p19.setCantInv(BigDecimal.valueOf(15));
repositorioProducto.save(p20);

Producto p21 = new Producto();
p21.setNombre("Donas empaquetadas");
p21.setPrecioUnitario(new BigDecimal("22"));
p21.setGTIN("888");
p21.setCantInv(BigDecimal.valueOf(10));
repositorioProducto.save(p21);

Producto p22 = new Producto();
p22.setNombre("Chocorroles");
p22.setPrecioUnitario(new BigDecimal("19"));
p22.setGTIN("999");
p22.setCantInv(BigDecimal.valueOf(12));
repositorioProducto.save(p22);

Producto p23 = new Producto();
p23.setNombre("Barra de granola");
p23.setPrecioUnitario(new BigDecimal("13"));
p23.setGTIN("1122");
p23.setCantInv(BigDecimal.valueOf(20));
repositorioProducto.save(p23);

Producto p24 = new Producto();
p24.setNombre("Atún en lata");
p24.setPrecioUnitario(new BigDecimal("24"));
p24.setGTIN("2211");
p24.setPrecioPaquete(BigDecimal.valueOf(264));
p24.setCantPaquete(BigDecimal.valueOf(12));
repositorioProducto.save(p24);

Producto p25 = new Producto();
p25.setNombre("Frijoles en lata");
p25.setPrecioUnitario(new BigDecimal("19"));
p25.setGTIN("3344");
p25.setCantInv(BigDecimal.valueOf(16));
repositorioProducto.save(p25);

Producto p26 = new Producto();
p26.setNombre("Chiles en lata");
p26.setPrecioUnitario(new BigDecimal("15"));
p26.setGTIN("4433");
p26.setCantInv(BigDecimal.valueOf(14));
repositorioProducto.save(p26);

Producto p27 = new Producto();
p27.setNombre("Sardinas");
p27.setPrecioUnitario(new BigDecimal("28"));
p27.setGTIN("5566");
p27.setCantInv(BigDecimal.valueOf(10));
repositorioProducto.save(p27);

Producto p28 = new Producto();
p28.setNombre("Sopa instantánea");
p28.setPrecioUnitario(new BigDecimal("18"));
p28.setGTIN("6655");
p28.setPrecioPromocion(BigDecimal.valueOf(30));
p28.setCantPromocion(BigDecimal.valueOf(2));
repositorioProducto.save(p28);

Producto p29 = new Producto();
p29.setNombre("Pasta de codito");
p29.setPrecioUnitario(new BigDecimal("13"));
p29.setGTIN("7788");
p29.setCantInv(BigDecimal.valueOf(20));
repositorioProducto.save(p29);

Producto p30 = new Producto();
p30.setNombre("Sopa de fideo");
p30.setPrecioUnitario(new BigDecimal("12"));
p30.setGTIN("8877");
p30.setCantInv(BigDecimal.valueOf(25));
repositorioProducto.save(p30);

Producto p31 = new Producto();
p31.setNombre("Harina de trigo");
p31.setPrecioUnitario(new BigDecimal("21"));
p31.setGTIN("1357");
p31.setCantInv(BigDecimal.valueOf(12));
repositorioProducto.save(p31);

Producto p32 = new Producto();
p32.setNombre("Azúcar 1kg");
p32.setPrecioUnitario(new BigDecimal("29"));
p32.setGTIN("7531");
p32.setPrecioPaquete(BigDecimal.valueOf(330));
p32.setCantPaquete(BigDecimal.valueOf(12));
repositorioProducto.save(p32);

Producto p33 = new Producto();
p33.setNombre("Sal 1kg");
p33.setPrecioUnitario(new BigDecimal("16"));
p33.setGTIN("246");
p32.setCantInv(BigDecimal.valueOf(20));
repositorioProducto.save(p33);

Producto p34 = new Producto();
p34.setNombre("Café soluble");
p34.setPrecioUnitario(new BigDecimal("85"));
p34.setGTIN("642");
p34.setCantInv(BigDecimal.valueOf(8));
repositorioProducto.save(p34);

Producto p35 = new Producto();
p35.setNombre("Chocolate en polvo");
p35.setPrecioUnitario(new BigDecimal("48"));
p35.setGTIN("369");
p35.setCantInv(BigDecimal.valueOf(10));
repositorioProducto.save(p35);

Producto p36 = new Producto();
p36.setNombre("Mayonesa");
p36.setPrecioUnitario(new BigDecimal("38"));
p36.setGTIN("963");
p36.setCantInv(BigDecimal.valueOf(12));
repositorioProducto.save(p36);

Producto p37 = new Producto();
p37.setNombre("Catsup");
p37.setPrecioUnitario(new BigDecimal("25"));
p37.setGTIN("159");
p37.setCantInv(BigDecimal.valueOf(15));
repositorioProducto.save(p37);

Producto p38 = new Producto();
p38.setNombre("Mostaza");
p38.setPrecioUnitario(new BigDecimal("22"));
p38.setGTIN("951");
p38.setCantInv(BigDecimal.valueOf(10));
repositorioProducto.save(p38);

Producto p39 = new Producto();
p39.setNombre("Aceite de oliva");
p39.setPrecioUnitario(new BigDecimal("95"));
p39.setGTIN("357");
p39.setCantInv(BigDecimal.valueOf(6));
repositorioProducto.save(p39);

Producto p40 = new Producto();
p40.setNombre("Vinagre blanco");
p40.setPrecioUnitario(new BigDecimal("18"));
p40.setGTIN("753");
p40.setCantInv(BigDecimal.valueOf(12));
repositorioProducto.save(p40);

Producto p41 = new Producto();
p41.setNombre("Cereal de maíz");
p41.setPrecioUnitario(new BigDecimal("55"));
p41.setGTIN("258");
p41.setCantInv(BigDecimal.valueOf(8));
repositorioProducto.save(p41);

Producto p42 = new Producto();
p42.setNombre("Avena");
p42.setPrecioUnitario(new BigDecimal("32"));
p42.setGTIN("852");
p42.setAGranel(true);
p42.setCantInv(BigDecimal.valueOf(20));
repositorioProducto.save(p42);

Producto p43 = new Producto();
p43.setNombre("Granola a granel");
p43.setPrecioUnitario(new BigDecimal("0.80"));
p43.setGTIN("741");
p43.setAGranel(true);
p43.setCantInv(BigDecimal.valueOf(50));
repositorioProducto.save(p43);

Producto p44 = new Producto();
p44.setNombre("Lentejas");
p44.setPrecioUnitario(new BigDecimal("38"));
p44.setGTIN("147");
p44.setAGranel(true);
p44.setCantInv(BigDecimal.valueOf(15));
repositorioProducto.save(p44);

Producto p45 = new Producto();
p45.setNombre("Garbanzo");
p45.setPrecioUnitario(new BigDecimal("42"));
p45.setGTIN("2580");
p45.setAGranel(true);
p45.setCantInv(BigDecimal.valueOf(12));
repositorioProducto.save(p45);

Producto p46 = new Producto();
p46.setNombre("Maíz palomero");
p46.setPrecioUnitario(new BigDecimal("28"));
p46.setGTIN("3690");
p46.setAGranel(true);
p46.setCantInv(BigDecimal.valueOf(18));
repositorioProducto.save(p46);

Producto p47 = new Producto();
p47.setNombre("Cacahuates");
p47.setPrecioUnitario(new BigDecimal("1.50"));
p47.setGTIN("481");
p47.setAGranel(true);
p47.setCantInv(BigDecimal.valueOf(40));
repositorioProducto.save(p47);

Producto p48 = new Producto();
p48.setNombre("Almendras");
p48.setPrecioUnitario(new BigDecimal("2"));
p48.setGTIN("582");
p48.setAGranel(true);
p48.setCantInv(BigDecimal.valueOf(25));
repositorioProducto.save(p48);

Producto p49 = new Producto();
p49.setNombre("Nuez");
p49.setPrecioUnitario(new BigDecimal("3"));
p49.setGTIN("683");
p49.setAGranel(true);
p49.setCantInv(BigDecimal.valueOf(20));
repositorioProducto.save(p49);

Producto p50 = new Producto();
p50.setNombre("Pasas");
p50.setPrecioUnitario(new BigDecimal("1"));
p50.setGTIN("784");
p50.setAGranel(true);
p50.setCantInv(BigDecimal.valueOf(30));
repositorioProducto.save(p50);

Producto p51 = new Producto();
p51.setNombre("Canela");
p51.setPrecioUnitario(new BigDecimal("2.50"));
p51.setGTIN("885");
p51.setAGranel(true);
p51.setCantInv(BigDecimal.valueOf(15));
repositorioProducto.save(p51);

Producto p52 = new Producto();
p52.setNombre("Jabón de baño");
p52.setPrecioUnitario(new BigDecimal("18"));
p52.setGTIN("1593");
p52.setPrecioPaquete(BigDecimal.valueOf(180));
p52.setCantPaquete(BigDecimal.valueOf(12));
repositorioProducto.save(p52);

Producto p53 = new Producto();
p53.setNombre("Jabón en polvo");
p53.setPrecioUnitario(new BigDecimal("35"));
p53.setGTIN("3579");
p53.setCantInv(BigDecimal.valueOf(10));
repositorioProducto.save(p53);

Producto p54 = new Producto();
p54.setNombre("Suavizante");
p54.setPrecioUnitario(new BigDecimal("42"));
p54.setGTIN("9513");
p54.setCantInv(BigDecimal.valueOf(8));
repositorioProducto.save(p54);

Producto p55 = new Producto();
p55.setNombre("Cloro 1L");
p55.setPrecioUnitario(new BigDecimal("19"));
p55.setGTIN("7539");
p55.setCantInv(BigDecimal.valueOf(15));
repositorioProducto.save(p55);

Producto p56 = new Producto();
p56.setNombre("Limpiador multiusos");
p56.setPrecioUnitario(new BigDecimal("28"));
p56.setGTIN("1479");
p56.setCantInv(BigDecimal.valueOf(10));
repositorioProducto.save(p56);

Producto p57 = new Producto();
p57.setNombre("Esponja");
p57.setPrecioUnitario(new BigDecimal("12"));
p57.setGTIN("2581");
p57.setCantInv(BigDecimal.valueOf(20));
repositorioProducto.save(p57);

Producto p58 = new Producto();
p58.setNombre("Papel higiénico");
p58.setPrecioUnitario(new BigDecimal("45"));
p58.setGTIN("3692");
p58.setPrecioPromocion(BigDecimal.valueOf(80));
p58.setCantPromocion(BigDecimal.valueOf(2));
repositorioProducto.save(p58);

Producto p59 = new Producto();
p59.setNombre("Servilletas");
p59.setPrecioUnitario(new BigDecimal("22"));
p59.setGTIN("4813");
p59.setCantInv(BigDecimal.valueOf(15));
repositorioProducto.save(p59);

Producto p60 = new Producto();
p60.setNombre("Toallas de cocina");
p60.setPrecioUnitario(new BigDecimal("32"));
p60.setGTIN("5824");
p60.setCantInv(BigDecimal.valueOf(10));
repositorioProducto.save(p60);

Producto p61 = new Producto();
p61.setNombre("Pasta dental");
p61.setPrecioUnitario(new BigDecimal("35"));
p61.setGTIN("6835");
p61.setCantInv(BigDecimal.valueOf(12));
repositorioProducto.save(p61);

Producto p62 = new Producto();
p62.setNombre("Cepillo dental");
p62.setPrecioUnitario(new BigDecimal("25"));
p62.setGTIN("7846");
p62.setCantInv(BigDecimal.valueOf(10));
repositorioProducto.save(p62);

Producto p63 = new Producto();
p63.setNombre("Shampoo");
p63.setPrecioUnitario(new BigDecimal("65"));
p63.setGTIN("8857");
p63.setCantInv(BigDecimal.valueOf(8));
repositorioProducto.save(p63);

Producto p64 = new Producto();
p64.setNombre("Acondicionador");
p64.setPrecioUnitario(new BigDecimal("58"));
p64.setGTIN("1968");
p64.setCantInv(BigDecimal.valueOf(7));
repositorioProducto.save(p64);

Producto p65 = new Producto();
p65.setNombre("Desodorante");
p65.setPrecioUnitario(new BigDecimal("48"));
p65.setGTIN("2879");
p65.setCantInv(BigDecimal.valueOf(9));
repositorioProducto.save(p65);

Producto p66 = new Producto();
p66.setNombre("Crema corporal");
p66.setPrecioUnitario(new BigDecimal("52"));
p66.setGTIN("3781");
p66.setCantInv(BigDecimal.valueOf(6));
repositorioProducto.save(p66);

Producto p67 = new Producto();
p67.setNombre("Rastrillo");
p67.setPrecioUnitario(new BigDecimal("20"));
p67.setGTIN("4692");
p67.setCantInv(BigDecimal.valueOf(15));
repositorioProducto.save(p67);

Producto p68 = new Producto();
p68.setNombre("Algodón");
p68.setPrecioUnitario(new BigDecimal("18"));
p68.setGTIN("5613");
p68.setCantInv(BigDecimal.valueOf(12));
repositorioProducto.save(p68);

Producto p69 = new Producto();
p69.setNombre("Curitas");
p69.setPrecioUnitario(new BigDecimal("25"));
p69.setGTIN("6524");
p69.setCantInv(BigDecimal.valueOf(10));
repositorioProducto.save(p69);

Producto p70 = new Producto();
p70.setNombre("Pilas AA");
p70.setPrecioUnitario(new BigDecimal("35"));
p70.setGTIN("7435");
p70.setPrecioPaquete(BigDecimal.valueOf(120));
p70.setCantPaquete(BigDecimal.valueOf(4));
repositorioProducto.save(p70);

Producto p71 = new Producto();
p71.setNombre("Focos LED");
p71.setPrecioUnitario(new BigDecimal("45"));
p71.setGTIN("8346");
p71.setCantInv(BigDecimal.valueOf(8));
repositorioProducto.save(p71);

Producto p72 = new Producto();
p72.setNombre("Cerillos");
p72.setPrecioUnitario(new BigDecimal("5"));
p72.setGTIN("9257");
p72.setCantInv(BigDecimal.valueOf(30));
repositorioProducto.save(p72);

Producto p73 = new Producto();
p73.setNombre("Encendedor");
p73.setPrecioUnitario(new BigDecimal("12"));
p73.setGTIN("3168");
p73.setCantInv(BigDecimal.valueOf(20));
repositorioProducto.save(p73);

Producto p74 = new Producto();
p74.setNombre("Bolsa de basura");
p74.setPrecioUnitario(new BigDecimal("28"));
p74.setGTIN("4279");
p74.setCantInv(BigDecimal.valueOf(15));
repositorioProducto.save(p74);

Producto p75 = new Producto();
p75.setNombre("Vasos térmicos");
p75.setPrecioUnitario(new BigDecimal("35"));
p75.setGTIN("5181");
p75.setCantInv(BigDecimal.valueOf(10));
repositorioProducto.save(p75);

Producto p76 = new Producto();
p76.setNombre("Platos desechables");
p76.setPrecioUnitario(new BigDecimal("25"));
p76.setGTIN("6292");
p76.setCantInv(BigDecimal.valueOf(12));
repositorioProducto.save(p76);

Producto p77 = new Producto();
p77.setNombre("Popotes");
p77.setPrecioUnitario(new BigDecimal("10"));
p77.setGTIN("7313");
p77.setCantInv(BigDecimal.valueOf(20));
repositorioProducto.save(p77);

Producto p78 = new Producto();
p78.setNombre("Papel aluminio");
p78.setPrecioUnitario(new BigDecimal("32"));
p78.setGTIN("8424");
p78.setCantInv(BigDecimal.valueOf(8));
repositorioProducto.save(p78);

Producto p79 = new Producto();
p79.setNombre("Plástico adherente");
p79.setPrecioUnitario(new BigDecimal("28"));
p79.setGTIN("9535");
p79.setCantInv(BigDecimal.valueOf(9));
repositorioProducto.save(p79);

Producto p80 = new Producto();
p80.setNombre("Detergente líquido");
p80.setPrecioUnitario(new BigDecimal("48"));
p80.setGTIN("1646");
p80.setPrecioPaquete(BigDecimal.valueOf(540));
p80.setCantPaquete(BigDecimal.valueOf(12));
repositorioProducto.save(p80);

Producto p81 = new Producto();
p81.setNombre("Leche deslactosada");
p81.setPrecioUnitario(new BigDecimal("32"));
p81.setGTIN("2757");
p81.setCantInv(BigDecimal.valueOf(12));
repositorioProducto.save(p81);

Producto p82 = new Producto();
p82.setNombre("Yogur natural");
p82.setPrecioUnitario(new BigDecimal("18"));
p82.setGTIN("3868");
p82.setCantInv(BigDecimal.valueOf(15));
repositorioProducto.save(p82);

Producto p83 = new Producto();
p83.setNombre("Queso panela");
p83.setPrecioUnitario(new BigDecimal("75"));
p83.setGTIN("4979");
p83.setCantInv(BigDecimal.valueOf(5));
repositorioProducto.save(p83);

Producto p84 = new Producto();
p84.setNombre("Crema");
p84.setPrecioUnitario(new BigDecimal("38"));
p84.setGTIN("5812");
p84.setCantInv(BigDecimal.valueOf(8));
repositorioProducto.save(p84);

Producto p85 = new Producto();
p85.setNombre("Mantequilla");
p85.setPrecioUnitario(new BigDecimal("42"));
p85.setGTIN("6923");
p84.setCantInv(BigDecimal.valueOf(10));
repositorioProducto.save(p85);

Producto p86 = new Producto();
p86.setNombre("Jamón");
p86.setPrecioUnitario(new BigDecimal("12"));
p86.setGTIN("7134");
p86.setAGranel(true);
p86.setCantInv(BigDecimal.valueOf(20));
repositorioProducto.save(p86);

Producto p87 = new Producto();
p87.setNombre("Salchicha");
p87.setPrecioUnitario(new BigDecimal("8"));
p87.setGTIN("8245");
p87.setAGranel(true);
p87.setCantInv(BigDecimal.valueOf(25));
repositorioProducto.save(p87);

Producto p88 = new Producto();
p88.setNombre("Tocino");
p88.setPrecioUnitario(new BigDecimal("15"));
p88.setGTIN("9356");
p88.setAGranel(true);
p88.setCantInv(BigDecimal.valueOf(12));
repositorioProducto.save(p88);

Producto p89 = new Producto();
p89.setNombre("Pan de caja");
p89.setPrecioUnitario(new BigDecimal("48"));
p89.setGTIN("1467");
p89.setCantInv(BigDecimal.valueOf(8));
repositorioProducto.save(p89);

Producto p90 = new Producto();
p90.setNombre("Bolillo");
p90.setPrecioUnitario(new BigDecimal("3"));
p90.setGTIN("2578");
p90.setAGranel(true);
p90.setCantInv(BigDecimal.valueOf(50));
repositorioProducto.save(p90);

Producto p91 = new Producto();
p91.setNombre("Tortillas");
p91.setPrecioUnitario(new BigDecimal("24"));
p91.setGTIN("3689");
p91.setAGranel(true);
p91.setCantInv(BigDecimal.valueOf(30));
repositorioProducto.save(p91);

Producto p92 = new Producto();
p92.setNombre("Queso amarillo");
p92.setPrecioUnitario(new BigDecimal("55"));
p92.setGTIN("4791");
p92.setCantInv(BigDecimal.valueOf(7));
repositorioProducto.save(p92);

Producto p93 = new Producto();
p93.setNombre("Mermelada");
p93.setPrecioUnitario(new BigDecimal("38"));
p93.setGTIN("5813");
p93.setCantInv(BigDecimal.valueOf(9));
repositorioProducto.save(p93);

Producto p94 = new Producto();
p94.setNombre("Miel");
p94.setPrecioUnitario(new BigDecimal("65"));
p94.setGTIN("6924");
p94.setCantInv(BigDecimal.valueOf(6));
repositorioProducto.save(p94);

Producto p95 = new Producto();
p95.setNombre("Crema de cacahuate");
p95.setPrecioUnitario(new BigDecimal("58"));
p95.setGTIN("7135");
p95.setCantInv(BigDecimal.valueOf(5));
repositorioProducto.save(p95);

Producto p96 = new Producto();
p96.setNombre("Galletas saladas");
p96.setPrecioUnitario(new BigDecimal("16"));
p96.setGTIN("8246");
p96.setCantInv(BigDecimal.valueOf(18));
repositorioProducto.save(p96);

Producto p97 = new Producto();
p97.setNombre("Papas a granel");
p97.setPrecioUnitario(new BigDecimal("1.50"));
p97.setGTIN("9357");
p97.setAGranel(true);
p97.setCantInv(BigDecimal.valueOf(40));
repositorioProducto.save(p97);

Producto p98 = new Producto();
p98.setNombre("Palomitas");
p98.setPrecioUnitario(new BigDecimal("15"));
p98.setGTIN("1468");
p98.setCantInv(BigDecimal.valueOf(15));
repositorioProducto.save(p98);

Producto p99 = new Producto();
p99.setNombre("Chicles");
p99.setPrecioUnitario(new BigDecimal("2"));
p99.setGTIN("2579");
p99.setAGranel(true);
p99.setCantInv(BigDecimal.valueOf(50));
repositorioProducto.save(p99);

Producto p100 = new Producto();
p100.setNombre("Caramelos");
p100.setPrecioUnitario(new BigDecimal("0.50"));
p100.setGTIN("3681");
p100.setAGranel(true);
p100.setCantInv(BigDecimal.valueOf(100));
repositorioProducto.save(p100);

Producto p101 = new Producto();
p101.setNombre("Mazapán");
p101.setPrecioUnitario(new BigDecimal("6"));
p101.setGTIN("4792");
p101.setCantInv(BigDecimal.valueOf(30));
repositorioProducto.save(p101);

Producto p102 = new Producto();
p102.setNombre("Paleta de dulce");
p102.setPrecioUnitario(new BigDecimal("3"));
p102.setGTIN("5814");
p102.setCantInv(BigDecimal.valueOf(40));
repositorioProducto.save(p102);

Producto p103 = new Producto();
p103.setNombre("Pulparindo");
p103.setPrecioUnitario(new BigDecimal("8"));
p103.setGTIN("6925");
p103.setCantInv(BigDecimal.valueOf(25));
repositorioProducto.save(p103);

Producto p104 = new Producto();
p104.setNombre("Tamarindo");
p104.setPrecioUnitario(new BigDecimal("5"));
p104.setGTIN("7136");
p104.setCantInv(BigDecimal.valueOf(30));
repositorioProducto.save(p104);

Producto p105 = new Producto();
p105.setNombre("Gomitas");
p105.setPrecioUnitario(new BigDecimal("1"));
p105.setGTIN("8247");
p105.setAGranel(true);
p105.setCantInv(BigDecimal.valueOf(60));
repositorioProducto.save(p105);

Producto p106 = new Producto();
p106.setNombre("Chocolate en barra");
p106.setPrecioUnitario(new BigDecimal("15"));
p106.setGTIN("9358");
p106.setCantInv(BigDecimal.valueOf(20));
repositorioProducto.save(p106);

Producto p107 = new Producto();
p107.setNombre("Palomitas para microondas");
p107.setPrecioUnitario(new BigDecimal("18"));
p107.setGTIN("1469");
p107.setCantInv(BigDecimal.valueOf(12));
repositorioProducto.save(p107);

Producto p108 = new Producto();
p108.setNombre("Gelatina en polvo");
p108.setPrecioUnitario(new BigDecimal("12"));
p108.setGTIN("2582");
p108.setCantInv(BigDecimal.valueOf(15));
repositorioProducto.save(p108);

Producto p109 = new Producto();
p109.setNombre("Flan en polvo");
p109.setPrecioUnitario(new BigDecimal("16"));
p109.setGTIN("3693");
p109.setCantInv(BigDecimal.valueOf(10));
repositorioProducto.save(p109);

Producto p110 = new Producto();
p110.setNombre("Leche condensada");
p110.setPrecioUnitario(new BigDecimal("32"));
p110.setGTIN("4714");
p110.setCantInv(BigDecimal.valueOf(8));
repositorioProducto.save(p110);

Producto p111 = new Producto();
p111.setNombre("Leche evaporada");
p111.setPrecioUnitario(new BigDecimal("25"));
p111.setGTIN("5825");
p111.setCantInv(BigDecimal.valueOf(10));
repositorioProducto.save(p111);

Producto p112 = new Producto();
p112.setNombre("Salsa verde");
p112.setPrecioUnitario(new BigDecimal("18"));
p112.setGTIN("6936");
p112.setCantInv(BigDecimal.valueOf(12));
repositorioProducto.save(p112);

Producto p113 = new Producto();
p113.setNombre("Salsa roja");
p113.setPrecioUnitario(new BigDecimal("18"));
p113.setGTIN("7147");
p113.setCantInv(BigDecimal.valueOf(12));
repositorioProducto.save(p113);


        LocalDateTime ahora = LocalDateTime.now();

    // VENTA 1 - Hoy
    Venta v1 = new Venta();
    v1.setFecha(ahora);
    v1.setTipoPago(Venta.TipoPago.EFECTIVO);

    DetalleVenta d1 = new DetalleVenta();
    d1.setNombreProducto("Agua natural 600ml");
    d1.setGTIN("456");
    d1.setCantidad(new BigDecimal("2"));
    d1.setCosto(new BigDecimal("15"));
    d1.setVenta(v1);
    v1.getDetalles().add(d1);

    DetalleVenta d2 = new DetalleVenta();
    d2.setNombreProducto("Sabritas");
    d2.setGTIN("1234");
    d2.setCantidad(new BigDecimal("1"));
    d2.setCosto(new BigDecimal("17"));
    d2.setVenta(v1);
    v1.getDetalles().add(d2);

    DetalleVenta d3 = new DetalleVenta();
    d3.setNombreProducto("Gansito");
    d3.setGTIN("666");
    d3.setCantidad(new BigDecimal("2"));
    d3.setCosto(new BigDecimal("18"));
    d3.setVenta(v1);
    v1.getDetalles().add(d3);

    v1.setTotal(new BigDecimal("83"));
    repositorioVenta.save(v1);


    // VENTA 2 - Hoy
    Venta v2 = new Venta();
    v2.setFecha(ahora.minusMinutes(35));
    v2.setTipoPago(Venta.TipoPago.TARJETA);

    DetalleVenta d4 = new DetalleVenta();
    d4.setNombreProducto("Coca Cola 600ml");
    d4.setGTIN("111");
    d4.setCantidad(new BigDecimal("2"));
    d4.setCosto(new BigDecimal("18"));
    d4.setVenta(v2);
    v2.getDetalles().add(d4);

    DetalleVenta d5 = new DetalleVenta();
    d5.setNombreProducto("Vasos desechables");
    d5.setGTIN("7501219112605");
    d5.setCantidad(new BigDecimal("1"));
    d5.setCosto(new BigDecimal("1"));
    d5.setVenta(v2);
    v2.getDetalles().add(d5);

    DetalleVenta d6 = new DetalleVenta();
    d6.setNombreProducto("Galletas");
    d6.setGTIN("4321");
    d6.setCantidad(new BigDecimal("2"));
    d6.setCosto(new BigDecimal("12"));
    d6.setVenta(v2);
    v2.getDetalles().add(d6);

    v2.setTotal(new BigDecimal("61"));
    repositorioVenta.save(v2);


    // VENTA 3 - Hoy
    Venta v3 = new Venta();
    v3.setFecha(ahora.minusHours(1));
    v3.setTipoPago(Venta.TipoPago.TRANSFERENCIA);

    DetalleVenta d7 = new DetalleVenta();
    d7.setNombreProducto("Leche 1L");
    d7.setGTIN("789");
    d7.setCantidad(new BigDecimal("2"));
    d7.setCosto(new BigDecimal("28"));
    d7.setVenta(v3);
    v3.getDetalles().add(d7);

    DetalleVenta d8 = new DetalleVenta();
    d8.setNombreProducto("Pan de caja");
    d8.setGTIN("1467");
    d8.setCantidad(new BigDecimal("1"));
    d8.setCosto(new BigDecimal("48"));
    d8.setVenta(v3);
    v3.getDetalles().add(d8);

    v3.setTotal(new BigDecimal("104"));
    repositorioVenta.save(v3);


    // VENTA 4 - Ayer
    Venta v4 = new Venta();
    v4.setFecha(ahora.minusDays(1).withHour(10).withMinute(25));
    v4.setTipoPago(Venta.TipoPago.EFECTIVO);

    DetalleVenta d9 = new DetalleVenta();
    d9.setNombreProducto("Arroz 1kg");
    d9.setGTIN("2468");
    d9.setCantidad(new BigDecimal("2"));
    d9.setCosto(new BigDecimal("32"));
    d9.setVenta(v4);
    v4.getDetalles().add(d9);

    DetalleVenta d10 = new DetalleVenta();
    d10.setNombreProducto("Frijoles en lata");
    d10.setGTIN("3344");
    d10.setCantidad(new BigDecimal("1"));
    d10.setCosto(new BigDecimal("19"));
    d10.setVenta(v4);
    v4.getDetalles().add(d10);

    DetalleVenta d11 = new DetalleVenta();
    d11.setNombreProducto("Aceite 1L");
    d11.setGTIN("147258");
    d11.setCantidad(new BigDecimal("1"));
    d11.setCosto(new BigDecimal("45"));
    d11.setVenta(v4);
    v4.getDetalles().add(d11);

    v4.setTotal(new BigDecimal("128"));
    repositorioVenta.save(v4);


    // VENTA 5 - Ayer
    Venta v5 = new Venta();
    v5.setFecha(ahora.minusDays(1).withHour(13).withMinute(40));
    v5.setTipoPago(Venta.TipoPago.TARJETA);

    DetalleVenta d12 = new DetalleVenta();
    d12.setNombreProducto("Refresco 600ml");
    d12.setGTIN("654");
    d12.setCantidad(new BigDecimal("3"));
    d12.setCosto(new BigDecimal("18"));
    d12.setVenta(v5);
    v5.getDetalles().add(d12);

    DetalleVenta d13 = new DetalleVenta();
    d13.setNombreProducto("Chocorroles");
    d13.setGTIN("8877");
    d13.setCantidad(new BigDecimal("1"));
    d13.setCosto(new BigDecimal("19"));
    d13.setVenta(v5);
    v5.getDetalles().add(d13);

    DetalleVenta d14 = new DetalleVenta();
    d14.setNombreProducto("Chocolate");
    d14.setGTIN("987");
    d14.setCantidad(new BigDecimal("2"));
    d14.setCosto(new BigDecimal("10"));
    d14.setVenta(v5);
    v5.getDetalles().add(d14);

    v5.setTotal(new BigDecimal("93"));
    repositorioVenta.save(v5);


    // VENTA 6 - Hace 2 días
    Venta v6 = new Venta();
    v6.setFecha(ahora.minusDays(2).withHour(11).withMinute(15));
    v6.setTipoPago(Venta.TipoPago.EFECTIVO);

    DetalleVenta d15 = new DetalleVenta();
    d15.setNombreProducto("Huevo por pieza");
    d15.setGTIN("8642");
    d15.setCantidad(new BigDecimal("6"));
    d15.setCosto(new BigDecimal("4"));
    d15.setVenta(v6);
    v6.getDetalles().add(d15);

    DetalleVenta d16 = new DetalleVenta();
    d16.setNombreProducto("Tortillas");
    d16.setGTIN("3689");
    d16.setCantidad(new BigDecimal("1"));
    d16.setCosto(new BigDecimal("24"));
    d16.setVenta(v6);
    v6.getDetalles().add(d16);

    DetalleVenta d17 = new DetalleVenta();
    d17.setNombreProducto("Queso panela");
    d17.setGTIN("4979");
    d17.setCantidad(new BigDecimal("1"));
    d17.setCosto(new BigDecimal("75"));
    d17.setVenta(v6);
    v6.getDetalles().add(d17);

    v6.setTotal(new BigDecimal("123"));
    repositorioVenta.save(v6);


    // VENTA 7 - Hace 2 días
    Venta v7 = new Venta();
    v7.setFecha(ahora.minusDays(2).withHour(17).withMinute(30));
    v7.setTipoPago(Venta.TipoPago.TRANSFERENCIA);

    DetalleVenta d18 = new DetalleVenta();
    d18.setNombreProducto("Shampoo");
    d18.setGTIN("8857");
    d18.setCantidad(new BigDecimal("1"));
    d18.setCosto(new BigDecimal("65"));
    d18.setVenta(v7);
    v7.getDetalles().add(d18);

    DetalleVenta d19 = new DetalleVenta();
    d19.setNombreProducto("Jabón de baño");
    d19.setGTIN("1593");
    d19.setCantidad(new BigDecimal("2"));
    d19.setCosto(new BigDecimal("18"));
    d19.setVenta(v7);
    v7.getDetalles().add(d19);

    DetalleVenta d20 = new DetalleVenta();
    d20.setNombreProducto("Papel higiénico");
    d20.setGTIN("3692");
    d20.setCantidad(new BigDecimal("1"));
    d20.setCosto(new BigDecimal("45"));
    d20.setVenta(v7);
    v7.getDetalles().add(d20);

    v7.setTotal(new BigDecimal("146"));
    repositorioVenta.save(v7);


    // VENTA 8 - Hace 3 días
    Venta v8 = new Venta();
    v8.setFecha(ahora.minusDays(3).withHour(12).withMinute(10));
    v8.setTipoPago(Venta.TipoPago.TARJETA);

    DetalleVenta d21 = new DetalleVenta();
    d21.setNombreProducto("Cereal de maíz");
    d21.setGTIN("258");
    d21.setCantidad(new BigDecimal("1"));
    d21.setCosto(new BigDecimal("55"));
    d21.setVenta(v8);
    v8.getDetalles().add(d21);

    DetalleVenta d22 = new DetalleVenta();
    d22.setNombreProducto("Yogur natural");
    d22.setGTIN("3868");
    d22.setCantidad(new BigDecimal("3"));
    d22.setCosto(new BigDecimal("18"));
    d22.setVenta(v8);
    v8.getDetalles().add(d22);

    DetalleVenta d23 = new DetalleVenta();
    d23.setNombreProducto("Boing mango");
    d23.setGTIN("555");
    d23.setCantidad(new BigDecimal("2"));
    d23.setCosto(new BigDecimal("12"));
    d23.setVenta(v8);
    v8.getDetalles().add(d23);

    v8.setTotal(new BigDecimal("133"));
    repositorioVenta.save(v8);


    // VENTA 9 - Hace 4 días
    Venta v9 = new Venta();
    v9.setFecha(ahora.minusDays(4).withHour(16).withMinute(45));
    v9.setTipoPago(Venta.TipoPago.EFECTIVO);

    DetalleVenta d24 = new DetalleVenta();
    d24.setNombreProducto("Mayonesa");
    d24.setGTIN("963");
    d24.setCantidad(new BigDecimal("1"));
    d24.setCosto(new BigDecimal("38"));
    d24.setVenta(v9);
    v9.getDetalles().add(d24);

    DetalleVenta d25 = new DetalleVenta();
    d25.setNombreProducto("Catsup");
    d25.setGTIN("159");
    d25.setCantidad(new BigDecimal("1"));
    d25.setCosto(new BigDecimal("25"));
    d25.setVenta(v9);
    v9.getDetalles().add(d25);

    DetalleVenta d26 = new DetalleVenta();
    d26.setNombreProducto("Sopa instantánea");
    d26.setGTIN("6655");
    d26.setCantidad(new BigDecimal("3"));
    d26.setCosto(new BigDecimal("18"));
    d26.setVenta(v9);
    v9.getDetalles().add(d26);

    DetalleVenta d27 = new DetalleVenta();
    d27.setNombreProducto("Palomitas");
    d27.setGTIN("1468");
    d27.setCantidad(new BigDecimal("2"));
    d27.setCosto(new BigDecimal("15"));
    d27.setVenta(v9);
    v9.getDetalles().add(d27);

    v9.setTotal(new BigDecimal("147"));
    repositorioVenta.save(v9);


    // VENTA 10 - Hace 5 días
    Venta v10 = new Venta();
    v10.setFecha(ahora.minusDays(5).withHour(19).withMinute(20));
    v10.setTipoPago(Venta.TipoPago.TRANSFERENCIA);

    DetalleVenta d28 = new DetalleVenta();
    d28.setNombreProducto("Café soluble");
    d28.setGTIN("642");
    d28.setCantidad(new BigDecimal("1"));
    d28.setCosto(new BigDecimal("85"));
    d28.setVenta(v10);
    v10.getDetalles().add(d28);

    DetalleVenta d29 = new DetalleVenta();
    d29.setNombreProducto("Azúcar 1kg");
    d29.setGTIN("7531");
    d29.setCantidad(new BigDecimal("2"));
    d29.setCosto(new BigDecimal("29"));
    d29.setVenta(v10);
    v10.getDetalles().add(d29);

    DetalleVenta d30 = new DetalleVenta();
    d30.setNombreProducto("Leche deslactosada");
    d30.setGTIN("2757");
    d30.setCantidad(new BigDecimal("2"));
    d30.setCosto(new BigDecimal("32"));
    d30.setVenta(v10);
    v10.getDetalles().add(d30);

    v10.setTotal(new BigDecimal("207"));
    repositorioVenta.save(v10);
    }
}