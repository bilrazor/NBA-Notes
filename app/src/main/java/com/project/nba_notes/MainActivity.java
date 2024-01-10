package com.project.nba_notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private Context context = this;
    private MaterialToolbar toolbar;
    private DrawerLayout drawerLayout;
    private FragmentContainerView fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.main_drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.main_navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        fragmentContainer = findViewById(R.id.main_fragment_container);

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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.menu_item_start);
            //transaction.replace(R.id.main_fragment_container, );
        else if (id == R.id.menu_item_profile)
            fragmentManager.beginTransaction()
                .add(R.id.main_fragment_container, ProfileFragment.class, null)
                .commit();
        else if (id == R.id.menu_item_favourite);
            //transaction.replace(R.id.main_fragment_container, );
        else if (id == R.id.menu_item_order);
            //transaction.replace(R.id.main_fragment_container, );

        drawerLayout.closeDrawers();

        return false;
    }
}