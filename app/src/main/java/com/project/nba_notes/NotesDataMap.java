package com.project.nba_notes;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class NotesDataMap {
    private int id;
    private String title;
    private String content;
    private boolean favorite;
    private Date lastModified;

    private double latitude;
    private double longitude;

    public NotesDataMap(int id, String title, double latitude, double longitude) {
        this.id = id;
        this.title = title;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public NotesDataMap(JSONObject robot) {
        try {
            this.id = robot.getInt("id");
            this.title = robot.getString("title");

            this.latitude = robot.getDouble("latitude");
            this.longitude = robot.getDouble("longitude");

        } catch (JSONException e) {
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

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public LatLng getCords(){ return new LatLng(this.latitude, this.longitude); }
}
