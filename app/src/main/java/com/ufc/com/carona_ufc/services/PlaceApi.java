package com.ufc.com.carona_ufc.services;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class PlaceApi {
    public ArrayList<String> autoComplete(String input) {
        ArrayList<String> arrayList = new ArrayList();
        HttpURLConnection connection = null;
        StringBuilder jsonResult = new StringBuilder();
        String keyAPI = "AIzaSyAnqFmI-lPuIWYaI6-_1oah3f6aIKc2QQs";
        String linkAPI = "https://maps.googleapis.com/maps/api/place/autocomplete/json?";

        try {
            StringBuilder stringBuilder = new StringBuilder(linkAPI);
            stringBuilder.append("input=" + input);
            stringBuilder.append("&key=" + keyAPI);
            URL url = new URL(stringBuilder.toString());

            connection = (HttpURLConnection) url.openConnection();
            InputStreamReader inputStreamReader = new InputStreamReader(connection.getInputStream());

            int read;

            char[] buff = new char[1024];

            while ((read = inputStreamReader.read(buff)) != -1) {
                jsonResult.append(buff, 0, read);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        try {
            JSONObject jsonObject = new JSONObject(jsonResult.toString());
            JSONArray jsonArray = jsonObject.getJSONArray("predictions");

            for (int i = 0; i < jsonArray.length(); i++) {
                arrayList.add(jsonArray.getJSONObject(i).getString("description"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arrayList;
    }
}
