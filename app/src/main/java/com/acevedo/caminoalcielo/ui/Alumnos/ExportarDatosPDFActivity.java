package com.acevedo.caminoalcielo.ui.Alumnos;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.text.TextPaint;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.acevedo.caminoalcielo.Adapters.PuntosAdapter;
import com.acevedo.caminoalcielo.Clases.Puntos;
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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ExportarDatosPDFActivity extends AppCompatActivity {


    private static final int CREATE_FILE_REQUEST_CODE = 1;

    private PdfDocument document;

    int id_alumno, id_foto;
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
        binding.ivAtras.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());
        ThemeActive();
        obtenerDatosIntent();
        obtenerDatosAPI();
        binding.llDownload.setOnClickListener(v -> generarPDFXml(this, binding.getRoot()));

    }
    private void generarPDFXml(Context context, View view){
        binding.llDownload.setVisibility(View.INVISIBLE);
        binding.ivAtras.setVisibility(View.INVISIBLE);

        String nombre_documento = binding.tvDni.getText().toString() + "_" + binding.tvNombres.getText().toString() + ".pdf";

        // Crear un nuevo documento PDF
        document = new PdfDocument();

        // Obtener el tamaño de la vista
        int width = view.getWidth();
        int height = view.getHeight();

        // Crear una página en el documento con el tamaño de la vista
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(width, height, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        // Configurar el color del texto dependiendo del modo de tema del dispositivo
        int colorTexto = Color.BLACK; // Color negro para modo oscuro

        // Configurar el color del texto para todos los TextView en la vista
        ArrayList<TextView> textViews = getAllTextViews(view);
        for (TextView textView : textViews) {
            textView.setTextColor(colorTexto);
        }

        // Dibujar la vista en el lienzo
        view.draw(canvas);

        // Terminar la página y agregar al documento
        document.finishPage(page);

        // Crear una intención para que el usuario seleccione dónde guardar el archivo
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf"); // Define el tipo de archivo que deseas crear (en este caso, PDF)
        // Puedes agregar un nombre predeterminado para el archivo
        intent.putExtra(Intent.EXTRA_TITLE, nombre_documento);
        startActivityForResult(intent, CREATE_FILE_REQUEST_CODE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_FILE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            try {
                // Abre un flujo de salida para escribir en el archivo seleccionado por el usuario
                OutputStream outputStream = getContentResolver().openOutputStream(uri);
                if (outputStream != null) {
                    // Escribe el contenido del documento PDF en el flujo de salida
                    document.writeTo(outputStream);
                    // Cierra el flujo de salida
                    outputStream.close();
                    // Notifica al usuario que el PDF se ha guardado correctamente
                    Toast.makeText(ExportarDatosPDFActivity.this, "Pdf guardado", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // Si el usuario cancela la acción, puedes manejarlo aquí
        }

        // Cerrar el documento
        document.close();
    }


    // Método para obtener todos los TextView dentro de una vista
    private ArrayList<TextView> getAllTextViews(View view) {
        ArrayList<TextView> textViews = new ArrayList<>();
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View childView = viewGroup.getChildAt(i);
                if (childView instanceof TextView) {
                    textViews.add((TextView) childView);
                } else if (childView instanceof ViewGroup) {
                    textViews.addAll(getAllTextViews(childView));
                }
            }
        }
        return textViews;
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
                                binding.rvPuntos.setVisibility(View.VISIBLE);
                                binding.tvSinRegistros.setVisibility(View.GONE);

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
                                binding.rvPuntos.setVisibility(View.GONE);
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
        id_foto = getResources().getIdentifier(foto, "drawable", getPackageName());
        binding.ivFoto.setImageResource(id_foto);


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
                binding.ivSave.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                break;
        }
    }
}