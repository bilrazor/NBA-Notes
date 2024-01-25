package com.project.nba_notes;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().beginTransaction()
                // Reemplaza el contenedor con el ID 'container' por el CalendarFragment
                .replace(R.id.container, new CalendarFragment())
                .commit();
    }


}
