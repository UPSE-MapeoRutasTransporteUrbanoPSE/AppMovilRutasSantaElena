package lineas;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.rutas.santaelena.app.rutas.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import denuncias.AbstractAsyncActivity;
import detectaRuta.Marcador;
import entities.Parada;
import entities.Point;
import entities.Ruta;
import models.HttpGetLinea;
import models.HttpGetParadasLinea;

public class LineaBus extends AppCompatActivity implements OnMapReadyCallback {
    //CLASE QUE MUESTRA EN EL MAPA LA RUTA Y SUS PARADAS DEL BUS SELECCIONADO  EN EL MENU
    String lineaBus = null;
    private GoogleMap mMap;
    private boolean verdadero = false;
    private FloatingActionButton verParaderos,verSitiosConcurridos;
    List<Point> listPuntos;
    private ArrayList<LatLng> listaPuntoosPardaderos = new ArrayList<>();
    private ArrayList<LatLng> listaPuntosRuta = new ArrayList<>();
    private Marcador marcador = new Marcador();

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lineas_buses);

        Bundle extras = getIntent().getExtras();
        if (extras != null)
            lineaBus = extras.getString("linea");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        verParaderos = (FloatingActionButton) findViewById(R.id.idFltVerParaderos);
        verSitiosConcurridos =(FloatingActionButton) findViewById(R.id.idFltverSitiosConcurridos);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);

        consultaLinea();

        LatLng SantaElena = new LatLng(-2.228228, -80.898366);
        CameraUpdate orig = CameraUpdateFactory.newLatLngZoom(SantaElena, 13f);
        mMap.animateCamera(orig);

        verParaderos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!verdadero)
                    paraderosWpt();


            }
        });

        verSitiosConcurridos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SitiosConcurridos().sitiosConcurridos(mMap, getApplicationContext());
            }
        });

    }


    public void consultaLinea() {

        AsyncTask<Object, Void, Ruta> httpLineas = new HttpGetLinea(new HttpGetLinea.AsyncResponse2() {
            @Override
            public void processFinish(Ruta rutaModel) {
                if (rutaModel != null) {
                    listPuntos = rutaModel.getListasPuntos();
                    polilineaRestful(listPuntos);
                } else
                    Toast.makeText(getApplicationContext(), getString(R.string.ruta_noDisponible), Toast.LENGTH_SHORT).show();
            }
        }).execute(lineaBus, this);

    }

    public void polilineaRestful(List<Point> listasPuntos) {
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        for (int i = 0; i < listasPuntos.size(); i++) {

            double latitude = listasPuntos.get(i).getY();
            double longitude = listasPuntos.get(i).getX();

            LatLng punto = new LatLng(latitude, longitude);
            listaPuntosRuta.add(punto);
        }
        mMap.addPolyline(new PolylineOptions().addAll(listaPuntosRuta).width(5).color(color));
        Toast.makeText(getApplicationContext(), getString(R.string.mostrarRuta) + " " + lineaBus, Toast.LENGTH_SHORT).show();

    }

    private void paraderosWpt() {

        AsyncTask<Object, Void, List<Parada>> httpGetParadasLinea = new HttpGetParadasLinea(new HttpGetParadasLinea.AsyncResponse2() {
            @Override
            public void processFinish(List<Parada> paradas) {
                if (paradas != null) {
                    getCoordenadas(paradas);
                    verdadero = true;
                } else
                    Toast.makeText(getApplicationContext(), "No dispobibles", Toast.LENGTH_LONG).show();
            }
        }).execute(lineaBus, this);

    }

    private void getCoordenadas(List<Parada> paradas) {

        for (int i = 0; i < paradas.size(); i++) {

            double latitude = paradas.get(i).getCoordenada().getY();
            double longitude = paradas.get(i).getCoordenada().getX();

            LatLng punto = new LatLng(latitude, longitude);
            listaPuntoosPardaderos.add(punto);

        }
        dibujaParaderos();
    }

    private void dibujaParaderos() {
        for (int i = 0; i < listaPuntoosPardaderos.size(); i++)
            marcador.colocarParaderosRutaBus(listaPuntoosPardaderos.get(i), getString(R.string.paraderoLinea) + lineaBus, mMap, this);

    }

}

