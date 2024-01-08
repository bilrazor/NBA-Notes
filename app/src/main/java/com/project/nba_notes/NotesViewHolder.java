package com.project.nba_notes;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class NotesViewHolder extends RecyclerView.ViewHolder {
    private final TextView note_title;
    private final TextView note_content;
    private final ImageButton button_favorite;
    private final TextView note_last_modified;
    private NotesData data; // Mantén una referencia a los datos de la nota

    public NotesViewHolder(@NonNull View itemView) {
        super(itemView);
        note_title = itemView.findViewById(R.id.note_title);
        note_content = itemView.findViewById(R.id.note_content);
        button_favorite = itemView.findViewById(R.id.button_favorite);
        note_last_modified = itemView.findViewById(R.id.note_last_modified); // Inicializa el TextView en tu constructor
    }

    public void showData(NotesData data, Context context) {
        this.data = data; // Asignar los datos actuales a la variable de referencia
        note_title.setText(data.getTitle());
        note_content.setText(data.getContent());
        button_favorite.setImageResource(data.isFavorite() ? R.drawable.baseline_star_24 : R.drawable.baseline_star_border_24);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        note_last_modified.setText(dateFormat.format(data.getLastModified()));


        button_favorite.setOnClickListener(v -> {
            boolean newFavoriteStatus = !data.isFavorite();
            data.setFavorite(newFavoriteStatus);
            updateFavoriteStatusOnServer(data.getId(), newFavoriteStatus, context);
        });

        // Establece un OnClickListener en toda la vista del ViewHolder.
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crea un Intent para iniciar NotesActivity.
                Intent intent = new Intent(context, NotesActivity.class);

                // Pone el ID de la nota como un extra en el Intent.
                intent.putExtra("NOTE_ID", data.getId());

                // Inicia NotesActivity utilizando el contexto de la actividad proporcionada.
                context.startActivity(intent);
            }
        });
    }



    private void updateFavoriteStatusOnServer(int noteId, boolean newFavoriteStatus, Context context) {
        String url = Server.name + "/api/auth/notes/" + noteId;
        JSONObject postData = new JSONObject();
        try {
            postData.put("favorite", newFavoriteStatus);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest2WithAuthHeader jsonObjectRequest = new JsonObjectRequest2WithAuthHeader(
                Request.Method.PUT,
                url,
                postData,
                response -> {
                    try {
                        // Suponiendo que la respuesta contiene el estado de favoritos actualizado
                        JSONObject responseObject = new JSONObject(response.toString());
                        boolean updatedFavoriteStatus = responseObject.getBoolean("favorite");

                        // Actualizar el objeto de datos y la UI
                        data.setFavorite(updatedFavoriteStatus);
                        button_favorite.setImageResource(updatedFavoriteStatus ? R.drawable.baseline_star_24 : R.drawable.baseline_star_border_24);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    Toast.makeText(context, "Error al actualizar el estado de favorito", Toast.LENGTH_SHORT).show();
                },
                context
        );

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsonObjectRequest);
    }

}