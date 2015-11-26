package obligatorio.ort.obligatorio.recorrido;

import android.location.Location;

import java.util.List;

/**
 * Created by fede on 25/11/2015.
 */
public class Recorrido {

    private Location origen;
    private List<PuntoIntermedio> puntos;
    //una List<Location> con las ubicaciones comunes y la de PuntoIntermedio con los que el usuario elige?
    private String codigoEstacionamiento;

}
