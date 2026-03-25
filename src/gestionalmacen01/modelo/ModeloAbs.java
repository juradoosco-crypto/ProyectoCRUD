package gestionalmacen01.modelo;
import java.util.List;

/**
 * Abstract class ModeloAbs - Clase de acceso a Modelo de DATOS
 * 
 * @author: Alberto Lopez
 * Date: 24/03/2026
 */
public interface  ModeloAbs
{
    
    public boolean insertarProducto ( Producto p);
    
    boolean borrarProducto ( int codigo );
    
    boolean modificarProducto (Producto nuevo);
    
    public Producto buscarProducto ( int codigo);
    
    void imprimirProductosTodos ();
    
    List <Producto> obtenerProductos(); 
    
    List <Producto> obtenerProductosStockMin();
    
    
    
    
}
