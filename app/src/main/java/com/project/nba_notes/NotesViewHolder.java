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

// Define una clase que extiende de RecyclerView.ViewHolder. Esta clase se utiliza para mostrar los elementos de una lista en un RecyclerView.
public class NotesViewHolder extends RecyclerView.ViewHolder {
    // Variables miembro para referenciar los elementos de la interfaz de usuario.
    private final TextView note_title;
    private final TextView note_content;
    private final ImageButton button_favorite;
    private final TextView note_last_modified;

    // Variable para mantener una referencia a los datos de la nota.
    private NotesData data;

    // Formateador de fecha para mostrar la última fecha de modificación de la nota.
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

    // Constructor de la clase. Se llama cuando se crea una instancia de este ViewHolder.
    public NotesViewHolder(@NonNull View itemView) {
        super(itemView);

        // Encuentra e inicializa los elementos de la interfaz de usuario del layout.
        note_title = itemView.findViewById(R.id.note_title);
        note_content = itemView.findViewById(R.id.note_content);
        button_favorite = itemView.findViewById(R.id.button_favorite);
        note_last_modified = itemView.findViewById(R.id.note_last_modified);
    }

    // Método para asignar datos a los elementos de la interfaz de usuario de este ViewHolder.
    public void showData(NotesData data, Context context) {
        this.data = data; // Almacena la referencia a los datos actuales.

        // Configura el contenido de los TextViews y el ImageButton.
        note_title.setText(data.getTitle());
        note_content.setText(data.getContent());
        button_favorite.setImageResource(data.isFavorite() ? R.drawable.baseline_star_24 : R.drawable.baseline_star_border_24);
        note_last_modified.setText(dateFormat.format(data.getLastModified()));

        // Manejador de clics para el botón de favorito.
        button_favorite.setOnClickListener(v -> {
            boolean newFavoriteStatus = !data.isFavorite();
            data.setFavorite(newFavoriteStatus);
            updateFavoriteStatusOnServer(data.getId(), newFavoriteStatus, context);
        });

        // Manejador de clics para la vista entera del ViewHolder.
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Crea un intent para iniciar una nueva actividad.
                Intent intent = new Intent(context, NotesActivity.class);
                // Agrega el ID de la nota al intent.
                intent.putExtra("NOTE_ID", data.getId());
                // Inicia la actividad.
                context.startActivity(intent);
            }
        });
    }

    // Método privado para actualizar el estado de favorito de una nota en el servidor.
    private void updateFavoriteStatusOnServer(int noteId, boolean newFavoriteStatus, Context context) {
        // Construye la URL para la solicitud de red.
        String url = Server.name + "/api/auth/notes/" + noteId;

        // Prepara los datos para la solicitud de red.
        JSONObject postData = new JSONObject();
        try {
            postData.put("favorite", newFavoriteStatus);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // Crea y configura la solicitud de red.
        JsonObjectRequestWithAuthHeader jsonObjectRequest = new JsonObjectRequestWithAuthHeader(
                Request.Method.PUT,
                url,
                postData,
                response -> {
                    try {
                        // Procesa la respuesta del servidor.
                        boolean updatedFavoriteStatus = response.getBoolean("favorite");

                        // Actualiza la interfaz de usuario y los datos del ViewHolder.
                        note_last_modified.setText(dateFormat.format(data.getLastModified()));
                        data.setFavorite(updatedFavoriteStatus);
                        button_favorite.setImageResource(updatedFavoriteStatus ? R.drawable.baseline_star_24 : R.drawable.baseline_star_border_24);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(context, "Error al procesar la respuesta", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    // Manejo de errores de la solicitud de red.
                    Toast.makeText(context, "Error al actualizar el estado de favorito", Toast.LENGTH_SHORT).show();
                },
                context
        );

        // Ejecuta la solicitud de red.
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(jsonObjectRequest);
    }
}
