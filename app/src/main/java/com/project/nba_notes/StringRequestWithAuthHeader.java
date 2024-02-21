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
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwZXBlIiwiZXhwIjoxNzA5MTExNzYyLCJpYXQiOjE3MDg1MTE3NjJ9.2ZemmJCSqbmMbgy0QqWqhYoFDfH8xmOqZ5mbDcNzxfRwmcOLSCIdKqIpV-A1i6DCLzbu3nhN8Teiw2bFIazDNA";

        headers.put("Authorization", "Bearer " + token);
        return headers;
    }
}
