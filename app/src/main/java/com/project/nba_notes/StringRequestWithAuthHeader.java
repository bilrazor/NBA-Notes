package com.project.nba_notes;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class StringRequestWithAuthHeader extends StringRequest {

    private Context context;

    public StringRequestWithAuthHeader(int method, String url, Response.Listener<String> listener,
                                       @Nullable Response.ErrorListener errorListener, Context context) {
        super(method, url, listener, errorListener);
        this.context = context;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        // Recupera el token de SharedPreferences o de donde lo tengas almacenado
        //SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
       // String token = sharedPreferences.getString("token", ""); // Asegúrate de descomentar esta línea y manejar la obtención del token correctamente
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwZXBlIiwiZXhwIjoxNzA2NTQzNTg2LCJpYXQiOjE3MDU5NDM1ODZ9._h5JyV6SkacQVe81l14mgnj2dtpPKAheYPe07q3xzLkOhRAnDYMTynQT4W66W6q1CB9VQNVtHV6isQCIwsSpuA";

        headers.put("Authorization", "Bearer " + token);
        return headers;
    }
}
