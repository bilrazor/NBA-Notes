package com.project.nba_notes;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotesRecyclerViewAdapter  extends RecyclerView.Adapter<NotesViewHolder> {

    private List<NotesData> allTheData;
    private Activity activity;

    public NotesRecyclerViewAdapter(List<NotesData> allTheData, Activity activity) {
        this.allTheData = allTheData;
        this.activity = activity;
    }


    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Utiliza LayoutInflater para crear una nueva vista inflando el layout XML definido para cada elemento del RecyclerView.
        // El contexto se obtiene del ViewGroup padre, que es el RecyclerView.
        // 'false' indica que no queremos que la vista inflada se adjunte al ViewGroup padre inmediatamente.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_view_holder, parent, false);

        // Crea una instancia de PokemonViewHolder pasándole la vista inflada. Esta instancia es utilizada para mostrar los datos.
        return new NotesViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        // Obtiene el elemento de datos actual basándose en la posición.
        NotesData dataInPositionToBeRendered = allTheData.get(position);
        // Llama al método showData del ViewHolder con el objeto de datos actual y la actividad.
        // Este método es responsable de actualizar el contenido del ViewHolder con los datos del objeto PokemonData.
        holder.showData(dataInPositionToBeRendered, activity);
    }

    @Override
    public int getItemCount() {
        // Retorna el tamaño de la lista de datos que se provee al adaptador.
        return allTheData.size();
    }

}