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

import denuncias.Denuncias;

/**
 * CLASE QUE ME DEVUELVE LOS TIPOS DE DENUNCIS QUE SON CARGADOS EN EL FORMULARIO DE DENUNCIAS
 */

public class HttpGetAsuntos extends AsyncTask<Object, Void, List<String>> {

    public AsynGetAsuntos delegate = null;
    Context context;
    RestTemplate restTemplate = new RestTemplate();
    private String userMovil = "";
    private String passMovil = "";
    private Denuncias denuncias;

    public HttpGetAsuntos(Denuncias denuncias) {
        this.denuncias = denuncias;
    }

    public HttpGetAsuntos(AsynGetAsuntos delegate) {
        this.delegate = delegate;
    }

    @Override
    protected List<String> doInBackground(Object... params) {

        context = (Context) params[0];

        userMovil = context.getString(R.string.user_movil);
        passMovil = context.getString(R.string.pass_movil);

        HeadersAuth headersAuth = new HeadersAuth();

        HttpHeaders headers = headersAuth.createHttpHeaders(userMovil, passMovil);

        HttpEntity<String> entity = new HttpEntity<String>(headers);

        try {
            System.out.println("Begin /GET request Restful Asuntos!");
            Log.d("WS", "doInBackground: Begin GET request tipos de Asuntos!");

            final String url = context.getString(R.string.url)+context.getString(R.string.url_asunto);

            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            ResponseEntity<String[]> asuntos = restTemplate.exchange(url, HttpMethod.GET, entity, String[].class);

            return Arrays.asList(asuntos.getBody());


        } catch (Exception ex) {
            Log.e("", ex.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<String> asuntos) {
        super.onPostExecute(asuntos);
        delegate.asuntos(asuntos);
        if (asuntos!=null)
            System.out.println("ASUNTOS SPINNER :  " + asuntos.toString());
    }

    public interface AsynGetAsuntos {
        void asuntos(List<String> asuntos);
    }
}
