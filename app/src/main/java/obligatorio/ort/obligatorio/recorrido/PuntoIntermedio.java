package obligatorio.ort.obligatorio.recorrido;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class PuntoIntermedio {

    private LatLng ubicacion;
    private String rutaDeFoto;
    private String titulo;
    private String descripcion;

    public PuntoIntermedio() {
    }

    public PuntoIntermedio(LatLng ubicacion, String rutaDeFoto, String titulo, String descripcion) {
        this.ubicacion = ubicacion;
        this.rutaDeFoto = rutaDeFoto;
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public String getRutaDeFoto() {
        return rutaDeFoto;
    }

    public void setRutaDeFoto(String rutaDeFoto) {
        this.rutaDeFoto = rutaDeFoto;
    }

    public LatLng getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(LatLng ubicacion) {
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

}
