package com.rutasbus.santaelena.app.rutas;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.rutas.santaelena.app.rutas.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import denuncias.AbstractAsyncActivity;
import detectaRuta.Buses_disponibles;
import detectaRuta.EncuentraRuta;
import detectaRuta.Marcador;
import inicio.SplashInicio;
import lineas.AllBusesCirculando;
import lineas.BusesMapa;
import lineas.LineaBus;
import placesNearPoint.Radio;
import placesNearPoint.SearchPlacesParaderosNear;
import sesion.CustomInfoWindowAdapter;
import sesion.IniciaSesion;

public class MapsActivity extends AbstractAsyncActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {
    private Marker markerOrigen, markerFindelViajeEnbus;
    private boolean[] detectarRutaCercanaPuntoDestino, detectarRutaCercanaPuntoOrigen;
    private boolean rutaDisponible = false, origenRutaEncontrado = false;
    private ArrayList<LatLng> markerPointsOrigenDestino = new ArrayList<>();
    private EncuentraRuta encuentraRuta = new EncuentraRuta();
    private Marcador colocarMarker = new Marcador();
    private BusesMapa busesMapa;
    private GoogleMap mMap;
    private List<ArrayList<LatLng>> listaRutasPuntosLatLng = new ArrayList<>();
    private List<String> nombreRutasTodasLineas = new ArrayList<>();
    private List<String> rutasDisponibleOrigenDestino = new ArrayList<>();
    private double distanciaOrigenDestino = 0;
    private LatLng origen;
    private int lineaPosicion=0;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Boolean mLocationPermissionsGranted = false;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private FloatingActionButton mGps,cleanMap;
    private BottomSheetBehavior mBottomSheetBehavior1;
    LinearLayout tapactionlayout;
    View bottomSheet;
    FloatingActionButton floatingActionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        getLocationPermission();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsActivity.this);

        mGps = (FloatingActionButton) findViewById(R.id.ic_gps);
        cleanMap =(FloatingActionButton) findViewById(R.id.fltCleanMap);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        tapactionlayout = (LinearLayout) findViewById(R.id.tap_action_layout);
        bottomSheet = findViewById(R.id.bottom_sheet1);

        mBottomSheetBehavior1 = BottomSheetBehavior.from(bottomSheet);
        mBottomSheetBehavior1.setPeekHeight(120);
        mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_COLLAPSED);
        mBottomSheetBehavior1.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED) tapactionlayout.setVisibility(View.VISIBLE);

                if (newState == BottomSheetBehavior.STATE_EXPANDED) tapactionlayout.setVisibility(View.GONE);

                if (newState == BottomSheetBehavior.STATE_DRAGGING) tapactionlayout.setVisibility(View.GONE);
            }

            @Override
            public void onSlide(@NonNull final View bottomSheet, final float slideOffset) {
                onClick(bottomSheet);
            }
        });

        tapactionlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mBottomSheetBehavior1.getState()==BottomSheetBehavior.STATE_COLLAPSED)
                { mBottomSheetBehavior1.setState(BottomSheetBehavior.STATE_EXPANDED); }
            }
        });
    }

    private void cargarRutas() {
        showLoadingProgressDialog();
        new SplashInicio().getTodasLineasBusesWS(this, new SplashInicio.OnOkRutaNombreCargadas() {
            @Override
            public void seleccionada(List<ArrayList<LatLng>> listaRutas, List<String> nombreRutasTodas) {
                listaRutasPuntosLatLng = listaRutas;
                nombreRutasTodasLineas = nombreRutasTodas;
                dismissProgressDialog();
                encuentraRutasAlPuntoOrigenOrigen(origen, 200);
            }});
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            if (busesMapa != null) busesMapa.detener();
            Intent intent = new Intent(MapsActivity.this, MapsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_contactos) {
            Intent intent = new Intent(MapsActivity.this, IniciaSesion.class);
            startActivity(intent);
        } else if (id == R.id.nav_ayuda) {
            Intent intent = new Intent(MapsActivity.this, CardHelp.class);
            startActivity(intent);
       /* } else if (id == R.id.nav_buses) {
            Intent intent = new Intent(MapsActivity.this, AllBusesCirculando.class);
            if (lineaPosicion!=0)
                intent.putExtra("bus",nombreRutasTodasLineas.get(lineaPosicion));
            startActivity(intent);*/
        } else if (id == R.id.nav_7) {
            String lineaBus = "7";
            Intent intent = new Intent(MapsActivity.this, LineaBus.class);
            intent.putExtra("linea", lineaBus);
            startActivity(intent);
        } else if (id == R.id.nav_8) {
            String lineaBus = "8";
            Intent intent = new Intent(MapsActivity.this, LineaBus.class);
            intent.putExtra("linea", lineaBus);
            startActivity(intent);
        } else if (id == R.id.nav_11) {
            String lineaBus = "11";
            Intent intent = new Intent(MapsActivity.this, LineaBus.class);
            intent.putExtra("linea", lineaBus);
            startActivity(intent);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setPadding(1,1,1,60);
        if (mLocationPermissionsGranted) {
            getDeviceLocation();
            cargarRutas();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
        }

        PlaceAutocompleteFragment placeAutoCompleteOrigenDestino = (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_Origendestino);
        placeAutoCompleteOrigenDestino.setBoundsBias(new LatLngBounds(new LatLng(-2.291430, -81.008619), new LatLng(-2.162431, -80.851164)));//PERMITE QUE MIS PRIMERAS OPCIONES DE BUSQUEDA SEAN EN LA PENIN
        placeAutoCompleteOrigenDestino.getView().setBackgroundColor(Color.WHITE);
        placeAutoCompleteOrigenDestino.setHint(getString(R.string.ir_A));
        ImageView searchIcon = (ImageView) ((LinearLayout) placeAutoCompleteOrigenDestino.getView()).getChildAt(0);
        searchIcon.setImageDrawable(getResources().getDrawable(R.drawable.menu));
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START); //Despliega el menu
            }});

        /////////////  BUSQUEDA DEL DESTINO INGRESANDO POR TEXTO ///////////////////////
        placeAutoCompleteOrigenDestino.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                LatLng latlangBusquedaTextoOrigenDestino = place.getLatLng(); //recuepramos la LatLong de la busqueda
                colocarEnMapaPuntoOrigenDestino(latlangBusquedaTextoOrigenDestino);
                placeAutoCompleteOrigenDestino.setText("");
            }
            @Override
            public void onError(Status status) { Log.d("Maps", "An error occurred: " + status); }
        });
        /**
         * METODOD PARA AGREGAR EN MARKER EN EL MAPA, PULSO EN PANTALLA
         */
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng pointOri_Dest) { colocarEnMapaPuntoOrigenDestino(pointOri_Dest); }
        });

        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { getDeviceLocation(); }
        });

        cleanMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { cleanMap(origen,false); }
        });
    }
    /**
     * METODO PARA POSICIONAR UN PUNTO EN EL MAPA Y BORRADO DE MARCADORES
     * @param pointOriDest
     */
    private void colocarEnMapaPuntoOrigenDestino(LatLng pointOriDest) {
        cleanMap(pointOriDest,true);
        // agregamos punto origen/destino al array list
        markerPointsOrigenDestino.add(pointOriDest);
        if (markerPointsOrigenDestino.size() == 2)
            encuentraRutaAlPuntoDestino(markerPointsOrigenDestino.get(1), 200);
    }
    /**
     * Método que dibuja la ruta seleccionada de una lista
     *
     * @param seleccion la ruta que elegimos
     * @param latLngPuntoCercanoAtomarElBus
     * @param latLngPuntoCercanoBajadaDelBus
     */
    private void mostrarRutaSeleccionada(int seleccion, LatLng latLngPuntoCercanoAtomarElBus, LatLng latLngPuntoCercanoBajadaDelBus) {
        lineaPosicion = seleccion;
        markerFindelViajeEnbus = colocarMarker.colocarMarcadorRutaBusMasCercanaDetino(latLngPuntoCercanoBajadaDelBus, "Llegue hasta aqui", mMap, this);

        markerPointsOrigenDestino.add(latLngPuntoCercanoAtomarElBus);
        markerPointsOrigenDestino.add(latLngPuntoCercanoBajadaDelBus);

        new MostrarRutaSeleccion().mostrarRutaSeleccionada(seleccion, this, mMap, markerPointsOrigenDestino.get(0), latLngPuntoCercanoAtomarElBus, latLngPuntoCercanoBajadaDelBus, markerPointsOrigenDestino.get(1), listaRutasPuntosLatLng);
        new SearchPlacesParaderosNear().getParadasCercanasAPuntoWS(nombreRutasTodasLineas.get(seleccion), mMap, latLngPuntoCercanoAtomarElBus, this, 200);
        new SearchPlacesParaderosNear().getParadasCercanasAPuntoWS(nombreRutasTodasLineas.get(seleccion), mMap, latLngPuntoCercanoBajadaDelBus, this, 200);

        busesMapa = new BusesMapa(mMap, this, nombreRutasTodasLineas.get(seleccion));
        if (busesMapa != null) busesMapa.ini();

        mMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(this, LayoutInflater.from(this), nombreRutasTodasLineas.get(seleccion),busesMapa)); //MUESTRA INFORMACION EN EL MARCADOR
        new Buses_disponibles().infoMaps(this);
    }

    private void getLocationPermission() {
        Log.d("Maps", "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
            } else {
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE); }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("Maps", "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            Log.d("Maps", "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d("Maps", "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    getDeviceLocation();
                    cargarRutas();
                }
            }
        }
    }


    private void getDeviceLocation() {
        Log.d("Maps", "getDeviceLocation: getting the devices current location");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try {
            if (mLocationPermissionsGranted) {
                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d("Sucessfull", "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();
                            if (currentLocation != null) {
                                origen = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                                CameraUpdate orig = CameraUpdateFactory.newLatLngZoom(origen, 17f);
                                mMap.animateCamera(orig);
                            }
                        } else {
                            Log.d("Maps", "onComplete: current location is null");
                            Toast.makeText(MapsActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) { Log.e("Maps", "getDeviceLocation: SecurityException: " + e.getMessage()); }
    }

    /**
     * Método que elimina un LatLng si no se encuentra una ruta
     *
     * @param eliminarPunto  punto que se eliminara y posteriormete sera ampliado su radio de busqueda
     * @param puntosCercanos amplia el radio cuando el punto origen y destino estan distantes a 1100mtrs
     */
    private void noDetectaRuta(LatLng eliminarPunto, boolean puntosCercanos) { //ELIMINA UN LATLONG SI NO ESTA DENTRO DEL ALCNCE DE UNA RUTA o menor 1000MTS

        colocarMarker.msmOrigenDestino(markerPointsOrigenDestino, distanciaOrigenDestino, mMap, this);
        markerPointsOrigenDestino.remove(eliminarPunto);

        if (!puntosCercanos) {
            new Radio().radioPickSelectorAmpliarBusquedaRutas(this, new Radio.OnOkRadio() {
                @Override
                public void radio(int radio) {
                    markerPointsOrigenDestino.add(eliminarPunto);
                    if (markerPointsOrigenDestino.size() == 1)
                        encuentraRutasAlPuntoOrigenOrigen(eliminarPunto, radio);
                    else
                        encuentraRutaAlPuntoDestino(eliminarPunto, radio);
                }});
        }
    }

    public void cleanMap(LatLng pointDestino, boolean valor){
        if (markerPointsOrigenDestino.size() > 1) {
            new Radio().continueNew(this, new Radio.OnOk() {
                @Override
                public void onOk(boolean onOk) {
                    if (onOk) { //Si el usuario selecciona YES, se inicializara TodoDenuevo
                      //  horizontalScrollView.setVisibility(View.INVISIBLE);
                        if (busesMapa != null) busesMapa.detener();
                        mMap.clear();
                        markerPointsOrigenDestino = new ArrayList<>();
                        rutaDisponible = false;
                        origenRutaEncontrado = false;
                        rutasDisponibleOrigenDestino = new ArrayList<>();
                        markerPointsOrigenDestino.add(origen);
                        if (markerOrigen != null) markerOrigen.remove();
                        markerOrigen = colocarMarker.colocarMarcadorPuntoOrigenEnMapa(origen, getString(R.string.ubicacion_Actual), mMap, getApplicationContext());

                        if (valor) {
                            markerPointsOrigenDestino.add(pointDestino);
                            encuentraRutaAlPuntoDestino(markerPointsOrigenDestino.get(1), 200);
                        }else
                            Toast.makeText(MapsActivity.this, getString(R.string.mensaje_Busqueda), Toast.LENGTH_LONG).show();
                    } }
            });
        }

    }
    public void onClick(View v) {
        new SearchPlacesParaderosNear().onClick(v, mMap, markerPointsOrigenDestino, this, markerOrigen, markerFindelViajeEnbus, markerFindelViajeEnbus, markerFindelViajeEnbus, nombreRutasTodasLineas.get(lineaPosicion));
    }

    /**
     * Método que detecta si el punto destino, esta cerca a alguna ruta
     * @param pointDestinoMapa
     * @param radio
     */
    private void encuentraRutaAlPuntoDestino(LatLng pointDestinoMapa, int radio) {
        distanciaOrigenDestino = encuentraRuta.distanciasEntrePuntos(markerPointsOrigenDestino.get(0), pointDestinoMapa);
        if (distanciaOrigenDestino > 1100) {
            detectarRutaCercanaPuntoDestino = new boolean[listaRutasPuntosLatLng.size()];

            for (int i = 0; i < listaRutasPuntosLatLng.size(); i++)
                detectarRutaCercanaPuntoDestino[i] = encuentraRuta.encuentraRutaBus(pointDestinoMapa, listaRutasPuntosLatLng.get(i), false, radio, mMap);

            List<LatLng> listPointMasCercanoOrigenRuta = new ArrayList<>();
            List<LatLng> listPointMasCercanoDestiRuta = new ArrayList<>();
            List<Double> listDistanciasOrigenRuta = new ArrayList<>();
            List<Double> listDistanciasRutaDestino = new ArrayList<>();

            for (int i = 0; i < detectarRutaCercanaPuntoDestino.length; i++) {
                if (detectarRutaCercanaPuntoDestino[i] && detectarRutaCercanaPuntoOrigen[i]) {
                    rutaDisponible = true;
                    rutasDisponibleOrigenDestino.add(nombreRutasTodasLineas.get(i));

                    LatLng findPtoOrigenRuta = new EncPuntoCerPoli().findNearestPoint(markerPointsOrigenDestino.get(0), listaRutasPuntosLatLng.get(i));//Encontramos el punto mas cercano Mi Ubi - Poli
                    listPointMasCercanoOrigenRuta.add(findPtoOrigenRuta);

                    double distUbiRuta = encuentraRuta.distanciasEntrePuntos(findPtoOrigenRuta, markerPointsOrigenDestino.get(0));
                    distUbiRuta = Double.parseDouble(new DecimalFormat("##,###").format(distUbiRuta));
                    listDistanciasOrigenRuta.add(distUbiRuta);

                    LatLng findPtoDesRuta = new EncPuntoCerPoli().findNearestPoint(pointDestinoMapa, listaRutasPuntosLatLng.get(i));//Encontramos el punto mas cercano Dest-Poli
                    listPointMasCercanoDestiRuta.add(findPtoDesRuta);

                    double distDestRuta = encuentraRuta.distanciasEntrePuntos(findPtoDesRuta, pointDestinoMapa);
                    distDestRuta = Double.parseDouble(new DecimalFormat("##,###").format(distDestRuta));
                    listDistanciasRutaDestino.add(distDestRuta);
                }
            }

            if (!rutaDisponible)
                noDetectaRuta(pointDestinoMapa, false);
            else {
                new Buses_disponibles().alertLineas(rutasDisponibleOrigenDestino, nombreRutasTodasLineas, this, listDistanciasOrigenRuta, listDistanciasRutaDestino, listPointMasCercanoOrigenRuta, listPointMasCercanoDestiRuta, new Buses_disponibles.OnOklineaSeleccionada() {
                    @Override
                    public void seleccionada(int seleccion, LatLng puntoCercanoATomarElBus, LatLng puntoCercanoBajarElBus) {
                        mostrarRutaSeleccionada(seleccion, puntoCercanoATomarElBus, puntoCercanoBajarElBus);
                    }});
            }
        } else
            noDetectaRuta(pointDestinoMapa, true);
    }

    /**
     * Metodo que detecta si la ubicacion actual esta cerca a alguna ruta
     * @param pointOrigenMapa
     * @param radio
     */
    private void encuentraRutasAlPuntoOrigenOrigen(LatLng pointOrigenMapa, int radio) {

        detectarRutaCercanaPuntoOrigen = new boolean[listaRutasPuntosLatLng.size()];
        if (pointOrigenMapa != null) {
            for (int i = 0; i < listaRutasPuntosLatLng.size(); i++)
                detectarRutaCercanaPuntoOrigen[i] = encuentraRuta.encuentraRutaBus(pointOrigenMapa, listaRutasPuntosLatLng.get(i), false, radio, mMap);

            for (int i = 0; i < detectarRutaCercanaPuntoOrigen.length; i++)
                if (detectarRutaCercanaPuntoOrigen[i]) {
                    origenRutaEncontrado = true;
                    break;
                }
            if (!origenRutaEncontrado)
                noDetectaRuta(pointOrigenMapa, false);
            else {
                if (markerOrigen != null) markerOrigen.remove();
                markerOrigen = colocarMarker.colocarMarcadorPuntoOrigenEnMapa(origen, getString(R.string.ubicacion_Actual), mMap, this);

                if (markerPointsOrigenDestino.size() == 0)
                    markerPointsOrigenDestino.add(origen);
                Toast.makeText(MapsActivity.this, getString(R.string.mensaje_Busqueda), Toast.LENGTH_LONG).show();
            }
        } else
            getDeviceLocation();
    }
}