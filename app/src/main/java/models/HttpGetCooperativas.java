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
import entities.Cooperativa;

public class HttpGetCooperativas extends AsyncTask<Object,Void,List<Cooperativa>> {

    private Denuncias denuncias;

    private String userMovil = "";
    private String passMovil = "";

    public AsynGetCooperativas delegate = null;
    private Context context;

    RestTemplate restTemplate = new RestTemplate();

    public HttpGetCooperativas(Denuncias denuncias) {
        this.denuncias = denuncias;
    }

    public HttpGetCooperativas(AsynGetCooperativas delegate) {
        this.delegate = delegate;
    }

    @Override
    protected List<Cooperativa> doInBackground(Object... objects) {


        context = (Context) objects[0];

        userMovil = context.getString(R.string.user_movil);
        passMovil = context.getString(R.string.pass_movil);

        HeadersAuth headersAuth = new HeadersAuth();

        HttpHeaders headers = headersAuth.createHttpHeaders(userMovil, passMovil);

        HttpEntity<String> entity = new HttpEntity<String>(headers);


        try {
            System.out.println("Begin /GET request Restful Cooperativas!");
            Log.d("WS", "doInBackground: Begin GET request Cooperativas!");

           final String url = context.getString(R.string.url)+ context.getString(R.string.url_cooperativas);
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            ResponseEntity<Cooperativa[]> cooperativas = restTemplate.exchange(url, HttpMethod.GET, entity, Cooperativa[].class);

            return Arrays.asList(cooperativas.getBody());

        } catch (Exception ex) {
            Log.e("", ex.getMessage());
        }
        return null;
    }

    @Override
    protected void onPostExecute(List<Cooperativa> cooperativas) {
        super.onPostExecute(cooperativas);
        delegate.cooperativas(cooperativas);
        if (cooperativas!=null)
            System.out.println("Cooperativas " + cooperativas.toString());
    }

    public interface AsynGetCooperativas {
        void cooperativas(List<Cooperativa> cooperativas);
    }
}
