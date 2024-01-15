package com.project.nba_notes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class ProfileFragment extends Fragment {
    public ProfileFragment(){
        super(R.layout.fragment_profile);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView usernameTextView = (TextView) getView().findViewById(R.id.username_text_view);
        TextView emailTextView = (TextView) getView().findViewById(R.id.email_text_view);
        Button deleteAccountButton = (Button) getView().findViewById(R.id.delete_button);

        // TODO: 12/01/2024 Cambiar name y key de las shared preferences por lo que corresponda
        SharedPreferences prefs = this.getActivity().getSharedPreferences("USER_PREFERENCES", Context.MODE_PRIVATE);
        String username = prefs.getString("VALID_USERNAME", null);
        String email = prefs.getString("VALID_EMAIL", null);

        String text = "USERNAME: " + username;
        usernameTextView.setText(text);

        text = "EMAIL: " + email;
        emailTextView.setText(text);

        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void sendDeleteAccountRequest(String username, String email, String password){
        JSONObject requestBody = new JSONObject();
        String loginUsername = username;
        String loginEmail = email;

        try{
            requestBody.put("username", loginUsername);
            requestBody.put("email", loginEmail);
        }catch (JSONException e){
            throw new RuntimeException(e);
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.DELETE,
                "/",
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );

    }
}
