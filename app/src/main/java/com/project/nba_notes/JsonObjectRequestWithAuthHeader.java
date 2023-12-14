package com.project.nba_notes;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JsonObjectRequestWithAuthHeader extends JsonObjectRequest {


    public JsonObjectRequestWithAuthHeader(int method, String url, @Nullable JSONObject jsonRequest, Response.Listener<JSONObject> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        /*SharedPreferences preferences = context.getSharedPreferences("SESSIONS_APP_PREFS", Context.MODE_PRIVATE);
        //final String sessionToken = preferences.getString("VALID_TOKEN", null);
        final String sessionToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwZXBlIiwiZXhwIjoxNzAyNjE5NzQ1LCJpYXQiOjE3MDI1NTk3NDV9.GdbQCl8mrMbALdFi8u0DZ5O8zHWj7GS7XyCzxiBD6OM77F_79p7X-J7didGvENpbvSsdAJQYrLN7tdabnbl5Mw";
        if (sessionToken == null) {
            Toast.makeText(context,"Sesion nula",Toast.LENGTH_LONG).show();

            throw new AuthFailureError();
         }*/
        final String sessionToken = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJwZXBlIiwiZXhwIjoxNzAyNjE5NzQ1LCJpYXQiOjE3MDI1NTk3NDV9.GdbQCl8mrMbALdFi8u0DZ5O8zHWj7GS7XyCzxiBD6OM77F_79p7X-J7didGvENpbvSsdAJQYrLN7tdabnbl5Mw";

        HashMap<String, String> myHeaders = new HashMap<>();
        myHeaders.put("Session-Token", sessionToken);
        return myHeaders;

    }

}