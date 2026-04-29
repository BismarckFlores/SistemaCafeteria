package servicios;

import modelos.Cliente;
import modelos.Pedido;

public class Cajero {
    private String nombre;

    public Pedido recibirPedido() { return new Pedido(); }

    public Pedido registrarPedido() {
        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setEstado("Registrado en Caja");
        return nuevoPedido;
    }

    public String enviarACocina(Pedido pedido, Cocina cocina) {
        pedido.setEstado("Enviado a Cocina");
        return cocina.procesarPedido(pedido, this);
    }

    public String recibirNotificacionCocina(Pedido pedido) {
        return "\n[Mensaje interno] Cajero " + this.nombre + " confirma que el pedido está: " + pedido.getEstado() + ".";
    }

    public String notificarCliente(Cliente cliente) {
        return "Cajero " + this.nombre + ": ¡Estimado/a " + cliente.getNombre() + ", su orden está lista en el mostrador!";
    }
}