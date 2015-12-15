package obligatorio.ort.obligatorio;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.w3c.dom.Text;

import obligatorio.ort.obligatorio.estacionamiento.Calificacion;
import obligatorio.ort.obligatorio.estacionamiento.Estacionamiento;
import obligatorio.ort.obligatorio.recorrido.RecorridoHolder;

public class VerComentariosActivity extends AppCompatActivity {

    public static final String ARG_ID_ESTACIONAMIENTO = "id_estacionamiento";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_comentarios);

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar_comentario);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) this.findViewById(R.id.toolbar_layout_comentario);
        if (appBarLayout != null) {
            appBarLayout.setTitle(getIntent().getStringExtra(ARG_ID_ESTACIONAMIENTO));
        }

        Estacionamiento estacionamiento = getEstacionamiento(getIntent().getStringExtra(ARG_ID_ESTACIONAMIENTO));
        TableLayout table = (TableLayout) findViewById(R.id.table_comentarios);

        TableRow.LayoutParams rowParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        TableRow.LayoutParams textViewParams = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);

        for (Calificacion calificacion : estacionamiento.getCalificaciones()) {
            TableRow row = new TableRow(this);
            row.setLayoutParams(rowParams);
            table.addView(row);

            TextView usuario = new TextView(this);
            usuario.setLayoutParams(textViewParams);
            usuario.setText(calificacion.getUsuario() + ", " + calificacion.getCalificacion());
            usuario.setPadding(15, 15, 15, 3);
            usuario.setTextSize(17f);
            row.addView(usuario);

            TableRow rowComment = new TableRow(this);
            rowComment.setLayoutParams(rowParams);
            table.addView(rowComment);

            TextView comentario = new TextView(this);
            comentario.setText(calificacion.getComentario());
            comentario.setPadding(15, 3, 15, 15);
            comentario.setLayoutParams(textViewParams);
            comentario.setTextSize(15f);
            comentario.setHorizontallyScrolling(false);
            comentario.setGravity(Gravity.LEFT);
            comentario.setMaxLines(10);
            comentario.setSingleLine(false);
            rowComment.addView(comentario);
        }
        table.invalidate();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Estacionamiento getEstacionamiento(String id) {
        for (Estacionamiento e : RecorridoHolder.getInstance().getEstacionamientos()) {
            if (e.getNombre().equals(id)) {
                return e;
            }
        }
        return null;
    }
}
