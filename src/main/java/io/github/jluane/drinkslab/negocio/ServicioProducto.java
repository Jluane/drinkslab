package io.github.jluane.drinkslab.negocio;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jluane.drinkslab.datos.RepositorioProducto;
import io.github.jluane.drinkslab.datos.RepositorioProveedor;
import io.github.jluane.drinkslab.datos.RepositorioRelacionProductoProveedor;
import io.github.jluane.drinkslab.negocio.modelo.Producto;
import io.github.jluane.drinkslab.negocio.modelo.Proveedor;
import io.github.jluane.drinkslab.negocio.modelo.RelacionProductoProveedor;

@Service
public class ServicioProducto {
    // Repositorios a los que se conecta
    private final RepositorioProducto repositorioProducto;
    private final RepositorioRelacionProductoProveedor repositorioRelacionProductoProveedor;
    private final RepositorioProveedor repositorioProveedor;

    @Autowired
    public ServicioProducto(RepositorioProducto repositorioProducto, RepositorioRelacionProductoProveedor repositorioRelacionProductoProveedor, RepositorioProveedor repositorioProveedor){
        this.repositorioProducto = repositorioProducto;
        this.repositorioRelacionProductoProveedor = repositorioRelacionProductoProveedor;
        this.repositorioProveedor = repositorioProveedor;
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
        if(producto.getCantPaquete() != null && producto.getCantPaquete().compareTo(BigDecimal.ZERO) > 0){
            BigDecimal division[] = restante.divideAndRemainder(producto.getCantPaquete()); // En [0] guarda el cosiente, y en [1] guarda el residuo
            
            BigDecimal paquetes = division[0]; // Cantidad de veces que se hace el paquete
            restante = division[1]; // Cantidad de piezas que no entran en el paquete

            total = total.add(paquetes.multiply(producto.getPrecioPaquete()));
        }

        // Evalúa el costo de la cantidad de promociones que se complementen
        if(producto.getCantPromocion() != null && producto.getCantPromocion().compareTo(BigDecimal.ZERO) > 0){
            BigDecimal division[] = restante.divideAndRemainder(producto.getCantPromocion());

            BigDecimal promociones = division[0]; // Cant de paquetes
            restante = division[1]; // Unidades que sobran

            total = total.add(promociones.multiply(producto.getPrecioPromocion()));
        }

        total = total.add(restante.multiply(producto.getPrecioUnitario())); // Sumamos a los restantes

        // Se redondea para que siempre tenga valores enteros
        return total.setScale(0, RoundingMode.CEILING)
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
    @Transactional(readOnly = true)
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
    @Transactional(readOnly = true)
    public List<String> obtenerSugerenciasNombres(String texto){
        // Conseguimos los productos que se tienen con ese texto
        List<Producto> productos = repositorioProducto.findTop5ByNombreContainingIgnoreCase(texto);
        // Se transforma la lista a lista de String y se regresa
        return productos.stream().map(Producto::getNombre).toList();
    }

    /**
     * Método que revisa que se guarde o actualice un producto en el repositorio, asegurándose de que no tenga un código repetido
     * @param producto Es el producto a actualizar o a guardar si es nuevo
     * @return Regresa true si se logra guardar el producto, false si no
     */
    @Transactional
    public boolean guardarProducto(Producto producto, String nombre, String codigo, String precio, String cant, String cantPaquete, String precioPaquete, String cantPromo, String precioPromo, boolean aGranel, List<Proveedor> proveedores){
        // En caso de tener que verificar que el codigo no exista ya
        if(producto.getIdProducto() == null || !producto.getGTIN().equals(codigo))
            if(repositorioProducto.findByGTIN(codigo) != null){
                return false;
            }
        
        try{
            producto.setNombre(nombre.trim().toUpperCase());
            producto.setGTIN(codigo);
            producto.setPrecioUnitario(new BigDecimal(precio));
            producto.setCantInv(new BigDecimal(cant));
            producto.setCantPaquete(new BigDecimal(cantPaquete));
            producto.setPrecioPaquete(new BigDecimal(precioPaquete));
            producto.setCantPromocion(new BigDecimal(cantPromo));
            producto.setPrecioPromocion(new BigDecimal(precioPromo));
            producto.setAGranel(aGranel);
            
            producto = repositorioProducto.save(producto);
            if(producto == null || !actualizarProveedores(producto, proveedores))
                return false;

            return true;
        }catch(Exception e){
            System.err.println("No se pudo guardar "+e);
            return false;
        }

        
    }

    /**
     * Actualizamos a los proveedores a los que un producto está relacionado
     * @param producto Es el producto a actualizar
     * @param proveedores Son los proveedores que queremos asignarle
     * @return
     */
    @Transactional
    private boolean actualizarProveedores(Producto producto, List<Proveedor> proveedores){
        // Guardamos y evaluamos a los proveedores
        // Obtenemos la lista de nuevas relaciones y de las antiguas
        List<RelacionProductoProveedor> relacionesActuales = repositorioRelacionProductoProveedor.findByProducto(producto);
        List<RelacionProductoProveedor> relacionesNuevas = new ArrayList<>();
        
        // Listas que guardarán las cosas a cambiar
        List<RelacionProductoProveedor> aCrear = new ArrayList<>();

        // Convertimos a los proveedores en relaciones con el producto
        for(Proveedor prov : proveedores){
            relacionesNuevas.add(new RelacionProductoProveedor(producto, prov));
        }

        // Evaluamos entre todos para verificar si existen coincidencias. Aquellos que tenga nueva y no 
        for(RelacionProductoProveedor nueva : relacionesNuevas){
            RelacionProductoProveedor quitar = null; // Nos dice si debemos quitar al actual de la lista para que no se borre
            
            // Busca coincidencia entre nueva y actual
            for(RelacionProductoProveedor actual : relacionesActuales){
                // Cuando consigue coincidencia, quita a actual de la lista de relaciones actuales
                if(nueva.equals(actual)){
                    quitar = actual;
                    break;
                }
            }
            if(quitar == null){ // Si no hubo coincidencia, se creará a nueva
                aCrear.add(nueva);
            }else
                relacionesActuales.remove(quitar); // Si hubo coincidencia, quitamos a actual de su lista. Ya sabemos que existe
            // relacionesActuales conserva solamente a aquellos que nunca tuvieron coincidencia, y que por lo tanto, serán eliminados
        }
        // En nueva quedaron todos los que queremos que se queden, en actual todos los que ya no queremos que estén
        
        try{
            // Eliminamos a las relaciones viejas
            for(RelacionProductoProveedor actual : relacionesActuales)
                repositorioRelacionProductoProveedor.delete(actual);

            // Agregamos a las relaciones nuevas
            for(RelacionProductoProveedor nueva : aCrear){
                repositorioProveedor.save(nueva.getProveedor());
                repositorioRelacionProductoProveedor.save(nueva);
            }
            return true;
        }catch(Exception e){
            return false;
        }
    }

    /**
     * Elimina a un producto del repositorio
     * @param producto Es el producto a eliminar
     * @return Regresa true si se eliminó con exito, false si no se logró
     */
    @Transactional
    public boolean eliminarProducto(Producto producto){
        try{
            repositorioProducto.delete(producto);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    /**
     * Método que regresa a los proveedores que tiene un producto
     * @param producto Producto del que se quiere a los proveedores
     * @return Regresa una lista de proveedores
     */
    @Transactional(readOnly = true)
    public List<Proveedor> obtenerProveedores(Producto producto){
        if(producto == null||producto.getIdProducto() == null) return new ArrayList<Proveedor>();
        List<RelacionProductoProveedor> lista = repositorioRelacionProductoProveedor.findByProducto(producto);
        List<Proveedor> proveedores = new ArrayList<>();
        for(RelacionProductoProveedor rel : lista){
            proveedores.add(rel.getProveedor());
        }
        return proveedores;
    }
}
