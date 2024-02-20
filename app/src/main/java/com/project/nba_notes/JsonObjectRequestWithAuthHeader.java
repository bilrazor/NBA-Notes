package com.project.nba_notes;


import android.content.Context;
import android.content.SharedPreferences;


import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JsonObjectRequestWithAuthHeader extends JsonObjectRequest {

    private Context context;

    public JsonObjectRequestWithAuthHeader(int method, String url, JSONObject jsonRequest,
                                           Response.Listener<JSONObject> listener,
                                           @Nullable Response.ErrorListener errorListener,

                                           Context context) { // Añade el contexto aquí
        super(method, url, jsonRequest, listener, errorListener);
        this.context = context; // Asigna el contexto

    }
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();

        // Recupera el token de SharedPreferences
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        // String token = sharedPreferences.getString("token", "");
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwZXBlIiwiZXhwIjoxNzA2NTQzNTg2LCJpYXQiOjE3MDU5NDM1ODZ9._h5JyV6SkacQVe81l14mgnj2dtpPKAheYPe07q3xzLkOhRAnDYMTynQT4W66W6q1CB9VQNVtHV6isQCIwsSpuA";

        headers.put("Authorization","Bearer " + token);
        return headers;
    }
}
