package com.acevedo.caminoalcielo.ui.Cuenta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.acevedo.caminoalcielo.Clases.ProgressDialogHelper;
import com.acevedo.caminoalcielo.R;
import com.acevedo.caminoalcielo.Util.Util;
import com.acevedo.caminoalcielo.ui.CrearCuentaActivity;
import com.acevedo.caminoalcielo.ui.LoginActivity;
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

public class ProfileActivity extends AppCompatActivity {

    ImageView ivLogo, ivAtras;
    LinearLayout lyRegresar;

    TextInputLayout tilNombres, tilApellidos, tilDni;
    TextInputEditText edtNombres, edtApellidos, edtDni;
    TextView info;
    Button btnActualizar;

    String id, nombres, apellidos, dni;

    RequestQueue requestQueue;
    ProgressDialogHelper progressDialogHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        info = findViewById(R.id.info);
        ivLogo = findViewById(R.id.ivLogoProfile);
        ivAtras = findViewById(R.id.ivAtrasProfile);
        lyRegresar = findViewById(R.id.lyRegresar);
        tilNombres = findViewById(R.id.tilNombres);
        tilApellidos = findViewById(R.id.tilApellidos);
        tilDni = findViewById(R.id.tilDni);
        edtNombres = findViewById(R.id.edtNombres);
        edtApellidos = findViewById(R.id.edtApellidos);
        edtDni = findViewById(R.id.edtDni);
        btnActualizar = findViewById(R.id.btnActualizar);

        requestQueue = Volley.newRequestQueue(this);
        progressDialogHelper = new ProgressDialogHelper(this);
        ThemeActive();
        obtenerDatosIntent();
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidarCampos();
            }
        });

        lyRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }

    private void obtenerDatosIntent() {
        id = getIntent().getStringExtra("id_usuario");
        nombres = getIntent().getStringExtra("nombres");
        apellidos = getIntent().getStringExtra("apellidos");
        dni = getIntent().getStringExtra("dni");
        edtNombres.setText(nombres);
        edtApellidos.setText(apellidos);
        edtDni.setText(dni);
    }

    private void ValidarCampos() {
        //obtener datos escritos
        String nombres = edtNombres.getText().toString();
        String apellidos = edtApellidos.getText().toString();
        String dni = edtDni.getText().toString();

        //validar que no hay datos vacios
        if(!nombres.isEmpty() && !apellidos.isEmpty() && !dni.isEmpty()){
            actualizarDatos(nombres, apellidos, dni);
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
        }
    }

    private void actualizarDatos(String nombresEdt, String apellidosEdt, String dniEdt) {
        progressDialogHelper.showProgressDialog();
        String url = Util.RUTA_ACTUALIZAR_USUARIO;

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
                                progressDialogHelper.hideProgressDialog();
                                Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                                // Guardar los datos del usuario en SharedPreferences
                                SharedPreferences sharedPreferences = getSharedPreferences("userLoginDocenteCaminoCieloApp", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("dni", dniEdt);
                                editor.putString("nombres", nombresEdt);
                                editor.putString("apellidos", apellidosEdt);
                                editor.apply();
                                finish();

                            }else{
                                info.setText(message);
                                info.setTextColor(Color.RED);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ProfileActivity.this, e + "", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar errores de la solicitud
                        progressDialogHelper.hideProgressDialog();
                        Toast.makeText(ProfileActivity.this, "Error al actualizar usuario", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nombres", nombresEdt);
                params.put("apellidos", apellidosEdt);
                params.put("dni", dniEdt);
                params.put("id_user", id);
                return params;
            }
        };

        // Agregar la solicitud a la cola de Volley para su ejecución
        requestQueue.add(request);
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
                ivLogo.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                ivAtras.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                break;
        }
    }
}