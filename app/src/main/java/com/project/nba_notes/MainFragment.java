package com.project.nba_notes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
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
import java.util.Comparator;
import java.util.List;

public class MainFragment extends Fragment {
    private String category;  // Variable para almacenar la categoría actual
    private RecyclerView recyclerView;  // Variable para referenciar el RecyclerView en la UI
    private List<NotesData> allTheNotes;
    private NotesRecyclerViewAdapter adapter;
    private RequestQueue queue;
    private View rootView;
    private TextView textViewTitle;
    private ImageButton buttonCreateNote;
    private String terminoBusqueda = null;
    private RelativeLayout loadingPanelMain;
    private TextView loaderTextView;
    private boolean isLoading = false;

    public MainFragment() {
        // Constructor público vacío requerido por Android para instanciar el fragmento
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Infla el layout XML asociado con este fragmento
        rootView = inflater.inflate(R.layout.main_fragment, container, false);
        initUI();
        handleFragmentArguments();
        // Configuración del botón para crear notas
        setupCreateNoteButton();
        // Verifica si el fragmento está asociado a una actividad
        if (getActivity() != null) {
            // Configuración del RecyclerView
            realizarFiltrado(terminoBusqueda); // Llama al método para filtrar datos basados en la categoría
            allTheNotes = new ArrayList<>();
            adapter = new NotesRecyclerViewAdapter(allTheNotes, getActivity());
        }
         return rootView; // Devuelve la vista del fragmento
    }
    @Override
    public void onResume() {
        super.onResume();
        // Llama a realizarFiltrado para actualizar los datos.
        realizarFiltrado(terminoBusqueda);
    }

    public void realizarFiltrado(String terminoBusqueda) {
        showLoader(); // Mostrar el loader antes de la solicitud de red

        String url;
        if (terminoBusqueda != null && !terminoBusqueda.isEmpty()) {
            // Si hay un término de búsqueda, construye la URL para la búsqueda
            url = Server.name + "/api/auth/notes/title?" +
                    "title=" + Uri.encode(terminoBusqueda);
        } else {
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
        }
        // Solicitud de red para obtener los datos de las notas
        JsonArrayRequestWithAuthHeader request = new JsonArrayRequestWithAuthHeader(
                Request.Method.GET,
                url,
                null,
                response -> {
                    allTheNotes.clear();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject note = response.getJSONObject(i);
                            NotesData data = new NotesData(note);
                            allTheNotes.add(data);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    sortNotes(false);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerView.setAdapter(adapter);
                    hideLoader();
                },
                error -> {
                        // Muestra un mensaje más detallado
                        hideLoader();
                        String mensajeError = error.getMessage() == null ? "Error desconocido" : error.getMessage();
                        Toast.makeText(getActivity(), "Error al cargar datos: " + mensajeError,Toast.LENGTH_LONG).show();
                },getActivity()
        );

        // Verifica si el contexto de la actividad está disponible
        if (getActivity() != null) {
            // Crea una cola de solicitudes y añade la solicitud de red a ella
            queue.add(request);
        }


    }
    public void sortNotes(boolean isAscending) {
        if (isAscending) {
            // Ordenar ascendente
            allTheNotes.sort((note1, note2) -> note1.getLastModified().compareTo(note2.getLastModified()));
        } else {
            // Ordenar descendente
            allTheNotes.sort((note1, note2) -> note2.getLastModified().compareTo(note1.getLastModified()));
        }
        adapter.notifyDataSetChanged(); // Notifica al adaptador del cambio
    }
    private void initUI() {
        // Encuentra y almacena referencias a los componentes de la interfaz de usuario en variables.
        textViewTitle = rootView.findViewById(R.id.textViewTitle);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        buttonCreateNote = rootView.findViewById(R.id.buttonCreateNote);
        loadingPanelMain = rootView.findViewById(R.id.loadingPanelMain);

        // Configura la cola de solicitudes HTTP.
        queue = Volley.newRequestQueue(getActivity());

    }
    private void showLoader() {
        isLoading = true;
        loadingPanelMain.setVisibility(View.VISIBLE);
    }

    private void hideLoader() {
        isLoading = false;
        loadingPanelMain.setVisibility(View.GONE);
    }

    // Recupera argumentos pasados al fragmento, si existen
    private void handleFragmentArguments() {
        if (getArguments() != null) {
            category = getArguments().getString("CATEGORY", "todas");
            if (category.equals("favoritos")) {
                textViewTitle.setText("Favoritos");
                textViewTitle.setVisibility(View.VISIBLE); // Hacer visible el título para favoritos
            } else {
                textViewTitle.setVisibility(View.GONE); // Ocultar el título para otras categorías
            }
        }
    }
    private void setupCreateNoteButton() {
        buttonCreateNote.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), NotesActivity.class);
            startActivity(intent);
        });
    }
}

