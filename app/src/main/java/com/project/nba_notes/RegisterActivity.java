package com.project.nba_notes;


import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    private Button registerButton;
    private ImageButton showPassword1;
    private ImageButton showPassword2;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextPassword2;
    private EditText editTextEmail;
    private RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextUsername = findViewById(R.id.edit_text_username);
        editTextPassword = findViewById(R.id.edit_text_password);
        editTextPassword2 = findViewById(R.id.edit_text_password_confirm);
        registerButton = findViewById(R.id.button_create_account);
        showPassword1 = findViewById(R.id.show_password_button1);
        showPassword2 = findViewById(R.id.show_password_button2);
        editTextEmail = findViewById(R.id.edit_text_email);
        queue = Volley.newRequestQueue(this);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = editTextPassword.getText().toString();
                String password2 = editTextPassword2.getText().toString();
                CharSequence email = editTextEmail.getText().toString();
                if (!password.equals(password2)) {
                    editTextPassword2.setError("Las contraseñas no coinciden");
                }if (!esEmailValido(email)) {
                    editTextEmail.setError("Introduce un email válido");
                } else {
                    Toast.makeText(RegisterActivity.this,"Registro correcto", Toast.LENGTH_LONG).show();
                    registerUser();
                }

            }
        });


        showPassword1.setOnClickListener(new View.OnClickListener() {
            boolean isImagenCambiada = false;
            @Override
            public void onClick(View v) {
                if (!isImagenCambiada) {
                    // Cambia la imagen
                    showPassword1.setImageResource(R.drawable.ojo_cerrado);

                    Typeface tempTypeface = editTextPassword.getTypeface();

                    // Cambia el EditText de contraseña a texto normal
                    editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT);

                    // Restaura el tipo de fuente original
                    editTextPassword.setTypeface(tempTypeface);
                } else {
                    // Cambia la imagen de nuevo a la original
                    showPassword1.setImageResource(R.drawable.ojo_abierto);

                    Typeface tempTypeface = editTextPassword.getTypeface();

                    // Cambia el EditText de texto normal a contraseña
                    editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                    editTextPassword.setTypeface(tempTypeface);
                }

                // Cambia el estado de la imagen
                isImagenCambiada = !isImagenCambiada;
            }
        });


        showPassword2.setOnClickListener(new View.OnClickListener() {
            boolean isImagenCambiada = false;
            @Override
            public void onClick(View v) {
                if (!isImagenCambiada) {
                    // Cambia la imagen
                    showPassword2.setImageResource(R.drawable.ojo_cerrado);

                    Typeface tempTypeface = editTextPassword2.getTypeface();

                    // Cambia el EditText de contraseña a texto normal
                    editTextPassword2.setInputType(InputType.TYPE_CLASS_TEXT);

                    // Restaura el tipo de fuente original
                    editTextPassword2.setTypeface(tempTypeface);
                } else {
                    // Cambia la imagen de nuevo a la original
                    showPassword2.setImageResource(R.drawable.ojo_abierto);

                    Typeface tempTypeface = editTextPassword2.getTypeface();

                    // Cambia el EditText de texto normal a contraseña
                    editTextPassword2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                    editTextPassword2.setTypeface(tempTypeface);
                }

                // Cambia el estado de la imagen
                isImagenCambiada = !isImagenCambiada;
            }
        });


    }
    boolean esEmailValido(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private void registerUser(){
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("username", editTextUsername.getText().toString());
            requestBody.put("password", editTextPassword.getText().toString());
            requestBody.put("email", editTextEmail.getText().toString());

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                Server.name + "/api/auth/signup",
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(RegisterActivity.this,"Registro correcto", Toast.LENGTH_LONG).show();
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse == null) {
                            Toast.makeText(RegisterActivity.this,"La conexión no se ha establecido",Toast.LENGTH_LONG).show();
                        } else {
                            int serverCode = error.networkResponse.statusCode;
                            Toast.makeText(RegisterActivity.this,"El servidor respondió con "+serverCode,Toast.LENGTH_LONG).show();
                        }

                    }

                }

        );
        this.queue.add(request);
    }

}
