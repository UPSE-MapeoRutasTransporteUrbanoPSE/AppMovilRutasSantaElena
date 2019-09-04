package models;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.rutasbus.santaelena.app.rutas.HeadersAuth;
import com.rutasbus.santaelena.app.rutas.MapsActivity;
import com.rutas.santaelena.app.rutas.R;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import entities.Parada;
import placesNearPoint.SearchPlacesParaderosNear;

/* CLASE QUE ME DEVUELVE UNA LISTA DE LOS PARADEROS CERCANOS A UN PUNTO DADO EN EL MAPA*/

public class HttpGetParadasCercanas extends AsyncTask<Object,Void, List<Parada>> {
    Context context;
    String userMovil = "";
    String passMovil = "";

    private MapsActivity mapsActivity;

    public HttpGetParadasCercanas(MapsActivity mapsActivity) {
        this.mapsActivity = mapsActivity;
    }

    private SearchPlacesParaderosNear searchPlacesParaderosNear;

    public HttpGetParadasCercanas(SearchPlacesParaderosNear searchPlacesParaderosNear) {
        this.searchPlacesParaderosNear = searchPlacesParaderosNear;
    }

    RestTemplate restTemplate = new RestTemplate();

    public  interface  AsynParadas {
        void paradas(List<Parada> paradas);
    }

    public AsynParadas delegate = null;

    public HttpGetParadasCercanas(AsynParadas delegate){
        this.delegate = delegate;
    }


    @Override
    protected List<Parada> doInBackground(Object... params) {

        String linea = (String) params[0];
        int radio = (int) params[1];
        double r = (double) radio / 1000;
        System.out.println("radio Respose " + radio);
        LatLng loc = (LatLng) params[2];
        context = (Context) params[3];

        userMovil = context.getString(R.string.user_movil);
        passMovil = context.getString(R.string.pass_movil);

        HeadersAuth headersAuth = new HeadersAuth();

        HttpHeaders headers = headersAuth.createHttpHeaders(userMovil, passMovil);

        HttpEntity<String> entity = new HttpEntity<String>(headers);

        try {

            System.out.println("Begin /GET request Restful paradas cercanas!");
            Log.d("WS", "doInBackground: Begin GET request paradas cercanas!");
            final String url = context.getString(R.string.url)+"paradas/"+ linea + "/radio/" + r + "/point/x=" + loc.longitude + ",y=" + loc.latitude;

            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            ResponseEntity<Parada[]> paradas = restTemplate.exchange(url, HttpMethod.GET, entity, Parada[].class);

            return Arrays.asList(paradas.getBody());

        } catch (Exception ex) {
            Log.e("", ex.getMessage());
        }
        return null;
    }


    @Override
    protected void onPostExecute( List<Parada> paradas) {
        super.onPostExecute(paradas);
        delegate.paradas(paradas);
        if (paradas!=null)
            System.out.println("PAREDROS  " + paradas.size());

    }


}







