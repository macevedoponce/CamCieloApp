package com.acevedo.caminoalcielo.CreateAccount;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.acevedo.caminoalcielo.Login.LoginActivity;
import com.acevedo.caminoalcielo.R;
import com.acevedo.caminoalcielo.Util.Util;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CrearCuentaActivity extends AppCompatActivity {

    TextInputLayout tilNombres, tilApellidos, tilDni, tilPassword;
    TextInputEditText edtNombres, edtApellidos, edtDni, edtPassword;
    Button btnCrearCuenta;
    TextView tvLogin,tvMensaje;

    ImageView ivAtras, ivLogo;

    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crear_cuenta);

        tilNombres = findViewById(R.id.tilNombres);
        tilApellidos = findViewById(R.id.tilApellidos);
        tilDni = findViewById(R.id.tilDni);
        tilPassword = findViewById(R.id.tilPassword);
        edtNombres = findViewById(R.id.edtNombres);
        edtApellidos = findViewById(R.id.edtApellidos);
        edtDni = findViewById(R.id.edtDni);
        edtPassword = findViewById(R.id.edtPassword);
        btnCrearCuenta = findViewById(R.id.btnCrearCuenta);
        tvLogin = findViewById(R.id.tvLogin);
        ivAtras = findViewById(R.id.ivAtras);
        ivLogo = findViewById(R.id.ivLogo);
        tvMensaje = findViewById(R.id.tvMensaje);

        requestQueue = Volley.newRequestQueue(this);

        ThemeActive();

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });

        ivAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });

        btnCrearCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidarCampos();
            }
        });

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }

    private void ThemeActive() {
        // Verificar el modo actual del tema
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // Modo claro
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                // Modo oscuro
                ivAtras.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                ivLogo.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                break;
        }
    }

    private void ValidarCampos() {
    //obtener datos escritos
        String nombres = edtNombres.getText().toString();
        String apellidos = edtApellidos.getText().toString();
        String dni = edtDni.getText().toString();
        String password = edtPassword.getText().toString();

        //validar que no hay datos vacios
        if(!nombres.isEmpty() && !apellidos.isEmpty() && !dni.isEmpty() && !password.isEmpty()){
            CrearCuenta(nombres, apellidos, dni, password);
        }else{

            if(nombres.isEmpty()){
                tilNombres.setError("Nombres no pueden ser vacios");
            }
            if(apellidos.isEmpty()){
                tilApellidos.setError("Apellidos no pueden ser vacios");
            }
            if(dni.isEmpty()){
                tilDni.setError("DNI no pueden ser vacios");
            }
            if(password.isEmpty()){
                tilPassword.setError("Contraseña no pueden ser vacios");
            }
        }
    }

    private void CrearCuenta(String nombres, String apellidos, String dni, String password) {
        String url = Util.RUTA_CREAR_USUARIO;

        // Crear una solicitud JsonObjectRequest para enviar los datos al servidor
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Procesar la respuesta del servidor
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String status = jsonResponse.getString("status");
                            String message = jsonResponse.getString("message");

                            if(status.equals("success")){
                                Toast.makeText(CrearCuentaActivity.this, message, Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(CrearCuentaActivity.this, LoginActivity.class);
                                startActivity(i);
                                finish();

                            }else{
                                tvMensaje.setVisibility(View.VISIBLE);
                                tvMensaje.setText(message);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(CrearCuentaActivity.this, e + "", Toast.LENGTH_SHORT).show();


                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar errores de la solicitud
                        Toast.makeText(CrearCuentaActivity.this, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nombres", nombres);
                params.put("apellidos", apellidos);
                params.put("dni", dni);
                params.put("password", password);
                params.put("id_rol", "1");
                return params;
            }
        };

        // Agregar la solicitud a la cola de Volley para su ejecución
        requestQueue.add(request);
    }
}