package io.github.jluane.drinkslab.negocio;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jluane.drinkslab.datos.RepositorioProducto;
import io.github.jluane.drinkslab.negocio.modelo.Producto;

@Service
public class ServicioProducto {
    // Repositorios a los que se conecta
    private final RepositorioProducto repositorioProducto;

    @Autowired
    public ServicioProducto(RepositorioProducto repositorioProducto){
        this.repositorioProducto = repositorioProducto;
    }

    /**
     * Va a repositorio para encontrar al producto que tenga al GTIN indicado
     * @param gtin Es la cadena que queremos que tenga en GTIN
     * @return Regresa al producto encontrado o null si no existen ningún objeto o si GTIN no tiene el formato esperado
     */
    @Transactional(readOnly = true)
    public Producto recuperaProducto(String texto){
        if(texto.matches("\\d+"))
            return repositorioProducto.findByGTIN(texto);
        else
            return repositorioProducto.findByNombre(texto);
    }

    /**
     * Obtiene el costo de total de la cantidad de cierto producto tomando en cuenta las posibles promociones o descuentos por paquete que puede tener
     * @param producto Es el producto del que se busca el costo
     * @param cant Es la cantidad de productos de las que se busca saber el costo
     * @return Regresa el valor del costo de la cantidad de productos establecida
     */
    public BigDecimal obtenerCosto(Producto producto, BigDecimal cant){
        if(producto == null || cant == null) return BigDecimal.ZERO;
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal restante = cant;

        // Evalúa el costo de la cantidad de paquetes que se complementen
        if(producto.getCantPaquete() != null){
            BigDecimal division[] = restante.divideAndRemainder(producto.getCantPaquete()); // En [0] guarda el cosiente, y en [1] guarda el residuo
            
            BigDecimal paquetes = division[0]; // Cantidad de veces que se hace el paquete
            restante = division[1]; // Cantidad de piezas que no entran en el paquete

            total = total.add(paquetes.multiply(producto.getPrecioPaquete()));
        }

        // Evalúa el costo de la cantidad de promociones que se complementen
        if(producto.getCantPromocion() != null){
            BigDecimal division[] = restante.divideAndRemainder(producto.getCantPromocion());

            BigDecimal promociones = division[0]; // Cant de paquetes
            restante = division[1]; // Unidades que sobran

            total = total.add(promociones.multiply(producto.getPrecioPromocion()));
        }

        total = total.add(restante.multiply(producto.getPrecioUnitario())); // Sumamos a los restantes

        // Se redondea para que siempre tenga valores de 0.50 y se redondeé hacia arriba en caso de ser necesario
        return total.divide(BigDecimal.valueOf(0.50), 0, RoundingMode.CEILING)
                    .multiply(BigDecimal.valueOf(0.50))
                    .setScale(2, RoundingMode.UNNECESSARY);
    }

    /**
     * Método encargado de realizar el conteo de cuántos productos existen en el repositorio
     * @return Regresa a la cantidad de productos en repositorio
     */
    @Transactional(readOnly = true)
    public int totalProductos(){
        return (int) repositorioProducto.count();
    }

    /**
     * Se obtiene un bloque de 8 de registros registros deseados
     * @param pagina Es el número del bloque que queremos obtener
     * @return Regresa una lista con todos los productos que se encuentran en el bloque deseado
     */
    public List<Producto> obtenerBloqueDeRegistros(int pagina){
        int tamanoBloque = 8;
        pagina--; // De esta manera tomamos el número de la página que se ve, como si iniciara en 1

        Pageable paginacion = PageRequest.of(pagina, tamanoBloque);
        
        Page<Producto> resultadoPagina = repositorioProducto.findAll(paginacion);

        return resultadoPagina.getContent();
    }

    /**
     * Regresa una lista de 5 productos con un texto similar al dado
     * @param texto Es el texto del que se busca similitud
     * @return Regresa una lista de 5 productos
     */
    public List<String> obtenerSugerenciasNombres(String texto){
        // Conseguimos los productos que se tienen con ese texto
        List<Producto> productos = repositorioProducto.findTop5ByNombreContainingIgnoreCase(texto);
        // Se transforma la lista a lista de String y se regresa
        return productos.stream().map(Producto::getNombre).toList();
    }
}
