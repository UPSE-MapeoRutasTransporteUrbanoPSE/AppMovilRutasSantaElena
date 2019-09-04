package models;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

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

import entities.Ruta;
import inicio.SplashInicio;

/*CLASE QUE ME DEVUELVE TODAS LAS RUTAS EN UNA LISTA DE TIPO RUTA MODEL*/

public class HttpGetRutas extends AsyncTask<Object, Void, List<Ruta>> {

    public AsyncResponse delegate = null;
    Context context;
    RestTemplate restTemplate = new RestTemplate();
    private String userMovil = "";
    private String passMovil = "";

    private SplashInicio splashInicio;

    public HttpGetRutas(SplashInicio splashInicio) {
        this.splashInicio = splashInicio;
    }

    public HttpGetRutas(AsyncResponse delegate) {
        this.delegate = delegate;
    }

    private MapsActivity mapsActivity;

    public HttpGetRutas(MapsActivity mapsActivity) {
        this.mapsActivity = mapsActivity;
    }


    @Override
    protected List<Ruta> doInBackground(Object... params) {

        context = (Context) params[0];


        userMovil = context.getString(R.string.user_movil);
        passMovil = context.getString(R.string.pass_movil);

        HeadersAuth headersAuth = new HeadersAuth();

        HttpHeaders headers = headersAuth.createHttpHeaders(userMovil, passMovil);

        HttpEntity<String> entity = new HttpEntity<String>(headers);

        try {

           String getUrl = context.getString(R.string.url)+context.getString(R.string.url_rutas_todas);

            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<Ruta[]> responseRutas = restTemplate.exchange(getUrl, HttpMethod.GET,entity, Ruta[].class);
            System.out.println("Result - status (" + responseRutas.getStatusCode() + ") has body: " + responseRutas.hasBody());

            return Arrays.asList(responseRutas.getBody());

        } catch (Exception ex) {
            Log.e("", ex.getMessage());
        }
        return null;

    }

    @Override
    protected void onPostExecute(List<Ruta> ruta) {
        super.onPostExecute(ruta);
        delegate.processFinish(ruta);

        if (ruta!=null)
         System.out.println("rutas " + ruta.size());
    }

    public interface AsyncResponse {
        void processFinish(List<Ruta> ruta);
    }


}
