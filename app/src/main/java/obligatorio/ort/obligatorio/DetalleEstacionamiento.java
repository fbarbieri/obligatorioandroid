package obligatorio.ort.obligatorio;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

public class DetalleEstacionamiento extends DialogFragment {

    private String title = "Estacionamiento";
    private String descripcion = "Descripcion del estacionamiento";
    private double puntajeActual = 0.0d;
    private TextView desc;
    private EditText mComentarioView;

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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.detalle_estacionamiento, null);
        TextView titulo = (TextView) view.findViewById(R.id.nombreEstacionamiento);
        desc = (TextView) view.findViewById(R.id.descripcionEstacionamiento);
        mComentarioView = (EditText) view.findViewById(R.id.comentario);
        title = getArguments().getString("title");
        if (getArguments().getString("descripcion") != null) {
            descripcion = getArguments().getString("descripcion");
        }

        RatingBar puntaje = (RatingBar) view.findViewById(R.id.ratingBar);
        puntaje.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(fromUser) {
                    puntajeActual = rating;
                    mComentarioView.setVisibility(View.VISIBLE);
                    desc.setVisibility(View.GONE);
                }
            }
        });
        puntajeActual = getArguments().getDouble("puntaje");
        puntaje.setRating((float)puntajeActual);
        titulo.setText(title);
        desc.setText(descripcion);

        builder.setView(view)
                // Add action buttons
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                    }
                });
        return builder.create();
    }

}