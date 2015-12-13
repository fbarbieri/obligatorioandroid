package obligatorio.ort.obligatorio;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import obligatorio.ort.obligatorio.recorrido.PuntoIntermedio;
import obligatorio.ort.obligatorio.recorrido.RecorridoHolder;

/**
 * An activity representing a single Punto Intermedio detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link PuntoIntermedioListActivity}.
 */
public class PuntoIntermedioDetailActivity extends AppCompatActivity /*implements OnMapReadyCallback */{

    private PuntoIntermedio punto;
//    private GoogleMap mMap;

//    public void onMapReady(GoogleMap googleMap) {
//        //localizar.
//        mMap = googleMap;
//        LatLng ubicacion = punto.getUbicacion();
//        localizar(null,ubicacion,punto.getTitulo(), BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
//    }

//    private Marker localizar(Marker mark, LatLng pos,String title,BitmapDescriptor icon) {
//        if (pos != null) {
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 20.0f));
//            if (mark == null) {
//                mark = mMap
//                        .addMarker(new MarkerOptions()
//                                .position(pos)
//                                .title(title)
//                                .icon(icon));
//            } else {
////                animateMarker(mark, pos, false);
//            }
//        }
//        return mark;
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntointermedio_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(PuntoIntermedioDetailFragment.ARG_ITEM_ID, getIntent().getStringExtra(PuntoIntermedioDetailFragment.ARG_ITEM_ID));
            arguments.putInt(PuntoIntermedioDetailFragment.ARG_ITEM_POS, getIntent().getIntExtra(PuntoIntermedioDetailFragment.ARG_ITEM_POS, 0));
            punto = RecorridoHolder.getInstance().getRecorrido().getPuntos().get(getIntent().getIntExtra(PuntoIntermedioDetailFragment.ARG_ITEM_POS, 0));
            PuntoIntermedioDetailFragment fragment = new PuntoIntermedioDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.puntointermedio_detail_container, fragment)
                    .commit();

//            NestedScrollView view = (NestedScrollView) this.findViewById(R.id.puntointermedio_detail_container);
//            SupportMapFragment mapFragment = (SupportMapFragment) view.findFragmentById(R.id.mapPuntoDeInteres);
//            mapFragment.getMapAsync(this);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, PuntoIntermedioListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
