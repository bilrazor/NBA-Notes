package com.project.nba_notes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Stack;

// Esta clase representa una actividad en una aplicación de Android para la creación de notas.
public class NotesActivity extends AppCompatActivity {

    // Variables para almacenar el contexto de la aplicación, y los campos de texto para el título y contenido de la nota.
    private Context context = this;
    private EditText noteTitle;
    private EditText noteContent;
    private TextView noteDate;
    private boolean isTextSizeIncreased = false;
    private Stack<String> undoStack = new Stack<>();
    private Stack<String> redoStack = new Stack<>();

    private UndoRedoHelper undoRedoHelper = new UndoRedoHelper();

    // Método que se llama cuando se crea la actividad.
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Establece el layout de la actividad.
        setContentView(R.layout.activity_notes);

        // Encuentra los botones y campos de texto en el layout y los asigna a las variables.
        ImageButton buttonBack = findViewById(R.id.buttonBack);
        ImageButton buttonUndo = findViewById(R.id.buttonUndo);
        ImageButton buttonRedo = findViewById(R.id.buttonRedo);
        ImageButton buttonCheck = findViewById(R.id.buttonCheck);
        ImageButton buttonNoteLetter = findViewById(R.id.noteLetter);
        ImageButton buttonDelete = findViewById(R.id.noteDelete);
        noteTitle = findViewById(R.id.noteTitle);
        noteDate = findViewById(R.id.noteDate);
        noteContent = findViewById(R.id.noteContent);

        // Llama al método para obtener una nota específica.
        obtainNote(1);

        // Establece los listeners para los botones. Cuando se hace clic, se ejecutan las acciones correspondientes.
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Acción para el botón de regresar.
            }
        });
        buttonNoteLetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isTextSizeIncreased) {
                    // Aumenta el tamaño del texto
                    increaseTextSize();
                    buttonNoteLetter.setImageResource(R.drawable.baseline_type_specimen_24); // Cambia al ícono Aa
                } else {
                    // Restablece el tamaño del texto
                    resetTextSize();
                    buttonNoteLetter.setImageResource(R.drawable.baseline_spellcheck_24); // Cambia al ícono aA
                }
                isTextSizeIncreased = !isTextSizeIncreased;
            }
        });

        // Listener para cuando el texto cambia
        // Listener para cuando el texto cambia
        noteContent.addTextChangedListener(new TextWatcher() {
            private Handler handler = new Handler();
            private Runnable runnable;
            private String previousText = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                handler.removeCallbacks(runnable);
                previousText = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No se necesita implementar nada aquí
            }

            @Override
            public void afterTextChanged(Editable s) {
                runnable = new Runnable() {
                    @Override
                    public void run() {
                        if (!s.toString().equals(undoRedoHelper.getCurrentState())) {
                            undoRedoHelper.onTextChanged(s.toString());
                        }
                    }
                };
                handler.postDelayed(runnable, 100); // retraso de 1 segundo
            }
        });


        buttonUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!undoRedoHelper.isUndoStackEmpty()) {
                    String previousState = undoRedoHelper.undo();
                    noteContent.setText(previousState);

                    // Coloca el cursor al final del texto
                    noteContent.setSelection(previousState.length());
                }
            }
        });


        buttonRedo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!undoRedoHelper.isRedoStackEmpty()) {
                    String nextState = undoRedoHelper.redo();
                    noteContent.setText(nextState);

                    // Coloca el cursor al final del texto
                    noteContent.setSelection(nextState.length());
                }
            }
        });
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNote(9);
            }
        });

    }


        // Método para obtener una nota desde un servidor mediante una petición GET.
    public void obtainNote(int id) {
        // Crea y configura la petición.
        JsonObjectRequestWithAuthHeader request = new JsonObjectRequestWithAuthHeader(
                Request.Method.GET,
                Server.name + "/api/auth/notes/" + id,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Maneja la respuesta del servidor.
                        Log.d("NoteResponse", response.toString());
                        try {
                            // Obtiene el título y contenido de la respuesta y los muestra en los campos de texto.
                            String title = response.getString("title");
                            String dateString = response.getString("lastModified");
                            String content = response.getString("content");

                            // Convertir la cadena de fecha a un objeto Date
                            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                            Date date = isoFormat.parse(dateString);

                            // Formatear la fecha a un formato más amigable
                            SimpleDateFormat friendlyFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                            String formattedDate = friendlyFormat.format(date);

                            noteTitle.setText(title);
                            noteDate.setText(formattedDate);
                            noteContent.setText(content);
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Muestra un mensaje de error si la petición falla.
                        Toast.makeText(context, "Error en la petición: " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                },
                this // Pasa el contexto de la actividad.
        );
        // Añade la petición a la cola de peticiones y la ejecuta.
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    // Método para crear una nueva nota en el servidor mediante una petición POST.
    public void createNote() {
        // Crea el cuerpo de la petición con el título y contenido de los campos de texto.
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("title", noteTitle.getText().toString());
            requestBody.put("content", noteContent.getText().toString());
        } catch (JSONException e) {
            // Manejo de excepciones JSON.
            throw new RuntimeException(e);
        }

        // Configura y envía la petición POST.
        JsonObjectRequestWithAuthHeader request = new JsonObjectRequestWithAuthHeader(
                Request.Method.POST,
                Server.name+"/api/auth/notes",
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Muestra un mensaje de éxito.
                        Toast.makeText(context,"Nota creada",Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejo de errores de la petición.
                    }
                },
                this
        );
        // Añade la petición a la cola y la ejecuta.
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
    // Método para eliminar una nota en el servidor mediante una petición DELETE.
    public void deleteNote(int noteId) {
        String url = Server.name + "/api/auth/notes/" + noteId;

        StringRequestWithAuthHeader request = new StringRequestWithAuthHeader(
                Request.Method.DELETE,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, "Nota eliminada con éxito", Toast.LENGTH_SHORT).show();
                        // Aquí puedes añadir cualquier lógica adicional necesaria tras la eliminación exitosa.
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "Error al eliminar la nota: " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                },
                this
        );

        // Añade la petición a la cola y la ejecuta.
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }

    private void increaseTextSize() {
        noteContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, noteContent.getTextSize() + 5);
        noteTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, noteTitle.getTextSize() + 5);
    }

    private void resetTextSize() {
        noteContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, noteContent.getTextSize() - 5);
        noteTitle.setTextSize(TypedValue.COMPLEX_UNIT_PX, noteTitle.getTextSize() - 5);
    }

}
