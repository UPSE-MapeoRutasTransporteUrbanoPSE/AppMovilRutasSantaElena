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

import entities.Parada;
import lineas.LineaBus;

/*******CLASE QUE ME DEVUELVE UNA RUTA DE BUS ESPECIFICA ****/

public class HttpGetParadasLinea extends AsyncTask<Object, Void, List<Parada>> {

    Context context;
    private String userMovil = "";
    private String passMovil = "";
    private String idParadasLinea;

    private LineaBus lineaBus;

    public HttpGetParadasLinea(LineaBus lineaBus) {
        this.lineaBus = lineaBus;
    }

    RestTemplate restTemplate = new RestTemplate() ;

    public interface AsyncResponse2 {
        void processFinish(List<Parada> paradas);
    }

    public AsyncResponse2 delegate = null;

    public HttpGetParadasLinea(AsyncResponse2 delegate){
        this.delegate = delegate;
    }

    @Override
    protected List<Parada> doInBackground(Object... params) {

        idParadasLinea = (String) params[0];
        context = (Context) params[1];


        userMovil = context.getString(R.string.user_movil);
        passMovil = context.getString(R.string.pass_movil);

        HeadersAuth headersAuth = new HeadersAuth();

        HttpHeaders headers = headersAuth.createHttpHeaders(userMovil, passMovil);

        HttpEntity<String> entity = new HttpEntity<String>(headers);


            try {
                Log.d("WS", "doInBackground: Begin GET request Restful lineas con sus paraderos!");

                final String getUrl = context.getString(R.string.url)+ context.getString(R.string.url_paradas_lineas)+ idParadasLinea;

                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                ResponseEntity<Parada[]> responseParadasLineas = restTemplate.exchange(getUrl, HttpMethod.GET, entity, Parada[].class);
                System.out.println("Result - status (" + responseParadasLineas.getStatusCode() + ") has body: " + responseParadasLineas.hasBody());


                return Arrays.asList(responseParadasLineas.getBody());

            } catch (Exception ex) {
                Log.e("", ex.getMessage());
            }

            return null;

    }

    @Override
    protected void onPostExecute(List<Parada> paradas) {
        super.onPostExecute(paradas);
        delegate.processFinish(paradas);
    }


}
