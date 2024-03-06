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
import java.util.List;


public class AlumnosFragment extends Fragment {

    SwipeRefreshLayout srlActualizarAlumnos;

    RecyclerView rvAlumnos;
    RequestQueue requestQueue;
    List<Alumnos> alumnosList;
    AlumnoAdapter alumnoAdapter;

    FloatingActionButton fabAgregarTarea;

    private ProgressDialogHelper progressDialogHelper;

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
        progressDialogHelper = new ProgressDialogHelper(getContext());
        srlActualizarAlumnos = view.findViewById(R.id.srlActualizarAlumnos);
        fabAgregarTarea = view.findViewById(R.id.fabAgregarTarea);
        rvAlumnos = view.findViewById(R.id.rvAlumnos);
        rvAlumnos.setHasFixedSize(true);
        rvAlumnos.setLayoutManager(new LinearLayoutManager(getContext()));
        requestQueue = Volley.newRequestQueue(getContext());
        alumnosList = new ArrayList<>();
        alumnoAdapter = new AlumnoAdapter(getContext(), alumnosList);
        rvAlumnos.setAdapter(alumnoAdapter);
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
        progressDialogHelper.showProgressDialog();
        String url = Util.RUTA_LIST_ALUMNOS + "?id_rol=2";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialogHelper.hideProgressDialog();
                        try {
                            String status = response.getString("status");
                            if (status.equals("success")) {
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    int id = jsonObject.getInt("id");
                                    String nombres = jsonObject.getString("nombres");
                                    String apellidos = jsonObject.getString("apellidos");
                                    String dni = jsonObject.getString("dni");
                                    String foto = jsonObject.getString("foto");
                                    Alumnos alumnos = new Alumnos(id, nombres, apellidos, dni, foto);
                                    alumnosList.add(alumnos);
                                }
                                alumnoAdapter.notifyDataSetChanged();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialogHelper.hideProgressDialog();
                        error.printStackTrace();
                    }
                });

        requestQueue.add(request);
    }
}