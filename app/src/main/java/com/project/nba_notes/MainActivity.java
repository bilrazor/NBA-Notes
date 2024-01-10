package com.project.nba_notes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Verifica que la actividad está usando el layout con el fragment_container FrameLayout


        // Sin embargo, si estamos siendo restaurados de un estado previo,
        // entonces no necesitamos hacer nada y deberíamos regresar o podríamos
        // terminar con fragmentos superpuestos.
        if (savedInstanceState == null) {
            Fragment fragment = new MainFragment();
            Bundle args = new Bundle();
            // Aquí decides qué argumento pasar basado en la selección del usuario
            args.putString("CATEGORY", "todas"); // O "favoritos"
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
        }

    }

}
