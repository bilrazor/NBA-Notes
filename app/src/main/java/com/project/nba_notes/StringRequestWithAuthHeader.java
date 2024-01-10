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
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwZXBlIiwiZXhwIjoxNzA1NDkyNzE2LCJpYXQiOjE3MDQ4OTI3MTZ9.pTpixkyu_txQP_73eAk7BczfFKq0TKJqFK5J7ubWzslLvlwkr3MB-Z6G6lWlsZgYE6vhP5PC3x1vkrxZOBgbyw";

        headers.put("Authorization", "Bearer " + token);
        return headers;
    }
}
