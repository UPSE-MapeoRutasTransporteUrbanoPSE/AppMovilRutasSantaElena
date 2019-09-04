package com.rutasbus.santaelena.app.rutas;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;


import java.util.List;

/**
 * Created by Javier on 08/12/2017.
 * DEVUELVE EL PUNTO MAS CERCANO A LA POLILINEA
 * Solucion basada en la respuesta de : Ver Link de abajo
 *
 * https://stackoverflow.com/questions/36104809/find-the-closest-point-on-polygon-to-user-location
 *
 */

public class EncPuntoCerPoli {

    //TODO BUSCAR URL METODO
    /**
     * Based on `distanceToLine` method from
     * https://github.com/googlemaps/android-maps-utils/blob/master/library/src/com/google/maps/android/PolyUtil.java
     */

    /**
     *
     * @param punto latlng
     * @param puntos ruta
     * @return El punto latlong mas cercano a la ruta en base al punto
     */
    public LatLng findNearestPoint(LatLng punto, List<LatLng> puntos) {
        double distance = -1;
        LatLng minimumDistancePoint = punto;

        if (punto == null || puntos == null) {
            return minimumDistancePoint;
        }

        for (int i = 0; i < puntos.size(); i++) {
            LatLng point = puntos.get(i);

            int segmentPoint = i + 1;
            if (segmentPoint >= puntos.size()) {
                segmentPoint = 0;
            }

            double currentDistance = PolyUtil.distanceToLine(punto, point, puntos.get(segmentPoint));
            if (distance == -1 || currentDistance < distance) {
                distance = currentDistance;
                minimumDistancePoint = findNearestPoint(punto, point, puntos.get(segmentPoint));

            }
}
        return minimumDistancePoint;
    }

    public LatLng findNearestPoint(final LatLng p, final LatLng start, final LatLng end) {
        if (start.equals(end)) {
            return start;
        }

        final double s0lat = Math.toRadians(p.latitude);
        final double s0lng = Math.toRadians(p.longitude);
        final double s1lat = Math.toRadians(start.latitude);
        final double s1lng = Math.toRadians(start.longitude);
        final double s2lat = Math.toRadians(end.latitude);
        final double s2lng = Math.toRadians(end.longitude);

        double s2s1lat = s2lat - s1lat;
        double s2s1lng = s2lng - s1lng;
        final double u = ((s0lat - s1lat) * s2s1lat + (s0lng - s1lng) * s2s1lng)
                / (s2s1lat * s2s1lat + s2s1lng * s2s1lng);
        if (u <= 0) {
            return start;
        }
        if (u >= 1) {
            return end;
        }

        return new LatLng(start.latitude + (u * (end.latitude - start.latitude)),
                start.longitude + (u * (end.longitude - start.longitude)));

    }
}
