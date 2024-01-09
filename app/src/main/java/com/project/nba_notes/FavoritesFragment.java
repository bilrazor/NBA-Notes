package com.project.nba_notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {

    public FavoritesFragment() {
        // Constructor público vacío requerido
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Infla el layout para este fragmento
        View rootView = inflater.inflate(R.layout.favorites_fragment, container, false);
        System.out.println("Estamos dentro de MainFragment");

        // Encuentra el RecyclerView en el layout inflado
        RecyclerView recyclerView = rootView.findViewById(R.id.recycler_view);
        ImageButton buttonCreateNote = rootView.findViewById(R.id.buttonCreateNote);
        // Establece un OnClickListener en el ImageButton
        buttonCreateNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crea un Intent para iniciar NotesActivity
                Intent intent = new Intent(getActivity(), NotesActivity.class);
                startActivity(intent);
            }
        });
        // Crea el Request para la API
        JsonArrayRequestWithAuthHeader request = new JsonArrayRequestWithAuthHeader(
                Request.Method.GET,
                Server.name + "/api/auth/notes/favorites",
                null,
                response -> {
                    List<NotesData> allTheNotes = new ArrayList<>();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject note = response.getJSONObject(i);
                            NotesData data = new NotesData(note);
                            allTheNotes.add(data);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    // Establece el adaptador y el layout manager para el RecyclerView
                    if (getActivity() != null) {
                        NotesRecyclerViewAdapter adapter = new NotesRecyclerViewAdapter(allTheNotes, getActivity());
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    }
                },
                error -> {
                    if (getActivity() != null) {
                        // Muestra un mensaje más detallado
                        String mensajeError = error.getMessage() == null ? "Error desconocido" : error.getMessage();
                        Toast.makeText(getActivity(), "Error al cargar datos: " + mensajeError,Toast.LENGTH_LONG).show();

                    }
                },getActivity()
        );

        // Comprueba si el contexto de la actividad está disponible antes de hacer la solicitud
        if (getActivity() != null) {
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(request);
        }

        return rootView; // Retorna la vista creada
    }
}