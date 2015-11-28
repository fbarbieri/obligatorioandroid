package obligatorio.ort.obligatorio.database;

import android.provider.BaseColumns;

/**
 * Created by Emi on 28/11/2015.
 */
public interface ConstantesBaseDatos extends BaseColumns {

    public static final String RECORRIDO_TABLA = "recorrido";
    public static final String DB_EXTENSION = ".db";
    public static final String RECORRIDO_EXTRA_DATA = "extra_data";
    public static final String RECORRIDO_EST_COD = "codigo_estacionamiento";
    public static final String RECORRIDO_ACTIVO = "activo";
    public static final String RECORRIDO_FECHA_INICIO = "fecha_inicio";
    public static final String RECORRIDO_FECHA_FIN = "fecha_fin";


    public static final String CREATE_RECORRIDO =
            "CREATE TABLE " + RECORRIDO_TABLA + " (" +
                    _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    RECORRIDO_EXTRA_DATA + " TEXT," +
                    RECORRIDO_EST_COD + " TEXT," +
                    RECORRIDO_ACTIVO + " INTEGER," +
                    RECORRIDO_FECHA_INICIO + " INTEGER," +
                    RECORRIDO_FECHA_FIN + " TEXT);";

    public static final String DROP_RECORRIDO = "DROP TABLE IF EXIST " + RECORRIDO_TABLA;


}
