package obligatorio.ort.obligatorio.estacionamiento;

import java.util.List;

public class Estacionamiento {

    private String nombre;
    private List<Calificacion> calificaciones;
    private List<Parcela> parcelas;
    private double puntaje = -1;
    private int capacidad;
    private String horaDeApertura;
    private String horaDeCierre;

    public int getCapacidad() {
        return capacidad;
    }

    public void setCapacidad(int capacidad) {
        this.capacidad = capacidad;
    }

    public String getHoraDeApertura() {
        return horaDeApertura;
    }

    public void setHoraDeApertura(String horaDeApertura) {
        this.horaDeApertura = horaDeApertura;
    }

    public String getHoraDeCierre() {
        return horaDeCierre;
    }

    public void setHoraDeCierre(String horaDeCierre) {
        this.horaDeCierre = horaDeCierre;
    }
    //ubicación?

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Calificacion> getCalificaciones() {
        return calificaciones;
    }

    public void setCalificaciones(List<Calificacion> calificaciones) {
        this.calificaciones = calificaciones;
    }

    public List<Parcela> getParcelas() {
        return parcelas;
    }

    public void setParcelas(List<Parcela> parcelas) {
        this.parcelas = parcelas;
    }

    public double getPuntaje() {
        return puntaje;
    }

    public void setPuntaje(double puntaje) {
        this.puntaje = puntaje;
    }
}
