package obligatorio.ort.obligatorio.recorrido;

public class RecorridoHolder {

    private Recorrido recorrido;
    private static RecorridoHolder instance;

    public Recorrido getRecorrido() {
        return recorrido;
    }

    public void setRecorrido(Recorrido recorrido) {
        this.recorrido = recorrido;
    }

    public static RecorridoHolder getInstance() {

        if (instance == null) {
            instance = new RecorridoHolder();
        }
        return instance;
    }

    private RecorridoHolder(){
        ;
    }

}
