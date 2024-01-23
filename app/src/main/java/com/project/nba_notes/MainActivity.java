package com.project.nba_notes;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtén el FragmentManager
        getSupportFragmentManager().beginTransaction()
                // Reemplaza el contenedor con el ID 'container' por el CalendarFragment
                .replace(R.id.container, new CalendarFragment(2024-10-27,"Primera nota"))
                .commit();
    }


}
