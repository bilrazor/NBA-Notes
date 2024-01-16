package com.project.nba_notes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
        Drawable newdrawable1 = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap1, 20, 20, true));
        editTextUsername.setCompoundDrawablesWithIntrinsicBounds(null, null, newdrawable1, null);

        Drawable drawable2 = ContextCompat.getDrawable(getApplicationContext(), R.drawable.password_icon);
        Bitmap bitmap2 = ((BitmapDrawable) drawable2).getBitmap();
        Drawable newdrawable2 = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap2, 20, 25, true));
        editTextPassword.setCompoundDrawablesWithIntrinsicBounds(null, null, newdrawable2, null);
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
                    showPassword.setImageResource(R.drawable.ojo_cerrado);

                    Typeface tempTypeface = editTextPassword.getTypeface();

                    // Cambia el EditText de contraseña a texto normal
                    editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT);

                    // Restaura el tipo de fuente original
                    editTextPassword.setTypeface(tempTypeface);
                } else {
                    // Cambia la imagen de nuevo a la original
                    showPassword.setImageResource(R.drawable.ojo_abierto);

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
                            Toast.makeText(context,"La conexión no se ha establecido",Toast.LENGTH_LONG).show();
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
