package com.project.nba_notes;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

// Esta clase representa una actividad en una aplicación de Android para la creación y edición de notas.
public class NotesActivity extends AppCompatActivity {

    // Variables de instancia para almacenar el contexto, campos de texto para el título, contenido y fecha de la nota,
    // así como botones de la interfaz de usuario y otros estados relevantes.
    private Context context = this;
    private EditText noteTitle,noteContent;
    private TextView noteDate, locationName;
    private ImageButton buttonCheck,buttonDelete,buttonUndo,buttonRedo,buttonBack,buttonFavorite,buttonNoteLetter;
    private boolean noteFavorite = false;
    private boolean isTextSizeIncreased = false;
    private UndoRedoHelper undoRedoHelper = new UndoRedoHelper();
    private boolean isLoadingData = false;
    private int noteId = -1;
    private RequestQueue queue;
    private RelativeLayout loadingPanel;
    private boolean isUndoAvailable;
    private boolean isRedoAvailable;
    private boolean onBackPressed = false;

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private double latitude,longitude;

    // Este método se llama cuando se crea la actividad.
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Establece el diseño de la actividad desde un archivo de recursos XML.
        setContentView(R.layout.activity_notes);

        // Inicializa los componentes de la interfaz de usuario.
        initUI();
        // Procesa los datos pasados a esta actividad, como el ID de la nota.
        processIntentData();
        // Configura los estados iniciales de los botones.
        configureInitialButtonStates();
        // Configura los listeners para los botones y otros componentes interactivos.
        setupListeners();
        // Configura un Listener para los camios de texto
        setupTextChangedListener();
        // Configura un Listener para los botónes undo y redo
        setupListenersUndoRedo();
        // Configura un Listener para la localización
        startLocationUpdates();
    }


    private void startLocationUpdates() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                    }
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        }
    }




    private void getLocationName(double latitude, double longitude) {

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                locationName.setVisibility(View.VISIBLE);
                String featureName = address.getFeatureName();
                String locality = address.getLocality();
                String adminArea = address.getAdminArea();
                String countryName = address.getCountryName();
                String fullAddress = locality + ", " + adminArea + ", " + countryName;
                locationName.setText(fullAddress);
            } else {
                // Maneja el caso donde no se encuentra la dirección
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para obtener una nota desde un servidor mediante una petición GET.
    public void obtainNote(int noteId) {
        // Muestra el panel de carga.
        showLoadingPanel();
        // Crea y configura la petición HTTP GET.
        JsonObjectRequestWithAuthHeader request = new JsonObjectRequestWithAuthHeader(
                Request.Method.GET,
                Server.name + "/api/auth/notes/" + noteId,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Oculta el panel de carga una vez que la respuesta es recibida.
                        hideLoadingPanel();
                        try {
                            // Extrae los datos de la respuesta y los muestra en la interfaz.
                            String title = response.getString("title");
                            String dateString = response.getString("lastModified");
                            String content = response.getString("content");
                            double latitude = response.getDouble("latitude");
                            double longitude = response.getDouble("longitude");
                            getLocationName(latitude, longitude);
                            noteFavorite = response.getBoolean("favorite");

                            buttonFavorite.setImageResource(noteFavorite ? R.drawable.baseline_star_24 : R.drawable.baseline_star_border_24);

                            noteTitle.setText(title);
                            noteDate.setText(parseDate(dateString));
                            noteContent.setText(content);
                            noteTitle.setSelection(title.length());
                            noteContent.setSelection(content.length());
                            String currentTitle = noteTitle.getText().toString();
                            String currentContent = noteContent.getText().toString();


                            // Actualiza el estado inicial del helper de deshacer/rehacer con los datos cargados.
                            undoRedoHelper.setInitialState(currentTitle, currentContent, title.length(), content.length());
                            // Actualiza el estado de los botones de deshacer y rehacer.
                            updateUndoRedoButtonState();
                        } catch (JSONException | ParseException e) {
                            e.printStackTrace();
                        }


                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideLoadingPanel(); // Oculta el spinner si hay un error
                        // Muestra un mensaje de error si la petición falla.
                        error(error);                    }
                },
                this // Pasa el contexto de la actividad.
        );
        // Añade la petición a la cola de peticiones y la ejecuta.
        queue.add(request);
    }

    // Método para crear una nueva nota en el servidor mediante una petición POST.
    public void createNote() {
        showLoadingPanel();
        UiUtils.hideKeyboard(NotesActivity.this);
        UiUtils.resetFocus(noteTitle, noteContent);
        // Crea el cuerpo de la petición con el título y contenido de los campos de texto.
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("title", noteTitle.getText().toString());
            requestBody.put("content", noteContent.getText().toString());
            requestBody.put("favorite", noteFavorite);
            requestBody.put("latitude",latitude);
            requestBody.put("longitude",longitude);

        } catch (JSONException e) {
            // Manejo de excepciones JSON.
            throw new RuntimeException(e);
        }

        // Configura y envía la petición POST.
        JsonObjectRequestWithAuthHeader request = new JsonObjectRequestWithAuthHeader(
                Request.Method.POST,
                Server.name + "/api/auth/notes",
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        noteDate.setVisibility(View.VISIBLE);
                        try {
                            String dateString = response.getString("lastModified");
                            double latitude = response.getDouble("latitude");
                            double longitude = response.getDouble("longitude");
                            getLocationName(latitude, longitude);
                            noteDate.setText(parseDate(dateString));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        // Muestra un mensaje de éxito.
                        Toast.makeText(context, "Nota creada", Toast.LENGTH_SHORT).show();
                        try {
                            // Aquí asumiendo que el servidor devuelve el ID de la nota recién creada
                            int newNoteId = response.getInt("id");
                            noteId = newNoteId; // Actualiza el noteId con el ID de la nueva nota
                            configureButtonState(buttonDelete,true);
                            hideLoadingPanel();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejo de errores de la petición.
                        hideLoadingPanel();
                        error(error);
                    }
                },
                this
        );
        // Añade la petición a la cola y la ejecuta.
        queue.add(request);
    }
    // Método para eliminar una nota en el servidor mediante una petición DELETE.
    public void deleteNote(int noteId) {
        showLoadingPanel();
        StringRequestWithAuthHeader request = new StringRequestWithAuthHeader(
                Request.Method.DELETE,
                Server.name + "/api/auth/notes/" + noteId,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideLoadingPanel();
                        Toast.makeText(context, "Nota eliminada con éxito", Toast.LENGTH_SHORT).show();
                        configureButtonState(buttonDelete,false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideLoadingPanel();
                        error(error);
                    }
                },
                this
        );
        // Añade la petición a la cola y la ejecuta.
        queue.add(request);
    }
    // Método para actualizar una nota en el servidor mediante una petición DELETE.
    public void updateNote(int noteId) {
        showLoadingPanel();
        UiUtils.hideKeyboard(NotesActivity.this);
        UiUtils.resetFocus(noteTitle, noteContent);
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
                        try {
                            String dateString = response.getString("lastModified");
                            double latitude = response.getDouble("latitude");
                            double longitude = response.getDouble("longitude");
                            getLocationName(latitude, longitude);
                            noteDate.setText(parseDate(dateString));
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        hideLoadingPanel();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejo de errores de la petición.
                        hideLoadingPanel();
                        error(error);
                    }
                },
                this
        );
        // Añade la petición a la cola y la ejecuta.
        queue.add(request);
    }
    // Revisa si alguno de los campos de texto (título o contenido de la nota) no está vacío.
    private void checkIfTextFieldsAreEmpty() {
        String title = noteTitle.getText().toString();
        String content = noteContent.getText().toString();
        boolean isAnyFieldNotEmpty = !title.isEmpty() && !content.isEmpty();
        // Si alguno de los campos no está vacío, habilita el botón para guardar/check.
        // Si ambos campos están vacíos, deshabilita el botón.
        configureButtonState(buttonCheck, isAnyFieldNotEmpty);
    }


    // Actualiza el estado de los botones Undo y Redo basado en si hay acciones para deshacer o rehacer.
    private void updateUndoRedoButtonState() {
        // Verifica si hay acciones para deshacer.
        isUndoAvailable = !undoRedoHelper.isUndoStackEmpty();
        // Verifica si hay acciones para rehacer.
        isRedoAvailable = !undoRedoHelper.isRedoStackEmpty();

        // Habilita o deshabilita los botones dependiendo de si las acciones están disponibles.
        configureButtonState(buttonUndo, isUndoAvailable);
        configureButtonState(buttonRedo, isRedoAvailable);
    }


    // Configura el estado de un botón (habilitado/deshabilitado) y ajusta su opacidad para reflejar su estado.
    private void configureButtonState(ImageButton button, boolean isEnabled) {
        button.setEnabled(isEnabled);
        // Opacidad completa para botones habilitados, opacidad reducida para deshabilitados.
        button.setAlpha(isEnabled ? 1.0f : 0.4f);
    }



    // Evento de retroceso personalizado para manejar cuando el usuario presiona el botón de regreso.
    public void onBack() {
        onBackPressed = true;
        if (!buttonCheck.isEnabled()){
            if (!isEmpty(noteTitle) && !isEmpty(noteContent)){
                finish();
                return;
            }
            if (isEmpty(noteTitle) && isEmpty(noteContent) && noteId != -1){
                setNoteDelete();
                return;
            }
            if(isEmpty(noteTitle) || isEmpty(noteContent) ){
                finish();
                return;
            }
        }
        if (buttonCheck.isEnabled()){
            if(!isEmpty(noteTitle) && !isEmpty(noteContent)){
                saveNote();
            }else if (isEmpty(noteTitle) && isEmpty(noteContent)){
                setNoteDelete();
             }
        }
    }


    // Verifica si un EditText está vacío (sin texto o solo con espacios en blanco).
    private boolean isEmpty(EditText editText) {
        // Devuelve true si el campo de texto está vacío o solo contiene espacios.
        return editText.getText().toString().trim().isEmpty();
    }

    // Intenta guardar o actualizar la nota dependiendo de si es una nota nueva o una existente.
    private void saveNote() {
        if (!isEmpty(noteTitle) || !isEmpty(noteContent)){
            // Si es una nueva nota (noteId == -1), y hay contenido, crea una nueva nota.
            if (noteId == -1){
                createNote();
                // Si es una nota existente y hay contenido, actualiza la nota.
            }else if(noteId != -1){
                updateNote(noteId);
            }
        }else{
            Toast.makeText(context,"Necesita introducir texto",Toast.LENGTH_SHORT).show();
        }
    }
    private String parseDate (String dateString) throws ParseException {
        // Convertir la cadena de fecha a un objeto Date
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = isoFormat.parse(dateString);

        // Formatear la fecha a un formato más amigable
        SimpleDateFormat friendlyFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        String formattedDate = friendlyFormat.format(date);
        return formattedDate;
    }
   // Procesa los datos de intención, como el ID de la nota, para determinar si se está creando una nota nueva o editando una existente.
    private void processIntentData() {
        Intent intent = getIntent();
        this.noteId = intent.getIntExtra("NOTE_ID", -1);
        // Si es una nueva nota, limpia los campos de texto y oculta la fecha.
        if (noteId == -1) {
            noteTitle.setText("");
            noteContent.setText("");
            noteDate.setVisibility(View.GONE);
            // Si es una nota existente, carga sus detalles para editar.
        } else {
            obtainNote(noteId);
        }
    }
    // Muestra el panel de carga con un spinner para indicar que se está cargando o procesando algo.
    private void showLoadingPanel() {
        loadingPanel.setVisibility(View.VISIBLE);
        setButtonsEnabled(false);
        isLoadingData = true;
    }

    private void hideLoadingPanel() {
        loadingPanel.setVisibility(View.GONE);
        setButtonsEnabled(true);
        updateUndoRedoButtonState();
        isLoadingData = false;
        if(onBackPressed){
            finish();
        }
    }
    private void setButtonsEnabled(boolean isEnabled) {
        buttonCheck.setEnabled(false);
        buttonDelete.setEnabled(noteId != -1 && isEnabled);
        buttonUndo.setEnabled(isEnabled);
        buttonRedo.setEnabled(isEnabled);
        buttonBack.setEnabled(isEnabled);
        buttonFavorite.setEnabled(isEnabled);
        buttonNoteLetter.setEnabled(isEnabled);
        // Establecer la opacidad para cada botón
        float alpha = isEnabled ? 1.0f : 0.4f;
        buttonCheck.setAlpha(0.4f);
        buttonDelete.setAlpha(alpha);
        buttonUndo.setAlpha(alpha);
        buttonRedo.setAlpha(alpha);
        buttonBack.setAlpha(alpha);
        buttonFavorite.setAlpha(alpha);
        buttonNoteLetter.setAlpha(alpha);

    }

    // Inicializa los componentes de la interfaz de usuario y configura la cola de solicitudes para las operaciones de red.
    private void initUI() {
        // Encuentra y almacena referencias a los componentes de la interfaz de usuario en variables.
        loadingPanel = findViewById(R.id.loadingPanel);
        buttonBack = findViewById(R.id.buttonBack);
        buttonUndo = findViewById(R.id.buttonUndo);
        buttonRedo = findViewById(R.id.buttonRedo);
        buttonCheck = findViewById(R.id.buttonCheck);
        buttonNoteLetter = findViewById(R.id.buttonLetter);
        buttonDelete = findViewById(R.id.buttonDelete);
        buttonFavorite = findViewById(R.id.buttonFavorite);
        noteTitle = findViewById(R.id.noteTitle);
        noteDate = findViewById(R.id.noteDate);
        noteContent = findViewById(R.id.noteContent);
        locationName = findViewById(R.id.locationName);

        // Configura la cola de solicitudes HTTP.
        queue = Volley.newRequestQueue(this);

    }
    // Configura el estado inicial de los botones cuando la actividad se crea por primera vez.
    private void configureInitialButtonStates() {
        // El botón Delete se habilita solo si se está editando una nota existente (noteId != -1).
        configureButtonState(buttonDelete, noteId != -1);
        // Los botones Undo, Redo y Check se deshabilitan inicialmente.
        configureButtonState(buttonUndo, false);
        configureButtonState(buttonRedo, false);
        configureButtonState(buttonCheck, false);
    }
    // Configura los listeners para los eventos de clic en los botones.
    private void setupListeners() {
        // Utiliza expresiones lambda para simplificar el código de los listeners de los botones.
        buttonBack.setOnClickListener(v -> onBack());
        buttonCheck.setOnClickListener(v -> saveNote());
        // Botón para la eliminación de una nota
        buttonDelete.setOnClickListener(v -> setNoteDelete());
        buttonFavorite.setOnClickListener(v -> changueStateFavorites());
        buttonNoteLetter.setOnClickListener(v -> textIncresedDecreased());
    };
    private void setNoteDelete(){
        // Se puede eliminar una nota solo si existe
        if (noteId != -1) {
            deleteNote(noteId);
            finish();// Cierra la actividad
        }
    }
    private void changueStateFavorites(){
        // Cambia el estado de favorito
        noteFavorite = !noteFavorite;
        // Actualiza el icono del botón de favorito
        buttonFavorite.setImageResource(noteFavorite ? R.drawable.baseline_star_24 : R.drawable.baseline_star_border_24);
        configureButtonState(buttonCheck,true);
    }
    private void textIncresedDecreased(){
        if (!isTextSizeIncreased) {
            // Aumenta el tamaño del texto
            UiUtils.increaseTextSize(noteContent, noteTitle);
            buttonNoteLetter.setImageResource(R.drawable.baseline_text_decrease_24); // Cambia al ícono Aa
        } else {
            // Restablece el tamaño del texto
            UiUtils.resetTextSize(noteContent, noteTitle);
            buttonNoteLetter.setImageResource(R.drawable.baseline_text_increase_24); // Cambia al ícono aA
        }
        isTextSizeIncreased = !isTextSizeIncreased;

    }
    private void setupTextChangedListener(){
        // Agrega un TextWatcher a noteTitle para manejar cambios de texto.
        noteTitle.addTextChangedListener(new TextWatcher() {
            private String previousText = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                previousText = s.toString(); // Guarda el texto anterior antes de que cambie.
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Este método se invoca cuando el texto cambia. En este caso, no es necesario implementar nada.
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (isLoadingData) {
                    return; // Ignora los cambios si la actividad está cargando datos.
                }
                String newTitle = s.toString();
                // Comprueba si el título ha cambiado y actualiza el estado de los botones y el helper de deshacer/rehacer.
                if (!newTitle.equals(previousText)) {
                    undoRedoHelper.onTextChanged(newTitle, noteContent.getText().toString(), noteTitle.getSelectionStart(), noteContent.getSelectionStart(), "title");
                    previousText = newTitle;
                }
                updateUndoRedoButtonState();
                checkIfTextFieldsAreEmpty();            }
        });

        // Similar al TextWatcher de noteTitle, este se aplica a noteContent.
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
                checkIfTextFieldsAreEmpty();
            }

        });
    }
    private void setupListenersUndoRedo(){
        // Configura el listener para el botón de deshacer.
        buttonUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!undoRedoHelper.isUndoStackEmpty()) {
                    NoteState previousState = undoRedoHelper.undo();
                    noteTitle.setText(previousState.getTitle());
                    noteContent.setText(previousState.getContent());

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

        // Configura el listener para el botón de rehacer.
        buttonRedo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Verifica si la pila de rehacer no está vacía.
                if (!undoRedoHelper.isRedoStackEmpty()) {
                    // Obtiene el estado siguiente al cual rehacer.
                    NoteState nextState = undoRedoHelper.redo();
                    // Establece el título y contenido de la nota con los valores del estado rehacer.
                    noteTitle.setText(nextState.getTitle());
                    noteContent.setText(nextState.getContent());

                    // Enfoca y posiciona el cursor en el campo editado más recientemente.
                    if (nextState.getLastModifiedField().equals("title")) {
                        noteTitle.requestFocus();
                        noteTitle.setSelection(nextState.getTitleCursorPosition());
                    } else {
                        noteContent.requestFocus();
                        noteContent.setSelection(nextState.getContentCursorPosition());
                    }
                }
                // Actualiza el estado de los botones de deshacer y rehacer.
                updateUndoRedoButtonState();
            }
        });
    }
    // Control de errores
    public void error(VolleyError error){
        String errorMessage = "Error desconocido";
        if (error.networkResponse != null) {
            switch (error.networkResponse.statusCode) {
                case 404:
                    errorMessage = "Recurso no encontrado";
                    break;
                case 500:
                    errorMessage = "Error interno del servidor";
                    break;
                case 401:
                case 403:
                    errorMessage = "No autorizado o prohibido. Verifica tus permisos.";
                    break;
                case 204:
                    errorMessage = "No hay contenido disponible.";
                    break;
                default:
                    errorMessage = "Error en la petición: Código de estado HTTP " + error.networkResponse.statusCode;
                    break;
            }
        } else if (error instanceof NoConnectionError) {
            errorMessage = "Sin conexión a Internet.";
        } else if (error instanceof TimeoutError) {
            errorMessage = "Tiempo de espera agotado, por favor intente de nuevo.";
        }

        // Muestra un mensaje de error al usuario
        Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show();

    }
}
/*# NBA Notes - Aplicación Móvil
![Logo NBA Notes](https://github.com/bilrazor/NBA-Notes/assets/113417155/a1d48f80-152e-44a9-81cd-a14ba05d31f1)

## Descripción General
NBA Notes es una aplicación móvil diseñada para facilitar la gestión de notas, con funcionalidades intuitivas y una interfaz amigable.
## 👨‍💻 Integrantes del Equipo
- Billy Daniel Hanover Tapia
- Sergio Fariñas Fernández
- Narciso Cordeiro Ríos


## Pantallas de la Aplicación

### Pantalla 1: Registrarse
- **Icono de la Aplicación**: Situado en la parte superior para identificación inmediata.
- **Título**: "Register User".
- **Campos de Entrada**: Usuario, Correo Electrónico, Contraseña.
- **Botón de Registro**: Para crear una nueva cuenta.
- **Enlace a Iniciar Sesión**: Para usuarios existentes.

### Pantalla 2: Inicio de Sesión
- **Icono y Título**: Identificación clara de la función de la pantalla.
- **Campos de Usuario y Contraseña**: Con íconos representativos.
- **Botón de Inicio de Sesión**: Para acceder a la cuenta.
- **Enlace para Registrarse**: Para nuevos usuarios.

### Pantalla Principal: NBA Notes
- **Menú Hamburguesa y Media Luna**: Para navegación y cambio de tema.
- **Icono de Logout**: Para cerrar sesión.
- **Lista de Notas**: Con opciones de favoritos y botón para añadir nuevas notas.

### Menú de Navegación
- **Inicio, Pantalla de Perfil, Notas Favoritas**: Opciones de navegación rápida.
- **Ordenar Notas**: Por fecha de creación en orden ascendente o descendente.

### Pantalla de Creación/Edición de Notas
- **Herramientas de Edición**: Incluyen íconos de regreso,undo,redo, guardado,añadir a favoritos y eliminación.
- **Opciones de Formato de Texto**: Para personalizar el contenido de las notas el botón que hace crecer la letra.

## Validaciones del Lado del Cliente
- **Campos Requeridos**: Verificación de campos no vacíos en todas las pantallas.
- **Validaciones Específicas**: Formato de correo, fortaleza de contraseña, autenticación.

## Contribuciones
Si deseas contribuir al proyecto, por favor revisa el archivo [CONTRIBUTING.md](#).

## Contacto
Para cualquier consulta o sugerencia, puedes contactarnos a través de [CONTACT.md](#).

## Licencia
Este proyecto está licenciado bajo [LICENSE.md](#).
*/