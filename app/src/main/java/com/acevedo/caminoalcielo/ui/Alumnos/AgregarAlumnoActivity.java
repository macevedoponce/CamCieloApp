package com.acevedo.caminoalcielo.ui.Alumnos;

import android.content.Intent;
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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.acevedo.caminoalcielo.Adapters.AvatarAdapter;
import com.acevedo.caminoalcielo.Clases.ProgressDialogHelper;
import com.acevedo.caminoalcielo.CreateAccount.CrearCuentaActivity;
import com.acevedo.caminoalcielo.Login.LoginActivity;
import com.acevedo.caminoalcielo.R;
import com.acevedo.caminoalcielo.Util.Util;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AgregarAlumnoActivity extends AppCompatActivity {

    TextInputLayout tilNombres, tilApellidos, tilDni;
    TextInputEditText edtNombres, edtApellidos, edtDni;
    Button btnAgregarAlumno;
    RecyclerView rvAvatars;
    TextView tvMensaje;
    RequestQueue requestQueue;
    ImageView ivAtras, ivLogo;

    LinearLayout llRegresar;
    private ProgressDialogHelper progressDialogHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        Util.obtenerListaAvatars();
        progressDialogHelper = new ProgressDialogHelper(this);
        setContentView(R.layout.activity_agregar_alumno);
        ivAtras = findViewById(R.id.ivAtrasCrearAlumno);
        ivLogo = findViewById(R.id.ivLogoCrearAlumno);
        llRegresar = findViewById(R.id.llRegresar);
        tvMensaje = findViewById(R.id.tvMensaje);
        tilNombres = findViewById(R.id.tilNombres);
        tilApellidos = findViewById(R.id.tilApellidos);
        tilDni = findViewById(R.id.tilDni);
        edtNombres = findViewById(R.id.edtNombres);
        edtApellidos = findViewById(R.id.edtApellidos);
        edtDni = findViewById(R.id.edtDni);
        btnAgregarAlumno = findViewById(R.id.btnAgregarAlumno);
        rvAvatars = findViewById(R.id.rvAvatars);
        requestQueue = Volley.newRequestQueue(this);
        rvAvatars.setHasFixedSize(true);
        rvAvatars.setLayoutManager(new GridLayoutManager(this, 3));

        AvatarAdapter avatarAdapter = new AvatarAdapter(Util.listAvatars);
        rvAvatars.setAdapter(avatarAdapter);
        ThemeActive();
        llRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });

        btnAgregarAlumno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidarDatos();
            }
        });

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
    }

    private void ValidarDatos() {
        String nombres = edtNombres.getText().toString();
        String apellidos = edtApellidos.getText().toString();
        String dni = edtDni.getText().toString();

        if(!nombres.isEmpty() && !apellidos.isEmpty() && !dni.isEmpty()){
            CrearAlumno(nombres, apellidos, dni);
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

    private void CrearAlumno(String nombres, String apellidos, String dni) {
        progressDialogHelper.showProgressDialog();
        String nombreAvatar = Util.nombreAvatar;
        String url = Util.RUTA_CREAR_ALUMNOS;
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            String status = jsonResponse.getString("status");
                            String message = jsonResponse.getString("message");

                            if(status.equals("success")){
                                progressDialogHelper.hideProgressDialog();
                                Toast.makeText(AgregarAlumnoActivity.this, message, Toast.LENGTH_SHORT).show();
                                getOnBackPressedDispatcher().onBackPressed();

                            }else{
                                tvMensaje.setVisibility(View.VISIBLE);
                                tvMensaje.setText(message);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(AgregarAlumnoActivity.this, e + "", Toast.LENGTH_SHORT).show();


                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar errores de la solicitud
                        progressDialogHelper.hideProgressDialog();
                        Toast.makeText(AgregarAlumnoActivity.this, "Error al registrar alumno", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("nombres", nombres);
                params.put("apellidos", apellidos);
                params.put("dni", dni);
                params.put("foto", nombreAvatar);
                params.put("id_rol", "2");
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
                ivAtras.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                ivLogo.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                break;
        }
    }

}