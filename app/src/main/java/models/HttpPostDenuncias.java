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

import java.util.Calendar;
import java.util.Date;

import denuncias.Denuncias;
import entities.Reporte;

public class HttpPostDenuncias extends AsyncTask<Object,Object, Reporte> {

    private String id_cooperativa;
    private String asunto;
    private String numeroDisco;
    private String ubicacion;
    private Date fecha_del_incidente;
    private String mensaje;
    private String placa;
    private boolean estado;
    private Context context;
    private String idUser;
    private String username;
    private String password;

    private Denuncias denuncias;

    public HttpPostDenuncias(Denuncias denuncias) {
        this.denuncias = denuncias;
    }

    public interface AsynDenuncias{
        void processDenuncia(Reporte reporte);
    }
    public AsynDenuncias delegate = null;

    public HttpPostDenuncias(AsynDenuncias delegate) {
        this.delegate = delegate;
    }

    @Override
    protected Reporte doInBackground(Object... report) {
        context = (Context) report[0];
        asunto =(String) report[1];
        id_cooperativa = (String) report [2];
        numeroDisco = (String)report[3];
        fecha_del_incidente = Calendar.getInstance().getTime();
        placa = (String) report[4];
        mensaje = (String) report[5];
        username = (String) report[6];
        password =(String)report[7];
        estado = true;
        ubicacion = (String)report[8];
        idUser =(String) report[9];
        try
        {

            HeadersAuth headersAuth = new HeadersAuth();

            HttpHeaders headers = headersAuth.createHttpHeaders(username, password);


           // final String url = context.getString(R.string.url_reporte);

            final String url = context.getString(R.string.url)+context.getString(R.string.url_reporte);

            Reporte reporte = new Reporte();

            reporte.setIdUsuario(idUser);
            reporte.setAsunto(asunto);
            reporte.setIdCooperativa(id_cooperativa);
            reporte.setNumeroDisco(numeroDisco);
            reporte.setUbicacion(ubicacion);
            reporte.setFecha(fecha_del_incidente);
            reporte.setPlaca(placa);
            reporte.setMensaje(mensaje);
            reporte.setEstado(estado);



            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<Reporte> entity = new HttpEntity<Reporte>(reporte,headers);

            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            ResponseEntity<Reporte> reporteUsuario = restTemplate.exchange(url, HttpMethod.POST ,entity, Reporte.class);

            return reporteUsuario.getBody();

        }catch (Exception ex) {
            Log.e("", ex.getMessage());
        }

        return null;
    }

    @Override
    protected void onPostExecute(Reporte reporte) {
        super.onPostExecute(reporte);
        delegate.processDenuncia(reporte);
    }
}
