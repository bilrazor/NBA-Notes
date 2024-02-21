package com.project.nba_notes;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LauncherActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 2000; // Tiempo de duración de la pantalla de carga

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loading_screen);
        // Recuperamos el nombre de usuario de las preferencias
        SharedPreferences preferences = getSharedPreferences("MySharedPref", MODE_PRIVATE);
        String username = preferences.getString("username", null); // null será el valor por defecto


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Si el usuario NO se ha logueado, el valor es 'null' por defecto
                // Se inicia la pantalla de Login
                if (username == null) {
                    Intent loginActivity = new Intent(LauncherActivity.this, LoginActivity.class);
                    startActivity(loginActivity);
                } else {
                    // Si el usuario SÍ se ha logueado, ya disponemos de su nombre de usuario
                    // Se inicia la pantalla principal
                    Intent mainActivity = new Intent(LauncherActivity.this, MainActivity.class);
                    startActivity(mainActivity);
                }
            }
        }, SPLASH_TIME_OUT);
    }
}
