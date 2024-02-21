package com.project.nba_notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.zip.Inflater;


import java.text.SimpleDateFormat;
import java.util.Date;



public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Context context = this;
    private RequestQueue queue;
    private MaterialToolbar toolbar;
    private DrawerLayout drawerLayout;
    private FragmentContainerView fragmentContainer;
    private SearchView searchView;
    private String searchRequest;
    private ImageButton themeSwapButton;
    private ImageButton logoutButton;
    private List<NotesData> allTheNotes;
    private TextView mainTitle;
    private boolean nightMode = true;
    private SharedPreferences prefs;
    private MainFragment mainFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        queue = Volley.newRequestQueue(this);

        initUI();
        initToolbar();
        retrieveUserInfo();

        Bundle args = new Bundle();
        args.putString("CATEGORY", "todas");
        mainFragment = new MainFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.main_fragment_container, MainFragment.class, args)
                .commit();
        mainTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putString("CATEGORY", "todas");
                fragmentManager.beginTransaction()
                        .replace(R.id.main_fragment_container, MainFragment.class, args)
                        .commit();
            }
        });
        themeSwapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nightMode){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    nightMode = false;
                }else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    nightMode = true;
                }
            }
        });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (hasFocus) {
                    // Opcionalmente, oculta el TextView
                    mainTitle.setVisibility(View.GONE);
                    themeSwapButton.setVisibility(View.GONE);
                    logoutButton.setVisibility(View.GONE);
                    // Expande el SearchView para ocupar más espacio
                    ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) searchView.getLayoutParams();
                    params.width = ConstraintLayout.LayoutParams.MATCH_PARENT; // O el ancho deseado
                    searchView.setLayoutParams(params);
                } else {
                    // Restaura el estado original si pierde el foco
                    mainTitle.setVisibility(View.VISIBLE);
                    themeSwapButton.setVisibility(View.VISIBLE);
                    logoutButton.setVisibility(View.VISIBLE);
                    // Restringe el ancho del SearchView a su estado original
                    ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) searchView.getLayoutParams();
                    params.width = ConstraintLayout.LayoutParams.WRAP_CONTENT; // O el ancho original
                    searchView.setLayoutParams(params);
                }
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchRequest =query.toString().trim();

                args.putString("searchRequest", query);


                MainFragment mainFragment = new MainFragment();
                mainFragment.setArguments(args);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.main_fragment_container, mainFragment)
                        .commit();

                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendLogoutRequest();
            }
        });

//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .setReorderingAllowed(true)
//                    .add(R.id.main_fragment_container, ProfileFragment.class, null)
//                    .commit();
//        }
    }

    //metodo deprecado pero mas simple que la alternativa
    @Override
    public void onBackPressed(){
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    private void initUI(){
        themeSwapButton = findViewById(R.id.theme_swap_button);
        logoutButton = findViewById(R.id.logout_button);
        fragmentContainer = findViewById(R.id.main_fragment_container);
        drawerLayout = findViewById(R.id.main_drawer_layout);
        toolbar = findViewById(R.id.main_toolbar);
        mainTitle = findViewById(R.id.main_title_text);

        searchView = findViewById(R.id.search_view);
        searchView.setMaxWidth(android.R.attr.width);

        NavigationView navigationView = findViewById(R.id.main_navigation_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void initToolbar(){
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        fragmentManager = getSupportFragmentManager();

        if (id == R.id.menu_item_start) {
            Bundle args = new Bundle();
            args.putString("CATEGORY", "todas");
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment_container, MainFragment.class, args)
                    .commit();
        }
        else if (id == R.id.menu_item_profile) {
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment_container, ProfileFragment.class, null)
                    .commit();
        }
        else if (id == R.id.menu_item_favourite){
            Bundle args = new Bundle();
            args.putString("CATEGORY", "favoritos");

            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment_container, MainFragment.class, args)
                    .commit();
        }
        else if (id == R.id.menu_item_order) {
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.order_dialog, null);

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setView(dialogView);

            AlertDialog dialog = builder.create();

            Button okButton = (Button) dialogView.findViewById(R.id.dialog_ok_button);
            RadioButton ascRadioButton = (RadioButton) dialogView.findViewById(R.id.asc_radio_button);
            RadioButton descRadioButton = (RadioButton) dialogView.findViewById(R.id.desc_radio_button);

            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MainFragment frag = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment_container);

                    if (descRadioButton.isChecked()) {
                        frag.sortNotes(false);
                    }

                    if (ascRadioButton.isChecked()) {
                        frag.sortNotes(true);
                    }

                    dialog.dismiss();
                }
            });

            dialog.show();
        } else if (id == R.id.menu_map) {
            Intent intent = new Intent(this, MapsActivity.class);
            this.startActivity(intent);
        } else if (id == R.id.menu_calendar) {
            fragmentManager.beginTransaction()
                    .replace(R.id.main_fragment_container, CalendarFragment.class, null)
                    .commit();
        }
        drawerLayout.closeDrawers();

        return false;
    }

    private void retrieveUserInfo(){
        prefs = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
    }

    private void sendLogoutRequest() {
        StringRequestWithAuthHeader request = new StringRequestWithAuthHeader(
                Request.Method.DELETE,
                Server.name + "/api/auth/signout",
                response -> {
                    Toast.makeText(context, "Logout exitoso", Toast.LENGTH_SHORT).show();
                    prefs.edit()
                            .remove("username")
                            .remove("email")
                            .remove("token")
                            .apply();
                    startActivity(new Intent(this, LoginActivity.class));
                },
                error -> Toast.makeText(context, "Error desconocido", Toast.LENGTH_LONG).show(),
                context
        );
        queue.add(request);
    }

}


