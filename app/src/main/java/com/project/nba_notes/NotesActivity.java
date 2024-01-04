package com.project.nba_notes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

    private boolean noteFavorite = false;
    private boolean isTextSizeIncreased = false;
    private Stack<String> undoStack = new Stack<>();
    private Stack<String> redoStack = new Stack<>();

    private UndoRedoHelper undoRedoHelper = new UndoRedoHelper();
    private boolean isLoadingData = false;
    private int noteId = -1;

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
        ImageButton buttonNoteLetter = findViewById(R.id.buttonLetter);
        ImageButton buttonDelete = findViewById(R.id.buttonDelete);
        ImageButton buttonFavorite = findViewById(R.id.buttonFavorite); // Asegúrate de tener este botón en tu layout

        noteTitle = findViewById(R.id.noteTitle);
        noteDate = findViewById(R.id.noteDate);
        noteContent = findViewById(R.id.noteContent);
        buttonUndo.setEnabled(false);
        buttonRedo.setEnabled(false);
        buttonCheck.setEnabled(false);
        Intent intent = getIntent();
        this.noteId = intent.getIntExtra("NOTE_ID", 29);

        // Deshabilitar el botón de eliminar si estamos creando una nueva nota
        buttonDelete.setEnabled(noteId != -1);
        buttonDelete.setAlpha(noteId != -1 ? 1.0f : 0.4f);
        buttonUndo.setAlpha(0.4f);
        buttonRedo.setAlpha(0.4f);
        buttonCheck.setAlpha(0.4f);
        // Llama al método para obtener una nota específica.

        if (noteId == -1) {
            noteTitle.setText("");
            noteContent.setText("");
            // Ocultar la fecha porque aún no se ha creado la nota
            noteDate.setVisibility(View.GONE);
        } else {
            // Estamos editando una nota existente
            obtainNote(noteId);  // Carga los detalles de la nota para editar
        }

        // Establece los listeners para los botones. Cuando se hace clic, se ejecutan las acciones correspondientes.
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBack();
            }
        });
        buttonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noteId == -1) {
                    createNote(); // Crea una nueva nota con los datos actuales de la UI
                } else {
                    updateNote(noteId); // Actualiza la nota existente con el ID y los datos actuales de la UI
                }
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
        noteTitle.addTextChangedListener(new TextWatcher() {
            private String previousText = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                previousText = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No se necesita implementar nada aquí
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isLoadingData) {
                    return; // Ignorar cambios mientras se está cargando datos
                }
                String newTitle = s.toString();
                String content = noteContent.getText().toString();
                int titleCursorPosition = noteTitle.getSelectionStart();
                int contentCursorPosition = noteContent.getSelectionStart();
                if (!newTitle.equals(previousText)) {
                    // Aquí agregamos "title" como el último campo modificado
                    undoRedoHelper.onTextChanged(newTitle, content, titleCursorPosition, contentCursorPosition, "title");
                    previousText = newTitle;
                }
                updateUndoRedoButtonState();
                buttonCheck.setEnabled(true);
                buttonCheck.setAlpha(1.0f);
            }

        });

        noteContent.addTextChangedListener(new TextWatcher() {
            private String previousText = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                previousText = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No se necesita implementar nada aquí
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isLoadingData) {
                    return; // Ignorar cambios mientras se está cargando datos
                }
                String newContent = s.toString();
                if (!newContent.equals(previousText)) {
                    String title = noteTitle.getText().toString();
                    int titleCursorPosition = noteTitle.getSelectionStart();
                    int contentCursorPosition = noteContent.getSelectionStart();
                    // Aquí agregamos "content" como el último campo modificado
                    undoRedoHelper.onTextChanged(title, newContent, titleCursorPosition, contentCursorPosition, "content");
                    previousText = newContent;
                }
                updateUndoRedoButtonState();
                buttonCheck.setEnabled(true);
                buttonCheck.setAlpha(1.0f);
            }

        });


        buttonUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!undoRedoHelper.isUndoStackEmpty()) {
                    NoteState previousState = undoRedoHelper.undo();
                    noteTitle.setText(previousState.getTitle());
                    noteContent.setText(previousState.getContent());

                    // Asegúrate de enfocar y mover el cursor al campo correcto
                    if (previousState.getLastModifiedField().equals("title")) {
                        noteTitle.requestFocus();
                        noteTitle.setSelection(previousState.getTitleCursorPosition());
                    } else {
                        noteContent.requestFocus();
                        noteContent.setSelection(previousState.getContentCursorPosition());
                    }
                }
                updateUndoRedoButtonState();
                checkIfTextFieldsAreEmpty();

            }
        });

// Similar para el botón redo


        buttonRedo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!undoRedoHelper.isRedoStackEmpty()) {
                    NoteState nextState = undoRedoHelper.redo();
                    noteTitle.setText(nextState.getTitle());
                    noteContent.setText(nextState.getContent());

                    // Asegúrate de enfocar y mover el cursor al campo correcto
                    if (nextState.getLastModifiedField().equals("title")) {
                        noteTitle.requestFocus();
                        noteTitle.setSelection(nextState.getTitleCursorPosition());
                    } else {
                        noteContent.requestFocus();
                        noteContent.setSelection(nextState.getContentCursorPosition());
                    }
                }
                updateUndoRedoButtonState();
            }
        });


        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (noteId != -1) {
                    deleteNote(noteId);
                    finish();
                } else {
                    // Manejar el caso en el que no se pase un ID válido
                }
            }
        });
        buttonFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cambia el estado de favorito
                noteFavorite = !noteFavorite;
                // Actualiza el icono del botón de favorito
                buttonFavorite.setImageResource(noteFavorite ? R.drawable.baseline_star_24 : R.drawable.baseline_star_border_24);


                // Si noteId == -1, esto significa que es una nueva nota, por lo que no es necesario actualizar aún.
            }
        });



    }
    private void checkIfTextFieldsAreEmpty() {
        ImageButton buttonCheck = findViewById(R.id.buttonCheck);

        String title = noteTitle.getText().toString();
        String content = noteContent.getText().toString();
        boolean isAnyFieldNotEmpty = !title.isEmpty() || !content.isEmpty();

        buttonCheck.setEnabled(isAnyFieldNotEmpty);
        buttonCheck.setAlpha(isAnyFieldNotEmpty ? 1.0f : 0.4f);
    }

    // Método para obtener una nota desde un servidor mediante una petición GET.
    public void obtainNote(int noteId) {
        ImageButton buttonFavorite = findViewById(R.id.buttonFavorite); // Asegúrate de tener este botón en tu layout

        isLoadingData = true;
        // Crea y configura la petición.
        JsonObjectRequestWithAuthHeader request = new JsonObjectRequestWithAuthHeader(
                Request.Method.GET,
                Server.name + "/api/auth/notes/" + noteId,
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
                            boolean favorite = response.getBoolean("favorite");

                            noteFavorite = response.getBoolean("favorite");
                            buttonFavorite.setImageResource(noteFavorite ? R.drawable.baseline_star_24 : R.drawable.baseline_star_border_24);
                            // Convertir la cadena de fecha a un objeto Date
                            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                            Date date = isoFormat.parse(dateString);

                            // Formatear la fecha a un formato más amigable
                            SimpleDateFormat friendlyFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                            String formattedDate = friendlyFormat.format(date);

                            noteTitle.setText(title.isEmpty() ? "" : title);
                            noteDate.setText(formattedDate);
                            noteContent.setText(content);
                            noteTitle.setSelection(title.length());
                            noteContent.setSelection(content.length());
                            String currentTitle = noteTitle.getText().toString();
                            String currentContent = noteContent.getText().toString();
                            isLoadingData = false;
                            undoRedoHelper.setInitialState(currentTitle, currentContent, title.length(), content.length());
                            updateUndoRedoButtonState();
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
    // Método para crear una nueva nota en el servidor mediante una petición POST.
    public void createNote() {
        ImageButton buttonDelete = findViewById(R.id.buttonDelete);
        // Crea el cuerpo de la petición con el título y contenido de los campos de texto.
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("title", noteTitle.getText().toString());
            requestBody.put("content", noteContent.getText().toString());
            requestBody.put("favorite", noteFavorite);
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
                        try {
                            // Aquí asumiendo que el servidor devuelve el ID de la nota recién creada
                            int newNoteId = response.getInt("id"); // Asegúrate de que "id" sea la clave correcta
                            noteId = newNoteId; // Actualiza el noteId con el ID de la nueva nota

                            hideKeyboard();
                            resetFocus();
                            buttonDelete.setEnabled(true);
                            buttonDelete.setAlpha(1.0f); // Restaurar la opacidad para mostrarlo como habilitado


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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
        ImageButton buttonDelete = findViewById(R.id.buttonDelete);
        StringRequestWithAuthHeader request = new StringRequestWithAuthHeader(
                Request.Method.DELETE,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, "Nota eliminada con éxito", Toast.LENGTH_SHORT).show();
                        // Aquí puedes añadir cualquier lógica adicional necesaria tras la eliminación exitosa.
                        buttonDelete.setEnabled(false);
                        buttonDelete.setAlpha(0.4f); // Cambiar la opacidad para mostrarlo como deshabilitado

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
    public void updateNote(int noteId) {
        // Crea el cuerpo de la petición con el título y contenido de los campos de texto.
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("title", noteTitle.getText().toString());
            requestBody.put("content", noteContent.getText().toString());
            requestBody.put("favorite", noteFavorite);
        } catch (JSONException e) {
            // Manejo de excepciones JSON.
            e.printStackTrace();
        }

        // Configura y envía la petición PUT.
        JsonObjectRequestWithAuthHeader request = new JsonObjectRequestWithAuthHeader(
                Request.Method.PUT,
                Server.name + "/api/auth/notes/" + noteId,
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Muestra un mensaje de éxito y maneja la respuesta, posiblemente actualizando la UI.
                        Toast.makeText(context, "Nota actualizada", Toast.LENGTH_SHORT).show();
                        hideKeyboard();
                        resetFocus();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejo de errores de la petición.
                        Toast.makeText(context, "Error al actualizar la nota: " + error.toString(), Toast.LENGTH_LONG).show();
                    }
                },
                this // Aquí necesitas pasar el token o las credenciales de autenticación, dependiendo de cómo lo manejes.
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
    private void updateUndoRedoButtonState() {
        ImageButton buttonUndo = findViewById(R.id.buttonUndo);
        ImageButton buttonRedo = findViewById(R.id.buttonRedo);

        boolean isUndoAvailable = !undoRedoHelper.isUndoStackEmpty();
        boolean isRedoAvailable = !undoRedoHelper.isRedoStackEmpty();

        buttonUndo.setEnabled(isUndoAvailable);
        buttonRedo.setEnabled(isRedoAvailable);

        // Cambiar la opacidad de los botones para indicar su estado
        buttonUndo.setAlpha(isUndoAvailable ? 1.0f : 0.4f); // 1.0 para activo, 0.4 para deshabilitado
        buttonRedo.setAlpha(isRedoAvailable ? 1.0f : 0.4f);
    }
    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
    private void resetFocus() {
        noteTitle.clearFocus();
        noteContent.clearFocus();

    }
    public void onBack() {
        // Guardar la nota si hay contenido antes de ir hacia atrás
        if (!isEmpty(noteTitle) || !isEmpty(noteContent)) {
            saveNote();
        }
        finish();
    }
    private boolean isEmpty(EditText editText) {
        return editText.getText().toString().trim().isEmpty();
    }
    private void saveNote() {
        if (noteId == -1) {
            // Solo crear una nueva nota si el título o el contenido no están vacíos
            if (!isEmpty(noteTitle) || !isEmpty(noteContent)) {
                createNote();
            }
        } else {
            // Si estamos editando una nota existente, siempre intentar actualizar
            updateNote(noteId);
        }
    }

}