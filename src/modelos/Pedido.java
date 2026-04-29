package modelos;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private final List<Producto> productos;
    private String estado;

    public Pedido() {
        this.productos = new ArrayList<>();
        this.estado = "Creado";
    }

    public void agregarProducto(Producto producto) { this.productos.add(producto); }

    public void setEstado(String estado) { this.estado = estado; }
    public String getEstado() { return estado; }

    @Override
    public String toString() {
        StringBuilder resumen = new StringBuilder();
        double total = 0;
        for (Producto p : productos) {
            resumen.append("- ").append(p.getNombre()).append(" ($").append(p.getPrecio()).append(")\n");
            total += p.getPrecio();
        }
        resumen.append("Total a pagar: $").append(total);
        return resumen.toString();
    }
}