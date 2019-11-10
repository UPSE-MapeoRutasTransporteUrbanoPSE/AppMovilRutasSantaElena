package detectaRuta;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rutas.santaelena.app.rutas.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Marcador extends FragmentActivity {

    String nameCalle;
    BitmapDescriptor icon;
    Marker markerOrigen;
    Marker markerRutaBusMasCercanaOrigen;
    Marker markerRutaBusMasCercanaDestino;
    Marker markerDestino;
    List<Address> addresses = null;

    public Marker colocarMarcador(LatLng PuntoLatLong, String title, BitmapDescriptor icon, GoogleMap mMap, Context context) {

        nameCalle = getNombreCalles(PuntoLatLong, context);
        Marker tempMarker = mMap.addMarker(new MarkerOptions()
                .position(PuntoLatLong)
                .title(title)
                .snippet(nameCalle)
                .draggable(true)
                .icon(icon));

        return tempMarker;
    }

    public Marker colocarParaderosRutaBus(LatLng PuntoLatLong, String title, GoogleMap mMap, Context context) {
        icon = BitmapDescriptorFactory.fromResource(R.drawable.stopbus);
        //icon = BitmapDescriptorFactory.fromResource(R.drawable.paradadeautobus);

        Marker markerFinBus;
        markerFinBus = colocarMarcador(PuntoLatLong, title, icon, mMap, context);

        return markerFinBus;
    }


    public Marker colocarMarcadorRutaBusMasCercanaDetino(LatLng PuntoLatLong, String title, GoogleMap mMap, Context context) {

        icon = BitmapDescriptorFactory.fromResource(R.drawable.llegadabus);

        Marker markerFinBus;
        markerFinBus = colocarMarcador(PuntoLatLong, title, icon, mMap, context);
        CameraUpdate orig = CameraUpdateFactory.newLatLngZoom(PuntoLatLong, 17f);
        // mMap.animateCamera(orig);

        return markerFinBus;
    }

    public Marker colocarMarcadorPuntoOrigenEnMapa(LatLng PuntoLatLong, String title, GoogleMap mMap, Context context) {

        icon = BitmapDescriptorFactory.fromResource(R.drawable.ubicacion);
        if (markerOrigen != null) markerOrigen.remove();

        markerOrigen = colocarMarcador(PuntoLatLong, title, icon, mMap, context);
        CameraUpdate orig = CameraUpdateFactory.newLatLngZoom(PuntoLatLong, 17f);
        mMap.animateCamera(orig);
        markerOrigen.showInfoWindow();

        return markerOrigen;
    }

    public Marker colocarMarcadorRutaBusMasCercanaOrigen(LatLng PuntoLatLong, String title, GoogleMap mMap, Context context) {
        //coloca el marcador donde deberia escoger y bajarse del bus
        icon = BitmapDescriptorFactory.fromResource(R.drawable.paradadeautobus);
        if (markerRutaBusMasCercanaOrigen != null) markerRutaBusMasCercanaOrigen.remove();

        markerRutaBusMasCercanaOrigen = colocarMarcador(PuntoLatLong, title, icon, mMap, context);
        CameraUpdate orig = CameraUpdateFactory.newLatLngZoom(PuntoLatLong, 17f);
        mMap.animateCamera(orig);
        return markerRutaBusMasCercanaOrigen;
    }

    public Marker colocarMarcadorRecorridoaPieDestino(LatLng PuntoLatLong, String title, GoogleMap mMap, Context context) {
        //coloca el marcador donde deberia escoger y bajarse del bus
        icon = BitmapDescriptorFactory.fromResource(R.drawable.paradadeautobus);
        if (markerRutaBusMasCercanaDestino != null) markerRutaBusMasCercanaDestino.remove();

        markerRutaBusMasCercanaDestino = colocarMarcador(PuntoLatLong, title, icon, mMap, context);
        CameraUpdate orig = CameraUpdateFactory.newLatLngZoom(PuntoLatLong, 17f);
        mMap.animateCamera(orig);
        markerRutaBusMasCercanaDestino.showInfoWindow();
        return markerRutaBusMasCercanaDestino;
    }

    public Marker colocarMarcadorSitiod(LatLng PuntoLatLong, String title, GoogleMap mMap, Context context) {
        //coloca el marcador donde no hay alguna ruta o estan cercanas
        icon = BitmapDescriptorFactory.fromResource(R.drawable.markercity);
       // icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE);

        markerDestino = colocarMarcador(PuntoLatLong, title, icon, mMap, context);

        return markerDestino;

    }

    public Marker colocarMarcadorBusesRutadelMapa(LatLng PuntoLatLong, String title, GoogleMap mMap, Context context) {
        //coloca el marcador donde deberia escoger y bajarse del bus
        icon = BitmapDescriptorFactory.fromResource(R.drawable.trolebus);
        Marker markerRutaBus;

        markerRutaBus = colocarMarcador(PuntoLatLong, title, icon, mMap, context);

        return markerRutaBus;
    }

    public Marker colocarMarcadorBusesRutadelMapaHistortial(LatLng PuntoLatLong, String title, GoogleMap mMap, Context context) {
        //coloca el marcador donde deberia escoger y bajarse del bus
        icon = BitmapDescriptorFactory.fromResource(R.drawable.avancebus);
        Marker markerRutaBus;

        markerRutaBus = colocarMarcador(PuntoLatLong, title, icon, mMap, context);

        return markerRutaBus;
    }

    public String getNombreCalles(LatLng street, Context context) { //Metodo que me devuelve el nombre de la calle
        String calle = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(street.latitude, street.longitude, 1);
            if (addresses.size() == 0)
                calle = "No hay Calles";
            else
                calle = (addresses.get(0).getAddressLine(0));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return calle;
    }

    public Marker parderosCercanos(LatLng PuntoLatLong, String title, String id_marker, BitmapDescriptor icon, GoogleMap mMap, Context context) {

        Marker busPosicionParaderosSitios = mMap.addMarker(new MarkerOptions()
                .position(PuntoLatLong)
                .title(title)
                .snippet(id_marker)
                .draggable(true)
                .icon(icon));
        busPosicionParaderosSitios.setTag("TAG_WAYPOINT");
       /* CameraUpdate orig = CameraUpdateFactory.newLatLngZoom(PuntoLatLong, 17f);
        mMap.animateCamera(orig);*/
        return busPosicionParaderosSitios;
    }

    public void msmOrigenDestino(List<LatLng> markerPointsOrigenDestino, double distanciaOrigenDestino, GoogleMap mMap, Context context) { //ELIMINA UN LATLONG SI NO ESTA DENTRO DEL ALCNCE DE UNA RUTA 1000MTS

        if (markerPointsOrigenDestino.size()==0)
            Toast.makeText(context, "No se detecto ninguna ruta desde tu ubicacion actual ", Toast.LENGTH_SHORT).show();

        if (markerPointsOrigenDestino.size() == 1) {
            Toast.makeText(context, "No se Encontro ninguna ruta Seleccione otro punto ", Toast.LENGTH_SHORT).show();
            colocarMarcadorPuntoOrigenEnMapa(markerPointsOrigenDestino.get(0), "No hay rutas", mMap, context);

        } else if (markerPointsOrigenDestino.size() == 2 && distanciaOrigenDestino < 1000)
            Toast.makeText(context, "Estas a menos de 1km de tu destino, camina o Selecciona otro punto ", Toast.LENGTH_LONG).show();

        else if (markerPointsOrigenDestino.size() == 2)
            Toast.makeText(context, "No se Encontro ninguna ruta Seleccione otro punto ", Toast.LENGTH_LONG).show();
    }


}