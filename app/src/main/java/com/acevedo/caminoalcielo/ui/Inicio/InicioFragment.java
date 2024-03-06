package com.acevedo.caminoalcielo.ui.Inicio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.acevedo.caminoalcielo.Login.LoginActivity;
import com.acevedo.caminoalcielo.MainActivity;
import com.acevedo.caminoalcielo.R;

import java.util.Objects;


public class InicioFragment extends Fragment {

    TextView tvNombres, tvApellidos;
    ImageView ivBanner;
    View viewDegradado;

    RecyclerView rvTopAlumnos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_inicio, container, false);
        tvNombres = view.findViewById(R.id.tvNombres);
        tvApellidos = view.findViewById(R.id.tvApellidos);
        ivBanner = view.findViewById(R.id.ivBanner);
        viewDegradado = view.findViewById(R.id.viewDegradado);
        rvTopAlumnos = view.findViewById(R.id.rvTopAlumnos);
        rvTopAlumnos.setHasFixedSize(true);
        rvTopAlumnos.setLayoutManager(new GridLayoutManager(getContext(), 2));
        ThemeActive();
        RecuperarPreferencias();
        cargarTopAlumnos();
        return view;
    }

    private void cargarTopAlumnos() {

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
        SharedPreferences preferences = requireContext().getSharedPreferences("userLogin", Context.MODE_PRIVATE);
        String nombres = preferences.getString("nombres","");
        String apellidos = preferences.getString("apellidos","");

        tvNombres.setText(nombres);
        tvApellidos.setText(apellidos);

    }
}