package com.project.nba_notes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class ProfileFragment extends Fragment {
    private RequestQueue queue;
    public ProfileFragment(){
        super(R.layout.fragment_profile);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView usernameTextView = (TextView) getView().findViewById(R.id.username_text_view);
        TextView emailTextView = (TextView) getView().findViewById(R.id.email_text_view);
        Button deleteAccountButton = (Button) getView().findViewById(R.id.delete_button);
        queue = Volley.newRequestQueue(getContext());
        
        SharedPreferences prefs = this.getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        String username = prefs.getString("username", null);
        String email = prefs.getString("email", null);

        String text = "USERNAME: " + username;
        usernameTextView.setText(text);

        text = "EMAIL: " + email;
        emailTextView.setText(text);

        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAccount();
            }
        });
    }

    private void deleteAccount(){
        JsonObjectRequestWithAuthHeader request = new JsonObjectRequestWithAuthHeader(
                Request.Method.DELETE,
                Server.name + "/api/auth/notes",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(
                                getContext(),
                                "Usuario borrado con exito",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(
                                getContext(),
                                "Estado de respuesta: " + error.networkResponse.statusCode,
                                Toast.LENGTH_LONG)
                                .show();
                    }
                },
                getContext()
        );
        queue.add(request);
    }
}
