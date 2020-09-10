package com.ufc.com.carona_ufc.controller.services;


import android.annotation.SuppressLint;
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
import com.ufc.com.carona_ufc.model.Carona;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class DirectionApi implements RoutingListener {
    private List<Polyline> polylines;
    private GoogleMap mMap;
    private TextView tvDistanciaHora;
    private TextView tvHora;
    private Carona carona;

    public DirectionApi(List<Polyline> polylines, GoogleMap mMap, TextView tvDistanciaHora, TextView tvHora, Carona carona) {
        super();
        this.polylines = polylines;
        this.mMap = mMap;
        this.tvDistanciaHora = tvDistanciaHora;
        this.tvHora = tvHora;
        this.carona = carona;
    }

    public void drawRoute(LatLng startPosition, LatLng stopPostion) {
        String keyApi = "AIzaSyDllDHAwx94Zj2viVKPWo4LQiMqh5WtBEE";

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
            Log.i("teste", e.getMessage());
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


            GregorianCalendar calendar = new GregorianCalendar();
            int horaStart, minutoStart;
            horaStart = Integer.parseInt(carona.getHora().substring(0, 2));
            minutoStart = Integer.parseInt(carona.getHora().substring(3, 5));

            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
            Time time = new Time(horaStart, minutoStart, 0);
            calendar.setTimeInMillis(time.getTime());

            int horaStop, minutoStop, segStop, input;
            input = route.get(i).getDurationValue();
            horaStop = input / 3600;
            minutoStop = (input - (horaStop * 3600)) / 60;
            segStop = 0;

            Log.i("dur", "hora: " + horaStop + ":" + minutoStop);


            calendar.add(Calendar.HOUR, horaStop);
            calendar.add(Calendar.MINUTE, minutoStop);

            String horaSomada = sdf2.format(calendar.getTime()).substring(0, 2);
            String minutoSomado = sdf2.format(calendar.getTime()).substring(3, 5);

            String horario = horaSomada + ":" + minutoSomado;
            carona.setHoraChegadaprox(horario);






        }
    }

    @Override
    public void onRoutingCancelled() {

    }


}
