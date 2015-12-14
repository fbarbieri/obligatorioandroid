package obligatorio.ort.obligatorio;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import obligatorio.ort.obligatorio.Servicios.EstacionamientosServices;
import obligatorio.ort.obligatorio.estacionamiento.Estacionamiento;
import obligatorio.ort.obligatorio.facebook.User;
import obligatorio.ort.obligatorio.recorrido.PuntoIntermedio;
import obligatorio.ort.obligatorio.recorrido.Recorrido;
import obligatorio.ort.obligatorio.recorrido.RecorridoHolder;
import obligatorio.ort.obligatorio.recorrido.RecorridoManager;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback, LocationListener, View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnCameraChangeListener {

    private static final int INICIAR_RECORRIDO = 1;
    private static final int INGRESAR_PUNTO_INTERMEDIO = 2;
    private static final int VER_LISTADO = 3;

    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;

    private Marker markInicial;
    private Marker markActual;

    private BitmapDescriptor iconMarkerInicial;
    private BitmapDescriptor iconMarkerActual;
    private BitmapDescriptor iconMarkerIntermedio;
    private BitmapDescriptor iconMarkerEstacionamiento;
    private Location mLocationInicial;
    private Location mLocationActual;

    private Button btnGuardarPuntoIntermedio;
    private Button btnIniciarRecorrido;
    private FloatingActionButton fab;

    private float zoom = 15.0f;
    private boolean seguir = true;

    private RecorridoManager recorridoManager;

    private static CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
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
                seguir = true;
            }
        });

        btnIniciarRecorrido = (Button) findViewById(R.id.btnIniciarRecorrido);
        btnIniciarRecorrido.setOnClickListener(this);
        btnGuardarPuntoIntermedio = (Button) findViewById(R.id.btnGuardarPuntoIntermedio);
        btnGuardarPuntoIntermedio.setOnClickListener(this);

        iconMarkerInicial = BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA);

        iconMarkerActual = BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_AZURE);

        iconMarkerIntermedio = BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_GREEN);

        iconMarkerEstacionamiento = BitmapDescriptorFactory.fromResource(R.drawable.ic_local_parking_black_24dp);

        recorridoManager = new RecorridoManager(this);
        //recorridoManager.limpiarBase();
        if (RecorridoHolder.getInstance().getRecorrido() == null) {
            Recorrido recorrido = recorridoManager.obtenerUltimoRecorrido(true);
            if(recorrido!=null){
                RecorridoHolder.getInstance().setRecorrido(recorrido);
                esconderBotonIniciarRecorrido();
                inicializarRecorrido(recorrido);
            }
        } else {
            esconderBotonIniciarRecorrido();
            inicializarRecorrido(RecorridoHolder.getInstance().getRecorrido());
        }

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // login successful
                        AccessToken accessToken = AccessToken.getCurrentAccessToken();
                        Log.i("DetalleEstacionamiento", "Facebook success callback " + accessToken.getToken());
                        checkFacebookSession();
                    }

                    @Override
                    public void onCancel() {
                        Log.i("DetalleEstacionamiento", "Facebook cancel callback ");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        Log.i("DetalleEstacionamiento", "Facebook Error callback ");
                    }
                });

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

        switch (id) {
            case R.id.nav_listado:
                Intent verListado = new Intent(this, PuntoIntermedioListActivity.class);
                verListado.putExtra("testextra","testextra");
                startActivityForResult(verListado, VER_LISTADO);
                break;
//            case R.id.btnGuardarPuntoIntermedio:
//                Intent guardarPuntoIntermedio = new Intent(this, IngresarPuntoIntermedioActivity.class);
//                guardarPuntoIntermedio.putExtra(getString(R.string.location),mLocationActual);
//                startActivityForResult(guardarPuntoIntermedio, INGRESAR_PUNTO_INTERMEDIO);
//                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        mMap.setOnCameraChangeListener(this);
        getLastKnownLocation();
        if(mLocationInicial!=null)
            markInicial = localizar(markInicial,mLocationInicial,"Inicio de recorrido",iconMarkerInicial);

        if (RecorridoHolder.getInstance().getRecorrido() != null)
            dibujarElementos();

        if(RecorridoHolder.getInstance().getEstacionamientos().isEmpty()){
            EstacionamientosServices.obtenerEstacionamiento(mMap, iconMarkerEstacionamiento);
        } else {
            for (Estacionamiento est : RecorridoHolder.getInstance().getEstacionamientos()){
                LatLng pos = new LatLng(est.getLatitud(),est.getLongitud());
                localizar(null,pos,est.getNombre(),iconMarkerEstacionamiento);
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnIniciarRecorrido:
                Intent iniciarRecorrido = new Intent(this, IniciarRecorridoActivity.class);
                iniciarRecorrido.putExtra(getString(R.string.location),mLocationActual);
                startActivityForResult(iniciarRecorrido, INICIAR_RECORRIDO);
                break;
            case R.id.btnGuardarPuntoIntermedio:
                Intent guardarPuntoIntermedio = new Intent(this, IngresarPuntoIntermedioActivity.class);
                guardarPuntoIntermedio.putExtra(getString(R.string.location),mLocationActual);
                startActivityForResult(guardarPuntoIntermedio, INGRESAR_PUNTO_INTERMEDIO);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode, data);
        switch (requestCode) {
            case INICIAR_RECORRIDO:
                if (resultCode == RESULT_OK) {
                    if(data.getExtras()!=null || data.getExtras().size()>0){
                        guardarRecorrido(data);
                    }
                }
                break;
            case INGRESAR_PUNTO_INTERMEDIO:
                if (resultCode == RESULT_OK) {
                    if(data.getExtras()!=null && data.getExtras().size()>0){
                        guardarPuntoIntermedio(data);
                    }
                }
                break;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng nuevaLocation = new LatLng(location.getLatitude(), location.getLongitude());
        mLocationActual = location;
        if (seguir)
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(nuevaLocation, zoom));

        if(RecorridoHolder.getInstance().getRecorrido()!=null){

            List<LatLng> recorrido = RecorridoHolder.getInstance().getRecorrido().getRecorrido();
            if(recorrido.size()>0){
                LatLng lastLocation = recorrido.get(recorrido.size() - 1);
                mMap.addPolyline(new PolylineOptions()
                        .add(lastLocation, nuevaLocation)
                        .width(5)
                        .color(Color.BLUE)
                );
            }
            RecorridoHolder.getInstance().getRecorrido().getRecorrido().add(nuevaLocation);
            recorridoManager.actualizarRecorrido(RecorridoHolder.getInstance().getRecorrido());
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

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        zoom = cameraPosition.zoom;
        seguir = false;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        Estacionamiento estacionamiento = buscarEstacionamiento(marker.getPosition());
        if(estacionamiento!=null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment prev = getSupportFragmentManager().findFragmentByTag("DetalleEstacionamiento");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);
            DialogFragment dialogFragment = DetalleEstacionamiento.newInstance(estacionamiento.getNombre(),
                    estacionamiento.getDescripcion(),estacionamiento.getPuntaje());
            dialogFragment.show(ft, "DetalleEstacionamiento");
        }
        return false;
    }


    // FUNCIONES AUXILIARES
    private void inicializarRecorrido(Recorrido recorrido) {
        mLocationInicial = new Location(LocationManager.GPS_PROVIDER);
        mLocationInicial.setLatitude(recorrido.getOrigen().getUbicacion().latitude);
        mLocationInicial.setLongitude(recorrido.getOrigen().getUbicacion().longitude);
        esconderBotonIniciarRecorrido();
        if(recorrido.getCodigoEstacionamiento()!=null)
            EstacionamientosServices.obtenerNotificaciones(recorrido.getCodigoEstacionamiento());
    }

    private void guardarRecorrido(Intent data){
        String fotoPath = data.getStringExtra(getString(R.string.foto_result));
        String codigoEstacionamiento = data.getStringExtra(getString(R.string.codigo_estacionamiento));
        Location location = data.getParcelableExtra(getString(R.string.location));

        PuntoIntermedio primerPI = new PuntoIntermedio();
        primerPI.setDescripcion(getString(R.string.guardar_recorrido));
        primerPI.setRutaDeFoto(fotoPath);
        primerPI.setTitulo(getString(R.string.guardar_recorrido));

        LatLng inicio = new LatLng(location.getLatitude(), location.getLongitude());
        primerPI.setUbicacion(inicio);

        Recorrido recorrido = new Recorrido();
        recorrido.setCodigoEstacionamiento(codigoEstacionamiento);
        recorrido.setOrigen(primerPI);
        recorrido.setActivo(true);
        recorrido.setFechaInicio(new Timestamp(System.currentTimeMillis()));
        recorrido.setRecorrido(new ArrayList<LatLng>());
        recorrido.setPuntos(new ArrayList<PuntoIntermedio>());

        recorrido = recorridoManager.AgregarRecorrido(recorrido);
        RecorridoHolder.getInstance().setRecorrido(recorrido);
        EstacionamientosServices.iniciarRecorrido(codigoEstacionamiento);
        esconderBotonIniciarRecorrido();
    }

    private Estacionamiento buscarEstacionamiento(LatLng pos){
        for (Estacionamiento est :RecorridoHolder.getInstance().getEstacionamientos()){
            LatLng posEst = new LatLng(est.getLatitud(),est.getLongitud());
            if (posEst.equals(pos))
                return est;
        }
        return null;
    }

    private void guardarPuntoIntermedio(Intent data){
        String fotoPath = data.getStringExtra(getString(R.string.foto_result));
        String titulo = data.getStringExtra(getString(R.string.titulo));
        String descripcion = data.getStringExtra(getString(R.string.descripcion));
        Location location = data.getParcelableExtra(getString(R.string.location));
        LatLng localizacion = new LatLng(location.getLatitude(), location.getLongitude());

        PuntoIntermedio nuevoPI = new PuntoIntermedio();
        nuevoPI.setTitulo(titulo);
        nuevoPI.setDescripcion(descripcion);
        nuevoPI.setRutaDeFoto(fotoPath);
        nuevoPI.setUbicacion(localizacion);

        Recorrido recorrido = RecorridoHolder.getInstance().getRecorrido();
        recorrido.getPuntos().add(nuevoPI);
        recorridoManager.actualizarRecorrido(recorrido);

    }

    private void esconderBotonIniciarRecorrido(){
        btnIniciarRecorrido.setVisibility(View.GONE);
        btnIniciarRecorrido.setVisibility(View.INVISIBLE);
        btnGuardarPuntoIntermedio.setVisibility(View.VISIBLE);

    }

    private Marker localizar(Marker mark, Location loc,String title,BitmapDescriptor icon) {
        if (loc != null) {
            final LatLng pos = new LatLng(loc.getLatitude(),
                    loc.getLongitude());
            mark = localizar(mark,pos,title,icon);
        }
        return mark;
    }

    private Marker localizar(Marker mark, LatLng pos,String title,BitmapDescriptor icon) {
        if (pos != null) {
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(pos,zoom));
            if (mark == null) {
                mark = mMap
                        .addMarker(new MarkerOptions()
                                .position(pos)
                                .title(title)
                                .icon(icon));
            } else {
                animateMarker(mark, pos, false);
            }
        }
        return mark;
    }

    private void animateMarker(final Marker marker, final LatLng toPosition,
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

    private void getLastKnownLocation() {
        mLocationActual = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        LocationManager mLocationManager = (LocationManager) getApplication().getSystemService(LOCATION_SERVICE);
        if (mLocationManager != null) {
            if (PermissionChecker.checkCallingOrSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, 10, this);
            }
        }
        markActual = localizar(markActual, mLocationActual, "posicionActual", iconMarkerActual);
    }

    private void dibujarElementos(){
        Recorrido recorrido = RecorridoHolder.getInstance().getRecorrido();
        List<Estacionamiento> estacionamientos = RecorridoHolder.getInstance().getEstacionamientos();
        for(PuntoIntermedio pi : recorrido.getPuntos()){
            localizar(null,pi.getUbicacion(),pi.getTitulo(),iconMarkerIntermedio);
        }

        mMap.addPolyline(new PolylineOptions()
                .addAll(recorrido.getRecorrido())
                .width(5)
                .color(Color.RED));
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
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
            LoginManager.getInstance().logInWithReadPermissions(this, permissions);
        }
    }

}
