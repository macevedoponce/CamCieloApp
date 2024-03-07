package com.acevedo.caminoalcielo.ui.Alumnos;

import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.acevedo.caminoalcielo.Clases.ProgressDialogHelper;
import com.acevedo.caminoalcielo.R;
import com.acevedo.caminoalcielo.Util.Util;
import com.acevedo.caminoalcielo.databinding.ActivityRegistrarPuntosBinding;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class RegistrarPuntosActivity extends AppCompatActivity {

    int id_alumno, puntoParticipacion = 0, puntoBiblia = 0, puntoAsistencia = 0;
    String nombres, apellidos, dni, foto, fecha;
    RequestQueue requestQueue;
    ProgressDialogHelper progressDialogHelper;

    private ActivityRegistrarPuntosBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityRegistrarPuntosBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        requestQueue = Volley.newRequestQueue(this);
        progressDialogHelper = new ProgressDialogHelper(this);
        ThemeActive();
        obtenerDatosIntent();
        binding.cvMenosParticipacion.setOnClickListener(v -> actualizarPuntos(-1, binding.tvPuntosParticipacion));
        binding.cvMasParticipacion.setOnClickListener(v -> actualizarPuntos(1, binding.tvPuntosParticipacion));
        binding.cvMenosAsistencia.setOnClickListener(v -> actualizarPuntos(-1, binding.tvPuntosAsistencia));
        binding.cvMasAsistencia.setOnClickListener(v -> actualizarPuntos(1, binding.tvPuntosAsistencia));
        binding.cvMenosBiblia.setOnClickListener(v -> actualizarPuntos(-1, binding.tvPuntosBiblia));
        binding.cvMasBiblia.setOnClickListener(v -> actualizarPuntos(1, binding.tvPuntosBiblia));
        
        
        binding.btnRegistrarPuntos.setOnClickListener(v -> registrarPuntos());
        binding.llRegresar.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

    }

    private void registrarPuntos() {
        progressDialogHelper.showProgressDialog();
        String puntPart = binding.tvPuntosParticipacion.getText().toString();
        String puntAsis = binding.tvPuntosAsistencia.getText().toString();
        String puntBib = binding.tvPuntosBiblia.getText().toString();
        String url = Util.RUTA_REGISTRAR_PUNTOS;

// Crear un Map para enviar los datos de los puntos a registrar
        Map<String, String> params = new HashMap<>();
        params.put("id_user", String.valueOf(id_alumno));
        params.put("puntos_participacion", puntPart);
        params.put("puntos_asistencia", puntAsis);
        params.put("puntos_biblia", puntBib);
        params.put("puntos_fecha", fecha);

        // Crear una solicitud StringRequest para enviar los datos al servidor
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
                                Toast.makeText(RegistrarPuntosActivity.this, message, Toast.LENGTH_SHORT).show();
                                getOnBackPressedDispatcher().onBackPressed();
                            }else{
                                progressDialogHelper.hideProgressDialog();
                                binding.tvMensaje.setVisibility(View.VISIBLE);
                                binding.tvMensaje.setText(message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(RegistrarPuntosActivity.this, "Error al registrar puntos", Toast.LENGTH_SHORT).show();

                    }

                }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };

        // Obtener la instancia de Volley y agregar la solicitud a la cola para su ejecución
        requestQueue.add(request);

    }

    private void actualizarPuntos(int puntos, TextView textView) {
        int nuevoPunto;
        if (textView == binding.tvPuntosParticipacion) {
            nuevoPunto = puntoParticipacion + puntos;
            if (nuevoPunto >= 0 && nuevoPunto <= 5) {
                puntoParticipacion = nuevoPunto;
                textView.setText(String.valueOf(puntoParticipacion));
            }
        } else if (textView == binding.tvPuntosAsistencia) {
            nuevoPunto = puntoAsistencia + puntos;
            if (nuevoPunto >= 0 && nuevoPunto <= 5) {
                puntoAsistencia = nuevoPunto;
                textView.setText(String.valueOf(puntoAsistencia));
            }
        } else if (textView == binding.tvPuntosBiblia) {
            nuevoPunto = puntoBiblia + puntos;
            if (nuevoPunto >= 0 && nuevoPunto <= 5) {
                puntoBiblia = nuevoPunto;
                textView.setText(String.valueOf(puntoBiblia));
            }
        }
    }



    private void obtenerDatosIntent() {
        id_alumno = getIntent().getIntExtra("id_alumno", 0);
        nombres = getIntent().getStringExtra("nombres");
        apellidos = getIntent().getStringExtra("apellidos");
        dni = getIntent().getStringExtra("dni");
        foto = getIntent().getStringExtra("foto");
        fecha = getIntent().getStringExtra("fecha");
        String puntPart = getIntent().getStringExtra("puntPart");
        String puntAsis = getIntent().getStringExtra("puntAsis");
        String puntBib = getIntent().getStringExtra("puntBib");


        binding.tvFecha.setText(fecha);
        binding.tvNombres.setText(nombres);
        binding.tvApellidos.setText(apellidos);
        int id_resource = getResources().getIdentifier(foto, "drawable", getPackageName());
        binding.ivFoto.setImageResource(id_resource);

        puntoParticipacion = parseAndSetPoints(puntPart, binding.tvPuntosParticipacion);
        puntoAsistencia = parseAndSetPoints(puntAsis, binding.tvPuntosAsistencia);
        puntoBiblia = parseAndSetPoints(puntBib, binding.tvPuntosBiblia);

    }

    private int parseAndSetPoints(String value, TextView textView) {
        int points;
        if (value.equals("null")) {
            points = 0;
        } else {
            textView.setText(value);
            points = Integer.parseInt(value);
        }
        return points;
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
                binding.ivAtras.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                binding.ivLogo.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                break;
        }
    }

}