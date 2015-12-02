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
import android.widget.TextView;

public class DetalleEstacionamiento extends DialogFragment {

    private String title;
    private String descripcion;
    private double puntajeActual;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // R.layout.my_layout - that's the layout where your textview is placed
        View view = inflater.inflate(R.layout.detalle_estacionamiento, container, false);
        TextView titulo = (TextView) view.findViewById(R.id.nombreEstacionamiento);
        TextView desc = (TextView) view.findViewById(R.id.descripcionEstacionamiento);
        titulo.setText(title);
        desc.setText(descripcion);
        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.detalle_estacionamiento, null))
                // Add action buttons
                .setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                    }
                });


        return builder.create();
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPuntajeActual(double puntajeActual) {
        this.puntajeActual = puntajeActual;
    }
}