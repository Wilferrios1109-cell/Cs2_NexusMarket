package Apliccation;

import java.util.List;

public class ShoppingCar {
    private Buyer comprador;
    private List<Producto> productosSeleccionados;

    public ShoppingCar() {}

    public Buyer getComprador() { return comprador; }
    public void setComprador(Buyer comprador) { this.comprador = comprador; }

    public List<Producto> getProductosSeleccionados() { return productosSeleccionados; }
    public void setProductosSeleccionados(List<Producto> productosSeleccionados) { this.productosSeleccionados = productosSeleccionados; }
}
