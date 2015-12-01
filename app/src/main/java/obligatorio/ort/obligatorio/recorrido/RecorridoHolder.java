package obligatorio.ort.obligatorio.recorrido;

import java.util.ArrayList;
import java.util.List;

import obligatorio.ort.obligatorio.estacionamiento.Estacionamiento;

public class RecorridoHolder {

    private Recorrido recorrido;
    private static RecorridoHolder instance;
    private List<Estacionamiento> estacionamientos;

    public Recorrido getRecorrido() {
        return recorrido;
    }

    public void setRecorrido(Recorrido recorrido) {
        this.recorrido = recorrido;
    }

    public static synchronized RecorridoHolder getInstance() {

        if (instance == null) {
            instance = new RecorridoHolder();
        }
        return instance;
    }

    private RecorridoHolder(){
        estacionamientos = new ArrayList<>();
    }

    public void setEstacionamientos(List<Estacionamiento> estacionamientos) {
        this.estacionamientos = estacionamientos;
    }

    public List<Estacionamiento> getEstacionamientos() {
        return estacionamientos;
    }
}
