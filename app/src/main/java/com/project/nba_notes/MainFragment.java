package com.project.nba_notes;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
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

public class MainFragment extends Fragment {
    private String category;  // Variable para almacenar la categoría actual
    private RecyclerView recyclerView;  // Variable para referenciar el RecyclerView en la UI

    public MainFragment() {
        // Constructor público vacío requerido por Android para instanciar el fragmento
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Infla el layout XML asociado con este fragmento
        View rootView = inflater.inflate(R.layout.main_fragment, container, false);
        System.out.println("Estamos dentro de MainFragment"); // Mensaje de depuración
        TextView textViewTitle = rootView.findViewById(R.id.textViewTitle);

        // Verifica si el fragmento está asociado a una actividad
        if (getActivity() != null) {
            // Recupera argumentos pasados al fragmento, si existen
            if (getArguments() != null) {
                category = getArguments().getString("CATEGORY", "todas");

                if (category.equals("favoritos")) {
                    textViewTitle.setText("Favoritos");
                    textViewTitle.setVisibility(View.VISIBLE); // Hacer visible el título para favoritos
                } else {
                    textViewTitle.setVisibility(View.GONE); // Ocultar el título para otras categorías
                }
            }


            // Configuración del RecyclerView
            recyclerView = rootView.findViewById(R.id.recycler_view);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

            // Configuración del botón para crear notas
            ImageButton buttonCreateNote = rootView.findViewById(R.id.buttonCreateNote);
            buttonCreateNote.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Inicia una nueva actividad al hacer clic en el botón
                    Intent intent = new Intent(getActivity(), NotesActivity.class);
                    startActivity(intent);
                }
            });

            realizarFiltrado(); // Llama al método para filtrar datos basados en la categoría
        }

        return rootView; // Devuelve la vista del fragmento
    }

    private void realizarFiltrado() {
        String url;
        switch (category) {
            case "favoritos":
                url = Server.name + "/api/auth/notes/favorites"; // URL para notas favoritas
                break;
            case "todas":
                url = Server.name + "/api/auth/notes"; // URL para todas las notas
                break;
            default:
                url = ""; // URL por defecto o manejo de categoría desconocida
                break;
        }
        // Solicitud de red para obtener los datos de las notas
        JsonArrayRequestWithAuthHeader request = new JsonArrayRequestWithAuthHeader(
                Request.Method.GET,
                url,
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
                    // Verifica si el contexto de la actividad está disponible
                    if (getActivity() != null) {
                        // Muestra un mensaje más detallado
                        String mensajeError = error.getMessage() == null ? "Error desconocido" : error.getMessage();
                        Toast.makeText(getActivity(), "Error al cargar datos: " + mensajeError,Toast.LENGTH_LONG).show();

                    }
                },getActivity()
        );
        // Verifica si el contexto de la actividad está disponible
        if (getActivity() != null) {
            // Crea una cola de solicitudes y añade la solicitud de red a ella
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(request);
        }


    }

}

