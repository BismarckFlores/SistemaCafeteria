package servicios;

import modelos.Pedido;

public class Cocina {
    public String procesarPedido(Pedido pedido, Cajero cajero) {
        StringBuilder log = new StringBuilder();

        log.append("Cocina: Recibiendo comanda...\n");

        pedido.setEstado("En preparación");
        log.append("Cocina: Estado de la orden -> ").append(pedido.getEstado()).append("\n");

        // Simulación de elaboración
        pedido.setEstado("Listo");
        log.append("Cocina: Estado de la orden -> ").append(pedido.getEstado()).append("\n");

        log.append("Cocina: Preparación terminada. Enviando mensaje al cajero...\n");

        // Paso de mensajes de regreso: Cocina avisa al cajero
        String respuestaCajero = cajero.recibirNotificacionCocina(pedido);
        log.append(respuestaCajero);

        return log.toString();
    }
}
