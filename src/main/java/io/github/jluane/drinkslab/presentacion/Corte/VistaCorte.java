package io.github.jluane.drinkslab.presentacion.Corte;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import io.github.jluane.drinkslab.negocio.modelo.DetalleVenta;
import io.github.jluane.drinkslab.negocio.modelo.Venta;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

@Component
public class VistaCorte {
    // Controlador de esta pantalla
    private  ControlCorte control;

    // Componentes FXML
    @FXML private Label fecha;
    @FXML private Label totalDia;
    @FXML private Label totalTarjeta;
    @FXML private Label totalTransferencia;
    @FXML private Label totalEfectivo;
    @FXML private VBox contenedorVentas;

    // Constructor
    public VistaCorte(){}

    /**
     * Se da el control que controlará a este panel
     * @param control Es específicamente el control que tendrá
     */
    public void setControlCorte(ControlCorte control){
        this.control = control;
    }

    /**
     * Inicialización
     */
    @FXML
    public void initialize(){
    }

    /**
     * Carga el FXML y devuelve el panel visual listo
     */
    public Parent cargaPanel() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ventana-corte.fxml"));
            loader.setController(this);
            Parent parent = loader.load();
            return parent;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void inicia(LocalDate fecha, List<Venta> ventas){
        this.fecha.setText(fecha.toString());
        mostrarVentas(ventas);
    }

    private void mostrarVentas(List<Venta> ventas){
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal totalTarjeta = BigDecimal.ZERO;
        BigDecimal totalTransferencia = BigDecimal.ZERO;
        BigDecimal totalEfectivo = BigDecimal.ZERO;

        contenedorVentas.getChildren().clear();

        for(Venta venta : ventas){
            String cadena = "Venta #"+venta.getIdVenta()+" :: "+venta.getFecha()+" :: "+venta.getTipoPago()+" $"+venta.getTotal();
            Label linea = new Label(cadena);

            contenedorVentas.getChildren().add(linea);
            mostrarDetalles(venta.getDetalles());
        
            BigDecimal totalVenta = venta.getTotal();
            total = total.add(totalVenta);
            switch (venta.getTipoPago()) {
                case TARJETA:
                    totalTarjeta = totalTarjeta.add(totalVenta);
                    break;
                case EFECTIVO:
                    totalEfectivo = totalEfectivo.add(totalVenta);
                    break;
                case TRANSFERENCIA:
                    totalTransferencia = totalTransferencia.add(totalVenta);
                    break;
            }
        }

        this.totalDia.setText("$"+total);
        this.totalTarjeta.setText("$"+totalTarjeta);
        this.totalTransferencia.setText("$"+totalTransferencia);
        this.totalEfectivo.setText("$"+totalEfectivo);
    }

    private void mostrarDetalles(List<DetalleVenta> detalles){
        for(DetalleVenta det : detalles){
            String cadena = "\t"+det.getGTIN()+" "+det.getNombreProducto()+" x"+det.getCantidad()+" $"+det.getCosto();
            Label linea = new Label(cadena);

            contenedorVentas.getChildren().add(linea);
        }
    }

    // On Action's
    @FXML
    public void onActionDiaAnterior(){
        control.cambiarDia(-1);
    }
    @FXML
    public void onActionDiaSiguiente(){
        control.cambiarDia(1);
    }
    @FXML
    public void onActionDiaActual(){
        control.inicia();
    }
}
