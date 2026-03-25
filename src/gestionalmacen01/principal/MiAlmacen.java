/**
 *  MiAlmacen
 *  Programa principal de gestión de un almacén de productos
 *  implementa un CRUD ( Creat, Read, Update, Delete) ofreciendo varios
 *  informes.
 */
package gestionalmacen01.principal;

import java.util.List;
import java.util.Scanner;

import gestionalmacen01.modelo.ModeloAbs;
import gestionalmacen01.modelo.ModeloArrayList;
import gestionalmacen01.modelo.Producto;

//Completar los métodos
public class MiAlmacen
{
    // Defino como estaticas para que puedan usarse 
    // dentro de la clase sin necesidad de pasarlas como 
    // parametros.
    static private ModeloAbs almacen;
    static private Scanner sc;
    
    public static void main(String[] args){
        almacen=new ModeloArrayList ();
        sc = new Scanner(System.in);
        
        cargarDatosPrueba();
        
        int opcion=0;
        do{
		mostrarMenu();
                opcion=leerOpcion(1,9);
                switch(opcion){
                    case 1: crear();break;
                    case 2: consultar();break;
                    case 3: borrar();break;
                    case 4: modificarPrecio();break;
                    case 5: comprar();break;
                    case 6: vender();break;
                    case 7: listar();break;
                    case 8: listarPocoStock();break;
                }
                System.out.println("\n---------------------------- ");
                System.out.print("Pulse enter para continuar");
                sc.nextLine();
        }while(opcion!=9);
        System.out.println("\n Fin del programa.");
        sc.close();
        
    }
    
    
    private static void mostrarMenu(){
        System.out.println("\n\n    MENU");
        System.out.println("1. Nuevo producto ");
        System.out.println("2. Consulta producto ");
        System.out.println("3. Borrar producto ");
        System.out.println("4. Modificar precio ");
        System.out.println("5. Compra de productos ");
        System.out.println("6. Venta de productos ");
        System.out.println("7. Listado completo de productos ");
        System.out.println("8. Listado de productos con stock inferior al mínimo");
        System.out.println("9. Terminar ");
        System.out.print("Elige una opción (1-9)");        
    }
    
    // Lee un entero del System.in que este comprendido entre primero y ultimo
    private static int leerOpcion(int primero, int ultimo){
        int valor = leerEntero();
        while ( valor <primero || valor > ultimo){
            valor = leerEntero();
        }
        return valor;
    }
      
    
    // Metodos Auxiliares leerFloat y LeerEntero, 
    // Lee de la System.in con el scanner sc y controlan la excepcion de NumberFormatException
    
    
    static private int leerEntero(){
       boolean error = false;
        int  valor =0;
        String cadena;
        do {
        error = false;  
          try {
             // Intento leer directamente un entero  
             cadena = sc.nextLine();
             valor = Integer.parseInt(cadena);
             
            } catch(NumberFormatException e){
              System.out.println("Error en formato.");
              error = true;
            }
        }
       while ( error);
       return valor; 
    }

    static private float leerFloat(){
    	 boolean error = false;
         float valor =0;
         String cadena;
         do {
         error = false;  
           try {
              // Intento leer directamente un entero
              
              cadena = sc.nextLine();
              valor = Float.parseFloat(cadena);
              
             } catch(NumberFormatException e){
               System.out.println("Error en formato.");
               error = true;
             }
         }
        while ( error);
        return valor;
    }
    
    // Muestra los datos de un producto a partir de su codigo
    
    private static void consultar(){        
       System.out.println("<CONSULTA>");
       System.out.print("Introduzca codigo:");
       int codigo = leerEntero();
       Producto p = almacen.buscarProducto(codigo);
       if ( p == null){
           System.out.println("El producto no se encuentra en almacen");
        }
       else {
           System.out.println("PRODUCTO "+p);
        }
       
    }
    
   
    // Borrar un producto a partir de su codigo
    
    private static void borrar(){       
      System.out.println("<ELIMINAR>");
      System.out.println("Aún no disponible");
      // IMPLEMENTAR
    }
    
    // Cambia el precio de un producto a partir de su codigo
    private static void modificarPrecio (){
       System.out.println("<MODIFICAR PRECIO>");
       System.out.println("Aún no disponible");
       // IMPLEMENTAR
    }
    
    
    
    // Incrementa el stock
    private static void comprar(){     
       System.out.println("<COMPRAR>");
       System.out.println("Aún no disponible");
       // IMPLEMENTAR 
    }
    
    // Decrementa el stock
    private static void vender(){
        System.out.println("<VENDER>");
        System.out.println("Aún no disponible");
       // IMPLEMENTAR
       
    }
    
    // Listado de todos los productos
    private static void listar(){        
         System.out.println("<LISTAR>");
         almacen.imprimirProductosTodos();
         
    }
    
    // Listado de todos los productos con stock inferior a stock minimo
    private static void listarPocoStock(){
        System.out.println("<LISTAR STOCK BAJO MINIMOS>");
        List <Producto> lista = almacen.obtenerProductosStockMin();
        for (Producto p : lista) {
			System.out.println(p);
		}
    }
    
    // Solicita datos al usuario para dar de alta un nuevo producto 
    // El codigo no se puede repetir
    private static void crear(){
       System.out.println("<NUEVO PRODUCTO>");
       System.out.println("Aún no disponible");
       // IMPLEMENTAR
    }
       
    /**
     * Inserta objetos Producto iniciales para pruebas
     */
    private static void cargarDatosPrueba() {
        // Producto(codigo, nombre)
        Producto p1 = new Producto(101, "Monitor 24 Pulgadas");
        p1.setStock(15); p1.setStock_min(5); p1.setPrecio(159.99f);
        
        Producto p2 = new Producto(102, "Teclado Mecánico");
        p2.setStock(4); p2.setStock_min(10); p2.setPrecio(45.50f); // Este saldrá en "Stock Mínimo"
        
        Producto p3 = new Producto(103, "Ratón Inalámbrico");
        p3.setStock(20); p3.setStock_min(8); p3.setPrecio(22.00f);
        
        Producto p4 = new Producto(104, "Disco SSD 1TB");
        p4.setStock(2); p4.setStock_min(5); p4.setPrecio(89.90f); // Este también bajo mínimos

        // Insertar en el modelo
        almacen.insertarProducto(p1);
        almacen.insertarProducto(p2);
        almacen.insertarProducto(p3);
        almacen.insertarProducto(p4);
    }
  
}

