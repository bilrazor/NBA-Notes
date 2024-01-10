package com.project.nba_notes;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotesRecyclerViewAdapter extends RecyclerView.Adapter<NotesViewHolder> {

    // Lista para almacenar los datos de las notas
    private List<NotesData> allTheData;
    // Referencia a la actividad donde se usa este adaptador
    private Activity activity;

    // Constructor del adaptador
    public NotesRecyclerViewAdapter(List<NotesData> allTheData, Activity activity) {
        this.allTheData = allTheData; // Asigna los datos de las notas a la variable local
        this.activity = activity;     // Asigna la actividad a la variable local
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla la vista de cada elemento del RecyclerView
        // Usa el layout 'notes_view_holder' para la vista de los elementos
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_view_holder, parent, false);

        // Retorna una nueva instancia de NotesViewHolder con la vista inflada
        return new NotesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        // Obtiene el elemento de datos en la posición actual
        NotesData dataInPositionToBeRendered = allTheData.get(position);

        // Configura el ViewHolder con los datos del elemento y la actividad
        holder.showData(dataInPositionToBeRendered, activity);
    }

    @Override
    public int getItemCount() {
        // Retorna el número total de elementos en la lista de datos
        return allTheData.size();
    }

}
