package io.github.jluane.drinkslab;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.github.jluane.drinkslab.datos.RepositorioProducto;
import io.github.jluane.drinkslab.datos.RepositorioProveedor;
import io.github.jluane.drinkslab.datos.RepositorioRelacionProductoProveedor;
import io.github.jluane.drinkslab.datos.RepositorioVenta;
import io.github.jluane.drinkslab.negocio.modelo.DetalleVenta;
import io.github.jluane.drinkslab.negocio.modelo.Producto;
import io.github.jluane.drinkslab.negocio.modelo.Proveedor;
import io.github.jluane.drinkslab.negocio.modelo.RelacionProductoProveedor;
import io.github.jluane.drinkslab.negocio.modelo.Venta;

@Component
public class DatosPrueba {
    // Repositorios para la base de datos de prueba
    private final RepositorioProducto repositorioProducto;
    private final RepositorioVenta repositorioVenta;
    private final RepositorioProveedor repositorioProveedor;
    private final RepositorioRelacionProductoProveedor repositorioRelacionProductoProveedor;

    @Autowired
    private DataSource dataSource;

    @Autowired
    public DatosPrueba(RepositorioProducto repositorioProducto, RepositorioVenta repositorioVenta, RepositorioProveedor repositorioProveedor, RepositorioRelacionProductoProveedor repositorioRelacionProductoProveedor){
        this.repositorioProducto = repositorioProducto;
        this.repositorioVenta = repositorioVenta;
        this.repositorioProveedor = repositorioProveedor;
        this.repositorioRelacionProductoProveedor = repositorioRelacionProductoProveedor;
    }
    
    private boolean esPersistente() {
        try (Connection conexion = dataSource.getConnection()) {
                String url = conexion.getMetaData().getURL();

                return url.startsWith("jdbc:h2:file:");
        } catch (SQLException e) {
                throw new RuntimeException("Error al consultar la conexión", e);
        }
    }
    
    /**
     * Inicializa BD para pruebas
     * **SÓLO ACTIVAR EN CASO DE ESTAR EN BASE DE DATOS NO PERSISTENTE**
    */
    public void iniciarDatosPruebaBD() {

        if (esPersistente()) {
              return;
        }


        // =========================
        // PROVEEDORES
        // =========================

        Proveedor cocaCola = repositorioProveedor.save(new Proveedor("Coca-Cola FEMSA"));
        Proveedor pepsiCo = repositorioProveedor.save(new Proveedor("PepsiCo"));
        Proveedor bonafont = repositorioProveedor.save(new Proveedor("Bonafont"));
        Proveedor bimbo = repositorioProveedor.save(new Proveedor("Grupo Bimbo"));
        Proveedor lala = repositorioProveedor.save(new Proveedor("Lala"));
        Proveedor gamesa = repositorioProveedor.save(new Proveedor("Gamesa"));
        Proveedor verdeValle = repositorioProveedor.save(new Proveedor("Verde Valle"));
        Proveedor laCostena = repositorioProveedor.save(new Proveedor("La Costeña"));
        Proveedor bachoco = repositorioProveedor.save(new Proveedor("Bachoco"));
        Proveedor tortilleria = repositorioProveedor.save(new Proveedor("Tortillería La Esperanza"));
        Proveedor nutrioli = repositorioProveedor.save(new Proveedor("Nutrioli"));


        // =========================
        // PRODUCTOS
        // =========================

        Producto coca = crearProducto(
                "Coca Cola 600ml", "18", "111", "30", false
        );

        Producto agua = crearProducto(
                "Agua natural 600ml", "15", "456", "40", false
        );

        Producto papas = crearProducto(
                "Sabritas", "17", "1234", "25", false
        );

        Producto galletas = crearProducto(
                "Galletas", "12", "4321", "30", false
        );

        Producto leche = crearProducto(
                "Leche 1L", "28", "789", "20", false
        );

        Producto pan = crearProducto(
                "Pan de caja", "48", "1467", "12", false
        );

        Producto arroz = crearProducto(
                "Arroz 1kg", "32", "2468", "25", false
        );

        Producto frijoles = crearProducto(
                "Frijoles en lata", "19", "3344", "18", false
        );

        Producto aceite = crearProducto(
                "Aceite 1L", "45", "147258", "15", false
        );

        Producto huevo = crearProducto(
                "Huevo por pieza", "4", "8642", "90", true
        );

        Producto tortillas = crearProducto(
                "Tortillas", "24", "3689", "35", true
        );

        Producto queso = crearProducto(
                "Queso panela", "75", "4979", "10", false
        );


        // =========================
        // RELACIONES PRODUCTO - PROVEEDOR
        // =========================

        // Bebidas
        relacionar(coca, cocaCola);
        relacionar(coca, pepsiCo);

        relacionar(agua, bonafont);
        relacionar(agua, cocaCola);

        // Botanas y galletas
        relacionar(papas, pepsiCo);
        relacionar(papas, gamesa);

        relacionar(galletas, gamesa);
        relacionar(galletas, bimbo);

        // Pan y lácteos
        relacionar(leche, lala);
        relacionar(leche, bimbo);

        relacionar(pan, bimbo);

        relacionar(queso, lala);

        // Abarrotes
        relacionar(arroz, verdeValle);
        relacionar(arroz, laCostena);

        relacionar(frijoles, laCostena);

        relacionar(aceite, nutrioli);

        // Productos frescos
        relacionar(huevo, bachoco);

        relacionar(tortillas, tortilleria);


        // =========================
        // VENTAS
        // =========================

        LocalDateTime ahora = LocalDateTime.now();

        // VENTA 1 - Hoy
        Venta v1 = crearVenta(
                ahora, Venta.TipoPago.EFECTIVO, "65"
        );

        agregarDetalle(v1, "Agua natural 600ml", "456", "2", "15");
        agregarDetalle(v1, "Sabritas", "1234", "1", "17");
        agregarDetalle(v1, "Coca Cola 600ml", "111", "1", "18");

        repositorioVenta.save(v1);


        // VENTA 2 - Hoy
        Venta v2 = crearVenta(
                ahora.minusMinutes(35), Venta.TipoPago.TARJETA, "75"
        );

        agregarDetalle(v2, "Coca Cola 600ml", "111", "2", "18");
        agregarDetalle(v2, "Galletas", "4321", "2", "12");
        agregarDetalle(v2, "Agua natural 600ml", "456", "1", "15");

        repositorioVenta.save(v2);


        // VENTA 3 - Hoy
        Venta v3 = crearVenta(
                ahora.minusHours(1), Venta.TipoPago.TRANSFERENCIA, "128"
        );

        agregarDetalle(v3, "Leche 1L", "789", "2", "28");
        agregarDetalle(v3, "Pan de caja", "1467", "1", "48");
        agregarDetalle(v3, "Huevo por pieza", "8642", "6", "4");

        repositorioVenta.save(v3);


        // VENTA 4 - Ayer
        Venta v4 = crearVenta(
                ahora.minusDays(1).withHour(10).withMinute(25),
                Venta.TipoPago.EFECTIVO, "128"
        );

        agregarDetalle(v4, "Arroz 1kg", "2468", "2", "32");
        agregarDetalle(v4, "Frijoles en lata", "3344", "1", "19");
        agregarDetalle(v4, "Aceite 1L", "147258", "1", "45");

        repositorioVenta.save(v4);


        // VENTA 5 - Ayer
        Venta v5 = crearVenta(
                ahora.minusDays(1).withHour(13).withMinute(40),
                Venta.TipoPago.TARJETA, "83"
        );

        agregarDetalle(v5, "Coca Cola 600ml", "111", "3", "18");
        agregarDetalle(v5, "Galletas", "4321", "1", "12");
        agregarDetalle(v5, "Sabritas", "1234", "1", "17");

        repositorioVenta.save(v5);


        // VENTA 6 - Hace 2 días
        Venta v6 = crearVenta(
                ahora.minusDays(2).withHour(11).withMinute(15),
                Venta.TipoPago.EFECTIVO, "123"
        );

        agregarDetalle(v6, "Huevo por pieza", "8642", "6", "4");
        agregarDetalle(v6, "Tortillas", "3689", "1", "24");
        agregarDetalle(v6, "Queso panela", "4979", "1", "75");

        repositorioVenta.save(v6);


        // VENTA 7 - Hace 2 días
        Venta v7 = crearVenta(
                ahora.minusDays(2).withHour(17).withMinute(30),
                Venta.TipoPago.TRANSFERENCIA, "151"
        );

        agregarDetalle(v7, "Leche 1L", "789", "1", "28");
        agregarDetalle(v7, "Queso panela", "4979", "1", "75");
        agregarDetalle(v7, "Pan de caja", "1467", "1", "48");

        repositorioVenta.save(v7);


        // VENTA 8 - Hace 3 días
        Venta v8 = crearVenta(
                ahora.minusDays(3).withHour(12).withMinute(10),
                Venta.TipoPago.TARJETA, "94"
        );

        agregarDetalle(v8, "Agua natural 600ml", "456", "2", "15");
        agregarDetalle(v8, "Leche 1L", "789", "1", "28");
        agregarDetalle(v8, "Coca Cola 600ml", "111", "2", "18");

        repositorioVenta.save(v8);


        // VENTA 9 - Hace 4 días
        Venta v9 = crearVenta(
                ahora.minusDays(4).withHour(16).withMinute(45),
                Venta.TipoPago.EFECTIVO, "115"
        );

        agregarDetalle(v9, "Arroz 1kg", "2468", "1", "32");
        agregarDetalle(v9, "Frijoles en lata", "3344", "2", "19");
        agregarDetalle(v9, "Aceite 1L", "147258", "1", "45");

        repositorioVenta.save(v9);


        // VENTA 10 - Hace 5 días
        Venta v10 = crearVenta(
                ahora.minusDays(5).withHour(19).withMinute(20),
                Venta.TipoPago.TRANSFERENCIA, "88"
        );

        agregarDetalle(v10, "Pan de caja", "1467", "1", "48");
        agregarDetalle(v10, "Huevo por pieza", "8642", "4", "4");
        agregarDetalle(v10, "Tortillas", "3689", "1", "24");

        repositorioVenta.save(v10);
    }

    private Producto crearProducto(
            String nombre,
            String precio,
            String gtin,
            String inventario,
            boolean aGranel) {

        Producto p = new Producto();

        p.setNombre(nombre);
        p.setPrecioUnitario(new BigDecimal(precio));
        p.setGTIN(gtin);
        p.setCantInv(new BigDecimal(inventario));
        p.setAGranel(aGranel);

        return repositorioProducto.save(p);
    }


    private void relacionar(Producto producto, Proveedor proveedor) {

        repositorioRelacionProductoProveedor.save(
                new RelacionProductoProveedor(producto, proveedor)
        );
    }


    private Venta crearVenta(
            LocalDateTime fecha,
            Venta.TipoPago tipoPago,
            String total) {

        Venta v = new Venta();

        v.setFecha(fecha);
        v.setTipoPago(tipoPago);
        v.setTotal(new BigDecimal(total));

        return v;
    }


    private void agregarDetalle(
            Venta venta,
            String nombre,
            String gtin,
            String cantidad,
            String costo) {

        DetalleVenta detalle = new DetalleVenta();

        detalle.setNombreProducto(nombre);
        detalle.setGTIN(gtin);
        detalle.setCantidad(new BigDecimal(cantidad));
        detalle.setCosto(new BigDecimal(costo));

        detalle.setVenta(venta);
        venta.getDetalles().add(detalle);
    }
}
