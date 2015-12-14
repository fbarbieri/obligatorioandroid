package obligatorio.ort.obligatorio;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import obligatorio.ort.obligatorio.Servicios.EstacionamientosServices;
import obligatorio.ort.obligatorio.estacionamiento.Estacionamiento;
import obligatorio.ort.obligatorio.estacionamiento.Tarifa;
import obligatorio.ort.obligatorio.facebook.User;
import obligatorio.ort.obligatorio.recorrido.RecorridoHolder;

public class DetalleEstacionamiento extends DialogFragment implements View.OnClickListener{

    private String title = "Estacionamiento";
    private String descripcion = "Descripcion del estacionamiento";
    private double puntajeActual = 0.0d;
    private TextView desc;
    private EditText mComentarioView;
    private TableLayout horarios;
    private Button comentarios;

    static DetalleEstacionamiento newInstance(String title, String descripcion, double puntajeActual) {
        DetalleEstacionamiento f = new DetalleEstacionamiento();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putString("descripcion", descripcion);
        args.putString("title", title);
        args.putDouble("puntaje", puntajeActual);
        f.setArguments(args);

        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.detalle_estacionamiento, null);
        TextView titulo = (TextView) view.findViewById(R.id.nombreEstacionamiento);
        desc = (TextView) view.findViewById(R.id.descripcionEstacionamiento);
        mComentarioView = (EditText) view.findViewById(R.id.comentario);
        horarios = (TableLayout) view.findViewById(R.id.horarios);
        comentarios = (Button) view.findViewById(R.id.comentarios);
        title = getArguments().getString("title");
        if (getArguments().getString("descripcion") != null) {
            descripcion = getArguments().getString("descripcion");
        }

        RatingBar puntaje = (RatingBar) view.findViewById(R.id.ratingBar);
        puntaje.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(fromUser) {
                    checkFacebookSession();
                    puntajeActual = rating;
                    mComentarioView.setVisibility(View.VISIBLE);
                    desc.setVisibility(View.GONE);
                    horarios.setVisibility(View.GONE);
                    comentarios.setVisibility(View.GONE);
                }
            }
        });
        puntajeActual = getArguments().getDouble("puntaje");
        puntaje.setRating((float) puntajeActual);
        titulo.setText(title);
        desc.setText(descripcion);

        comentarios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent verComentarios = new Intent(getContext(), IngresarPuntoIntermedioActivity.class);
                startActivity(verComentarios);
            }
        });

        for(Estacionamiento est : RecorridoHolder.getInstance().getEstacionamientos()){
            if(est.getNombre().equals(title)){
                for(Tarifa tarifa : est.getTarifas()){

                    TableRow newRow = new TableRow(getContext());
                    TextView vehiculo = new TextView(getContext());
                    TextView tarifaHora = new TextView(getContext());
                    TextView tarifaDia = new TextView(getContext());

                    TableRow.LayoutParams llp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                    llp.setMargins(12, 0, 0, 0); // llp.setMargins(left, top, right, bottom);

                    vehiculo.setPadding(3, 3, 3, 3);
                    vehiculo.setLayoutParams(llp);
                    tarifaHora.setPadding(3, 3, 3, 3);
                    tarifaHora.setLayoutParams(llp);
                    tarifaDia.setPadding(3, 3, 3,3);
                    tarifaDia.setLayoutParams(llp);

                    vehiculo.setText(tarifa.getDescripcion());
                    tarifaHora.setText("$"+tarifa.getPrecioHora());
                    tarifaDia.setText("$"+tarifa.getPrecioDia());

                    newRow.addView(vehiculo, 0);
                    newRow.addView(tarifaHora, 1);
                    newRow.addView(tarifaDia, 2);

                    horarios.addView(newRow);
                }
                break;
            }
        }


        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if(mComentarioView.getVisibility() == View.VISIBLE)
                            EstacionamientosServices.calificar(title,puntajeActual,mComentarioView.getText().toString(),User.getCurrentUser().getName());
                    }
                });

        return builder.create();
    }

    protected void checkFacebookSession() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        if (accessToken != null && !accessToken.isExpired()) {
            GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
                @Override
                public void onCompleted(JSONObject userJson, GraphResponse graphResponse) {
                    User.facebookUser(userJson);
                }
            });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,link,gender,birthday,email");
            request.setParameters(parameters);
            request.executeAsync();
        } else {
            List<String> permissions = new ArrayList<String>();
            permissions.add("public_profile");
            permissions.add("email");
            LoginManager.getInstance().logInWithReadPermissions(getActivity(), permissions);
        }
    }

    @Override
    public void onClick(View v) {

    }
}