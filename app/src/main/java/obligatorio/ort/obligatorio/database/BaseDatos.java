package obligatorio.ort.obligatorio.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static obligatorio.ort.obligatorio.database.ConstantesBaseDatos.*;

/**
 * Created by Emi on 28/11/2015.
 */
public class BaseDatos extends SQLiteOpenHelper {

    public static final int VERSION = 1;

    public BaseDatos(Context context) {
        super(context, RECORRIDO_TABLA.concat(DB_EXTENSION), null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_RECORRIDO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_RECORRIDO);
        onCreate(db);
    }
}
