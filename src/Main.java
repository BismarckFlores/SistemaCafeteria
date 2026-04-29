import modelos.Cliente;
import modelos.Pedido;
import modelos.Producto;
import servicios.Cajero;
import servicios.Cocina;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<Cliente> clientes = new ArrayList<>();
        List<Pedido> pedidos = new ArrayList<>();

        List<Producto> menu = new ArrayList<>(List.of(
                new Producto("Café Americano", 35.00),
                new Producto("Latte Vainilla", 55.00),
                new Producto("Croissant", 40.00),
                new Producto("Sándwich de Pavo", 75.00)
        ));

        Cajero cajero = new Cajero("Marta");
        Cocina cocina = new Cocina();

        // Clientes de prueba por defecto
        clientes.add(new Cliente("Roberto"));
        clientes.add(new Cliente("Ana"));

        boolean continuar = true;
        String[] opcionesMenu = {
                "1. Registrar Cliente",
                "2. Ver Clientes",
                "3. Hacer un Pedido",
                "4. Procesar Pedidos en Cocina",
                "5. Ver/Filtrar Pedidos",
                "6. Salir"
        };

        JOptionPane.showMessageDialog(null, "¡Bienvenido al Sistema de la Cafetería Universitaria!", "Inicio", JOptionPane.INFORMATION_MESSAGE);

        while (continuar) {
            String opcionSeleccionada = (String) JOptionPane.showInputDialog(
                    null, "Seleccione una opción:", "Menú Principal",
                    JOptionPane.QUESTION_MESSAGE, null, opcionesMenu, opcionesMenu[0]
            );

            if (opcionSeleccionada == null || opcionSeleccionada.equals("6. Salir")) {
                continuar = false;
                JOptionPane.showMessageDialog(null, "Cerrando el sistema. ¡Hasta pronto!");
                break;
            }

            switch (opcionSeleccionada) {
                case "1. Registrar Cliente":
                    registrarCliente(clientes);
                    break;
                case "2. Ver Clientes":
                    verClientes(clientes);
                    break;
                case "3. Hacer un Pedido":
                    hacerPedido(clientes, menu, pedidos, cajero);
                    break;
                case "4. Procesar Pedidos en Cocina":
                    procesarEnCocina(pedidos, cajero, cocina);
                    break;
                case "5. Ver/Filtrar Pedidos":
                    filtrarPedidos(pedidos);
                    break;
            }
        }
    }

    public static void registrarCliente(List<Cliente> clientes) {
        String nombre = JOptionPane.showInputDialog("Ingrese el nombre del nuevo cliente:");
        if (nombre != null && !nombre.trim().isEmpty()) {
            clientes.add(new Cliente(nombre));
            JOptionPane.showMessageDialog(null, "Cliente '" + nombre + "' registrado con éxito.");
        } else {
            JOptionPane.showMessageDialog(null, "Registro cancelado o nombre inválido.", "Cancelado", JOptionPane.WARNING_MESSAGE);
        }
    }

    private static void verClientes(List<Cliente> clientes) {
        if (clientes.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay clientes registrados.");
            return;
        }
        StringBuilder lista = new StringBuilder("Lista de Clientes:\n");
        for (int i = 0; i < clientes.size(); i++) {
            lista.append((i + 1)).append(". ").append(clientes.get(i).getNombre()).append("\n");
        }
        JOptionPane.showMessageDialog(null, lista.toString(), "Clientes", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void hacerPedido(List<Cliente> clientes, List<Producto> menu, List<Pedido> pedidos, Cajero cajero) {
        if (clientes.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Debe registrar un cliente primero.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 1. SELECCIONAR CLIENTE
        String[] opcionesClientes = new String[clientes.size()];
        for (int i = 0; i < clientes.size(); i++) {
            opcionesClientes[i] = clientes.get(i).getNombre();
        }

        String clienteSeleccionadoStr = (String) JOptionPane.showInputDialog(
                null, "¿Qué cliente está haciendo el pedido?", "Seleccionar Cliente",
                JOptionPane.QUESTION_MESSAGE, null, opcionesClientes, opcionesClientes[0]
        );

        if (clienteSeleccionadoStr == null) return;

        Cliente clienteElegido = null;
        for (Cliente c : clientes) {
            if (c.getNombre().equals(clienteSeleccionadoStr)) {
                clienteElegido = c;
                break;
            }
        }

        Pedido nuevoPedido = cajero.registrarPedido(clienteElegido);

        boolean agregando = true;
        boolean pedidoCancelado = false;

        while (agregando) {
            String[] opcionesProductos = new String[menu.size() + 1];
            for (int i = 0; i < menu.size(); i++) {
                opcionesProductos[i] = menu.get(i).getNombre() + " ($" + menu.get(i).getPrecio() + ")";
            }
            opcionesProductos[menu.size()] = "Terminar Pedido";

            String seleccion = (String) JOptionPane.showInputDialog(
                    null, "Seleccione un producto para agregar:", "Agregando al Pedido de " + clienteElegido.getNombre(),
                    JOptionPane.QUESTION_MESSAGE, null, opcionesProductos, opcionesProductos[0]
            );

            if (seleccion == null) {
                pedidoCancelado = true;
                agregando = false;
            } else if (seleccion.equals("Terminar Pedido")) {
                agregando = false;
            } else {
                for (Producto p : menu) {
                    if (seleccion.startsWith(p.getNombre())) {
                        nuevoPedido.agregarProducto(p);
                        JOptionPane.showMessageDialog(null, p.getNombre() + " agregado al pedido.");
                        break;
                    }
                }
            }
        }

        if (pedidoCancelado) {
            JOptionPane.showMessageDialog(null, "Creación de pedido cancelada. No se ha guardado nada.", "Cancelado", JOptionPane.WARNING_MESSAGE);
        } else if (nuevoPedido.toString().contains("-")) {
            pedidos.add(nuevoPedido);
            JOptionPane.showMessageDialog(null, "Pedido registrado exitosamente:\n\n" + nuevoPedido.toString() + "\n\nEstado: " + nuevoPedido.getEstado());
        } else {
            JOptionPane.showMessageDialog(null, "Pedido finalizado sin productos. No se guardará.", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private static void procesarEnCocina(List<Pedido> pedidos, Cajero cajero, Cocina cocina) {
        List<Pedido> pedidosPendientes = new ArrayList<>();
        for (Pedido p : pedidos) {
            if (p.getEstado().equals("Registrado en Caja")) {
                pedidosPendientes.add(p);
            }
        }

        if (pedidosPendientes.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No hay pedidos pendientes para enviar a cocina.");
            return;
        }
        String[] opcionesPedidos = new String[pedidosPendientes.size()];
        for (int i = 0; i < pedidosPendientes.size(); i++) {
            Pedido p = pedidosPendientes.get(i);
            opcionesPedidos[i] = "Pedido de: " + p.getCliente().getNombre() + " | Índice: " + pedidos.indexOf(p);
        }

        String pedidoSeleccionadoStr = (String) JOptionPane.showInputDialog(
                null, "Seleccione el pedido que la cocina va a procesar:", "Cocina - Pedidos Pendientes",
                JOptionPane.QUESTION_MESSAGE, null, opcionesPedidos, opcionesPedidos[0]
        );

        if (pedidoSeleccionadoStr == null) return;

        int idReal = Integer.parseInt(pedidoSeleccionadoStr.split("Índice: ")[1]);
        Pedido pedidoAProcesar = pedidos.get(idReal);

        String logCocina = cajero.enviarACocina(pedidoAProcesar, cocina);

        JOptionPane.showMessageDialog(null, "Procesando pedido de " + pedidoAProcesar.getCliente().getNombre() + " en cocina:\n\n" + logCocina, "Cocina", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void filtrarPedidos(List<Pedido> pedidos) {
        if (pedidos.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Aún no hay pedidos en el sistema.");
            return;
        }

        String[] filtros = {"Todos", "Registrados en Caja", "Enviado a Cocina", "Listo"};
        String filtroSeleccionado = (String) JOptionPane.showInputDialog(
                null, "Seleccione qué pedidos desea ver:", "Filtro de Pedidos",
                JOptionPane.QUESTION_MESSAGE, null, filtros, filtros[0]
        );

        if (filtroSeleccionado == null) return;

        StringBuilder resultado = new StringBuilder("--- Pedidos (" + filtroSeleccionado + ") ---\n\n");
        int contador = 0;

        for (int i = 0; i < pedidos.size(); i++) {
            Pedido p = pedidos.get(i);
            boolean mostrar = false;

            if (filtroSeleccionado.equals("Todos")) mostrar = true;
            else if (filtroSeleccionado.equals("Registrados en Caja") && p.getEstado().equals("Registrado en Caja")) mostrar = true;
            else if (filtroSeleccionado.equals("Enviado a Cocina") && p.getEstado().equals("Enviado a Cocina")) mostrar = true;
            else if (filtroSeleccionado.equals("Listo") && p.getEstado().equals("Listo")) mostrar = true;

            if (mostrar) {
                resultado.append("Pedido #").append(i + 1).append(" | Cliente: ").append(p.getCliente() != null ? p.getCliente().getNombre() : "N/A").append(" | Estado: ").append(p.getEstado()).append("\n");
                resultado.append(p).append("\n--------------------------\n");
                contador++;
            }
        }

        if (contador == 0) {
            resultado.append("No se encontraron pedidos con este filtro.");
        }

        JTextArea textArea = new JTextArea(resultado.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        JOptionPane.showMessageDialog(null, scrollPane, "Visor de Pedidos", JOptionPane.INFORMATION_MESSAGE);
    }
}