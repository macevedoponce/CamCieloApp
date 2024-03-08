package com.acevedo.caminoalcielo.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.acevedo.caminoalcielo.MainActivity;
import com.acevedo.caminoalcielo.R;
import com.acevedo.caminoalcielo.Util.Util;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout tilDni, tilPassword;
    TextInputEditText edtDni, edtPassword;
    Button btnLogin;
    TextView tvCrearCuenta, tvMensaje;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        tilDni = findViewById(R.id.tilDniLogin);
        tilPassword = findViewById(R.id.tilPasswordLogin);
        edtDni = findViewById(R.id.edtDniLogin);
        edtPassword = findViewById(R.id.edtPasswordLogin);
        btnLogin = findViewById(R.id.btnLogin);
        tvCrearCuenta = findViewById(R.id.tvCrearCuenta);
        tvMensaje = findViewById(R.id.tvMensaje);
        requestQueue = Volley.newRequestQueue(this);
        recuperarPreferencias();
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        tvCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, CrearCuentaActivity.class);
                startActivity(i);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ValidarDatos();
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private void ValidarDatos() throws UnsupportedEncodingException {
        String dni = edtDni.getText().toString();
        String password = edtPassword.getText().toString();

        if(!dni.isEmpty() && !password.isEmpty()){
            ValidarUsuario(dni, password);
        }else{
            if(dni.isEmpty()){
                tilDni.setError("DNI no pueden ser vacios");
            }
            if(password.isEmpty()){
                tilPassword.setError("Contraseña no pueden ser vacios");
            }
        }
    }

    private void ValidarUsuario(String dni, String password) throws UnsupportedEncodingException {
        String encodedDni = URLEncoder.encode(dni, "UTF-8");
        String encodedPassword = URLEncoder.encode(password, "UTF-8");
        String url = Util.RUTA_VALIDAR_USUARIO +"?dni="+ encodedDni + "&password=" + encodedPassword;

        // Crear una solicitud JsonObjectRequest para enviar los datos al servidor
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Procesar la respuesta del servidor
                        try {
                            String status = response.getString("status");
                            if (status.equals("success")) {
                                // Usuario validado correctamente
                                JSONObject userData = response.getJSONObject("data");
                                String id = userData.getString("id");
                                String nombres = userData.getString("nombres");
                                String apellidos = userData.getString("apellidos");
                                String foto = userData.getString("foto");

                                // Guardar los datos del usuario en SharedPreferences
                                SharedPreferences sharedPreferences = getSharedPreferences("userLogin", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("id", id);
                                editor.putString("dni", dni);
                                editor.putString("password", password);
                                editor.putString("nombres", nombres);
                                editor.putString("apellidos", apellidos);
                                editor.putString("foto", foto);
                                editor.putBoolean("session", true);
                                editor.apply();

                                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(i);
                                finish();

                            } else {
                                // Error al validar el usuario
                                String message = response.getString("message");
                                tvMensaje.setVisibility(View.VISIBLE);
                                tvMensaje.setText(message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar errores de la solicitud
                        Toast.makeText(LoginActivity.this, "Error al validar usuario", Toast.LENGTH_SHORT).show();
                    }
                });

        // Obtener la instancia de Volley y agregar la solicitud a la cola para su ejecución
        requestQueue.add(request);
    }

    private void recuperarPreferencias() {
        SharedPreferences preferences = getSharedPreferences("userLogin", Context.MODE_PRIVATE);
        boolean session = preferences.getBoolean("session", false);

        if(session){
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }

    }
}
