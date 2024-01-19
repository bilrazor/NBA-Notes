package com.project.nba_notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Context context = this;
    private MaterialToolbar toolbar;
    private DrawerLayout drawerLayout;
    private FragmentContainerView fragmentContainer;
    private SearchView searchView;
    private static String searchRequest;
    private ImageButton themeSwapButton;
    private ImageButton logoutButton;
    private List<NotesData> allTheNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        initToolbar();

        themeSwapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchRequest = (String) searchView.getQuery();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
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

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.menu_item_start) {
//            Bundle args = new Bundle();
//            args.putString("CATEGORY", "todas");
//
//            fragmentManager.beginTransaction()
//                    .add(R.id.main_fragment_container, MainFragment.class, args)
//                    .commit();
        }
        else if (id == R.id.menu_item_profile)
            fragmentManager.beginTransaction()
                .add(R.id.main_fragment_container, ProfileFragment.class, null)
                .commit();
        else if (id == R.id.menu_item_favourite){
//            Bundle args = new Bundle();
//            args.putString("CATEGORY", "favorite");
//
//            fragmentManager.beginTransaction()
//                    .add(R.id.main_fragment_container, MainFragment.class, args)
//                    .commit();
        }
        else if (id == R.id.menu_item_order) {
        }
        drawerLayout.closeDrawers();

        return false;
    }

    private void retrieveUserInfo(){
        SharedPreferences prefs = getSharedPreferences("SESSIONS_APP_PREFS", Context.MODE_PRIVATE);
    }

    private void sendFilterQuery(){
        JSONObject jsonObject = new JSONObject();
        JSONArray requestBody = new JSONArray();
        try{
            jsonObject.put("title", searchRequest);
            requestBody.put(1, jsonObject);
        }catch(JSONException e){
            throw new RuntimeException(e);
        }
        allTheNotes.clear();

        JsonArrayRequestWithAuthHeader2 request = new JsonArrayRequestWithAuthHeader2(
                Request.Method.GET,
                "http://10.0.2.2:8000/api/auth/notes",
                requestBody,
                response -> {
                    for(int i = 0; i < response.length(); i++){
                        try{
                            JSONObject note = response.getJSONObject(i);
                            NotesData data = new NotesData(note);
                            allTheNotes.add(data);
                        }catch (JSONException e){}
                    }
                    //
                    //
                    //
                },
                error -> {

                },
                context
        );
    }
}