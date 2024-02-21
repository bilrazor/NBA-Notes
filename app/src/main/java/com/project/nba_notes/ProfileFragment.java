package com.project.nba_notes;

import android.content.Context;
import android.content.Intent;
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

    private void deleteAccount() {
        SharedPreferences prefs = this.getActivity().getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
        StringRequestWithAuthHeader request = new StringRequestWithAuthHeader(
                Request.Method.DELETE,
                Server.name + "/api/auth/delete",
                response -> {
                    Toast.makeText(getContext(), "Usuario eliminado", Toast.LENGTH_SHORT).show();
                    prefs.edit()
                            .remove("username")
                            .remove("email")
                            .remove("token")
                            .apply();
                    Intent intent = new Intent(getContext(), LoginActivity.class);
                    startActivity(intent);
                },
                error -> Toast.makeText(getContext(), "Error desconocido", Toast.LENGTH_LONG).show(),
                getContext()
        );
        queue.add(request);
    }
}
