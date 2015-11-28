package obligatorio.ort.obligatorio.recorrido;

import android.location.Location;

import com.google.android.gms.maps.model.Marker;

public class PuntoIntermedio {

    private Location ubicacion;
    private String rutaDeFoto;
    private String titulo;
    private String descripcion;
    private Marker marker;

    public PuntoIntermedio() {
    }

    public String getRutaDeFoto() {
        return rutaDeFoto;
    }

    public void setRutaDeFoto(String rutaDeFoto) {
        this.rutaDeFoto = rutaDeFoto;
    }

    public Location getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Location ubicacion) {
        this.ubicacion = ubicacion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }
}
