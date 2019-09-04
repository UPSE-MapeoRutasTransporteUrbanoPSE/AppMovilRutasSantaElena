package com.rutasbus.santaelena.app.rutas;

import android.content.Context;
import android.graphics.Color;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import detectaRuta.GetRunAPie;
import detectaRuta.Marcador;
import netDisponible.DisponibleNet;


public class MostrarRutaSeleccion {
    private Marker markerDestino, markerTomarBus;
    private DisponibleNet disponibleNet = new DisponibleNet();
    private boolean conectaNet;
    private GetRunAPie getRunAPie = new GetRunAPie();

    /**
     * Metodo que muestra el recorrido a pie y la ruta seleccionada
     * @param seleccion
     * @param context
     * @param mMap
     * @param miUbicacion
     * @param findPtoOrigenRuta
     * @param findPtoDesRuta
     * @param miDestino
     * @param listaRutas
     */
    public void mostrarRutaSeleccionada(int seleccion, Context context, GoogleMap mMap, LatLng miUbicacion, LatLng findPtoOrigenRuta,
                                                LatLng findPtoDesRuta, LatLng miDestino, List<ArrayList<LatLng>> listaRutas) {

        conectaNet = disponibleNet.compruebaConexion(context);
        if (conectaNet) {//Solo si hay connexion a internet mostrara el recorrido entre los puntos

            getRunAPie.getRecorridoAPie("walking", miUbicacion, findPtoOrigenRuta, mMap, context);

            getRunAPie.getRecorridoAPie("walking", findPtoDesRuta, miDestino, mMap, context);

        } else { //caso contrario solo ubicara los marcadores si no hay conexion a net sea wifi o datos

            markerTomarBus = new Marcador().colocarMarcadorRutaBusMasCercanaOrigen(findPtoOrigenRuta, "", mMap,context);

            markerDestino = new Marcador().colocarMarcadorRecorridoaPieDestino(miDestino, "", mMap,context); //este estaba
            markerDestino.showInfoWindow();
        }
        dibujaRutaSeleccionada(Collections.singletonList(listaRutas.get(seleccion)), mMap);  //MOSTRAMOS LA RUTA SELECCIONADA


    }

    /**
     * Metodo que dibuja la ruta seleccioanda
     * @param listaRutasTodas
     * @param mMap
     */
    private void dibujaRutaSeleccionada(List<ArrayList<LatLng>> listaRutasTodas, GoogleMap mMap) {
        for (int i = 0; i < listaRutasTodas.size(); i++)
            mMap.addPolyline(new PolylineOptions().addAll(listaRutasTodas.get(i)).width(3).color(Color.BLUE));
    }


}
