package obligatorio.ort.obligatorio.recorrido;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;

import obligatorio.ort.obligatorio.database.BaseDatos;

import static obligatorio.ort.obligatorio.database.ConstantesBaseDatos.*;

/**
 * Created by Emi on 28/11/2015.
 */
public class RecorridoManager {

    private BaseDatos recorridoBD;
    private SQLiteDatabase db;
    private static String[] FROM = {_ID,RECORRIDO_EXTRA_DATA,RECORRIDO_ACTIVO,RECORRIDO_EST_COD,RECORRIDO_FECHA_INICIO,RECORRIDO_FECHA_FIN};
    private static String ORDER_BY = RECORRIDO_FECHA_INICIO + " DESC";

    public RecorridoManager (Context context){
        recorridoBD = new BaseDatos(context);
        db = recorridoBD.getReadableDatabase();
    }

    public void AgregarRecorrido(Recorrido recorrido){
        ContentValues values = new ContentValues();
        values.put(RECORRIDO_FECHA_INICIO,recorrido.getFechaInicio().getTime());
        if(recorrido.getFechaFin()!=null)
            values.put(RECORRIDO_FECHA_FIN,recorrido.getFechaFin().getTime());
        values.put(RECORRIDO_EST_COD,recorrido.getCodigoEstacionamiento());
        values.put(RECORRIDO_ACTIVO,recorrido.getActivo());

        for(int i = 0; i<5000; i++){
            recorrido.getPuntos().add(recorrido.getOrigen());
            recorrido.getRecorrido().add(recorrido.getOrigen().getUbicacion());
        }

        String extraData = new Gson().toJson(recorrido);
        values.put(RECORRIDO_EXTRA_DATA,extraData);
        db.insertOrThrow(RECORRIDO_TABLA,null,values);
    }

    public Recorrido obtenerUltimoRecorrido(Boolean activo){
        Recorrido recorrido = null;
        int flag = (activo)? 1 : 0;
        String[] args = {flag+""};
        Cursor cursor = db.query(RECORRIDO_TABLA, FROM, "activo = ?", args, null, null, ORDER_BY);
        if(cursor.moveToNext()){
            String extraData = cursor.getString(0);
            recorrido = new Gson().fromJson(cursor.getString(1),Recorrido.class);
        }
        return recorrido;
    }

    public void limpiarBase(){
        db.delete(RECORRIDO_TABLA,null,null);
    }

}
