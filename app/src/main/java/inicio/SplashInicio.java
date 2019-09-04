package inicio;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.WindowManager;

import com.google.android.gms.maps.model.LatLng;
import com.rutasbus.santaelena.app.rutas.MapsActivity;
import com.rutas.santaelena.app.rutas.R;

import java.util.ArrayList;
import java.util.List;

import entities.Ruta;
import lineas.LineAllWayPcercanos;
import models.HttpGetRutas;


public class SplashInicio extends FragmentActivity {
    private final int DURACION_SPLASH = 2000;
    private List<ArrayList<LatLng>> listaRutasLatLng = new ArrayList<>();
    private List<String> nombreLineasTodas = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_inicio);

        new Handler().postDelayed(new Runnable() {
            public void run() {

                Intent intent = new Intent(SplashInicio.this, MapsActivity.class);
                startActivity(intent);
                finish();
            }

        }, DURACION_SPLASH);

    }

    public void getTodasLineasBusesWS(Context context, OnOkRutaNombreCargadas onOkRutaNombreCargadas) {
        AsyncTask<Object, Void, List<Ruta>> httpGetRutas = new HttpGetRutas(new HttpGetRutas.AsyncResponse() {
            @Override
            public void processFinish(List<Ruta> ruta) {
                if (ruta != null) {
                    listaRutasLatLng = new LineAllWayPcercanos().getRecorridoLineas(ruta);
                    nombreLineasTodas = new LineAllWayPcercanos().getNombreLineas(ruta);

                   /* Toast toast = Toast.makeText(context,"Eliga o Busque su destino ", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();*/

                    onOkRutaNombreCargadas.seleccionada(listaRutasLatLng, nombreLineasTodas);

                } else
                    getTodasLineasBusesWS(context, onOkRutaNombreCargadas);

            }
        }).execute(context);
    }

    public interface OnOkRutaNombreCargadas {
        void seleccionada(List<ArrayList<LatLng>> listaRutas, List<String> nombreRutasTodas);
    }
}
