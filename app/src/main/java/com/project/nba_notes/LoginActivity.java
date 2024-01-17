package com.project.nba_notes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    Button noAccountButton;
    Button logInButton;
    EditText editTextUsername;
    EditText editTextPassword;
    ImageButton showPassword;
    private Context context=this;
    private RequestQueue queue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        noAccountButton=findViewById(R.id.no_account_button);
        logInButton=findViewById(R.id.login_button);
        editTextUsername=findViewById(R.id.user_text);
        editTextPassword=findViewById(R.id.password_text);
        showPassword=findViewById(R.id.show_password_button);

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



        Drawable drawable3 = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ojo_abierto);
        Bitmap bitmap3 = ((BitmapDrawable) drawable3).getBitmap();
        Drawable newDrawable3 = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap3, 28, 18, true));

// Define el color que se va a usar
        int color3 = Color.parseColor("#FF7800");

// Aplica el filtro de color
        newDrawable3.setColorFilter(new PorterDuffColorFilter(color3, PorterDuff.Mode.SRC_IN));


        Drawable drawable4 = ContextCompat.getDrawable(getApplicationContext(), R.drawable.ojo_cerrado);
        Bitmap bitmap4 = ((BitmapDrawable) drawable4).getBitmap();
        Drawable newDrawable4 = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap4, 30, 30, true));

// Define el color que se va a usar
        int color4 = Color.parseColor("#FF7800");

// Aplica el filtro de color
        newDrawable4.setColorFilter(new PorterDuffColorFilter(color4, PorterDuff.Mode.SRC_IN));


        showPassword.setImageDrawable(newDrawable3);


        queue = Volley.newRequestQueue(this);
        noAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendPostLogin();
            }
        });

        showPassword.setOnClickListener(new View.OnClickListener() {
            boolean isImagenCambiada = false;
            @Override
            public void onClick(View v) {
                if (!isImagenCambiada) {
                    // Cambia la imagen
                    showPassword.setImageDrawable(newDrawable4);

                    Typeface tempTypeface = editTextPassword.getTypeface();

                    // Cambia el EditText de contraseña a texto normal
                    editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT);

                    // Restaura el tipo de fuente original
                    editTextPassword.setTypeface(tempTypeface);
                } else {
                    // Cambia la imagen de nuevo a la original
                    showPassword.setImageDrawable(newDrawable3);

                    Typeface tempTypeface = editTextPassword.getTypeface();

                    // Cambia el EditText de texto normal a contraseña
                    editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                    editTextPassword.setTypeface(tempTypeface);
                }

                // Cambia el estado de la imagen
                isImagenCambiada = !isImagenCambiada;
            }
        });
    }

    private void sendPostLogin() {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("username", editTextUsername.getText().toString());
            requestBody.put("password", editTextPassword.getText().toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                Server.name + "/api/auth/signin",
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String receivedToken;
                        try {
                            receivedToken = response.getString("sessionToken");
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        Toast.makeText(context,"Token: " + receivedToken, Toast.LENGTH_SHORT).show();
                        SharedPreferences preferences = context.getSharedPreferences("MySharedPref", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("username",editTextUsername.getText().toString());
                        editor.putString("token", receivedToken);
                        editor.commit();
                        finish();
                        Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse == null) {
                            Toast.makeText(context,"Error de conexión",Toast.LENGTH_LONG).show();
                        } else {
                            int serverCode = error.networkResponse.statusCode;
                            Toast.makeText(context,"El servidor respondió con "+serverCode,Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
        this.queue.add(request);
    }


}
