package obligatorio.ort.obligatorio;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.support.v7.app.ActionBar;
import android.view.MenuItem;

//import obligatorio.ort.obligatorio.dummy.DummyContent;

import com.google.android.gms.maps.model.LatLng;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import obligatorio.ort.obligatorio.recorrido.PuntoIntermedio;
import obligatorio.ort.obligatorio.recorrido.Recorrido;
import obligatorio.ort.obligatorio.recorrido.RecorridoHolder;

/**
 * An activity representing a list of Puntos Intermedios. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link PuntoIntermedioDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class PuntoIntermedioListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    private Recorrido instanciarRecorrido() {
        Recorrido recorrido = new Recorrido();
        recorrido.setActivo(true);
        recorrido.setCodigoEstacionamiento("1");
        recorrido.setFechaFin(null);
        recorrido.setFechaInicio(new Timestamp(System.currentTimeMillis()));
        recorrido.setId(new Long(1));
        PuntoIntermedio origen = new PuntoIntermedio();
        origen.setDescripcion("Este es el origen del recorrido");
        origen.setTitulo("Origen");
        origen.setUbicacion(new LatLng(-34.872003, -56.068961));
        recorrido.setOrigen(origen);
        List<PuntoIntermedio> listaPuntos = new ArrayList<>();
        listaPuntos.add(new PuntoIntermedio(new LatLng(-34.873843, -56.069028), null, "Punto Intermedio 1", "Este es el punto intermedio 1!"));
        listaPuntos.add(new PuntoIntermedio(new LatLng(-34.876571, -56.068742), null, "Punto Intermedio 2", "Este es el punto intermedio 2!" +
                " Este es el punto intermedio 2! Este es el punto intermedio 2! Este es el punto intermedio 2! Este es el punto intermedio 2!" +
                " Este es el punto intermedio 2! Este es el punto intermedio 2! Este es el punto intermedio 2! Este es el punto intermedio 2!" +
                " Este es el punto intermedio 2! Este es el punto intermedio 2! Este es el punto intermedio 2! Este es el punto intermedio 2!" +
                " Este es el punto intermedio 2! Este es el punto intermedio 2! Este es el punto intermedio 2! Este es el punto intermedio 2!" +
                " Este es el punto intermedio 2! Este es el punto intermedio 2! Este es el punto intermedio 2! Este es el punto intermedio 2!" +
                " Este es el punto intermedio 2! Este es el punto intermedio 2! Este es el punto intermedio 2! Este es el punto intermedio 2!" +
                " Este es el punto intermedio 2! Este es el punto intermedio 2! Este es el punto intermedio 2! Este es el punto intermedio 2!" +
                " Este es el punto intermedio 2! Este es el punto intermedio 2! Este es el punto intermedio 2! Este es el punto intermedio 2!" +
                " Este es el punto intermedio 2! Este es el punto intermedio 2! Este es el punto intermedio 2! Este es el punto intermedio 2!" +
                " Este es el punto intermedio 2! Este es el punto intermedio 2! Este es el punto intermedio 2! Este es el punto intermedio 2!" +
                " Este es el punto intermedio 2!"));
        recorrido.setPuntos(listaPuntos);
        List<LatLng> listaRecorrido = new ArrayList<>();
        listaRecorrido.add(new LatLng(-34.872003, -56.068961));
        listaRecorrido.add(new LatLng(-34.873843, -56.069028));
        listaRecorrido.add(new LatLng(-34.876571, -56.068742));
        listaRecorrido.add(new LatLng(-34.878077, -56.067950));
        listaRecorrido.add(new LatLng(-34.878127, -56.068467));
        recorrido.setRecorrido(listaRecorrido);

        return recorrido;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //RecorridoHolder.getInstance().setRecorrido(instanciarRecorrido());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntointermedio_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        View recyclerView = findViewById(R.id.puntointermedio_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.puntointermedio_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
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

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(RecorridoHolder.getInstance().getRecorrido().getPuntos()));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<PuntoIntermedio> mValues;

        public SimpleItemRecyclerViewAdapter(List<PuntoIntermedio> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.puntointermedio_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mPosition = position;
            holder.mIdView.setText(String.valueOf(position + 1));
            holder.mContentView.setText(mValues.get(position).getTitulo());

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(PuntoIntermedioDetailFragment.ARG_ITEM_ID, holder.mItem.getTitulo());
                        arguments.putInt(PuntoIntermedioDetailFragment.ARG_ITEM_POS, holder.mPosition);
                        PuntoIntermedioDetailFragment fragment = new PuntoIntermedioDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.puntointermedio_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, PuntoIntermedioDetailActivity.class);
                        intent.putExtra(PuntoIntermedioDetailFragment.ARG_ITEM_ID, holder.mItem.getTitulo());
                        intent.putExtra(PuntoIntermedioDetailFragment.ARG_ITEM_POS, holder.mPosition);
                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public PuntoIntermedio mItem;
            public Integer mPosition;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }
}
