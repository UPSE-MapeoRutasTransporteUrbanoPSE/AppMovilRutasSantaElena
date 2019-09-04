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

import java.util.Arrays;
import java.util.List;

import entities.EstadoBusTemporal;
import lineas.AllBusesCirculando;

/**
 * CLASE QUE ME DEVUELVE LA ULTIMA POSICION DEL BUS
 */
public class HttpGetAllbus extends AsyncTask<Object, Void, List<EstadoBusTemporal>> {


    public Posicionbus delegate = null;
    Context context;

    private String userMovil = "";
    private String passMovil = "";
    RestTemplate restTemplate = new RestTemplate();

    private AllBusesCirculando allBusesCirculando;

    public HttpGetAllbus(AllBusesCirculando allBusesCirculando) {
        this.allBusesCirculando = allBusesCirculando;
    }

    public HttpGetAllbus(Posicionbus delegate) {
        this.delegate = delegate;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected List<EstadoBusTemporal> doInBackground(Object... params) {

        context = (Context) params[0];
        userMovil = context.getString(R.string.user_movil);
        passMovil = context.getString(R.string.pass_movil);

        HeadersAuth headersAuth = new HeadersAuth();

        HttpHeaders headers = headersAuth.createHttpHeaders(userMovil, passMovil);

        HttpEntity<String> entity = new HttpEntity<String>(headers);

        try {

            System.out.println("Begin /GET request Restful todos los buses en el mapa!");
            Log.d("WS", "doInBackground: Begin GET request Restful bus mapa!");
            final String getUrl = context.getString(R.string.url)+context.getString(R.string.url_buses_todos);

            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            ResponseEntity<EstadoBusTemporal[]> estadoBus = restTemplate.exchange(getUrl, HttpMethod.GET, entity, EstadoBusTemporal[].class);

            return Arrays.asList(estadoBus.getBody());

        } catch (Exception ex) {
            Log.e("", ex.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<EstadoBusTemporal> estadoBus) {
        super.onPostExecute(estadoBus);
        delegate.busPosicion(estadoBus);
        if (estadoBus!=null)
            System.out.println("numero de buses : " + estadoBus.size());
    }

    public interface Posicionbus {
        void busPosicion(List<EstadoBusTemporal> estadoBus);
    }

}
