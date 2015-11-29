package obligatorio.ort.obligatorio.recorrido;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

/**
 * Created by fede on 25/11/2015.
 */
public class Recorrido {

    private Long id;
    private PuntoIntermedio origen;
    private List<PuntoIntermedio> puntos;
    private List<LatLng> recorrido;
    private String codigoEstacionamiento;
    private Boolean activo;
    private Timestamp fechaInicio;
    private Timestamp fechaFin;

    public Long getIid() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public List<LatLng> getRecorrido() {
        return recorrido;
    }

    public void setRecorrido(List<LatLng> recorrido) {
        this.recorrido = recorrido;
    }

    public String getCodigoEstacionamiento() {
        return codigoEstacionamiento;
    }

    public void setCodigoEstacionamiento(String codigoEstacionamiento) {
        this.codigoEstacionamiento = codigoEstacionamiento;
    }

    public void setFechaInicio(Timestamp fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Timestamp getFechaFin() {
        return fechaFin;
    }

    public Timestamp getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaFin(Timestamp fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
}
