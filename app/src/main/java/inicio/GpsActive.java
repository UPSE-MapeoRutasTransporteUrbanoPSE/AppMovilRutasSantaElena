package inicio;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

public class GpsActive extends AppCompatActivity{
    private AlertDialog alert;


    /*  private boolean isGPSEnabled (Context mContext){
        LocationManager locationManager = (LocationManager)
                mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

    }



    public void msm(Context context) {
      boolean  activeGPS = isGPSEnabled(context);
        if (!activeGPS)
            Toast.makeText(context, "Active el GPS", Toast.LENGTH_LONG).show();
    }
*/

    public void AlertNoGps(Context context) {

         AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("El sistema GPS esta desactivado, ¿Desea activarlo?")
                .setCancelable(false)
                .setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                        alert.dismiss();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                        alert.dismiss();
                    }
                });
        alert = builder.create();
        alert.show();
    }
}
