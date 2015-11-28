package obligatorio.ort.obligatorio;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.PermissionChecker;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import obligatorio.ort.obligatorio.recorrido.PuntoIntermedio;
import obligatorio.ort.obligatorio.recorrido.Recorrido;
import obligatorio.ort.obligatorio.recorrido.RecorridoHolder;
import obligatorio.ort.obligatorio.recorrido.RecorridoManager;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, LocationListener, View.OnClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleMap mMap;
    private Marker mark;
    public static final int INICIAR_RECORRIDO = 1;
    private Button btnIniciarRecorrido;
    private Location mLocation;
    private FloatingActionButton fab;
    private GoogleApiClient mGoogleApiClient;
    private RecorridoManager recorridoManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        buildGoogleApiClient();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLastKnownLocation();
            }
        });
        btnIniciarRecorrido = (Button) findViewById(R.id.btnIniciarRecorrido);
        btnIniciarRecorrido.setVisibility(View.VISIBLE);
        btnIniciarRecorrido.setOnClickListener(this);
        recorridoManager = new RecorridoManager(this);
        if (RecorridoHolder.getInstance().getRecorrido() == null) {
            Recorrido recorrido = recorridoManager.obtenerUltimoRecorrido(true);
            if(recorrido!=null){
                RecorridoHolder.getInstance().setRecorrido(recorrido);
                esconderBotonIniciarRecorrido();
            }
        } else {
            esconderBotonIniciarRecorrido();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getLastKnownLocation();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnIniciarRecorrido:
                Intent iniciarRecorrido = new Intent(this, IniciarRecorridoActivity.class);
                startActivityForResult(iniciarRecorrido, INICIAR_RECORRIDO);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case INICIAR_RECORRIDO:
                if (resultCode == RESULT_OK) {
                    if(data.getExtras()!=null || data.getExtras().size()>0){
                        String fotoPath = data.getStringExtra(getString(R.string.foto_result));
                        String codigoEstacionamiento = data.getStringExtra(getString(R.string.codigo_estacionamiento));

                        PuntoIntermedio primerPI = new PuntoIntermedio();
                        primerPI.setDescripcion(getString(R.string.guardar_recorrido));
                        primerPI.setRutaDeFoto(fotoPath);
                        primerPI.setTitulo(getString(R.string.guardar_recorrido));
                        primerPI.setUbicacion(mLocation);

                        Recorrido recorrido = new Recorrido();
                        recorrido.setCodigoEstacionamiento(codigoEstacionamiento);
                        recorrido.setOrigen(primerPI);
                        recorrido.setActivo(true);
                        recorrido.setFechaInicio(new Timestamp(System.currentTimeMillis()));
                        recorrido.setRecorrido(new ArrayList<Location>());
                        recorrido.setPuntos(new ArrayList<PuntoIntermedio>());
                        RecorridoHolder.getInstance().setRecorrido(recorrido);

                        recorridoManager.AgregarRecorrido(recorrido);
                        esconderBotonIniciarRecorrido();

                    }
                }
                break;
        }
    }

    private void esconderBotonIniciarRecorrido(){
        btnIniciarRecorrido.setVisibility(View.GONE);
        btnIniciarRecorrido.setVisibility(View.INVISIBLE);

        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) fab.getLayoutParams();
        params.setMargins(params.leftMargin, params.topMargin, params.rightMargin , params.rightMargin);
        fab.setLayoutParams(params);
    }

    private void localizar() {
        if (mLocation != null) {
            final LatLng pos = new LatLng(mLocation.getLatitude(),
                    mLocation.getLongitude());
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos, 17));
            if (mark == null) {
                mark = mMap
                        .addMarker(new MarkerOptions()
                                .position(
                                        new LatLng(mLocation.getLatitude(),
                                                mLocation.getLongitude()))
                                .title("Aca")
                                .icon(BitmapDescriptorFactory
                                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
            } else {
                animateMarker(mark, pos, false);
            }
        }
    }

    private void getLastKnownLocation() {
        mLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        LocationManager mLocationManager = (LocationManager) getApplication().getSystemService(LOCATION_SERVICE);

        if (mLocationManager != null) {
            if (PermissionChecker.checkCallingOrSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 10, this);
            }
        }
        localizar();
    }

    public void animateMarker(final Marker marker, final LatLng toPosition,
                              final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = mMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 500;
        final Interpolator interpolator = new LinearInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        if(RecorridoHolder.getInstance().getRecorrido()!=null){
            RecorridoHolder.getInstance().getRecorrido().getRecorrido().add(location);
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        getLastKnownLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
