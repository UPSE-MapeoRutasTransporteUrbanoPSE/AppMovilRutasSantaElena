package models;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.rutasbus.santaelena.app.rutas.HeadersAuth;
import com.rutas.santaelena.app.rutas.R;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import entities.Ruta;
import lineas.LineaBus;

/*******CLASE QUE ME DEVUELVE UNA RUTA DE BUS ESPECIFICA ****/

public class HttpGetLinea extends AsyncTask<Object, Void, Ruta> {

    Context context;
    private String userMovil = "";
    private String passMovil = "";
    String linea ="";

    private LineaBus lineaBus;

    public HttpGetLinea(LineaBus lineaBus) {
        this.lineaBus = lineaBus;
    }

    RestTemplate restTemplate = new RestTemplate() ;

    public interface AsyncResponse2 {
        void processFinish(Ruta ruta);
    }

    public AsyncResponse2 delegate = null;

    public HttpGetLinea(AsyncResponse2 delegate){
        this.delegate = delegate;
    }

    @Override
    protected Ruta doInBackground(Object... params) {

        linea = (String) params[0];
        context = (Context) params[1];


        userMovil = context.getString(R.string.user_movil);
        passMovil = context.getString(R.string.pass_movil);

        HeadersAuth headersAuth = new HeadersAuth();

        HttpHeaders headers = headersAuth.createHttpHeaders(userMovil, passMovil);

        HttpEntity<String> entity = new HttpEntity<String>(headers);
        try {
            // final String getUrl = context.getString(R.string.url)+context.getString(R.string.url_rutas_todas);
            final String getUrl = context.getString(R.string.url)+context.getString(R.string.url_linea_bus_individual)+linea;

            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<Ruta> responseRutaUnica = restTemplate.exchange(getUrl, HttpMethod.GET, entity, Ruta.class);
            System.out.println("Result - status (" + responseRutaUnica.getStatusCode() + ") has body: " + responseRutaUnica.hasBody());

            return responseRutaUnica.getBody();

        } catch (Exception ex) {
            Log.e("", ex.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Ruta ruta) {
        super.onPostExecute(ruta);
        delegate.processFinish(ruta);

    }



}
