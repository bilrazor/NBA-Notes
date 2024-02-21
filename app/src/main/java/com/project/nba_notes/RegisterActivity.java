package com.project.nba_notes;


import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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

import java.io.UnsupportedEncodingException;

public class RegisterActivity extends AppCompatActivity {
    private Button registerButton;
    private Button hasAccountButton;
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
        hasAccountButton = findViewById(R.id.has_account_button);
        queue = Volley.newRequestQueue(this);




        Drawable drawable1 = ContextCompat.getDrawable(getApplicationContext(), R.drawable.user_icon);
        Bitmap bitmap1 = ((BitmapDrawable) drawable1).getBitmap();
        Drawable newDrawable1 = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap1, 20, 20, true));

// Define el color que se va a usar
        int color1 = Color.parseColor("#FF7800");

// Aplica el filtro de color
        newDrawable1.setColorFilter(new PorterDuffColorFilter(color1, PorterDuff.Mode.SRC_IN));
        editTextUsername.setCompoundDrawablesWithIntrinsicBounds(null, null, newDrawable1, null);




        Drawable drawable2 = ContextCompat.getDrawable(getApplicationContext(), R.drawable.password_icon);
        Bitmap bitmap2 = ((BitmapDrawable) drawable2).getBitmap();
        Drawable newDrawable2 = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap2, 22, 22, true));

// Define el color que se va a usar
        int color2 = Color.parseColor("#FF7800");

// Aplica el filtro de color
        newDrawable2.setColorFilter(new PorterDuffColorFilter(color2, PorterDuff.Mode.SRC_IN));
        editTextPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, newDrawable2, null);




        Drawable drawable3 = ContextCompat.getDrawable(getApplicationContext(), R.drawable.email_icon);
        Bitmap bitmap3 = ((BitmapDrawable) drawable3).getBitmap();
        Drawable newDrawable3 = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap3, 20, 15, true));

// Define el color que se va a usar
        int color3 = Color.parseColor("#FF7800");

// Aplica el filtro de color
        newDrawable3.setColorFilter(new PorterDuffColorFilter(color3, PorterDuff.Mode.SRC_IN));
        editTextEmail.setCompoundDrawablesWithIntrinsicBounds(null, null, newDrawable3, null);



        Drawable drawable4 = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ojo_abierto);
        Bitmap bitmap4 = ((BitmapDrawable) drawable4).getBitmap();
        Drawable newDrawable4 = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap4, 28, 18, true));

// Define el color que se va a usar
        int color4 = Color.parseColor("#FF7800");

// Aplica el filtro de color
        newDrawable4.setColorFilter(new PorterDuffColorFilter(color4, PorterDuff.Mode.SRC_IN));



        Drawable drawable5 = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ojo_cerrado);
        Bitmap bitmap5 = ((BitmapDrawable) drawable5).getBitmap();
        Drawable newDrawable5 = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap5, 30, 30, true));

// Define el color que se va a usar
        int color5 = Color.parseColor("#FF7800");

// Aplica el filtro de color
        newDrawable5.setColorFilter(new PorterDuffColorFilter(color5, PorterDuff.Mode.SRC_IN));



        showPassword1.setImageDrawable(newDrawable5);
        showPassword2.setImageDrawable(newDrawable5);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = editTextPassword.getText().toString();
                String password2 = editTextPassword2.getText().toString();
                CharSequence email = editTextEmail.getText().toString();

                if(editTextUsername.getText().toString().isEmpty()){
                    editTextUsername.setError("Este campo es obligatorio");
                }if(editTextEmail.getText().toString().isEmpty()){
                    editTextEmail.setError("Este campo es obligatorio");
                }if(editTextPassword.getText().toString().isEmpty()){
                    editTextPassword.setError("Este campo es obligatorio");
                }if(editTextUsername.getText().toString().isEmpty()) {
                    editTextPassword2.setError("Este campo es obligatorio");
                }if (editTextPassword.getText().toString().length()<6){
                    editTextPassword.setError("La contraseña debe tener un mínimo de 6 carácteres");
                } else if (password.equals(password2) && esEmailValido(email)) {
                    registerUser();
                } else {
                    if (!password.equals(password2)) {
                        editTextPassword2.setError("Las contraseñas no coinciden");
                    }
                    if (!esEmailValido(email)) {
                        editTextEmail.setError("Introduce un email válido");
                    }
                }
            }
        });

        hasAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });


        showPassword1.setOnClickListener(new View.OnClickListener() {
            boolean isImagenCambiada = false;
            @Override
            public void onClick(View v) {
                if (!isImagenCambiada) {
                    // Cambia la imagen
                    showPassword1.setImageDrawable(newDrawable4);

                    Typeface tempTypeface = editTextPassword.getTypeface();

                    // Cambia el EditText de contraseña a texto normal
                    editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT);

                    // Restaura el tipo de fuente original
                    editTextPassword.setTypeface(tempTypeface);
                } else {
                    // Cambia la imagen de nuevo a la original
                    showPassword1.setImageDrawable(newDrawable5);

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
                    showPassword2.setImageDrawable(newDrawable4);

                    Typeface tempTypeface = editTextPassword2.getTypeface();

                    // Cambia el EditText de contraseña a texto normal
                    editTextPassword2.setInputType(InputType.TYPE_CLASS_TEXT);

                    // Restaura el tipo de fuente original
                    editTextPassword2.setTypeface(tempTypeface);
                } else {
                    // Cambia la imagen de nuevo a la original
                    showPassword2.setImageDrawable(newDrawable5);

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
                        setContentView(R.layout.loading_screen);
                        Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                        startActivity(intent);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse == null) {
                            Toast.makeText(RegisterActivity.this,"Error de conexión",Toast.LENGTH_LONG).show();
                        } else {
                            String serverCode = null;
                            try {
                                serverCode = new String(error.networkResponse.data,"utf-8");

                            } catch (UnsupportedEncodingException e) {
                                throw new RuntimeException(e);
                            }
                            Toast.makeText(RegisterActivity.this,serverCode,Toast.LENGTH_LONG).show();
                        }

                    }

                }

        );
        this.queue.add(request);
    }

}
