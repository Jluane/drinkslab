package io.github.jluane.drinkslab.presentacion;

import java.util.function.Consumer;

import org.springframework.stereotype.Component;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

@Component
public class LectorCodigoBarras {
    private static final int VELOCIDAD = 9600;

    private SerialPort puerto;
    private final StringBuilder buffer = new StringBuilder();

    public void iniciar(Consumer<String> alRecibir){
        if(puerto != null && puerto.isOpen()){
            return;
        }

        puerto = detectarPuerto();
        if(puerto == null){
            return;
        }

        puerto.setComPortParameters(
            VELOCIDAD,
            8,
            SerialPort.ONE_STOP_BIT,
            SerialPort.NO_PARITY
        );

        puerto.setComPortTimeouts(
            SerialPort.TIMEOUT_NONBLOCKING,
            0,
            0
        );

        if (!puerto.openPort()) {
            System.err.println("No se pudo abrir el puerto del lector");
            return;
        }

        System.out.println("Lector conectado correctamente");

        puerto.addDataListener(new SerialPortDataListener() {
            @Override 
            public int getListeningEvents(){
                return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
            }

            @Override
            public void serialEvent(SerialPortEvent evento){
                byte[] datos = evento.getReceivedData();
                if(datos == null) return;

                synchronized(buffer){
                    for(byte dato:datos){
                        char caracter = (char) (dato & 0xFF);

                        //Enter puede llegar como CR, LF o CRLF
                        if(caracter == '\r' || caracter == '\n'){
                            if(buffer.length() > 0){
                                String codigo = buffer.toString().trim();
                                buffer.setLength(0);

                                if(!codigo.isEmpty()){
                                    alRecibir.accept(codigo);
                                }
                            }
                        }else{
                            buffer.append(caracter);
                        }
                    }
                }
            }
        });
    }

    /**
     * Cierra la comunicación con el lector
     */
    public void detener(){
        if(puerto != null && puerto.isOpen()){
            puerto.removeDataListener();
            puerto.closePort();
            System.out.println("Lector desconectado");
        }
        puerto = null;

        synchronized(buffer){
            buffer.setLength(0);
        }
    }

    /**
     * Detectar al puerto por si cambia
     */
    private SerialPort detectarPuerto(){
        SerialPort[] puertos = SerialPort.getCommPorts();
        
        for(SerialPort puerto:puertos){
            String nombre = puerto.getSystemPortName();

            if(nombre.startsWith("ttyACM") || nombre.startsWith("ttyUSB")){
                System.out.println("Lector detectado en: "+nombre);
                return puerto;
            }
        }
        System.err.println("No se encontró ningún lector de códigos de barras");
        return null;
    }
}
