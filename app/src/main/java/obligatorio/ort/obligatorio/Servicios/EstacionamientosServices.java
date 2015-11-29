package obligatorio.ort.obligatorio.Servicios;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import obligatorio.ort.obligatorio.estacionamiento.Estacionamiento;

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

    public static void pruebaEstacionamiento(){
        RequestParams params = new RequestParams();
        client.get("http://appenlanube-barbieri-gamboa.appspot.com/servicios/prueba", new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Estacionamiento est = new Gson().fromJson(response.toString(),Estacionamiento.class);
                System.out.println("Prueba response");
                System.out.println("statusCode: " + statusCode);
                System.out.println("response: " +response);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                System.out.println("Prueba response");
                System.out.println("statusCode: " + statusCode);
                System.out.println("response: " +response);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println("Prueba error response");
                System.out.println("Error Code: " + statusCode);
                System.out.println("Error Desc: " +responseString);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                System.out.println("Prueba error response");
                System.out.println("Error Code: " + statusCode);
                System.out.println("Error Desc: " +errorResponse);
            }
        });
    }

}
