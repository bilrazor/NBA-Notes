package com.project.nba_notes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {
    public ProfileFragment(){
        super(R.layout.fragment_profile);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView usernameTextView = (TextView) getView().findViewById(R.id.username_text_view);
        TextView emailTextView = (TextView) getView().findViewById(R.id.email_text_view);

        // TODO: 12/01/2024 Cambiar name y key de las shared preferences por lo que corresponda
        SharedPreferences prefs = this.getActivity().getSharedPreferences("USER_PREFERENCES", Context.MODE_PRIVATE);
        String username = prefs.getString("VALID_USERNAME", null);
        String email = prefs.getString("VALID_EMAIL", null);

        String text = "USERNAME: " + username;
        usernameTextView.setText(text);

        text = "EMAIL: " + email;
        emailTextView.setText(text);


    }
}
