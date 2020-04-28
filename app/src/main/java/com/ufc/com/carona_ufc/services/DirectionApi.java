package com.ufc.com.carona_ufc.services;


import android.graphics.Color;
import android.util.Log;
import android.widget.TextView;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class DirectionApi implements RoutingListener {
    private List<Polyline> polylines;
    private GoogleMap mMap;
    private TextView tvDistanciaHora;
    private TextView tvHora;

    public DirectionApi(List<Polyline> polylines, GoogleMap mMap, TextView tvDistanciaHora, TextView tvHora) {
        super();
        this.polylines = polylines;
        this.mMap = mMap;
        this.tvDistanciaHora = tvDistanciaHora;
        this.tvHora = tvHora;
    }

    public void drawRoute(LatLng startPosition, LatLng stopPostion) {
        String keyApi = "AIzaSyDc4aPCYe_WQ2trzMKMxF0FvQKugjIwrBM";

        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(false)
                .waypoints(startPosition, stopPostion)
                .key(keyApi)
                .build();
        routing.execute();

    }

    @Override
    public void onRoutingFailure(RouteException e) {
        //The Routing request failed
        if (e != null) {
            Log.i("error", e.getMessage());
        }
    }

    @Override
    public void onRoutingStart() {

    }

    //desenhar a rota
    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }
        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i < route.size(); i++) {
            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(Color.rgb(153, 50, 204));
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);
            tvDistanciaHora.setText(route.get(i).getDistanceText());
            tvHora.setText(route.get(i).getDurationText());
        }
    }

    @Override
    public void onRoutingCancelled() {

    }


}
