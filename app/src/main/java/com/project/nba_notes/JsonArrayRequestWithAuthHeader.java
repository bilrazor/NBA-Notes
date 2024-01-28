package com.project.nba_notes;

import android.content.Context;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

public class JsonArrayRequestWithAuthHeader extends JsonArrayRequest {

    private Context context;

    public JsonArrayRequestWithAuthHeader(int method, String url, JSONArray jsonRequest,
                                          Response.Listener<JSONArray> listener,
                                          @Nullable Response.ErrorListener errorListener,
                                          Context context) { // Añade el contexto aquí
        super(method, url, jsonRequest, listener, errorListener);
        this.context = context; // Asigna el contexto
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        // Recupera el token de SharedPreferences o de donde sea apropiado
        //SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        //String token = sharedPreferences.getString("token", ""); // Usar el token guardado en SharedPreferences
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwZXBlIiwiZXhwIjoxNzA3MDUwODc2LCJpYXQiOjE3MDY0NTA4NzZ9.sNpt2oHxdqNQl0bUCWxsBM39gKQ_RW-eLD9SwKjUl_7_gjxnzmSHdCc5-1U78Onhq3YrPDPb3HSgDwTbiemR7A";
        headers.put("Authorization","Bearer " + token);
        return headers;
    }
}
