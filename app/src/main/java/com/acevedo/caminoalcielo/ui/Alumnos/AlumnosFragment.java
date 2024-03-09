package com.acevedo.caminoalcielo.ui.Alumnos;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.acevedo.caminoalcielo.Adapters.AlumnoAdapter;
import com.acevedo.caminoalcielo.Clases.Alumnos;
import com.acevedo.caminoalcielo.Clases.ProgressDialogHelper;
import com.acevedo.caminoalcielo.R;
import com.acevedo.caminoalcielo.Util.Util;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class AlumnosFragment extends Fragment {

    SwipeRefreshLayout srlActualizarAlumnos;
    LinearLayout viewLoading;
    RecyclerView rvAlumnos;
    RequestQueue requestQueue;
    List<Alumnos> alumnosList;
    AlumnoAdapter alumnoAdapter;
    TextView tvSinAlumnos;

    String fechaActual;
    FloatingActionButton fabAgregarTarea;

    public AlumnosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alumnos, container, false);
        tvSinAlumnos = view.findViewById(R.id.tvSinAlumnos);
        viewLoading = view.findViewById(R.id.viewLoading);
        srlActualizarAlumnos = view.findViewById(R.id.srlActualizarAlumnos);
        fabAgregarTarea = view.findViewById(R.id.fabAgregarTarea);
        rvAlumnos = view.findViewById(R.id.rvAlumnos);
        rvAlumnos.setHasFixedSize(true);
        rvAlumnos.setLayoutManager(new LinearLayoutManager(getContext()));
        requestQueue = Volley.newRequestQueue(getContext());
        alumnosList = new ArrayList<>();
        alumnoAdapter = new AlumnoAdapter(getContext(), alumnosList);
        rvAlumnos.setAdapter(alumnoAdapter);
        obtenerFechaActual();
        cargarAlumnos();

        srlActualizarAlumnos.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                alumnosList.clear();
                cargarAlumnos();
                srlActualizarAlumnos.setRefreshing(false);
            }
        });
        fabAgregarTarea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AgregarAlumnoActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

    private void cargarAlumnos() {
        String url = Util.RUTA_LIST_ALUMNOS + "?id_rol=2&fecha=" + fechaActual;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            if (status.equals("success")) {
                                viewLoading.setVisibility(View.GONE);
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    int id = jsonObject.getInt("user_id");
                                    String nombres = jsonObject.getString("user_nombres");
                                    String apellidos = jsonObject.getString("user_apellidos");
                                    String dni = jsonObject.getString("user_dni");
                                    String foto = jsonObject.getString("user_foto");
                                    String puntPart = jsonObject.getString("puntos_participacion");
                                    String puntAsis = jsonObject.getString("puntos_asistencia");
                                    String puntBib = jsonObject.getString("puntos_biblia");
                                    Alumnos alumnos = new Alumnos(id, nombres, apellidos, dni, foto, puntPart, puntAsis, puntBib);
                                    alumnosList.add(alumnos);
                                }
                                alumnoAdapter.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        DialogoMenu(v);
                                    }
                                });
                                alumnoAdapter.notifyDataSetChanged();
                            }else{
                                viewLoading.setVisibility(View.GONE);
                                rvAlumnos.setVisibility(View.GONE);
                                tvSinAlumnos.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getContext(), "No podemos acceder a nuestro servidor", Toast.LENGTH_SHORT).show();
                    }
                });

        requestQueue.add(request);
    }

    private void DialogoMenu(View view) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView( R.layout.dialog_menu_alumno);


        //datos del item seleccionado
        int id = alumnosList.get(rvAlumnos.getChildAdapterPosition(view)).getUser_id();
        String nombres = alumnosList.get(rvAlumnos.getChildAdapterPosition(view)).getUser_nombres();
        String apellidos = alumnosList.get(rvAlumnos.getChildAdapterPosition(view)).getUser_apellidos();
        String dni = alumnosList.get(rvAlumnos.getChildAdapterPosition(view)).getUser_dni();
        String foto = alumnosList.get(rvAlumnos.getChildAdapterPosition(view)).getUser_foto();
        String puntPart = alumnosList.get(rvAlumnos.getChildAdapterPosition(view)).getPuntos_participacion();
        String puntAsis = alumnosList.get(rvAlumnos.getChildAdapterPosition(view)).getPuntos_asistencia();
        String puntBib = alumnosList.get(rvAlumnos.getChildAdapterPosition(view)).getPuntos_biblia();

        CardView cvRegistrarPuntos = dialog.findViewById(R.id.cvRegistrarPuntos);
        CardView cvExportarPDF = dialog.findViewById(R.id.cvExportarPDF);

        cvRegistrarPuntos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent i = new Intent(getContext(), RegistrarPuntosActivity.class);
                i.putExtra("id_alumno",id);
                i.putExtra("nombres",nombres);
                i.putExtra("apellidos",apellidos);
                i.putExtra("dni",dni);
                i.putExtra("foto",foto);
                i.putExtra("puntPart",puntPart);
                i.putExtra("puntAsis",puntAsis);
                i.putExtra("puntBib",puntBib);
                i.putExtra("fecha",fechaActual);
                startActivity(i);
            }
        });

        cvExportarPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent i = new Intent(getContext(), ExportarDatosPDFActivity.class);
                i.putExtra("id_alumno",id);
                i.putExtra("nombres",nombres);
                i.putExtra("apellidos",apellidos);
                i.putExtra("dni",dni);
                i.putExtra("foto",foto);
                startActivity(i);
            }
        });




        dialog.show();
        dialog.setCancelable(true);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
    }

    private void obtenerFechaActual() {
        // Obtener la fecha y hora actuales
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // El mes comienza desde 0
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        fechaActual = year + "-" + month + "-" + dayOfMonth;

    }
}