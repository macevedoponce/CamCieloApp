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
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class ExportarDatosPDFActivity extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_CODE = 23;
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
        binding.llDownload.setOnClickListener(v -> validarPermisos());

    }

    private void validarPermisos(){
        // Verificar si los permisos ya están concedidos
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                // Los permisos ya están concedidos, puedes realizar la otra función
                //generarPDF();
                generarPDFXml(this, binding.getRoot());
            } else {
                // Los permisos no están concedidos, solicitarlos nuevamente
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, STORAGE_PERMISSION_CODE);
            }
        }else{
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            == PackageManager.PERMISSION_GRANTED) {
                // Los permisos están concedidos, puedes realizar la otra función
                //generarPDF();
                generarPDFXml(this, binding.getRoot());

            } else {
                // Los permisos no están concedidos, solicítalos nuevamente
                requestForStoragePermissions();
            }
        }
    }

    private void requestForStoragePermissions() {
        //Android is 11 (R) or above
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            try {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                intent.setData(uri);
                storageActivityResultLauncher.launch(intent);
            }catch (Exception e){
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                storageActivityResultLauncher.launch(intent);
            }
        }else{
            //Below android 11
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    },
                    STORAGE_PERMISSION_CODE
            );
        }

    }

    private ActivityResultLauncher<Intent> storageActivityResultLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>(){

                        @Override
                        public void onActivityResult(ActivityResult o) {
                            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
                                //Android is 11 (R) or above
                                if(Environment.isExternalStorageManager()){
                                    //Manage External Storage Permissions Granted
                                    Log.d(TAG, "onActivityResult: Manage External Storage Permissions Granted");
                                }else{
                                    Toast.makeText(ExportarDatosPDFActivity.this, "Storage Permissions Denied", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                //Below android 11

                            }
                        }
                    });

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == STORAGE_PERMISSION_CODE){
            if(grantResults.length > 0){
                boolean write = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean read = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if(read && write){
                    Toast.makeText(ExportarDatosPDFActivity.this, "Storage Permissions Granted", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ExportarDatosPDFActivity.this, "Storage Permissions Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    private void generarPDFXml(Context context, View view){
        binding.llDownload.setVisibility(View.INVISIBLE);
        binding.ivAtras.setVisibility(View.INVISIBLE);

        String nombre_documento = binding.tvDni.getText().toString() + "_" + binding.tvNombres.getText().toString() + ".pdf";

        // Crear un nuevo documento PDF
        PdfDocument document = new PdfDocument();

        // Obtener el tamaño de la vista
        int width = view.getWidth();
        int height = view.getHeight();

        // Crear una página en el documento con el tamaño de la vista
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(width, height, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        // Dibujar la vista en el lienzo
        view.draw(canvas);

        // Terminar la página y agregar al documento
        document.finishPage(page);

        // Guardar el documento PDF
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),  nombre_documento);
        try {
            document.writeTo(new FileOutputStream(file));
            Toast.makeText(context, "Pdf: "+ nombre_documento + " descargado", Toast.LENGTH_SHORT).show();
            finish();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Cerrar el documento
        document.close();
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