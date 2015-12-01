package obligatorio.ort.obligatorio.Servicios;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import obligatorio.ort.obligatorio.estacionamiento.Estacionamiento;
import obligatorio.ort.obligatorio.recorrido.RecorridoHolder;

/**
 * Created by Emi on 29/11/2015.
 */
public class EstacionamientosServices {

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void iniciarRecorrido(String codigo){
        RequestParams params = new RequestParams();
        params.put("codigo",codigo);
        client.post("http://appenlanube-barbieri-gamboa.appspot.com/servicios/inicio",params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println("inicio response");
                System.out.println("statusCode: " + statusCode + " response: " +response);
                System.out.println("response: " +response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                System.out.println("inicio response");
                System.out.println("statusCode: " + statusCode);
                System.out.println("response: " +response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println("inicio error response");
                System.out.println("Error Code: " + statusCode);
                System.out.println("response: " +responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                System.out.println("inicio error response");
                System.out.println("Error Code: " + statusCode);
                System.out.println("Error Desc: " +errorResponse);
            }
        });
    }

    public static void obtenerAvisos(String codigo, Integer minutos){
        RequestParams params = new RequestParams();
        params.put("codigo",codigo);
        params.put("minutos",minutos);
        client.post("http://appenlanube-barbieri-gamboa.appspot.com/servicios/aviso",params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                System.out.println("aviso response");
                System.out.println("statusCode: " + statusCode);
                System.out.println("response: " +response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                System.out.println("aviso response");
                System.out.println("statusCode: " + statusCode);
                System.out.println("response: " +response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println("aviso error response");
                System.out.println("Error Code: " + statusCode);
                System.out.println("Error Desc: " +responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                System.out.println("aviso error response");
                System.out.println("Error Code: " + statusCode);
                System.out.println("Error Desc: " +errorResponse);
            }
        });
    }

    public static void pruebaEstacionamiento(final GoogleMap googleMap, final BitmapDescriptor icon){
        RequestParams params = new RequestParams();
        client.get("http://appenlanube-barbieri-gamboa.appspot.com/servicios/estacionamientos", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Estacionamiento est = new Gson().fromJson(response.toString(), Estacionamiento.class);
                RecorridoHolder.getInstance().getEstacionamientos().add(est);
                System.out.println("Prueba response");
                System.out.println("statusCode: " + statusCode);
                System.out.println("response: " + response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                for(int i=0; i < response.length() ; i++) {
                    JSONObject json_data = null;
                    try {
                        json_data = response.getJSONObject(i);

                        Estacionamiento est = new Gson().fromJson(json_data.toString(), Estacionamiento.class);
                        LatLng pos = new LatLng(est.getLatitud(),est.getLongitud());
                        googleMap.addMarker(new MarkerOptions()
                                .position(pos)
                                .title(est.getNombre())
                                .icon(icon));
                        RecorridoHolder.getInstance().getEstacionamientos().add(est);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("Prueba response");
                System.out.println("statusCode: " + statusCode);
                System.out.println("response: " + response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println("Prueba error response");
                System.out.println("Error Code: " + statusCode);
                System.out.println("Error Desc: " + responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                System.out.println("Prueba error response");
                System.out.println("Error Code: " + statusCode);
                System.out.println("Error Desc: " + errorResponse);
            }
        });

    }

}
