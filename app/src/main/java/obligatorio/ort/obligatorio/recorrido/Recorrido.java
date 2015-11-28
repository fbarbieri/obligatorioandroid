package obligatorio.ort.obligatorio.recorrido;

import android.location.Location;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by fede on 25/11/2015.
 */
public class Recorrido {

    private PuntoIntermedio origen;
    private List<PuntoIntermedio> puntos;
    private List<Location> recorrido;
    private String codigoEstacionamiento;
    private Boolean activo;
    private Date fechaInicio;
    private Date fechaFin;

    public PuntoIntermedio getOrigen() {
        return origen;
    }

    public void setOrigen(PuntoIntermedio origen) {
        this.origen = origen;
    }

    public List<PuntoIntermedio> getPuntos() {
        return puntos;
    }

    public void setPuntos(List<PuntoIntermedio> puntos) {
        this.puntos = puntos;
    }

    public List<Location> getRecorrido() {
        return recorrido;
    }

    public void setRecorrido(List<Location> recorrido) {
        this.recorrido = recorrido;
    }

    public String getCodigoEstacionamiento() {
        return codigoEstacionamiento;
    }

    public void setCodigoEstacionamiento(String codigoEstacionamiento) {
        this.codigoEstacionamiento = codigoEstacionamiento;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }
}
