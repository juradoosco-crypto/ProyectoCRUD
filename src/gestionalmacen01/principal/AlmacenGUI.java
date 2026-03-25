/**
 *  MiAlmacen
 *  Programa principal de gestión de un almacén de productos
 *  implementa un CRUD ( Creat, Read, Update, Delete) ofreciendo varios
 *  informes.
 *  Versión gráfica sencilla.
 */
package gestionalmacen01.principal;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import gestionalmacen01.modelo.*;

/**
 * AlmacenGUI - Versión gráfica del CRUD de productos.
 * Utiliza Swing para la interfaz y se apoya en la interfaz ModeloAbs.
 */
public class AlmacenGUI extends JFrame {
    private ModeloAbs almacen;
    private JTable tablaProductos;
    private DefaultTableModel modeloTabla;

    public AlmacenGUI() {
        // Inicialización del modelo (usando ArrayList por defecto)
    
        almacen = new ModeloHashMap();
        
        // Inicializo el almacen con unos datos de prueba
        cargarDatosPrueba();
        
        // Configuración básica de la ventana
        setTitle("Sistema de Gestión de Almacén - CRUD");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // --- Componentes de la Interfaz ---
        configurarTabla();
        configurarPanelBotones();
        
        // Carga inicial de datos
        actualizarTabla(false);
    }

    private void configurarTabla() {
        String[] columnas = {"Código", "Nombre", "Stock", "Stock Mínimo", "Precio (€)"};
        modeloTabla = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; } // Tabla de solo lectura
        };
        
        tablaProductos = new JTable(modeloTabla);
        tablaProductos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        add(new JScrollPane(tablaProductos), BorderLayout.CENTER);
    }

    
    private void configurarPanelBotones() {
        JPanel panelBotones = new JPanel(new GridLayout(0, 1, 5, 10));
        panelBotones.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Botones de acción
        JButton btnCrear = new JButton("Nuevo Producto");
        JButton btnBorrar = new JButton("Eliminar Producto");
        JButton btnModificar = new JButton("Modificar Precio");
        JButton btnComprar = new JButton("Registrar Compra");
        JButton btnVender = new JButton("Registrar Venta");
        JButton btnListarTodo = new JButton("Ver Todos");
        JButton btnStockBajo = new JButton("Ver Stock Mínimo");

        // Estética
        btnStockBajo.setForeground(Color.RED.darker());

        // Añadir al panel
        panelBotones.add(btnCrear);
        panelBotones.add(btnModificar);
        panelBotones.add(new JSeparator());
        panelBotones.add(btnComprar);
        panelBotones.add(btnVender);
        panelBotones.add(new JSeparator());
        panelBotones.add(btnBorrar);
        panelBotones.add(new JSeparator());
        panelBotones.add(btnListarTodo);
        panelBotones.add(btnStockBajo);

        add(panelBotones, BorderLayout.EAST);

        // --- Listeners (Eventos) ---
        btnCrear.addActionListener(e -> crearProducto());
        btnBorrar.addActionListener(e -> borrar());
        btnModificar.addActionListener(e -> modificarPrecio());
        btnComprar.addActionListener(e -> transaccionStock(true));
        btnVender.addActionListener(e -> transaccionStock(false));
        btnListarTodo.addActionListener(e -> actualizarTabla(false));
        btnStockBajo.addActionListener(e -> actualizarTabla(true));
    }

    // --- Lógica del CRUD conectada a la Interfaz ---

    private void actualizarTabla(boolean soloStockBajo) {
        modeloTabla.setRowCount(0); // Limpiar tabla actual
        
        // Uso del método obtener todos o solo con stock menor o igual a mínimo
        
        List<Producto> lista = soloStockBajo ? almacen.obtenerProductosStockMin() : almacen.obtenerProductos();
        
        // Añado los productos a la tabla de visualización
        for (Producto p : lista) {
            Object[] fila = {
                p.getCodigo(),
                p.getNombre(),
                p.getStock(),
                p.getStock_min(),
                String.format("%.2f", p.getPrecio())
            };
            modeloTabla.addRow(fila);
        }
    }

    private void crearProducto() {
        try {
            int cod = Integer.parseInt(JOptionPane.showInputDialog(this, "Código del nuevo producto:"));
            if (almacen.buscarProducto(cod) != null) {
                JOptionPane.showMessageDialog(this, "Error: El código ya existe.");
                return;
            }
            
            // NO SE VALIDAN LOS DATOS
            String nom = JOptionPane.showInputDialog(this, "Nombre del producto:");
            int stock = Integer.parseInt(JOptionPane.showInputDialog(this, "Stock inicial:"));
            int min = Integer.parseInt(JOptionPane.showInputDialog(this, "Stock mínimo recomendado:"));
            float precio = Float.parseFloat(JOptionPane.showInputDialog(this, "Precio de venta:"));

            Producto p = new Producto(cod, nom);
            p.setStock(stock);
            p.setStock_min(min);
            p.setPrecio(precio);

            if (almacen.insertarProducto(p)) {
                actualizarTabla(false);
                JOptionPane.showMessageDialog(this, "Producto guardado con éxito.");
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Error: Ingrese valores numéricos válidos.");
        }
    }

    private void borrar() {
        int fila = tablaProductos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto de la tabla para borrar.");
            return;
        }

        int codigo = (int) modeloTabla.getValueAt(fila, 0);
        int confirmar = JOptionPane.showConfirmDialog(this, "¿Seguro que desea eliminar el código " + codigo + "?");
        
        if (confirmar == JOptionPane.YES_OPTION) {
            if (almacen.borrarProducto(codigo)) {
                actualizarTabla(false);
                JOptionPane.showMessageDialog(this, "Producto eliminado.");
            }
        }
    }

    private void modificarPrecio() {
        int fila = tablaProductos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto para modificar.");
            return;
        }

        int codigo = (int) modeloTabla.getValueAt(fila, 0);
        Producto p = almacen.buscarProducto(codigo);
        
        try {
            float nuevoPrecio = Float.parseFloat(JOptionPane.showInputDialog(this, "Nuevo precio para " + p.getNombre() + ":", p.getPrecio()));
            if (nuevoPrecio > 0) {
                p.setPrecio(nuevoPrecio);
                almacen.modificarProducto(p);
                actualizarTabla(false);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Entrada no válida.");
        }
    }

    private void transaccionStock(boolean esCompra) {
        int fila = tablaProductos.getSelectedRow();
        if (fila == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto en la tabla.");
            return;
        }

        int codigo = (int) modeloTabla.getValueAt(fila, 0);
        Producto p = almacen.buscarProducto(codigo);
        
        try {
            String titulo = esCompra ? "Entrada de Stock" : "Venta de Producto";
            int cantidad = Integer.parseInt(JOptionPane.showInputDialog(this, "Cantidad:", titulo, JOptionPane.QUESTION_MESSAGE));
            
            if ( cantidad <= 0) {
            	 JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor que 0");
                 return;
            }
            
            if (esCompra) {
                p.setStock(p.getStock() + cantidad);
            } else {
                if (cantidad > p.getStock()) {
                    JOptionPane.showMessageDialog(this, "No hay suficiente stock para la venta.");
                    return;
                }
                p.setStock(p.getStock() - cantidad);
            }
            
            almacen.modificarProducto(p);
            actualizarTabla(false);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error en la operación.");
        }
    }

    public static void main(String[] args) {
        // Ejecución en el hilo de despacho de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            new AlmacenGUI().setVisible(true);
        });
    }


    /**
     * Inserta objetos Producto iniciales para pruebas
     */
    private void cargarDatosPrueba() {
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