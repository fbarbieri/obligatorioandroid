package obligatorio.ort.obligatorio.estacionamiento;

public class Tarifa {

    private String descripcion;
    private double precioHora;
    private double precioDia;

    public String getDescripcion() {
        return descripcion;
    }

    public double getPrecioHora() {
        return precioHora;
    }

    public void setPrecioHora(double precioHora) {
        this.precioHora = precioHora;
    }

    public double getPrecioDia() {
        return precioDia;
    }

    public void setPrecioDia(double precioDia) {
        this.precioDia = precioDia;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

}
