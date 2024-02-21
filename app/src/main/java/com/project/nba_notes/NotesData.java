package com.project.nba_notes;


import com.google.android.gms.maps.model.LatLng;


import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class NotesData {
    private int id;
    private String title;
    private String content;
    private boolean favorite;
    private Date lastModified;

    private double latitude;
    private double longitude;

    public NotesData(JSONObject robot) {
        try {
            this.id = robot.getInt("id");
            this.title = robot.getString("title");
            this.content = robot.getString("content");
            this.favorite = robot.getBoolean("favorite");
            String dateString = robot.getString("lastModified");
            this.latitude = robot.getDouble("latitude");
            this.longitude = robot.getDouble("longitude");

            // Quitar los microsegundos de la cadena de fecha y asumir que es UTC añadiendo 'Z'
            String modifiedDateString = dateString.substring(0, dateString.length() - 3) + "Z";

            // Configurar SimpleDateFormat para manejar la fecha en formato ISO 8601
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            isoFormat.setTimeZone(TimeZone.getTimeZone("UTC")); // Asegúrate de que el objeto Date esté en UTC

            this.lastModified = isoFormat.parse(modifiedDateString);

        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }


    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public LatLng getCords(){ return new LatLng(this.latitude, this.longitude); }
}

