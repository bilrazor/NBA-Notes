package com.project.nba_notes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

//Esta actividad es para la creacion de notas
public class NotesActivity extends AppCompatActivity {
    private Context context = this;
    private EditText noteTitle;
    private EditText noteContent;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        ImageButton buttonBack = findViewById(R.id.buttonBack);
        ImageButton buttonUndo = findViewById(R.id.buttonUndo);
        ImageButton buttonRedo = findViewById(R.id.buttonRedo);
        ImageButton buttonCheck = findViewById(R.id.buttonCheck);
        noteTitle = findViewById(R.id.noteTitle);
        noteContent = findViewById(R.id.noteContent);
        obtainNote(1);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        buttonUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        buttonRedo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        buttonCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNote();
                Toast.makeText(context,"Nota creada",Toast.LENGTH_LONG).show();

            }
        });


    }
    public void obtainNote(int id) {
        JsonObjectRequest request = new  JsonObjectRequest (
                Request.Method.GET,
                Server.name+"/api/auth/notes/" + id,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Manejo correcto de la respuesta
                        try {
                            String title = response.getString("title");
                            String content = response.getString("content");
                            noteTitle.setText(title);
                            noteContent.setText(content);
                        } catch (JSONException e) {
                            e.printStackTrace(); // Manejo del error
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context,"No se ha podido conectar al servidor",Toast.LENGTH_LONG).show();;
                    }
                }
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
    public void createNote() {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("title", noteTitle.getText().toString());
            requestBody.put("content", noteContent.getText().toString()); // Corregido: falta paréntesis
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequestWithAuthHeader request = new JsonObjectRequestWithAuthHeader(
                Request.Method.POST,
                Server.name+"/api/auth/notes",
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(context,"Nota creada",Toast.LENGTH_SHORT).show();;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejo de error
                    }
                }
        );
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}
