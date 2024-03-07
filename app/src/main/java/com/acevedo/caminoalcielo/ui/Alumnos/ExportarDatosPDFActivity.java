package com.acevedo.caminoalcielo.ui.Alumnos;

import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.acevedo.caminoalcielo.Adapters.PuntosAdapter;
import com.acevedo.caminoalcielo.Clases.Puntos;
import com.acevedo.caminoalcielo.R;
import com.acevedo.caminoalcielo.Util.Util;
import com.acevedo.caminoalcielo.databinding.ActivityExportarDatosPdfactivityBinding;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ExportarDatosPDFActivity extends AppCompatActivity {
    int id_alumno;
    String nombres, apellidos, dni, foto;
    RequestQueue requestQueue;
    PuntosAdapter puntosAdapter;
    List<Puntos> puntosList;
    ActivityExportarDatosPdfactivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityExportarDatosPdfactivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        requestQueue = Volley.newRequestQueue(this);
        puntosList = new ArrayList<>();
        puntosAdapter = new PuntosAdapter(ExportarDatosPDFActivity.this, puntosList);
        binding.rvPuntos.setHasFixedSize(true);
        binding.rvPuntos.setLayoutManager(new LinearLayoutManager(this));
        binding.rvPuntos.setAdapter(puntosAdapter);
        ThemeActive();
        obtenerDatosIntent();
        obtenerDatosAPI();

    }

    private void obtenerDatosAPI() {
        String url = Util.RUTA_REPORTE_PUNTOS + "?id_user=" + id_alumno;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String status = response.getString("status");
                            if (status.equals("success")) {
                                binding.viewLoading.setVisibility(View.GONE);

                                String subtotalParticipacion = response.getString("subtotal_participacion");
                                String subtotalAsistencia = response.getString("subtotal_asistencia");
                                String subtotalBiblia = response.getString("subtotal_biblia");
                                String totalCompleto = response.getString("total_completo");

                                binding.tvSubTotalParticipacion.setText(subtotalParticipacion);
                                binding.tvSubTotalAsistencia.setText(subtotalAsistencia);
                                binding.tvSubTotalBiblia.setText(subtotalBiblia);
                                binding.tvTotalPuntos.setText(totalCompleto+"  Pts.");


                                JSONArray jsonArray = response.getJSONArray("data");

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    int ppart = jsonObject.getInt("puntos_participacion");
                                    int pasis = jsonObject.getInt("puntos_asistencia");
                                    int pbi = jsonObject.getInt("puntos_biblia");
                                    String fecha = jsonObject.getString("puntos_fecha");
                                    Puntos punto = new Puntos(ppart, pasis, pbi, fecha);
                                    puntosList.add(punto);
                                }
                                puntosAdapter.notifyDataSetChanged();
                            } else {
                                String message = response.getString("message");
                                binding.viewLoading.setVisibility(View.GONE);
                                binding.tvSinRegistros.setVisibility(View.VISIBLE);
                                binding.tvSinRegistros.setText(message);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ExportarDatosPDFActivity.this, "Error al obtener puntos del usuario" + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(request);
    }

    private void obtenerDatosIntent() {
        id_alumno = getIntent().getIntExtra("id_alumno", 0);
        nombres = getIntent().getStringExtra("nombres");
        apellidos = getIntent().getStringExtra("apellidos");
        dni = getIntent().getStringExtra("dni");
        foto = getIntent().getStringExtra("foto");


        binding.tvDni.setText(dni);
        binding.tvNombres.setText(nombres);
        binding.tvApellidos.setText(apellidos);
        int id_resource = getResources().getIdentifier(foto, "drawable", getPackageName());
        binding.ivFoto.setImageResource(id_resource);


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