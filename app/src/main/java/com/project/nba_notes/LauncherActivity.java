package com.project.nba_notes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.project.nba_notes.MainActivity;

public class LauncherActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Recuperamos el nombre de usuario de las preferencias
        SharedPreferences preferences = getSharedPreferences("SESSIONS_APP_PREFS", MODE_PRIVATE);
        String username = preferences.getString("VALID_USERNAME", null); // null será el valor por defecto

        // Si el usuario NO se ha logueado, el valor es 'null' por defecto
        // Se inicia la pantalla de Login
        if (username == null) {
            Intent loginActivity = new Intent(this, LoginActivity.class);
            startActivity(loginActivity);

            // Si el usuario SÍ se ha logueado, ya disponemos de su nombre de usuario
            // Se inicia la pantalla principal
        } else {
            Intent mainActivity = new Intent(this, MainActivity.class);
            startActivity(mainActivity);
        }
    }
}
