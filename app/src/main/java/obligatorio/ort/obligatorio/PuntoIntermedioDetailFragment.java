package obligatorio.ort.obligatorio;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.media.Image;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import obligatorio.ort.obligatorio.recorrido.PuntoIntermedio;
import obligatorio.ort.obligatorio.recorrido.RecorridoHolder;

//import obligatorio.ort.obligatorio.dummy.DummyContent;

/**
 * A fragment representing a single Punto Intermedio detail screen.
 * This fragment is either contained in a {@link PuntoIntermedioListActivity}
 * in two-pane mode (on tablets) or a {@link PuntoIntermedioDetailActivity}
 * on handsets.
 */
public class PuntoIntermedioDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_ITEM_POS = "item_pos";

    /**
     * The dummy content this fragment is presenting.
     */
    private PuntoIntermedio mItem;
    private static MapView mapView;
    private GoogleMap mMap;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public PuntoIntermedioDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_POS)) {
            List<PuntoIntermedio> puntos = RecorridoHolder.getInstance().getRecorrido().getPuntos();
            mItem = puntos.get(getArguments().getInt(ARG_ITEM_POS));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getTitulo());
            }
            if (mItem.getRutaDeFoto() != null) {
                ImageView image = (ImageView) activity.findViewById(R.id.puntointermedio_image);
                image.setMinimumWidth(appBarLayout.getWidth());
                image.setMinimumHeight(appBarLayout.getHeight());
                setImage(mItem.getRutaDeFoto(), image);
            }

        }

        //NestedScrollView view = (NestedScrollView) this.findViewById(R.id.puntointermedio_detail_container);
//        SupportMapFragment mapFragment = (SupportMapFragment) this.getActivity().getSupportFragmentManager().findFragmentById(R.id.mapPuntoDeInteres);
//        mapFragment.getMapAsync((OnMapReadyCallback) this.getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.puntointermedio_detail, container, false);
        View layoutHeader = inflater.inflate(R.layout.activity_puntointermedio_detail, container, false);

        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.puntointermedio_detail)).setText(mItem.getDescripcion());
        }

        mapView = (MapView) rootView.findViewById(R.id.mapPuntoIntermedio);
        mapView.onCreate(savedInstanceState);
        MapsInitializer.initialize(this.getActivity());
        mMap = mapView.getMap();
        localizar(mMap, mItem.getUbicacion());

        return rootView;
    }

    private void localizar(GoogleMap mapa, LatLng pos) {

        if (pos != null) {

            mapa.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 15));
            mapa.addMarker(new MarkerOptions()
                    .position(pos)
                    .title(mItem.getTitulo())
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        }
    }

    private void setImage(String rutaDeFoto, ImageView view) {
        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(rutaDeFoto, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = 1;
        if ((view.getWidth() > 0) || (view.getHeight() > 0)) {
            scaleFactor = Math.min(photoW/view.getWidth(), photoH/view.getHeight());
        }
        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(rutaDeFoto, bmOptions);
        view.setImageBitmap(bitmap);
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

}
