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

import java.util.Map;

import entities.EstadoBusTemporal;
import sesion.CustomInfoWindowAdapter;

/**
 * CLASE QUE ME DEVUELVE LOS TIEMPOS APROXIMADOS DEL BUS O BUSES A CADA PARADA
 */

public class HttpGetTiempoBuses extends AsyncTask<Object, Void, Map<Void, EstadoBusTemporal>> {

    Context context;
    public AsynGetBusTime delegate = null;
    private CustomInfoWindowAdapter customInfoWindowAdapter;
    RestTemplate restTemplate = new RestTemplate();
    private String userMovil = "";
    private String passMovil = "";
    private String idParada = "";
    private String linea = "";


    public interface AsynGetBusTime {
        void timeBusParadero(Map hashMap);
    }

    public HttpGetTiempoBuses(AsynGetBusTime delegate) {
        this.delegate = delegate;
    }

    public HttpGetTiempoBuses(CustomInfoWindowAdapter customInfoWindowAdapter) {
        this.customInfoWindowAdapter = customInfoWindowAdapter;
    }

    @Override
    protected  Map doInBackground(Object... objects) {

        context = (Context) objects[0];
        idParada = (String) objects[1];
        linea =(String) objects[2];
        userMovil = context.getString(R.string.user_movil);
        passMovil = context.getString(R.string.pass_movil);


        HeadersAuth headersAuth = new HeadersAuth();

        HttpHeaders headers = headersAuth.createHttpHeaders(userMovil, passMovil);

        HttpEntity<String> entity = new HttpEntity<String>(headers);

        try {
            System.out.println("Begin /GET request Restful TimeLLegadaBus!");
            Log.d("WS", "doInBackground: Begin GET request TimeLLegadaBus ");

            final String url = context.getString(R.string.url)+context.getString(R.string.url_time_calculate)+idParada+"/"+linea;

            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            ResponseEntity<Map> tiempo = restTemplate.exchange(url, HttpMethod.GET, entity, Map.class);

          /* Iterator it = tiempo.getBody().entrySet().iterator();
            while (it.hasNext()){
                Map.Entry p = (Map.Entry) it.next();
                System.out.println("Placa "+ p.getKey() + "tiempo "+p.getValue());
                it.remove();
            }*/

            return tiempo.getBody();

        } catch (Exception ex) {
            Log.e("", ex.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(Map hashMap) {
        super.onPostExecute(hashMap);
        delegate.timeBusParadero(hashMap);
    }


}



