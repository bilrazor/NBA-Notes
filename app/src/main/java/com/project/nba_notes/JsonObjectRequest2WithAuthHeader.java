package com.project.nba_notes;


import android.content.Context;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JsonObjectRequest2WithAuthHeader extends JsonObjectRequest {

    private Context context;

    public JsonObjectRequest2WithAuthHeader(int method, String url, JSONObject jsonRequest,
                                            Response.Listener<JSONObject> listener,
                                            @Nullable Response.ErrorListener errorListener,
                                            Context context) {
        super(method, url, jsonRequest, listener, errorListener);
        this.context = context;
    }
    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        // Recupera el token de SharedPreferences o de donde sea apropiado
        //SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        //String token = sharedPreferences.getString("token", ""); // Usar el token guardado en SharedPreferences
        String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwZXBlIiwiZXhwIjoxNzA1MzA2NzY4LCJpYXQiOjE3MDQ3MDY3Njh9.OqNURKDKquq8KawOjSN4_A4gCB4ic3C1JFqCTKZGFMhO3dbcpyjSd3aS1YNwipq5fhtO-NfiOPT56vpwEP2flA";
        headers.put("Authorization","Bearer " + token);
        return headers;
    }
}
