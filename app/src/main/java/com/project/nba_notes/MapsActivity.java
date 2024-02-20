package com.project.nba_notes;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.project.nba_notes.databinding.ActivityMapsBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private RequestQueue requestQueue;
    private Location lastKnownLocation;
    private FusedLocationProviderClient fusedLocationClient;
    private BitmapDescriptor markerIcon;
    private List<NotesData> notes;
    private Context context = this;
    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        queue = Volley.newRequestQueue(this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @SuppressLint("PotentialBehaviorOverride")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        markerIcon = vectorToBitmap();

        // Move the camera
        LatLng coruna = new LatLng(43.3713, -8.396);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(coruna));

        getAllNotes();

        // Crea los marcadores de las notas en el mapa
        for(int i = 0; i < notes.size(); i++){
            NotesData note = notes.get(i);
            initMarker(note);
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                Intent intent = new Intent(MapsActivity.this, NotesActivity.class);
                intent.putExtra("NOTE_ID", marker.getTitle());
                startActivity(intent);
                finish();
                return false;
            }
        });
    }

    private void initMarker(NotesData note){
        LatLng cords = note.getCords();
        String title = String.valueOf(note.getId());
        MarkerOptions marker = new MarkerOptions()
                .position(cords)
                .title(title)
                .icon(markerIcon);
        mMap.addMarker(marker);
    }

    private void getAllNotes(){
        notes = new ArrayList<>();
        JsonArrayRequestWithAuthHeader2 jsonRequest = new JsonArrayRequestWithAuthHeader2(
                Request.Method.GET,
                "http://10.0.2.2:8000/api/auth/notes",
                null,
                response -> {
                    for (int i = 0; i < response.length(); i++){
                        try{
                            JSONObject note = response.getJSONObject(i);
                            notes.add(new NotesData(note));
                        }catch (JSONException e){}
                    }
                },
                error -> {

                },
                context
        );
        queue.add(jsonRequest);
    }

    private BitmapDescriptor vectorToBitmap(){
        Drawable vectorDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.note_icon,null);
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}