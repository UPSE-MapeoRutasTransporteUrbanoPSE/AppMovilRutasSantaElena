package models;

import android.content.Context;
import android.os.AsyncTask;

import com.rutasbus.santaelena.app.rutas.HeadersAuth;
import com.rutas.santaelena.app.rutas.R;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import entities.SegUsuario;
import sesion.IniciaSesion;
import sesion.RegistraUsuario;

/**
 * Clase que me trae todos los usuarios registrados en la base de datos COUCHBASE
 */
public class HttpGetUsers extends AsyncTask<Object, Void, SegUsuario> {
    Context context;

    private IniciaSesion iniciaSesion;

    private RegistraUsuario registraUsuario;

    public HttpGetUsers(IniciaSesion iniciaSesion) {
        this.iniciaSesion = iniciaSesion;
    }

    public HttpGetUsers(RegistraUsuario registraUsuario) {
        this.registraUsuario = registraUsuario;
    }

    public interface AsyncResponse {
        void processFinish(SegUsuario users);
    }

     public AsyncResponse delegate = null;

    public HttpGetUsers(AsyncResponse delegate) {
        this.delegate = delegate;
    }


    @Override
    protected SegUsuario doInBackground(Object... objects) {


        String user = (String) objects[0];
        String clave = (String) objects[1];
        context = (Context) objects[2];

       final String theUrl = context.getString(R.string.url)+context.getString(R.string.url_usuarios)+user+"/"+clave;

        RestTemplate restTemplate = new RestTemplate();

        try {
            HeadersAuth headersAuth = new HeadersAuth();

            HttpHeaders headers = headersAuth.createHttpHeaders(user, clave);

            HttpEntity<String> entity = new HttpEntity<String>(headers);

            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<SegUsuario> responseSegUser = restTemplate.exchange(theUrl, HttpMethod.GET, entity, SegUsuario.class);
            System.out.println("Result - status (" + responseSegUser.getStatusCode() + ") has body: " + responseSegUser.hasBody());

            return responseSegUser.getBody();

        }catch (Exception eek) {

            System.out.println("** Exception: " + eek.getMessage());

        }
        return null;
    }

    @Override
    protected void onPostExecute(SegUsuario Users) {
        super.onPostExecute(Users);
        delegate.processFinish(Users);

    }
}
