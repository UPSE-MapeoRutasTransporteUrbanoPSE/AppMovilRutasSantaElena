package inicio;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
//import android.util.Log;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AlertDialog;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.rutas.santaelena.app.rutas.R;

import com.rutasbus.santaelena.app.rutas.MapsActivity;

public class Splashscreen extends FragmentActivity {
    ProgressBar bar;
    TextView txt;
    private final int DURACION_SPLASH = 4000;
    private AlertDialog alert;
    boolean activo = false;
    boolean constataGPS ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        bar = (ProgressBar) findViewById(R.id.ProgressBar01);
        txt = (TextView) findViewById(R.id.txtrere);

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
            AlertNoGps();
        else {
            new Handler().postDelayed(new Runnable() {
                public void run() {

                    Intent intent = new Intent(Splashscreen.this, MapsActivity.class);
                    startActivity(intent);
                    Splashscreen.this.finish();
                }

            }, DURACION_SPLASH);
        }
    }

    private void AlertNoGps() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.activar_Gps))
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                         activo = true;

                        new Handler().postDelayed(new Runnable() {
                            public void run() {

                                Intent intent = new Intent(Splashscreen.this, MapsActivity.class);
                                startActivity(intent);
                                Splashscreen.this.finish();
                            }

                        }, DURACION_SPLASH);

                         alert.dismiss();
                    }
                });


        alert = builder.create();

        alert.show();



    }


}