package modelos;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private List<Producto> productos;
    private String estado;

    public Pedido() {
        this.productos = new ArrayList<>();
        this.estado = "Creado";
    }

    public void agregarProducto(Producto producto) {
        this.productos.add(producto);
    }

    private void eliminarProducto(Producto producto) {
        this.productos.remove(producto);
    }

    private void actualizarEstado(String nuevoEstado) {
        this.estado = nuevoEstado;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public String getEstado() {
        return estado;
    }
}