package com.acevedo.caminoalcielo.ui.Inicio;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.acevedo.caminoalcielo.Adapters.AlumnoAdapter;
import com.acevedo.caminoalcielo.Adapters.AlumnoTopAdapter;
import com.acevedo.caminoalcielo.Clases.Alumnos;
import com.acevedo.caminoalcielo.R;
import com.acevedo.caminoalcielo.Util.Util;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class InicioFragment extends Fragment {

    ShimmerFrameLayout sfl;

    TextView tvNombres, tvApellidos,tvSinAlumnos;
    ImageView ivBanner;
    View viewDegradado;

    RecyclerView rvTopAlumnos;
    RequestQueue requestQueue;
    List<Alumnos> alumnosList;
    AlumnoTopAdapter alumnoAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_inicio, container, false);
        requestQueue = Volley.newRequestQueue(getContext());
        tvSinAlumnos = view.findViewById(R.id.tvSinAlumnos);
        sfl = view.findViewById(R.id.sfl);
        tvNombres = view.findViewById(R.id.tvNombres);
        tvApellidos = view.findViewById(R.id.tvApellidos);
        ivBanner = view.findViewById(R.id.ivBanner);
        viewDegradado = view.findViewById(R.id.viewDegradado);
        rvTopAlumnos = view.findViewById(R.id.rvTopAlumnos);
        rvTopAlumnos.setHasFixedSize(true);
        rvTopAlumnos.setLayoutManager(new GridLayoutManager(getContext(), 2));;
        alumnosList = new ArrayList<>();
        alumnoAdapter = new AlumnoTopAdapter(getContext(), alumnosList);
        rvTopAlumnos.setAdapter(alumnoAdapter);
        ThemeActive();
        RecuperarPreferencias();
        cargarTopAlumnos();
        return view;
    }

    private void cargarTopAlumnos() {
        sfl.setVisibility(View.VISIBLE);
        String url = Util.RUTA_LIST_TOP_PUNTOS;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");

                            if (status.equals("success")) {
                                sfl.setVisibility(View.GONE);
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    //int id = jsonObject.getInt("user_id");
                                    String nombres = jsonObject.getString("user_nombres");
                                    String apellidos = jsonObject.getString("user_apellidos");
                                    String foto = jsonObject.getString("user_foto");
                                    String total_puntos = jsonObject.getString("total_puntos");
                                    Alumnos alumnos = new Alumnos(nombres, apellidos, foto, total_puntos);
                                    alumnosList.add(alumnos);
                                }
                                alumnoAdapter.notifyDataSetChanged();
                            }else{

                                sfl.setVisibility(View.GONE);
                                rvTopAlumnos.setVisibility(View.GONE);
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

    private void ThemeActive() {
        // Verificar el modo actual del tema
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // Modo claro
                ivBanner.setImageResource(R.drawable.dia_bg);
                viewDegradado.setBackgroundResource(R.drawable.degradado_dia);
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                // Modo oscuro
                ivBanner.setImageResource(R.drawable.noche_bg);
                viewDegradado.setBackgroundResource(R.drawable.degradado_noche);
                break;
        }
    }


        private void RecuperarPreferencias() {
        SharedPreferences preferences = requireContext().getSharedPreferences("userLoginDocenteCaminoCieloApp", Context.MODE_PRIVATE);
        String nombres = preferences.getString("nombres","");
        String apellidos = preferences.getString("apellidos","");
        String lenguaje = preferences.getString("lenguaje","");
        if(lenguaje.isEmpty()){
            lenguaje = "es";
        }
        setLocale(lenguaje);

        tvNombres.setText(nombres);
        tvApellidos.setText(apellidos);

    }

    private void setLocale(String lenguaje) {
        Locale locale = new Locale(lenguaje);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getContext().getResources().updateConfiguration(configuration,this.getResources().getDisplayMetrics());
    }
}